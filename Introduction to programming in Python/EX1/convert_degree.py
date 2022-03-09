# Helper function: Check if a string represents a real number
def is_number(s):
    try:
        float(s)
        return True
    except ValueError:
        return False


def convert_degree(temp, units):
    if not is_number(temp):
        return None
    if units == "C":
        if temp < -459.67:
            return None
        temp = (temp-32.0)*5/9
    elif units == "F":
        if temp < -273.15:
            return None
        temp = temp*9.0/5+32
    else:
        return None
    return temp



def natural_language_convert_degree(user_request):

    s = user_request.split()
    units = "C" if s[2] == "celsius" else "F"
    if not is_number(s[3]): return None
    orig = float(s[3])
    cvt = convert_degree(orig,units)
    if cvt is None:
        return None
    return (orig, cvt)



def user_input_convert_degree():
    g = float(input("Please enter degrees: "))
    if not is_number(g):
        print ("Number required")
        return
    g = int(g)
    v = input("Convert to (C)elsius, or (F)ahrenheit: ")
    r = convert_degree(g, v)
    if v == "C":
        print (g, "in Fahrenheit is", r, "in Celsius")
    else:
        print (g, "in Celsius is", r, "in Fahrenheit")
    return

# user_input_convert_degree()

# Helper function for determining state of matter from a real number representing the temperature
def water_temp_to_state(temp):
    temp = float(temp)
    if temp >= -273.15 and temp <= 0:
        state = "Solid"
    elif temp > 0 and temp < 100:
        state = "Liquid"
    elif temp > 100:
        state = "Gas"
    else:
        state = "Illegal temperature"
    return state


# Convert water temperature to state of matter for user input.
def state_of_matter():
    temperatures = input("Please enter a number of temperatures (1-3): ")
    temperatures = int(temperatures)
    if not (temperatures >=1 and temperatures <=3):
        print ("The number you entered is not between 1 and 3, goodbye!")
        return
    print("Please enter the temperatures, one in each line")

    temps = [0,0,0]
    for i in range(temperatures):
        temps[i] = input("")
    for i in range(temperatures):
        print (water_temp_to_state(temps[i]))

    return
