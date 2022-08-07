#include <iostream>
#include "Movie.h"
#include "RecommenderSystem.h"
#include "RecommenderSystemLoader.h"
#include "RSUser.h"
#include "RSUsersLoader.h"

int main ()
{
  auto rs = RecommenderSystemLoader::create_rs_from_movies_file
      ("../RecommenderSystemLoader_input.txt");
  std::cout << *rs << std::endl;
  std::vector<RSUser> users = RSUsersLoader::create_users_from_file
      ("../RSUsersLoader_input.txt", rs);
  for (auto &print : users)
  {
    std::cout << print << std::endl;
  }
}
