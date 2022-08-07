//
// Created by אוהד רביב on 2/6/2022.
//
#include "RecommenderSystem.h"
#include <cmath>

/*
 * This is an assisting function to calculate the distance between a couple
 * of vector. This function is used by predict_movie_score and
 * recommend_by_content.
 */
double RecommenderSystem::cos_angle(const std::vector<double> v1,
                                    const std::vector<double>
                                    v2) const
{
  if (v1.size() != num_features || v2.size() != num_features)
  {
    throw std::length_error("Wrong vectors length");
  }
  double product = 0;
  double sum1 = 0;
  double sum2 = 0;
  for (size_t i = 0; i < num_features; ++i)
  {
    product += v1[i] * v2[i];
    sum1 += v1[i] * v1[i];
    sum2 += v2[i] * v2[i];
  }
  return product / std::sqrt (sum1 * sum2);
}

/*
 * A function to add a movie to the recommender system.
 */
sp_movie RecommenderSystem::add_movie(const std::string& name,int year,const
std::vector<double>& features)
{
  sp_movie m = std::make_shared<Movie>(name, year);
  feature_table[m] = features;
  if (num_features == 0)
  {
    num_features = features.size();
  }
  else if (num_features != features.size())
  {
    throw std::length_error("Inconsistent number of features");
  }
  return m;
}

// This is a getter function to extract a movie from the recommender system,
// it uses the stl function of find to search for the movie.
sp_movie RecommenderSystem::get_movie(const std::string &name, int year) const
{
  sp_movie m = std::make_shared<Movie>(name, year);
  feature_map::const_iterator p;
  p = feature_table.find(m);
  if (p != feature_table.end())
  {
    return p->first;
  }
  return nullptr;
}

// This operator is used to print out the system.
std::ostream &operator<< (std::ostream &os, const RecommenderSystem &rs)
{
  for (auto const &p: rs.feature_table)
  {
      os << *p.first;
  }
  return os;
}

/*
 * This function executes the given algorithm to recommend a movie by
 * content for the user. We'll calculate the similarity bewteen the movies
 * according to content and then recommend the movie with the most similar
 * features to what the user likes.
 */
sp_movie RecommenderSystem::recommend_by_content(const RSUser& user) const
{
  const rank_map &ranks = user.get_ranks();
  double sum = 0.0;
  int count = 0;
  for (auto rank : ranks)
  {
    sum += rank.second;
    count++;
  }
  double average = sum / count;
  std::vector<double> preference(num_features, 0.0);
  for (auto rank : ranks)
  {
    for (size_t i = 0; i < num_features; ++i)
    {
      preference[i] += (rank.second - average) * feature_table.at(rank
          .first)[i];
    }
  }
  double best_score = 0;
  sp_movie best_movie;
  for (auto movie : feature_table)
  {
    if (ranks.count(movie.first) == 0)
    {
      double score = cos_angle (preference, movie.second);
      if (score > best_score)
      {
        best_movie = movie.first;
        best_score = score;
      }
    }
  }
  return best_movie;
}

/*
 * Here we'll want to give the user a recommendation for a movie he hasn't
 * seen which is the most similar to the movies he's already seen. We'll
 * create a set of k most similar movies to what the user has already seen
 * and liked, then we'll calculate the angles between each vector of
 * features to the vector of the movie we want to predict. In the end we'll
 * return the movie which has the best score.
 */
sp_movie RecommenderSystem::recommend_by_cf(const RSUser& user, int k) const
{
  double best_score = 0;
  sp_movie best_movie;
  std::vector<double> preference(num_features, 0.0);
  const rank_map &ranks = user.get_ranks();
  for (auto movie : feature_table)
  {
    if (ranks.count(movie.first) == 0)
    {
      double score = predict_movie_score(user, movie.first, k);
      if (score > best_score)
      {
        best_movie = movie.first;
        best_score = score;
      }
    }
  }
  return best_movie;
}

/*
 * Taking a user's rankings, a movie to predict score, and an int k to use
 * for the shared algorithm we'll want to predict the movie's score.
 */
double RecommenderSystem::predict_movie_score (const RSUser &user,
                                               const sp_movie &movie, int k)
                                               const
{
  std::multimap<double, double> scores;
  const rank_map &ranks = user.get_ranks();
  auto features = feature_table.at(movie);
  for (auto m : ranks)
  {
    double score = cos_angle (features, feature_table.at(m.first));
    scores.insert ({-score, m.second});
  }
  int i = 0;
  double nom = 0.0;
  double den = 0.0;
  for (auto sp : scores)
  {
    nom += sp.first * sp.second;
    den += sp.first;
    if (++i >= k)
    {
      break;
    }
  }
  return nom / den;
}