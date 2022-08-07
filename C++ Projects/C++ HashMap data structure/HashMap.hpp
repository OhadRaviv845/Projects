#ifndef _HASHMAP_H_
#define _HASHMAP_H_

#define INIT_TABLE_SIZE 16
#define MIN_LOAD_FACTOR 0.25
#define MAX_LOAD_FACTOR 0.75
#include <vector>
#include <stdexcept>

template <typename KeyT, typename ValueT>

/*
 * Class HashMap is the generic base class which is used for the data base.
 * It holds a vector of pointers where each one represents a bucket. The
 * init table size and the top and bottom load factors are set according to
 * the ex requirements. The class holds all the required methods and
 * operators, as well as a few extra methods I've added in the private
 * section in order to complete the task.
 */

class HashMap {
 public:
  HashMap() {
    _capacity = INIT_TABLE_SIZE;
    _size = 0;
    _arr = new std::vector<std::pair<KeyT, ValueT>> [_capacity];
  }

  HashMap(std::vector<KeyT> keys, std::vector<ValueT> values) : HashMap()
  {
    if (keys.size() != values.size())
    {
      throw std::length_error("The vectors have different lengths.");
    }
    _arr = new std::vector<std::pair<KeyT, ValueT>> [_capacity];
    for (size_t i = 0; i < keys.size(); ++i)
    {
      (*this)[keys[i]] = values[i];
    }
  }

  HashMap(const HashMap &hm) : _capacity(hm._capacity), _size(hm._size)
  {
    _arr = new std::vector<std::pair<KeyT, ValueT>> [_capacity];
    for (int i = 0; i < _capacity; ++i)
    {
      _arr[i] = hm._arr[i];
    }
  }

  virtual ~HashMap()
  {
    delete [] _arr;
  }

  int size() const
  {
    return _size;
  }

  int capacity() const
  {
    return _capacity;
  }

  bool empty() const
  {
    return _size == 0;
  }

  bool insert(KeyT key, ValueT val)
  {
    int index = std::hash<KeyT>()(key) & (_capacity - 1);
    for (std::pair<KeyT, ValueT> &i : _arr[index])
    {
      if (i.first == key)
      {
        return false;
      }
    }
    _arr[index].push_back(std::make_pair (key, val));
    _size++;
    if (_size > _capacity * _max_lf)
    {
      rehash (_capacity * 2);
    }
    return true;
  }

  bool contains_key(KeyT key) const
  {
    int index = std::hash<KeyT>()(key) & (_capacity - 1);
    for (const std::pair<KeyT, ValueT> &i : _arr[index])
    {
      if (i.first == key)
      {
        return true;
      }
    }
    return false;
  }

  ValueT& at(KeyT key)
  {
    int index = std::hash<KeyT>()(key) & (_capacity - 1);
    for (std::pair<KeyT, ValueT> &i : _arr[index])
    {
      if (i.first == key)
      {
        return i.second;
      }
    }
    throw std::out_of_range("Index not found");
  }

  virtual bool erase(KeyT key)
  {
    int index = std::hash<KeyT>()(key) & (_capacity - 1);
    for (typename std::vector<std::pair<KeyT, ValueT>>::iterator i =
        _arr[index].begin(); i != _arr[index].end(); ++i)
    {
      if (i->first == key)
      {
        _arr[index].erase(i);
        _size--;
        while (_size < _capacity * _min_lf && _capacity > 1)
        {
          rehash (_capacity / 2);
        }
        return true;
      }
    }
    return false;
  }

  double get_load_factor() const
  {
    return (double)_size / (double)_capacity;
  }

  int bucket_size(KeyT key) const
  {
    int index = std::hash<KeyT>()(key) & (_capacity - 1);
    for (const std::pair<KeyT, ValueT> &i : _arr[index])
    {
      if (i.first == key)
      {
        return _arr[index].size();
      }
    }
    throw std::range_error("Key does not exist.");
  }

  int bucket_index(KeyT key) const
  {
    int index = std::hash<KeyT>()(key) & (_capacity - 1);
    for (const std::pair<KeyT, ValueT> &i : _arr[index])
    {
      if (i.first == key)
      {
        return index;
      }
    }
    throw std::range_error("Key does not exist.");
  }

  void clear()
  {
    for (int i = 0; i < _capacity; ++i)
    {
      _arr[i].clear();
    }
    _size = 0;
  }

  HashMap &operator=(const HashMap &hm)
  {
    if (this == &hm)
    {
      return *this;
    }
    delete [] _arr;
    _capacity = hm._capacity;
    _size = hm._size;
    _arr = new std::vector<std::pair<KeyT, ValueT>> [_capacity];
    for (int i = 0; i < _capacity; ++i)
    {
      _arr[i] = hm._arr[i];
    }
    return *this;
  }

  ValueT &operator[](const KeyT &key)
  {
    int index = std::hash<KeyT>()(key);
    index &= (_capacity - 1);
    for (std::pair<KeyT, ValueT> &i : _arr[index])
    {
      if (i.first == key)
      {
        return i.second;
      }
    }
    _arr[index].push_back(std::make_pair (key, ValueT()));
    _size++;
    if (_size > _capacity * _max_lf)
    {
      rehash (_capacity * 2);
    }
    return _arr[index].back().second;
  }

  bool operator==(const HashMap &rhs) const
  {
    if (_size != rhs._size)
    {
      return false;
    }
    for (auto &i : rhs)
    {
      if (!contains_pair(i))
      {
        return false;
      }
    }
    return true;
  }

  bool operator!=(const HashMap &rhs) const
  {
    return !(*this == rhs);
  }


  /*
   * This is a nested class to implement an iterator for the hash map to be
   * used. The iterator class holds a constructor and all of the operators
   * an iterator needs to have. The functions of begin and end are held in
   * the parent HashMap class under the private section.
   */
  typedef class ConstIterator
  {
   public:
    ConstIterator(const std::vector<std::pair<KeyT, ValueT>> *p,
             const HashMap<KeyT, ValueT> *base) : ptr(p), itr(p->cbegin()),
             base
             (base)
                 {
                    while (ptr != base->_arr + base->_capacity && itr ==
                    ptr->cend())
                    {
                      ++ptr;
                      itr = ptr->cbegin();
                    }
                  }

    ConstIterator &operator++()
    {
      ++itr;
      while (ptr != base->_arr + base->_capacity && itr == ptr->cend())
      {
        ++ptr;
        itr = ptr->cbegin();
      }
      return *this;
    }

    ConstIterator &operator++(int)
    {
      auto save = *this;
      ++*this;
      return save;
    }

    bool operator==(const ConstIterator &rhs) const
    {
      return itr == rhs.itr;
    }

    bool operator!=(const ConstIterator &rhs) const
    {
      return itr != rhs.itr;
    }

    const std::pair<KeyT, ValueT> &operator*() const
    {
      return *itr;
    }

    const std::pair<KeyT, ValueT> *operator->() const
    {
      return &*itr;
    }

   private:
    const std::vector<std::pair<KeyT, ValueT>> *ptr;
    typename std::vector<std::pair<KeyT, ValueT>>::const_iterator itr;
    const HashMap<KeyT, ValueT> *base;
  } const_iterator;

  // These are the begin functions used for the iterator.
  ConstIterator begin() const
  {
    return ConstIterator(_arr, this);
  }

  ConstIterator cbegin() const
  {
    return ConstIterator(_arr, this);
  }

  // These are the end functions used for the iterator.
  ConstIterator end() const
  {
    return ConstIterator(_arr + _capacity, this);
  }

  ConstIterator cend() const
  {
    return ConstIterator(_arr + _capacity, this);
  }

 private:
  int _capacity;
  int _size;
  double _min_lf = MIN_LOAD_FACTOR;
  double _max_lf = MAX_LOAD_FACTOR;
  std::vector<std::pair<KeyT, ValueT>> *_arr;

  // This is a function I've added which checks if a map contains a pair.
  // It's working pretty similar to the contains_ket function but here we
  // want to check if a pair exists in the map. It is used by the ==
  // operator of the hash map.
  bool contains_pair(const std::pair<KeyT, ValueT> &pair) const
  {
    int index = std::hash<KeyT>()(pair.first) & (_capacity - 1);
    for (std::pair<KeyT, ValueT> &i : _arr[index])
    {
      if (i.first == pair.first)
      {
        return i.second == pair.second;
      }
    }
    return false;
  }

  // This function is used to rehash and reorganize the map once we exceed
  // the top or bottom load factor that was defined. It is used by the
  // insert and erase functions as well as by the [] operator - all of which
  // can enter or delete pairs from the map and change the current load
  // factor.
  void rehash(int new_capacity)
  {
    auto new_arr = new std::vector<std::pair<KeyT, ValueT>> [new_capacity];
    for (int index = 0; index < _capacity; ++index)
    {
      for (const std::pair<KeyT, ValueT> &i : _arr[index])
      {
        int new_index = std::hash<KeyT>()(i.first) & (new_capacity - 1);
        new_arr[new_index].push_back(i);
      }
    }
    delete [] _arr;
    _arr = new_arr;
    _capacity = new_capacity;
  }
};




#endif
