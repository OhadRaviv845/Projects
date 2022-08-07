//
// Created by אוהד רביב on 5/6/2022.
//
#include "RSUsersLoader.h"

#define HASH_TABLE_SIZE 8


std::vector<RSUser> RSUsersLoader::create_users_from_file(const std::string&
users_file_path, sp_RecommenderSystem rs) noexcept(false)
{
  std::ifstream users_file(users_file_path);
  if (!users_file)
  {
    throw std::runtime_error(users_file_path + " Not found");
  }
  std::vector<RSUser> users;
  std::string line;
  std::getline (users_file, line);
  std::istringstream ss(line);
  std::vector<sp_movie> movies;
  std::string name;
  int year = 0;
  while (std::getline (ss, name, '-'))
  {
    ss >> year >> std::ws;
    sp_movie movie = rs->get_movie (name, year);
    if (movie == nullptr)
    {
      throw std::runtime_error("Movie not found");
    }
    movies.push_back (movie);
  }

  while (std::getline (users_file, line))
  {
    std::istringstream ss(line);
    rank_map ranks(HASH_TABLE_SIZE, sp_movie_hash, sp_movie_equal);
    std::string tmp;
    ss >> name;
    for (int i = 0; ss >> tmp; i++)
    {
      if (tmp != "NA")
      {
        double v = std::stod (tmp);
        ranks[movies[i]] = v;
      }
    }
    users.push_back (RSUser(name, ranks, rs));
  }
  return users;
}
