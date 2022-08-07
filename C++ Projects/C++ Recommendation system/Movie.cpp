
#include "Movie.h"
#define HASH_START 17
#define RES_MULT 31

/**
 * hash function used for a unordered_map (implemented for you)
 * @param movie shared pointer to movie
 * @return an integer for the hash map
 */
std::size_t sp_movie_hash(const sp_movie& movie)
{
    std::size_t res = HASH_START;
    res = res * RES_MULT + std::hash<std::string>()(movie->get_name());
    res = res * RES_MULT + std::hash<int>()(movie->get_year());
    return res;
}

/**
 * equal function used for an unordered_map (implemented for you)
 * @param m1
 * @param m2
 * @return true if the year and name are equal else false
 */
bool sp_movie_equal(const sp_movie& m1,const sp_movie& m2)
{
    return !(*m1 < *m2) && !(*m2 < *m1);
}

/**
     * operator<< for movie, used to print
     * @param os ostream to output info with
     * @param movie movie to output
     */
std::ostream &operator<< (std::ostream &os, const Movie &movie)
{
  os << movie.get_name() << " (" << movie.get_year() << ")" << std::endl;
  return os;
}

// A bool condition testing for comparison between a couple of movies
// according to the given logic.
bool Movie::operator< (const Movie &rhs) const
{
  return year < rhs.year || (year == rhs.year && name < rhs.name);
}
