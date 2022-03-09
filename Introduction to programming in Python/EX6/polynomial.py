# Template for polynomial exercise
from math import log10, floor
import copy


# Helper function round number to significant digits
def sig(x, digits):
    if x == 0:
        return 0
    return round(x, -int(floor(log10(abs(x))))+digits-1)


# A class representing a polynomial
class Polynomial:

    # 1. Constructor
    """the method recieves a list of natural numbers and sets the polynom, either by coefficients or by roots."""
    def __init__(self, a, data='coef'):
        if data == 'coef':
            self.__a = a.copy()
        elif data == 'roots':
            temp = Polynomial([1])
            for root in a:
                temp *= Polynomial([-root, 1])
            self.__a = temp.__a
        else:
            return None

    # 3. Other operators
    """the method evaluates two different polynomials to be equal, if their values are equal it returns true, if not return false."""
    def __eq__(self, other):
        if len(self.__a) == len(other.__a):
            for i in range(len(self.__a)):
                if self.__a[i] != other.__a[i]:
                    return False
            return True
        else:
            return False

    """the method adds two polinomials together, checking the max length of the two, then looping through the range of the longer
    one of the two. if one polynom is shorter than the other it would appear as zero after it's coefficients are zero."""
    def __add__(self, other):
        m = max(len(self.__a), len(other.__a))
        new_p = [(self.__a[i] if i < len(self.__a) else 0) + (other.__a[i] if i < len(other.__a) else 0) for i in range(m)]
        while new_p[-1] == 0 and len(new_p) > 1:
            del new_p[-1]
        return Polynomial(new_p)

    """the method subtracts two polinomials, running through all the coefficients subtracting each one from each other.
    it then normalizes the now polinom - if the leading coefficients equal zero they are deleted."""
    def __sub__(self, other):
        m = max(len(self.__a), len(other.__a))
        new_p = [(self.__a[i] if i < len(self.__a) else 0) - (other.__a[i] if i < len(other.__a) else 0) for i in
                 range(m)]
        while new_p[len(new_p) - 1] == 0 and len(new_p) > 1:
            del new_p[len(new_p) - 1]
        return Polynomial(new_p)


    """the method multyplies two polinomials, in the first stage it checks if other is a polinomial - if not it multiplies
    by a polinomial made of this number. it then sets an empty polinomial in the length of both polinomials together. it then
    goes on to multyply all of the coefficients, it adds together their index to keep the degree."""
    def __mul__(self, other):
        if not isinstance(other, Polynomial):
            return self * Polynomial([other])
        p = [0] * (len(self.__a) + len(other.__a) - 1)
        for i in range(len(self.__a)):
            for j in range(len(other.__a)):
                p[i + j] += self.__a[i] * other.__a[j]
        return Polynomial(p)

    """assisting method to use reverse multiply - number * polinomial"""
    def __rmul__(self, other):
         return self * Polynomial([other])

    """the method recursively powers the polinom, if power is zero then the result is '1', if not it calls on itself until power
    will be equal 1 and then it finishes."""
    def __pow__(self, power, modulo=None):
        if power == 0:
            return Polynomial([1])
        elif power == 1:
            return self
        else:
            return self * self ** (power-1)


    """the method takes a polinomial and returns an argument. it goes through the coefficients and with different
    conditions add numbers and signs to the str representation returning the full string in the end."""
    def __str__(self):
        string = ""
        for i in range(len(self.__a)-1, -1, -1):
            coef = sig(self.__a[i], 2)
            if coef == 0 and len(self.__a) != 1:
                continue
            if coef > 0 and i != (len(self.__a) - 1):
                string += '+'
            if coef == -1 and i != 0:
                string += '-'
            if coef != 1 or i == 0:
                string += str(coef)
            if i > 0:
                string += 'x'
            if i > 1:
                string += '^' + str(i)
        return string

    """using an assisting function euclid"""
    # (*) Divide by another polynomial
    def __floordiv__(self, other):
        return self.__euclid(other)[0]

    """using an assisting function euclid"""
    # (*) get modulo
    def __mod__(self, other):
        return self.__euclid(other)[1]


    """according to euclid's division algorythm and with the same variable names, it sets an empty polinomial to keep
    the resuly. it then gets the degree and coefficients of each polinom, then it runs until the polinom degree is
    equal between the two. dividing the degree each time, then dividing the coefficient, the multiplyin for the remainder.
    in the end return a tupple with the quotient result and the remainder."""
    def __euclid(self, other):
        quotient = Polynomial([0])
        r = self
        dr = r.get_deg()
        d = other.get_deg()
        c = other.__a[-1]
        while dr >= d:
            cr = r.__a[-1]
            s = cr / c
            si = dr - d
            partial_result = s * Polynomial([0, 1]) ** si
            quotient += partial_result
            r -= partial_result * other
            dr = r.get_deg()
        return (quotient,r)

    '''
        Euclidean division algorithm for polynomials:

        Input: a and b ≠ 0 two polynomials in the variable    x;
        Output: q, the    quotient, and r, the    remainder;

        Begin
        q := 0
        r := a
        d := deg(b)
        c := lc(b)
        while deg(r) ≥ d do
        s :=lc(r)/c
        xdeg(r)−d
        q := q + s
        r := r − sb
        end
        do
        return (q, r)
        end.
    '''
    """return a list of coefficients."""
    # 2.a. return coefficients of polynomial
    def get_coef(self):
        return self.__a
    """return the degree of the polynom."""
    # 2.b. return degree of polynomial
    def get_deg(self):
        return (len(self.__a) - 1)


    """the method evaluates the polinom with value of x."""
    # 4. Compute polynomial at value x
    def evaluate(self, x):
        result = 0
        for coef in self.__a[::-1]:
            result = result * x + coef
        return result

    """the method recursively returns the kth derivative, if k = 0 it stops. it then runs through all the indexes of
    the list representation of the polinom - each time setting each coefficient to be the coefficient of the derivative.
    whenever the length of the polinom is longer than 1, after each derivative the leading degree has to be popped out
    to normalize the polinom. if the length is smaller or equal to one the result is zero. in the end the recursion
    calls for another derivative until k = 0."""
    # 5. compute the k-th derivative
    def differentiate(self, k=1):
        if k == 0:
            return
        else:
            for i in range(len(self.__a)-1):
                self.__a[i] = self.__a[i+1] * (i + 1)
            if len(self.__a) > 1:
                self.__a.pop()
            else:
                self.__a[0] = 0
            self.differentiate(k - 1)


    """using the same way as the derivative but in the other way around - adding a zero in index [0] to raise the degree,
    then calculating each number to be the new value. in the end recursively calling itself until k = 0."""
    # 6. compute the k-th integral
    def integrate(self, k=1):
        if k == 0:
            return
        else:
            self.__a = [0] + [self.__a[i] / (i + 1) for i in range(len(self.__a))]
            self.integrate(k - 1)
        return


    """using the method evaluate and the method of subtract returning the result of a definite integral."""
    # 7. definite integral from a to b
    def definite_integral(self, a, b):
        self.integrate()
        return self.evaluate(b) - self.evaluate(a)


    """given a certaing number of iters and a certain precision rate, using the variable selftag for as many times
    as given in iters, it then evaluates the function for the x0 given and evaluates x0 for the former function. if at
    any time y0 is zero the function breaks. then the delta is the function divided by the derivative of the function,
    x0 is subtracted the delta on each iteration. if the absolut value of delta is at any time smaller than epsilon
    the root is found, other wise it would run until maximum number of iterations."""
    # 8. Get one real root of polynomial (Newtorn Raphson method)
    def get_root(self, x0=0, iters=100, epsilon=0.000000001):
        selftag = Polynomial(self.__a)
        selftag.differentiate()
        for i in range(iters):
            y0 = self.evaluate(x0)
            ytag = selftag.evaluate(x0)
            if ytag == 0:
                break
            delta = y0 / ytag
            x0 -= delta
            if abs(delta) < epsilon:
                break
        return x0


    """using the get_root method finding the closest to the root, then using the floor division dividing to polinom with the
    polinom x-r. then creating a list of roots and returning the list."""
    # 9 (*). Get all real roots of polynomial (how?)
    def get_roots(self, iters=100, epsilon=0.000001):
        if self.get_deg() == 0:
            return []
        root = self.get_root(0, 100)
        temp = self // Polynomial([-root, 1])
        root_lst = [root] + temp.get_roots(iters)
        return root_lst


    """using the euclidean algorithm, recursively replacing the two polinomials until either the degree of the dividing
    polinom is lower than one or it's equal to zero. in order to return the gcd with a leading coefficient 1 it is 
    1 / leading coef * polinom"""
    # 10 (*). Compute gcd of two polynomials. Euclidean algorithm
    def gcd(self, other):
        while len(other.__a) > 1 or other.__a[0] != 0:
            self, other = other, self % other
        return (1 / self.__a[-1]) * self


    """starting by creating an empty polinom, looping through all the coefficients in the polinom, computing each number
    for the composition of the polinoms."""
    # 11 (*). Compute composition of polynomials: result is P(Q(x))
    def compose(self, other):
        result = Polynomial([0])
        for coef in self.__a[::-1]:
            result = result * other + Polynomial([coef])
        self.__a = result.__a

