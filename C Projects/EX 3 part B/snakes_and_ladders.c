#include <string.h> // For strlen(), strcmp(), strcpy()
#include "markov_chain.h"

#define MAX(X, Y) (((X) < (Y)) ? (Y) : (X))

#define EMPTY -1
#define BOARD_SIZE 100
#define MAX_GENERATION_LENGTH 60

#define DICE_MAX 6
#define NUM_OF_TRANSITIONS 20

#define PROGRAM_ARGS 3
#define BASE 10
#define USAGE_MESSAGE "Usage: seed tracks.\n"
#define BAD_SEED "Error: bad seed provided.\n"
#define BAD_TRACKS "Error: bad number of tracks provided.\n"

/**
 * represents the transitions by ladders and snakes in the game
 * each tuple (x,y) represents a ladder from x to if x<y or a snake otherwise
 */
const int transitions[][2] = {{13, 4},
                              {85, 17},
                              {95, 67},
                              {97, 58},
                              {66, 89},
                              {87, 31},
                              {57, 83},
                              {91, 25},
                              {28, 50},
                              {35, 11},
                              {8,  30},
                              {41, 62},
                              {81, 43},
                              {69, 32},
                              {20, 39},
                              {33, 70},
                              {79, 99},
                              {23, 76},
                              {15, 47},
                              {61, 14}};

/**
 * struct represents a Cell in the game board
 */
typedef struct Cell {
    int number; // Cell number 1-100
    int ladder_to;  // ladder_to represents the jump of the
                    // ladder in case there is one from this square
    int snake_to;  // snake_to represents the jump of the
                  // snake in case there is one from this square
    //both ladder_to and snake_to should be -1 if the Cell doesn't have them
} Cell;

/** Error handler **/
static int handle_error(char *error_msg, MarkovChain **database)
{
    printf("%s", error_msg);
    if (database != NULL)
    {
        free_markov_chain(database);
    }
    return EXIT_FAILURE;
}


static int create_board(Cell *cells[BOARD_SIZE])
{
    for (int i = 0; i < BOARD_SIZE; i++)
    {
        cells[i] = malloc(sizeof(Cell));
        if (cells[i] == NULL)
        {
            for (int j = 0; j < i; j++) {
                free(cells[j]);
            }
            handle_error(ALLOCATION_ERROR_MASSAGE,NULL);
            return EXIT_FAILURE;
        }
        *(cells[i]) = (Cell) {i + 1, EMPTY, EMPTY};
    }

    for (int i = 0; i < NUM_OF_TRANSITIONS; i++)
    {
        int from = transitions[i][0];
        int to = transitions[i][1];
        if (from < to)
        {
            cells[from - 1]->ladder_to = to;
        }
        else
        {
            cells[from - 1]->snake_to = to;
        }
    }
    return EXIT_SUCCESS;
}

/**
 * fills database
 * @param markov_chain
 * @return EXIT_SUCCESS or EXIT_FAILURE
 */
static int fill_database(MarkovChain *markov_chain)
{
    Cell* cells[BOARD_SIZE];
    if(create_board(cells) == EXIT_FAILURE)
    {
        return EXIT_FAILURE;
    }
    MarkovNode *from_node = NULL, *to_node = NULL;
    size_t index_to;
    for (size_t i = 0; i < BOARD_SIZE; i++)
    {
        add_to_database(markov_chain, cells[i]);
    }

    for (size_t i = 0; i < BOARD_SIZE; i++)
    {
        from_node = get_node_from_database(markov_chain,cells[i])->data;

        if (cells[i]->snake_to != EMPTY || cells[i]->ladder_to != EMPTY)
        {
            index_to = MAX(cells[i]->snake_to,cells[i]->ladder_to) - 1;
            to_node = get_node_from_database(markov_chain, cells[index_to])
                    ->data;
            add_node_to_counter_list(from_node, to_node, markov_chain);
        }
        else
        {
            for (int j = 1; j <= DICE_MAX; j++)
            {
                index_to = ((Cell*) (from_node->data))->number + j - 1;
                if (index_to >= BOARD_SIZE)
                {
                    break;
                }
                to_node = get_node_from_database(markov_chain, cells[index_to])
                        ->data;
                add_node_to_counter_list(from_node, to_node, markov_chain);
            }
        }
    }
    // free temp arr
    for (size_t i = 0; i < BOARD_SIZE; i++)
    {
        free(cells[i]);
    }
    return EXIT_SUCCESS;
}

// A generic function that checks whether a certain cell and returns a
// boolean value.
static bool is_last_cell (void const *data)
{
  Cell const *cell = data;
  return cell->number == BOARD_SIZE;
}

// A generic function that prints the data inside the cell.
static void print_cell (void const *data)
{
  Cell const *cell = data;
  printf ("[%d]", cell->number);
  if (is_last_cell (data))
  {
    return;
  }
  if (cell->ladder_to > 0)
  {
    printf ("-ladder to %d", cell->ladder_to);
  }
  if (cell->snake_to > 0)
  {
    printf ("-snake to %d", cell->snake_to);
  }
  printf (" -> ");
}

// A generic function used to copy a certain cell and returns the new cell.
static void *copy_cell (void const *data)
{
  void *new_cell = malloc (sizeof (Cell));
  if (new_cell == NULL)
  {
    return NULL;
  }
  memcpy (new_cell, data, sizeof (Cell));
  return new_cell;
}

// A function to compare two cells and return the value of the subtraction
// between a to b.
int comp_cells (const void *a, const void *b)
{
  const Cell *ca = a;
  const Cell *cb = b;
  return ca->number - cb->number;
}

/**
 * The main function is used to run the whole program. It checks for
 * validity of given arguments, setting given seed using srand, allocate a
 * markov chain data structure, fill database, print possible tracks, finally
 * it frees the memory allocated.
 * @param argc num of arguments
 * @param argv 1) Seed
 *             2) Number of sentences to generate
 * @return EXIT_SUCCESS or EXIT_FAILURE
 */

int main(int argc, char *argv[])
{
  char *endptr = NULL;
  MarkovChain *chain = NULL;

  if (argc != PROGRAM_ARGS)
  {
    printf (USAGE_MESSAGE);
  }

  int seed = (int) strtol(argv[1], &endptr, BASE);
  if (*endptr != '\0')
  {
    printf (BAD_SEED);
    return EXIT_FAILURE;
  }
  srand (seed);

  int tracks = (int) strtol(argv[2], &endptr, BASE);
  if (*endptr != '\0')
  {
    printf (BAD_TRACKS);
    return EXIT_FAILURE;
  }

  chain = markov_chain (print_cell, comp_cells, free, copy_cell,
                        is_last_cell);
  if (chain == NULL)
  {
    return EXIT_FAILURE;
  }
  if (fill_database (chain) != EXIT_SUCCESS)
  {
    free_markov_chain (&chain);
    return EXIT_FAILURE;
  }
  MarkovNode *first = chain->database->first->data;
  for (int i = 1; i <= tracks; i++)
  {
    printf ("Random Walk %d: ", i);
    generate_random_sequence (chain, first, MAX_GENERATION_LENGTH);
    printf("\n");
  }

  free_markov_chain (&chain);
  return EXIT_SUCCESS;
}

