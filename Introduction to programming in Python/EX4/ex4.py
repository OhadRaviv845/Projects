# Implement sudoku solving
import random
import copy
#import pygame
# from sudoku_helper import sudoku_action


################################################
######### Start functions in exercises #########
################################################


'''#the function checks all rows in the board, if any small square in any row equals 0 it means the sudoku is till not complete'''
# 1. Check if board is complete
def sudoku_iscomplete(board):
    # fill code
    for row in board:
        if 0 in row:
            return False
    return True

'''#the function divides the board into a few indexes - each square starts from the same point
#by dividing and leaving out the fractional part the function gets the same index for three different rows\culomns - 
#0,1,2 start from 0, 3,4,5 start from 3 etc. it then goes on to use an empty list which is appended with each row -
#each row has a list of three numbers that add up to a list of lists - this list represents the square.
#using the flag variant each time the new list is appended to the right index. the function returns the square.'''
# 2. Return the 3*3 square covering x,y
def sudoku_square3x3(board, x, y):
    start_row = 3*(x//3)
    start_col = 3*(y//3)
    square = [[],[],[]]
    flag=0
    for i in range(start_row, start_row+3):
        for j in range(start_col, start_col+3):
            square[flag].append(board[i][j])
        flag +=1
    return square

#helper function to determine weather a certain number is in a chosen column.
def is_in_col(board, n, col_num):
    for row in board:
        if row[col_num] == n:
            return True
    return False

#helper function to determine weather a certain number is in a chosen row.
def is_in_row(board, n, row_num):
    return n in board[row_num]

#helper function to determine weather a certain number is in a chosen square. this function calls for the square function
#from question 2 to extract the square it's checking.
def is_in_square(board, n, row_num, col_num):
    square = sudoku_square3x3(board, row_num, col_num)
    for row in square:
        if n in row:
            return True
    return False

#helper function to check if a certain number is valid for a certain coordinate in the board. conflicting is a variant
#going through all helper functions above to check if it is valid to add the number being checked. the function
#returns a boolean value weather it's possible or not.
def is_possible(board, x, y, n):

    save = board[x][y]
    board[x][y] = 0
    conflicting = is_in_row(board, n, x) or is_in_col(board, n, y) or is_in_square(board, n, x, y)
    board[x][y] = save
    return not conflicting


'''the function checks all the options for a certain coordinate in the board. it runs through 1-9 and checks each
number with the function is_possible. after doing the check if the number is valid it is added to a set, finally
the function returns the set of all the valid numbers in a set.'''
#3. sudoku_options
def sudoku_options(board, x, y):
    # fill code
    num_set = set()
    #k = board[x][y]
    for i in range(1, 10):
        if is_possible(board, x, y, i):
            num_set.add(i)
    #num_set.pop(k)
    return num_set

'''this function checks for all the places which only have one valid option to fill in. it sets an empty list and then
goes all over the coordinates in the board, if any checked coordinate has only one option to fill in it stores that
value as a tupple inside of a set. it then adds the tupple into a list which gives all of the coordinats with the one
value that fits each. the function returns that list.'''
# 4. find all positions which have one option to fill
def find_all_unique(board):
    # fill code
    unique_lst = []
    for row in range(9):
        for col in range(9):
            x = board[row][col]
            if x == 0:
                x = sudoku_options(board, row, col)
                if len(x) == 1:
                    return_x = x.pop()
                    unique_lst.append((row, col, return_x))
    return unique_lst


'''this function goes through all the board and checkes with the sudoku_options function how many options there are
to fill for any square. if any square has no valid option to fill in it means that it's in conflict. the function
returns the list of all the squares that are in conflict.'''
# 5. Find squares with no option to fill
def find_all_conflicts(board):
    # fill code
    conflict_lst = []
    for row in range(9):
        for col in range(9):
            x = sudoku_options(board, row, col)
            if not x:
                conflict_lst.append((row, col))
    return conflict_lst

'''the runction receives a coordinate and a value. it checks if the value is valid through the helper function is_possible.
if the value is valid for that coordinate it returns True, and adds the value. if it's in conflict it returns False.'''
#6. Add square:
def add_square(board, i, j, val):
    # fill code
    if is_possible(board, i, j, val):
        board[i][j] = val
        return True
    else:
        print("Error! Value in conflict with current board")
        return False


'''the function goes through the board in a loop. each time it is searching to see weather there is any square that
has no valid value - if so it returns error and the sudoku board leads to inconsistencies. it then goes on to check
if there is any square that has only one option that's valid to fill in - and if there is it fills it. filled_any is
used as a flag for the while loop, if any square was filled then it is marked as True and then the loop goes on again.
if no square was fille then it is left as False and the loop will stop. if the function runs and there were no invalid
squares, no squares that were filled then it means there were squares with more than one option - leading to the any_empty
flag. if any_empty is True it means the sudoku is only partially solved. if all squares has only one option to fill but
all of them are already filled, the function will exit the loop and go on to the condition that says that the sudoku is 
completely solved.'''
# 7. Iteratively fill the board with unique options
def fill_board(board):
    # fill code
    filled_any = True
    any_empty = True
    while filled_any:
        filled_any = False
        any_empty = False
        for x in range(9):
            for y in range(9):
                    options = sudoku_options(board, x, y)
                    if len(options) == 0:
                        print("Error! current sudoku leads to inconsistencies. Must delete values")
                        return False
                    if len(options) == 1:
                        if board[x][y] == 0:
                            board[x][y] = options.pop()
                            filled_any = True
                    else:
                        any_empty = True
    if any_empty:
        print("Sudoku partially solved")
    else:
        print("Success! sudoku solved")
    return True
