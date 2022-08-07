#include "Activation.h"
#include <cmath>

// ReLU function - used for the first three layers.
Matrix activation::relu (const Matrix &m)
{
  Matrix res = m;
  for (int i = 0; i < res.get_rows() * res.get_cols(); ++i)
  {
    if (res[i] < 0)
    {
      res[i] = 0;
    }
  }
  return res;
}
// Softmax function - used for the last layer.
Matrix activation::softmax (const Matrix &m)
{
  Matrix res = m;
  float sum = 0;
  for (int i = 0; i < res.get_rows() * res.get_cols(); i++)
  {
    res[i] = std::exp (m[i]);
    sum += res[i];
  }
  return res * (1 / sum);
}