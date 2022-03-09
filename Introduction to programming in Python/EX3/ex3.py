
import copy
import random
import time

#function one receives two items and intersects the two, returning their intersection.
# 1. Intersect two cards
def cards_intersect(card1, card2):
    # Fill code
    return list(set(card1).intersection(set(card2)))


#function two compares the sets of a card with it's given deck, if the set of the card equals a set of a card
#in the deck, the card is then removed from the deck with a pop function.
# 2. Check if card is in deck and remove it
def remove_card(deck, card):
    for i in range(len(deck)):
        if set(deck[i]) == set(card):
            deck.pop(i)
            return True
    print("Error! card is not in the deck!")
    return False


#function three first checks length of new card and then returns error if length deosn't match,
#it then goes on to check if there is only one match of the sets of symbols in both the card and all the cards of the deck.
#in the end it appends the correct cards to the deck
# 3. Check if new card matches and add it to the deck
def add_card(deck, card):
    # Fill code
    if deck and len(card) != len(deck[0]):
        print("Error! Card is of wrong length")
        return False
    for c in deck:
        if len(set(c).intersection(set(card))) != 1:
            print("Error! number of matches for new card is not one")
            return False
    deck.append(card)
    return True

#d2 = [['dolphin', 'bomb', 'spider'], ['eye', 'bomb', 'fire'], ['spider', 'fire', 'lock'], ['bomb', 'lock', 'tree']]
#print(add_card(d2, ['spider', 'eye', 'tree']))
#print(d2)

#the function compares each item in the list to see if has exactly one match with all the other items,
#if it does then the deck is valid
# 4. Check if a deck is valid
def is_valid(deck):
    # Fill code
    for i in range(len(deck)):
        for j in range(i):
            if len(deck[i]) != len(deck[j]) or len(set(deck[i]).intersection(set(deck[j]))) != 1:
                return False
    return True



#print(is_valid([['dolphin', 'bomb', 'spider'], ['eye', 'bomb', 'fire'], ['spider', 'fire', 'lock'], ['bomb', 'lock', 'tree']]))



#random function
# 5. Draw 2 cards at random
import random
def draw_random_cards(deck):
    # Fill code
    return random.sample(deck, 2)

#print(draw_random_cards([['dolphin', 'bomb', 'spider'], ['eye', 'bomb', 'fire'], ['spider', 'fire', 'lock'], ['bomb', 'lock', 'tree']]))

#the function creates a new dictionary and then runs over all the cards in the deck, if a symbol of one of the cards
#doesn't exist inside the deck then it is set to be value 1, if it does exist then it gets +1
# 6. Print all symbols with counts
def print_symbols_counts(deck):
    # Fill code
    symbol_count = {}
    for card in deck:
        for j in range(len(card)):
            if not card[j] in symbol_count.keys():
                symbol_count[card[j]] = 1
            else:
                symbol_count[card[j]] += 1
    for keys,values in symbol_count.items():
        print(keys,values)
    return


#print_symbols_counts([['dolphin', 'bomb', 'spider'], ['eye', 'bomb', 'fire'],
#                      ['spider', 'fire', 'lock'], ['bomb', 'lock', 'tree']])


#the function receives an input from the user, it then summons the assistant functions accordingly to add a card,
#to remove a card or to count symbols of the current deck.
# 7. Interactive function for playing the game
def play_dobble(deck):
        #if not deck:
        x = input("Select operation: (P)lay, (A)dd card, (R)emove card, or (C)ount\n") # Get option from user

        if x == "A":
            y = input().split(",")
            y = [e.strip() for e in y]
            status = add_card(deck, y)

        if x == "R":
            y = input().split(",")
            y = [e.strip() for e in y]
            status = remove_card(deck, y)


        if x == "C":
            print_symbols_counts(deck)

        if x == "P":
            tmp = play_func(deck)
            if not tmp[1] == 0:
                print("Finished Game. Correct:", tmp[1], "Wrong:",  tmp[2], "Average time:",  round(tmp[0] / tmp[1], 2), "sec.")
            else:
                print("Finished Game. Correct:", tmp[1], "Wrong:", tmp[2], "Average time: 0.0 sec.")
#the calculation of avarage time of success is inside the print of finish game.

#the play is a long part of the code and therefore it's on a separate assisting function. the function starts by making a
#copy of the deck to not change any of the original values. it goes on the game as long as the deck has 2 cards or more.
#each time of the game it prints two random cards and checks how long it takes for the player to identify the joint symbol.
#in the end it returns number of success, number of fails, and total time of success.
def play_func(deck):
    new_deck = copy.deepcopy(deck)
    num_success = 0
    num_fail = 0
    total_time = 0.0
    while len(new_deck) >= 2:
        #print(new_deck)
        #draw_random_cards(new_deck)
        print("Identify joint symbol:")
        random = draw_random_cards(new_deck)
        print(list2str(random[1]))
        print(list2str(random[0]))
        start = time.time()
        k = input()
        end = time.time()
        calc_time = end - start
        round(calc_time, 2)
        total_time += calc_time
        if k in random[0] and k in random[1]:
            print("Very nice! Found the correct card in", round(calc_time, 2), "sec.")
            num_success += 1

        else:
            print("Wrong!")
            num_fail += 1
        remove_card(new_deck, random[0])
        remove_card(new_deck, random[1])
        #play_time = (end - start) / (num_success + num_fail)
    return [total_time, num_success, num_fail]

#a function to change the random cards list from list to str, using an external function because it is used twice and
#easier to modulate.
def list2str(l):
    s = ''
    first = True
    for i in l:
        s += i if first else ',' + i
        first = False
    return s

    # Fill code for each option

# 8. Bonus question: create deck as large as possible
#the same function as in question 3, seperated the functions because here I don't want to print an error message for
#a different number of matches.
def try_add_card(deck, card):
    # Fill code
    if deck and len(card) != len(deck[0]):
        print("Error! Card is of wrong length")
        return False
    for c in deck:
        if len(set(c).intersection(set(card))) != 1:
            return False
    deck.append(card)
    return True


#using the itertools.combinations to sort out the biggest deck i can, checking each new card through the try_add_card
#function.
import itertools
def create_deck(symbols, k):
    deck = []
    new_card = []
    for new_card in \
            itertools.combinations(symbols, k):
        try_add_card(deck, new_card)
    return deck








deck = create_deck(['stop', 'exclamation point', 'maple leaf', 'bomb', 'moon', 'heart',
 'sun', 'iglu', 'pencil', 'scarecrow', 'spider', 'snowflake', 'dolphin', 'tortoise',
 'apple', 'treble clef', 'scissors', 'dog', 'zebra', 'tree'], 5)



#    deck = [symbols[:k]]  # This will create a deck with one card. Can you make it larger?
#   return deck


