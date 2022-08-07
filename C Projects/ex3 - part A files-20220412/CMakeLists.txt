cmake_minimum_required(VERSION 3.21)
project(ex3___part_A_files_20220412 C)

set(CMAKE_C_STANDARD 11)

include_directories(.)

add_executable(ex3___part_A_files_20220412
        linked_list.c
        linked_list.h
        markov_chain.c
        markov_chain.h
        tweets_generator.c)
