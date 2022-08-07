//
// Created by אוהד רביב on 2/6/2022.
//
#include "RecommenderSystemLoader.h"
#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include <memory>

sp_RecommenderSystem RecommenderSystemLoader::create_rs_from_movies_file
(const
std::string& movies_file_path)
noexcept(false)
{
  std::ifstream rs_file(movies_file_path);
  if (!rs_file)
  {
    throw std::runtime_error(movies_file_path + " Not found");
  }
  sp_RecommenderSystem r = std::make_shared<RecommenderSystem>();
  std::string line;
  while (std::getline (rs_file, line))
  {
    std::istringstream ss(line);
    std::vector<double> new_features;
    std::string name;
    int year = 0;
    double v = 0.0;
    std::getline (ss, name, '-');
    ss >> year;
    while (ss >> v)
    {
      new_features.push_back (v);
    }
    r->add_movie (name, year, new_features);
  }
  return r;
}