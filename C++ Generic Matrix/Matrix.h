// Matrix.h
#ifndef MATRIX_H
#define MATRIX_H


#include <iostream>
#include <fstream>
/**
 * @struct matrix_dims
 * @brief Matrix dimensions container. Used in MlpNetwork.h and main.cpp
 */
typedef struct matrix_dims
{
	int rows, cols;
} matrix_dims;


/*
 * Matrix template used for all operations - representation of a matrix,
 * vector for calculation of the network and for the layers.
 */
class Matrix
{
 public:
  // Constructors.
  Matrix (int, int);
  Matrix ();
  Matrix (const Matrix &);
  ~Matrix ();
  // Getter functions.
  int get_rows() const
  {
    return rows;
  }
  int get_cols() const
  {
    return cols;
  }
  // Class methods.
  Matrix &vectorize();
  Matrix &transpose();
  void plain_print() const;
  Matrix dot(const Matrix &m) const;
  float norm() const;
  // Class operators.
  Matrix operator+(const Matrix &m) const;
  Matrix& operator+=(const Matrix &m);
  Matrix& operator=(const Matrix &m);
  Matrix operator*(const Matrix &m) const;
  Matrix operator*(float num) const;
  friend Matrix operator*(float num, const Matrix &m);
  friend std::istream& operator>>(std::istream& is, Matrix &m);


  float &operator()(int i, int j)
  {
    if (i < 0 || j < 0 || i >= rows || j >= cols)
    {
      throw std::out_of_range ("Matrix index()");
    }
    return matrix[i * cols + j];
  }

  // The () operator is used for the calculation required according to the
  // formula given in the pdf.
  float operator()(int i, int j) const
  {
    if (i < 0 || j < 0 || i >= rows || j >= cols)
    {
      throw std::out_of_range ("Matrix index()");
    }
    return matrix[i * cols + j];
  }

  // The [] operator is used to allow indexing approach to variables in the
  // matrix.
  float &operator[](int i)
  {
    if (i < 0 || i >= rows * cols)
    {
      throw std::out_of_range ("Matrix index[]");
    }
    return matrix[i];
  }

  float operator[](int i) const
  {
    if (i < 0 || i >= rows * cols)
    {
      throw std::out_of_range ("Matrix index[]");
    }
    return matrix[i];
  }

 private:
  // Fiels of matrix rows and cols, in the case of a vector it is
  // represented as a matrix with one col.
  int rows;
  int cols;
  // Matrix field.
  float *matrix;

  Matrix &operator*=(float num);

  // A function to dynamically allocate space for the matrix, memory is
  // freed in the destructor.
  void alloc_space();
};

std::ostream& operator<<(std::ostream& os, const Matrix &m);

#endif //MATRIX_H