import math


# 1. log with certain basis
"""the function returns the function of log with a certain basis."""
def log_function(basis):
    return lambda x: math.log(x, basis)


# 2. Composition function
"""the function returns the function of composition of two functions together."""
def compose(f, g):
    return lambda x: f(g(x))


# 3. Take numerical derivative
"""the function returns the numerival derivative of function g with a delta."""
def differential_function(g,delta):
    return lambda x: (g(x+delta/2) - g(x-delta/2))/delta


# 4. Create numerical integral function using trapezoid method
"""using the trapezoid method the function returns the numerical integral function for a and b."""
def get_integral_func(a, b, num_segments):
    delta = (b - a) / num_segments
    return lambda g: sum([(g(a + i * delta) + g(a + (i + 1) * delta)) * delta / 2 for i in range(num_segments)])

# 5. Binary search
"""the function looks for a point on the function f that is equal to 0 with precision epsilon. first it checks if both
points of the function given are either positive or negitive - in this case the function returns False. then it runs a 
while loop as long as the distance between the two sides of the interval are bigger than 0. then mid is set to be
the middle between these two points. if epsilon is bigger than abs(mid) then mid is returned and that is the 0 of the
function. then it goes by the binary search - according to weather mid is bigger/smaller the 0, and according to which
side of the interval is the positive, a new mid point is set until the function will reach the zero in between."""
def binary_search(f, interval, epsilon):
    if f(interval[0]) * f(interval[1]) > 0:
        return False
    while interval[1] - interval[0] > epsilon:
        mid = (interval[0] + interval[1]) / 2
        if epsilon > abs(f(mid)):
            return mid
        if f(mid) > 0:
            if f(interval[0]) > 0:
                interval[0] = mid
            else:
                interval[1] = mid
        else:
            if f(interval[0]) > 0:
                interval[1] = mid
            else:
                interval[0] = mid
    return mid