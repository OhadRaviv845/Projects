#ifndef DENSE_H
#define DENSE_H

#include "Activation.h"

class Dense
{
 public:
  // Constructor with assignment of the class fields.
  Dense (const Matrix &weights, const Matrix &bias, ActivationFunction
  function) :
  weights(weights), bias(bias), function(function) {}
  // Class getter functions.
  Matrix get_weights() const
  {
    return weights;
  }
  Matrix get_bias() const
  {
    return bias;
  }
  ActivationFunction *get_activation() const
  {
    return function;
  }

  Matrix operator()(const Matrix &x) const;

 private:
  const Matrix weights;
  const Matrix bias;
  ActivationFunction *const function;
};


#endif //DENSE_H
