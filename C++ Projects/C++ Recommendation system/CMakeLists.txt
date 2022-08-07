cmake_minimum_required(VERSION 3.21)
project(EX5)

set(CMAKE_CXX_STANDARD 14)

add_executable(EX5 main.cpp Movie.cpp RecommenderSystem.cpp RecommenderSystemLoader.cpp RSUser.cpp RSUsersLoader.cpp)

