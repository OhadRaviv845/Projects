#ifndef ACTIVATION_H
#define ACTIVATION_H

#include "Matrix.h"
typedef Matrix ActivationFunction(const Matrix &m);
// A namespace for activation with two different functions - relu and softmax.
namespace activation
{
    ActivationFunction relu;
    ActivationFunction softmax;
}

#endif //ACTIVATION_H