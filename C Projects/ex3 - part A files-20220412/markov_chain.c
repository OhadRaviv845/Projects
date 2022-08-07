//
// Created by אוהד רביב on 12/4/2022.
//
#include "markov_chain.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

char *strdup (const char *);
/**
 * insert MarkovChain struct
 */

static bool is_last_word(MarkovNode *node)
{
  return node->data[strlen(node->data) - 1] == '.';
}

int get_random_number(int max_number);

MarkovNode* get_first_random_node(MarkovChain *markov_chain)
{
  Node *ptr = NULL;

  do
  {
    int n = get_random_number(markov_chain->database->size);
    ptr = markov_chain->database->first;
    for (int i = 0; i < n; i++)
    {
      ptr = ptr->next;
    }
  }
  while (is_last_word (ptr->data));

  return ptr->data;
}

MarkovNode* get_next_random_node(MarkovNode *state_struct_ptr)
{
  int sum = 0;
  int random_test = 0;
  int i = 0;
  for (i = 0; i < state_struct_ptr->num_counters; ++i)
  {
    sum += state_struct_ptr->counter_list[i].frequency;
  }
  int n = get_random_number (sum);

  for (i = 0; random_test <= n; ++i)
  {
    random_test += state_struct_ptr->counter_list[i].frequency;
  }
  return state_struct_ptr->counter_list[i-1].markov_node;
}

void generate_random_sequence(MarkovChain *markov_chain, MarkovNode *
first_node, int max_length)
{
  MarkovNode *ptr = first_node;
  if (ptr == NULL)
  {
    ptr = get_first_random_node (markov_chain);
  }
  for (int i = 1; !is_last_word (ptr) && i < max_length; i++)
  {
    printf ("%s ", ptr->data);
    ptr = get_next_random_node (ptr);
  }
  printf ("%s\n", ptr->data);
}

void free_markov_chain(MarkovChain **ptr_chain)
{
  Node *next = NULL;
  if ((*ptr_chain)->database != NULL)
  {
    for (Node *ptr = (*ptr_chain)->database->first; ptr != NULL; ptr = next)
    {
      next = ptr->next;
      free (ptr->data->data);
      free (ptr->data->counter_list);
      free (ptr->data);
      free (ptr);
    }
    free ((*ptr_chain)->database);
  }
  free (*ptr_chain);
  *ptr_chain = NULL;
}

bool add_node_to_counter_list(MarkovNode *first_node, MarkovNode *second_node)
{
  for (int i = 0; i < first_node->num_counters; i++)
  {
    if (second_node == first_node->counter_list[i].markov_node)
    {
      ++first_node->counter_list[i].frequency;
      return true;
    }
  }
  NextNodeCounter *r = realloc (first_node->counter_list,
                                (first_node->num_counters + 1) *
                                sizeof (NextNodeCounter));
  if (r == NULL)
  {
    printf (ALLOCATION_ERROR_MASSAGE);
    return false;
  }
  first_node->counter_list = r;
  first_node->counter_list[first_node->num_counters].markov_node = second_node;
  first_node->counter_list[first_node->num_counters].frequency = 1;
  ++first_node->num_counters;
  return true;
}

int get_random_number(int max_number)
{
  return rand() % max_number;
}

Node* get_node_from_database(MarkovChain *markov_chain, const char *data_ptr)
{
  Node *ptr = markov_chain->database->first;
  for (; ptr != NULL; ptr = ptr->next)
  {
    if (data_ptr == ptr->data->data)
    {
      return ptr;
    }
  }
  return NULL;
}

Node* add_to_database(MarkovChain *markov_chain, char *data_ptr)
{
  Node *ptr = markov_chain->database->first;

  for (; ptr != NULL; ptr = ptr->next)
  {
    if (strcmp(data_ptr, ptr->data->data) == 0)
    {
      return ptr;
    }
  }

  MarkovNode *new_markov_node = calloc (1, sizeof (MarkovNode));
  if (new_markov_node == NULL)
  {
    return NULL;
  }
  new_markov_node->data = strdup (data_ptr);
  if (new_markov_node->data == NULL)
  {
    free (new_markov_node);
    return NULL;
  }
  if (add(markov_chain->database, new_markov_node))
  {
    free (new_markov_node->data);
    free (new_markov_node);
    return NULL;
  }
  return markov_chain->database->last;
}