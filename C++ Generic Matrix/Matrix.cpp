//
// Created by אוהד רביב on 17/5/2022.
//

#include "Matrix.h"
#include <cmath>

#define BRIGHTNESS_THRESHOLD 0.1
// Constructor with given field of rows and cols.
Matrix::Matrix(int rows, int cols) : rows(rows), cols(cols)
{
  alloc_space();
}
// Default ctr.
Matrix::Matrix() : rows(1), cols(1)
{
  alloc_space();
}
// Copy ctr.
Matrix::Matrix(const Matrix& m) : rows(m.rows), cols(m.cols)
{
  matrix = new float [rows * cols];
  for (int i = 0; i < rows * cols; ++i)
  {
    matrix[i] = m.matrix[i];
  }
}
// Destructor.
Matrix::~Matrix()
{
  delete[] matrix;
}
// A method to represent a matrix as a vector and update the class fields
// rows and cols.
Matrix& Matrix::vectorize()
{
  rows *= cols;
  cols = 1;
  return *this;
}
// A method to transpose the matrix, using a temp copy, updating changes and
// return the given matrix.
Matrix &Matrix::transpose()
{
  float *tmatrix = new float[cols * rows];
  for (int i = 0; i < rows; ++i) {
    for (int j = 0; j < cols; ++j) {
      tmatrix[j*rows + i] = matrix[i*cols + j];
    }
  }
  int temp = cols;
  cols = rows;
  rows = temp;
  return *this;
}
// Matrix print method.
void Matrix::plain_print() const
{
  for (int i = 0; i < get_rows(); i++)
  {
    for (int j = 0; j < get_cols(); j++)
    {
      std::cout << (*this)(i, j) << " ";
    }
    std::cout << std::endl;
  }
}
// Calculate the dot product of the matrix.
Matrix Matrix::dot(const Matrix &m) const
{
  if (rows != m.rows || cols != m.cols)
  {
    throw std::length_error ("dot different sizes");
  }
  Matrix multi = Matrix(rows, cols);
  for(int i = 0; i < rows * cols; ++i)
  {
    multi.matrix[i] = matrix[i] * m.matrix[i];
  }
  return multi;
}
// Calculate the norm of the matrix.
float Matrix::norm() const
{
  float sum = 0;
  for (int i = 0; i < rows * cols; ++i)
  {
    sum += matrix[i] * matrix[i];
  }
  return std::sqrt (sum);
}

Matrix Matrix::operator+(const Matrix &m) const
{
  Matrix temp = *this;
  return (temp += m);
}

Matrix& Matrix::operator+=(const Matrix &m)
{
  if (rows != m.rows || cols != m.cols)
  {
    throw std::length_error ("+= different matrix sizes");
  }
  for (int i = 0; i < rows * cols; ++i)
  {
    matrix[i] += m.matrix[i];
  }
  return *this;
}

Matrix& Matrix::operator=(const Matrix& m)
{
  if (this == &m) {
    return *this;
  }

  if (rows != m.rows || cols != m.cols)
  {
    delete[] matrix;

    rows = m.rows;
    cols = m.cols;
    alloc_space();
  }

  for (int i = 0; i < rows * cols; ++i)
  {
      matrix[i] = m.matrix[i];
  }
  return *this;
}

Matrix Matrix::operator*(const Matrix &A) const
{
  //The two matrices to multiply are (*this) and A
  if (cols != A.rows)
  {
    throw std::length_error ("matrix multiplication");
  }
  Matrix c(rows,A.cols);
  for (int i=0; i < rows; i++)
  {
    for (int j=0; j < A.cols; j++)
    {
      float sum = 0;
      for (int k=0; k < cols; k++)
      {
        sum += (*this)(i,k)*A(k,j);
      }
      c(i,j) = sum;
    }
  }
  return c;
}

Matrix Matrix::operator*(float num) const
{
  Matrix temp(*this);
  return (temp *= num);
}

Matrix operator*(float num, const Matrix &m)
{
  Matrix temp(m);
  return (temp *= num);
}



void Matrix::alloc_space()
{
  if (rows <= 0 || cols <= 0)
  {
    throw std::length_error ("Matrix size");
  }
  matrix = new float [rows * cols];
  for (int i = 0; i < rows * cols; ++i) {
    matrix[i] = 0.0;
  }
}



Matrix &Matrix::operator*=(float num)
{
  for (int i = 0; i < rows * cols; ++i)
  {
    matrix[i] *= num;
  }
  return *this;
}



std::ostream &operator<<(std::ostream &os, const Matrix &m)
{
  for (int i = 0; i < m.get_rows(); ++i) {
    for (int j = 1; j < m.get_cols(); ++j) {
      if (m(i, j) > BRIGHTNESS_THRESHOLD)
      {
        os << "**";
      }
      else
      {
        os << "  ";
      }
    }
    os << std::endl;
  }
  return os;
}

std::istream &operator>>(std::istream &is, Matrix &m)
{
  is.seekg (0, std::istream::end);
  size_t length = is.tellg();
  is.seekg (0, std::istream::beg);
  if (length != m.get_rows() * m.get_cols() * sizeof(float))
  {
    throw std::length_error ("read matrix");
  }
  is.read ((char *)m.matrix, length);
  if (is.fail())
  {
    throw std::runtime_error ("read matrix failed");
  }
  return is;
}