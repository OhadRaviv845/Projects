#ifndef _DICTIONARY_H_
#define _DICTIONARY_H_

#include "HashMap.hpp"
#include <string>

/*
 * Class Dictionary is a derived class from HashMap, and it contains the
 * specific implementation of HashMap when used with string as key and as
 * value.
 */

// Class InvalidKey is used for exceptions.
class InvalidKey : public std::invalid_argument
{
 public:
  InvalidKey() : std::invalid_argument ("Invalid key") {}
  InvalidKey(std::string msg) : std::invalid_argument (msg) {}
};

class Dictionary : public HashMap<std::string, std::string>
{
 public:
  Dictionary() {}
  Dictionary(std::vector<std::string> keys, std::vector<std::string> values)
  : HashMap<std::string, std::string>(keys, values) {}

  bool erase(const std::string &key)
  {
    if (!HashMap<std::string, std::string>::erase(key))
    {
     throw InvalidKey();
    }
    return true;
  }

  void update(std::vector<std::pair<std::string, std::string>>::iterator it,
              std::vector<std::pair<std::string, std::string>>::iterator
              end_it)
  {
    while (it != end_it)
    {
      (*this)[it->first] = it->second;
      ++it;
    }
  }
};

#endif //_DICTIONARY_H_
