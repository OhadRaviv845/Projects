//
// Created on 2/20/2022.
//

#ifndef SCHOOL_SOLUTION_RECOMMENDERSYSTEM_H
#define SCHOOL_SOLUTION_RECOMMENDERSYSTEM_H

#include <map>
#include <iostream>
#include <memory>

class RecommenderSystem;
class RSUser;
typedef std::shared_ptr<RecommenderSystem> sp_RecommenderSystem;

#include "RSUser.h"
struct cmp_movies {
    bool operator()(const sp_movie& a, const sp_movie& b) const
    {
      return *a < *b;
    }
};

typedef std::map<sp_movie, std::vector<double>, cmp_movies> feature_map;

class RecommenderSystem
{
public:

	RecommenderSystem() : num_features(0) {}
    /**
     * adds a new movie to the system
     * @param name name of movie
     * @param year year it was made
     * @param features features for movie
     * @return shared pointer for movie in system
     */
	sp_movie add_movie(const std::string& name, int year,
                       const std::vector<double>& features);


    /**
     * a function that calculates the movie with
     * highest score based on movie features
     * @param ranks user ranking to use for algorithm
     * @return shared pointer to movie in system
     */
	sp_movie recommend_by_content(const RSUser& user) const;

    /**
     * a function that calculates the movie with highest
     * predicted score based on ranking of other movies
     * @param ranks user ranking to use for algorithm
     * @param k
     * @return shared pointer to movie in system
     */
	sp_movie recommend_by_cf(const RSUser& user, int k) const;


    /**
     * Predict a user rating for a movie given argument using item cf
     * procedure with k most similar movies.
     * @param user_rankings: ranking to use
     * @param movie: movie to predict
     * @param k:
     * @return score based on algorithm as described in pdf
     */
	double predict_movie_score(const RSUser &user, const sp_movie &movie,
												  int k) const;

	/**
	 * gets a shared pointer to movie in system
	 * @param name name of movie
	 * @param year year movie was made
	 * @return shared pointer to movie in system
	 */
	sp_movie get_movie(const std::string &name, int year) const;

    friend std::ostream& operator<<(std::ostream& os, const
    RecommenderSystem& rs);
 private:
  feature_map feature_table;
  size_t num_features;
  // The assisting function is declared private in order to maintain the
  // given api.
  double cos_angle(std::vector<double> v1, std::vector<double>
      v2) const;
};

#endif //SCHOOL_SOLUTION_RECOMMENDERSYSTEM_H
