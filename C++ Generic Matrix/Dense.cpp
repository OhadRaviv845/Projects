//
// Created by אוהד רביב on 17/5/2022.
//

#include "Dense.h"

// This operator is used to create the layers of the network - given
// weights, bias and an activation function.
Matrix Dense::operator()(const Matrix &x) const
{
  return function(weights * x + bias);
}
