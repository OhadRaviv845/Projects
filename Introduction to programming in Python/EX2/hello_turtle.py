import turtle


# 1. Draw Mercedes car symbol
def draw_mercedes():
    # fill code here
    mercedes = turtle.Turtle()

    mercedes.circle(50)
    mercedes.penup()
    mercedes.left(90)
    mercedes.forward(50)
    mercedes.pendown()
    mercedes.forward(50)
    mercedes.penup()
    mercedes.right(180)
    mercedes.pendown()
    mercedes.forward(50)
    mercedes.right(60)
    mercedes.forward(50)
    mercedes.penup()
    mercedes.right(180)
    mercedes.forward(50)
    mercedes.right(60)
    mercedes.pendown()
    mercedes.forward(50)
    turtle.done()

    return







# 2. Draw smiley
def draw_smiley():
    # fill code here

    turtle.penup()
    turtle.goto(-50, 0)
    turtle.pendown()
    turtle.circle(50)
    turtle.penup()
    turtle.goto(-70, 60)
    turtle.pendown()
    turtle.circle(10)
    turtle.penup()
    turtle.goto(-30,60)
    turtle.pendown()
    turtle.circle(10)
    turtle.penup()
    turtle.goto(-70, 40)
    turtle.pendown()
    turtle.setheading(270)
    turtle.circle(20, 180)
    turtle.hideturtle()

    return




# 3. Draw variable-length centipede with loop
def draw_centipede(n):
    r = 50 # set radius for all circles
    draw_smiley()
    turtle.penup()
    turtle.showturtle()
    turtle.goto(-25, (11 / 6 * 50))
    turtle.pendown()
    turtle.left(0)
    turtle.forward(25)
    turtle.penup()
    turtle.goto(-75, (11 / 6 * 50))
    turtle.pendown()
    turtle.forward(25)
    turtle.right(90)
    turtle.penup()

    for i in range(n):
        turtle.goto(100 * i + 50, 0)
        turtle.pendown()
        turtle.setheading(0)
        turtle.circle(50)
        turtle.penup()
        turtle.goto(100 * i + 75, 100 - (11 / 6 * 50))
        turtle.right(90)
        turtle.pendown()
        turtle.forward(25)
        turtle.penup()
        turtle.goto(100 * i + 25, 100 - (11 / 6 * 50))
        turtle.pendown()
        turtle.forward(25)
        turtle.penup()

    turtle.done()

    return



    # fill code here


# 4. Draw polygon with n sides of length L
def draw_regular_polygon(n, L):
    # fill code here

    for i in range(n):
        turtle.forward(L)
        turtle.right(360/n)

    turtle.done()
    return





# 5. Draw all pairwise lines between points
def connect_all_pairs(x, y):
    for i in range(len(x)):
        for j in range(i):
            turtle.penup()
            turtle.goto(x[i], y[i])
            turtle.pendown()
            turtle.goto(x[j],y[j])

    turtle.done()

    return
    # fill code here
connect_all_pairs([0, 100, 150, 50], [0, 0, 150, 150])