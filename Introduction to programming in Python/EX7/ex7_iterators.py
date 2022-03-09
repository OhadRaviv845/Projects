import random


# 1. Iterator for reverse order
"""the class returns a reverse iteration for lists. it receives a list and takes it's length as the first index.
then once the iterator is set, it runs backword through the index of the original list to return the reverse iteration."""
class ReverseIter:
    def __init__(self, a):
        self.__a = a
        self.__a = a[-1::-1]
        self.start = -1

    def __iter__(self):
        return self

    def __next__(self):
        # while self.__index >= 1:
        self.start += 1
        return self.__a[self.start]

    # Rest of code for class here

# 2. Generator for computing n!
"""the generator computes n factorial. with two variables - sum and count, whenever the generator is called it computes
the next number. it starts yielding 1 in the first cound, and each time it's called upon count is increased by one. it
then multiplies sum with count to generate the next number. the sum is the result of n!"""
def factorial():
    sum = 1
    count = 0
    while True:
        if count <= 1:
            yield 1
        else:
            sum = sum * count
            yield sum
        count += 1


# 3. Create range function
"""the function performs as the range function of python. it goes either up or down depending on the jump, with each step
the jum is added to the last stop."""
def myRange(start, stop, jump):
    if jump > 0:
        while stop > start:
            yield start
            start = start + jump
    else:
        while start > stop:
            yield start
            start = start + jump


# 4. Draw a card from deck
"""the function generates a deck of cards with a couple of for loops. then the deck is shuffeled using the random module.
the a simple generator iterates through the deck returning one card at a time."""
def take_card():
    deck = []
    shapes = ["diamond", "heart", "club", "spade"]
    for i in range(13):
        for shape in shapes:
            deck.append((i, shape))
    random.shuffle(deck)
    for card in deck:
        yield card
