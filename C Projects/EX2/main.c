#include "sort_bus_lines.h"
#include "test_bus_lines.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>



int get_lines()
{
  char str[61];
  int n = 0;

  printf ("Enter number of lines. Then enter\n");
  fgets (str, sizeof(str), stdin);
  sscanf (str, "%d", &n);
  if (n < 1)
  {
    printf ("ERROR: Line number should be an integer bigger than zero\n");
    return -1;
  }
  return n;
}

int get_line_details(BusLine *ptr)
{
  char str[61];

  printf ("Enter line info . Then enter\n");
  fgets (str, sizeof(str), stdin);
  if (sscanf (str, "%d,%d,%d", &ptr->line_number, &ptr->distance,
              &ptr->duration) != 3)
  {
    return 1;
  }
  if (*ptr->line_number < 1 || *ptr->line_number > 999)
  {
    printf ("ERROR: line number must be an integer between 1 and 999\n");
    return 1;
  }
  if (*ptr->distance < 0 || *ptr->distance > 1000)
  {
    printf ("ERROR: distance must be an integer between 0 and 1000.\n")
    return 1;
  }
  if (*ptr->duration <10 || *ptr->duration > 100)
  {
    printf ("ERROR: duration must be an integer between 10 and 100.\n");
    return 1;
  }
  return 0;
}

/**
 * TODO add documentation
 */
int main (int argc, char *argv[])
{
  BusLine *ptr = NULL;
  int lines = 0;

  if (argc != 2)
  {
    printf("USAGE: wrong number of arguments, this program only takes one "
           "argument.\n");
    return EXIT_FAILURE;
  }
  else
  {
    if (strcmp (argv[1], "quick") == 0)
    {
      int lines = -1;
      while (lines == -1)
      {
        lines = get_lines();
      }

      ptr = malloc (sizeof (BusLine) * lines);
      if (ptr == NULL)
      {
        printf ("ERROR: cannot allocate memory.\n");
        return EXIT_FAILURE;
      }
      for(int i = 0; i <= lines; i++)
      {
        while (get_line_details (ptr) != 0)
        {
          get_line_details (ptr);
        }
      }
      
    }
    else if (strcmp (argv[1], "bubble") == 0)
    {

    }
    else if (strcmp (agrv[1], "test") == 0)
    {

    }
    else
    {
      printf ("ERROR: wrong argument, program only accepts "
              "test/bubble/quick.\n");
      return EXIT_FAILURE;
    }
  }

  return EXIT_SUCCESS;
}
