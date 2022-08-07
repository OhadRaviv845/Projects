//
// Created by אוהד רביב on 12/4/2022.
//
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <string.h>
#include "markov_chain.h"

#define OPEN_FILE_ERROR "Error: open file failed."
#define WRONG_ARGUMENTS_NUMBER "Usage: seed tweets input.txt *words to read*."
#define BAD_NUMBER_OF_WORDS "Error: bad number of words."
#define BAD_NUMBER_OF_TWEETS "Error: bad number of tweets."
#define BAD_SEED "Error: bad seed provided."
#define MAX_LINE_LENGTH 1000
#define ARGUMENTS_WITHOUT_WORDS 4
#define ARGUMENTS_WITH_WORDS 5
#define STRTOK_DELIMITER " \n\t"
#define BASE 10
#define MAX_TWEET_LENGTH 20

int fill_database (FILE *fp, int words_to_read, MarkovChain *markov_chain)
{
  int words_count = 0;
  char line[MAX_LINE_LENGTH + 2];
  markov_chain->database = calloc (1, sizeof (LinkedList));
  if (markov_chain->database == NULL)
  {
    printf (ALLOCATION_ERROR_MASSAGE);
    return EXIT_FAILURE;
  }

  while (fgets (line, sizeof line, fp) != NULL && words_count < words_to_read)
  {
    char *ptr = strtok (line, STRTOK_DELIMITER);
    Node *prev = NULL;
    while (ptr != NULL && words_count < words_to_read)
    {
      Node *res = add_to_database (markov_chain, ptr);
      if (res == NULL)
      {
        printf (ALLOCATION_ERROR_MASSAGE);
        return EXIT_FAILURE;
      }
      if (prev && !add_node_to_counter_list (prev->data, res->data))
      {
        printf (ALLOCATION_ERROR_MASSAGE);
        return EXIT_FAILURE;
      }

      prev = res;
      ptr = strtok (NULL, STRTOK_DELIMITER);
      ++words_count;
    }
  }
  return EXIT_SUCCESS;
}

int parse_args(int argc, char *argv[], int *read_words, int *tweets_to_create)
{
  char *endptr = NULL;

  if (argc != ARGUMENTS_WITHOUT_WORDS && argc != ARGUMENTS_WITH_WORDS)
  {
    printf (WRONG_ARGUMENTS_NUMBER);
    return EXIT_FAILURE;
  }

  if (argc == ARGUMENTS_WITH_WORDS)
  {
    *read_words = (int) strtol(argv[4], &endptr, BASE);
    if (*endptr != '\0')
    {
      printf(BAD_NUMBER_OF_WORDS);
      return EXIT_FAILURE;
    }
  }

  *tweets_to_create = (int) strtol(argv[2], &endptr, BASE);
  if (*endptr != '\0')
  {
    printf (BAD_NUMBER_OF_TWEETS);
    return EXIT_FAILURE;
  }

  int seed = (int) strtol(argv[1], &endptr, BASE);
  if (*endptr != '\0')
  {
    printf (BAD_SEED);
    return EXIT_FAILURE;
  }
  srand (seed);

  return EXIT_SUCCESS;
}

int main(int argc, char *argv[])
{
  FILE *fp = NULL;
  int read_words = INT_MAX;
  int tweets_to_create = 0;
  MarkovChain *chain = NULL;

  if (parse_args (argc, argv, &read_words, &tweets_to_create) != EXIT_SUCCESS)
  {
    return EXIT_FAILURE;
  }

  fp = fopen (argv[3], "r");
  if (fp == NULL)
  {
    printf (OPEN_FILE_ERROR);
    return EXIT_FAILURE;
  }

  chain = calloc (1, sizeof (MarkovChain));
  if (chain == NULL)
  {
    printf (ALLOCATION_ERROR_MASSAGE);
    return EXIT_FAILURE;
  }

  if (fill_database (fp, read_words, chain) != EXIT_SUCCESS)
  {
    free_markov_chain (&chain);
    return EXIT_FAILURE;
  }

  for (int i = 1; i <= tweets_to_create; i++)
  {
    printf ("Tweet %d: ", i);
    generate_random_sequence (chain, NULL, MAX_TWEET_LENGTH);
  }

  free_markov_chain (&chain);
  return EXIT_SUCCESS;
}