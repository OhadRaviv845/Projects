#include "markov_chain.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

/*
 * Forward declaration for the function.
 */
int get_random_number(int max_number);

/*
 * this function draws
 * a random number using get_random_number function, then using the
 * different fields of the given markov chain it draws the word in the place
 * that was randomly drawn. If the word that is drawn happens to be a last
 * word - the process will continue until it will not be a last word.
 */
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
  while (markov_chain->is_last (ptr->data->data));

  return ptr->data;
}

/*
 * The variable of random test is used to measure the probability for each
 * node. The function is used to randomly generate next node according to the
 * frequency each one of the nodes appears after the previous one.
 */
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

/*
 * Using both functions of get_first_random_node and get_next_random_node
 * this function creates a random sequence of nodes.
 */
void generate_random_sequence(MarkovChain *markov_chain, MarkovNode *
first_node, int max_length)
{
  MarkovNode *ptr = first_node;
  if (ptr == NULL)
  {
    ptr = get_first_random_node (markov_chain);
  }
  for (int i = 1; !markov_chain->is_last (ptr->data) && i < max_length;
       i++)
  {
    markov_chain->print_func(ptr->data);
    ptr = get_next_random_node (ptr);
  }
  markov_chain->print_func(ptr->data);
}

/*
 * This function is used to free all memory allocated for the markov chain
 * data structure. By the order, it starts from the inner fields of the
 * struct, finally it frees the pointer to the markov chain.
 */
void free_markov_chain(MarkovChain **ptr_chain)
{
  Node *next = NULL;
  if ((*ptr_chain)->database != NULL)
  {
    for (Node *ptr = (*ptr_chain)->database->first; ptr != NULL; ptr = next)
    {
      next = ptr->next;
      (*ptr_chain)->free_data (ptr->data->data);
      free (ptr->data->counter_list);
      free (ptr->data);
      free (ptr);
    }
    free ((*ptr_chain)->database);
  }
  free (*ptr_chain);
  *ptr_chain = NULL;
}

/*
 * The function receives two nodes and a markov chain which they are both a
 * part of, if the nodes are both the same then we increment the frequency
 * list of the first node. If not, we reallocate and increase the size of
 * counter list for the first node
 */
bool add_node_to_counter_list(MarkovNode *first_node, MarkovNode
*second_node, MarkovChain *markov_chain)
{
  markov_chain = markov_chain;    // Eliminate compilation warning.
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

/*
 * This function receives a markov chain and a data pointer, iteratively it
 * checks if the given pointer exists in the database. if it does, return
 * the node which holds the data, if not return NULL.
 */
Node* get_node_from_database(MarkovChain *markov_chain, void *data_ptr)
{
  Node *ptr = markov_chain->database->first;
  for (; ptr != NULL; ptr = ptr->next)
  {
    if (markov_chain->comp_func(data_ptr, ptr->data->data) == 0)
    {
      return ptr;
    }
  }
  return NULL;
}


Node* add_to_database(MarkovChain *markov_chain, void *data_ptr)
{
  Node *ptr = markov_chain->database->first;

  for (; ptr != NULL; ptr = ptr->next)
  {
    if (markov_chain->comp_func(data_ptr, ptr->data->data) == 0)
    {
      return ptr;
    }
  }

  MarkovNode *new_markov_node = calloc (1, sizeof (MarkovNode));
  if (new_markov_node == NULL)
  {
    return NULL;
  }
  new_markov_node->data = markov_chain->copy_func (data_ptr);
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

MarkovChain *markov_chain(Print_t print_func, Comp_t comp_func, Free_t
free_data, Copy_t copy_func, Is_last_t is_last)
{
  MarkovChain *chain = calloc (1, sizeof (MarkovChain));
  if (chain == NULL)
  {
    printf (ALLOCATION_ERROR_MASSAGE);
    return NULL;
  }

  chain->database = calloc (1, sizeof (LinkedList));
  if (chain->database == NULL)
  {
    free (chain);
    printf (ALLOCATION_ERROR_MASSAGE);
    return NULL;
  }

  chain->print_func = print_func;
  chain->comp_func = comp_func;
  chain->free_data = free_data;
  chain->copy_func = copy_func;
  chain->is_last = is_last;

  return chain;
}

