//
// Created on 2/20/2022.
//

// don't change those includes
#include "RSUser.h"
#include "RecommenderSystem.h"

void RSUser::add_movie_to_rs(const std::string &name, int year,
                        const std::vector<double> &features,
                        double rate)
{
  sp_movie movie = system->add_movie (name, year, features);
  ranks[movie] = rate;
}

std::ostream& operator<<(std::ostream& os, const
RSUser& rsu)
{
  os << "name: " << rsu.get_name() << std::endl;
  os << *rsu.system << std::endl;
  return os;
}

sp_movie RSUser::get_recommendation_by_content() const
{
  return system->recommend_by_content(*this);
}

sp_movie RSUser::get_recommendation_by_cf(int k) const
{
  return system->recommend_by_cf(*this, k);
}

double RSUser::get_prediction_score_for_movie(const std::string& name, int
year,
                                       int k) const
{
  return system->predict_movie_score(*this, system->get_movie(name,
                                                              year), k);
}
