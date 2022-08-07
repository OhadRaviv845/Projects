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

/*
 * This function checks whether a certain word is the last (ends with a '.').
 * In usage, this function is cast to the generic type Is_last_t.
 */
static bool is_last_word (char const *data)
{
  return data[strlen(data) - 1] == '.';
}

/*
 * This function receives a char ptr and prints the string it contains.
 * In usage it is cast to the generic type Print_t.
 */
static void print_word (char const *data)
{
  printf ("%s", data);
  if (!is_last_word (data))
  {
    printf(" ");
  }
}

/*
 * This function receives the program's arguments of file to read and how
 * many words to read out of it, along with a markov_chain which was already
 * allocated in the main function. Using the two conditions - while the file
 * hasn't ended / we haven't reached our max words to read, it runs
 * iteratively, parsing the file and reads it into a markov cahin
 * datastructure.
 */
static int fill_database (FILE *fp, int words_to_read, MarkovChain
*markov_chain)
{
  int words_count = 0;
  char line[MAX_LINE_LENGTH + 2];

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
      if (prev && !add_node_to_counter_list (prev->data, res->data,
                                             markov_chain))
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

/*
 * A straightforward function that parses the arguments the program is being
 * run with. It also runs the basic tests to check given number of arguments
 * given is valid, and that the arguments themselves are valid.
 */
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

/*
 * main function, responsible to call parse_args functions, open given file,
 * allocate memory for the markov chain data structure, print the tweets and
 * finally to free the allocated memory.
 */
int main(int argc, char *argv[])
{
  FILE *fp = NULL;
  int read_words = INT_MAX;
  int tweets_to_create = 0;
  MarkovChain *chain = NULL;
  char *strdup (const char *);

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

  chain = markov_chain ((Print_t)print_word, (Comp_t)strcmp, (Free_t)
  free, (Copy_t)
  strdup,
                        (Is_last_t)is_last_word);
  if (chain == NULL)
  {
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
    printf("\n");
  }

  free_markov_chain (&chain);
  return EXIT_SUCCESS;
}