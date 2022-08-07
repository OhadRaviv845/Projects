#include "MlpNetwork.h"

#define LAYERS 4

// Initiation of the network, assignment to the layers array.
MlpNetwork::MlpNetwork(const Matrix weights[LAYERS], const Matrix
biases[LAYERS]) :
network{Dense(weights[0], biases[0], activation::relu),
         Dense(weights[1], biases[1], activation::relu),
         Dense(weights[2], biases[2], activation::relu),
         Dense(weights[3], biases[3], activation::softmax)}
{}
// This is the method used to calculate the probabilities for a given
// matrix, the result is returned as a struct digit.
digit MlpNetwork::operator()(const Matrix &img) const
{
  // A new variable so we can overrun it each time, not changing the
  // original given matrix.
  Matrix calc = img;
  // Iterating through the layers array, calculating each step.
  for (int i = 0; i < LAYERS; ++i)
  {
    calc = network[i](calc);
  }
  unsigned int index = 0;
  float prob = 0;
  // An iteration to extract the maximum probability and finding it's index
  // (the index represents the digit itself).
  for (int i = 0; i < calc.get_rows(); ++i)
  {
    if (calc[i] > prob)
    {
      index = i;
      prob = calc[i];
    }
  }
  return digit{index, prob};
}