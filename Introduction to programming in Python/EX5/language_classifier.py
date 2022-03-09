from ngrams import *
import os

"""the function recieves a text file, n-grams length, and a dict_filename to save. the function starts by testing if
there is a file that's not empty for the name given. if there is it saves its text to the raw_text parameter. using
this parameter it computes the frequency of each combination in the length of n in the text. if there is no file or if
there is no text in the file the function would return none. if there is a dict_filename given the function will
save the edited dictionary and write it to a file with the given name."""
# 6. Build an n-gram dictionary from text in a file
def build_language_model(language_filename, n, dict_filename=''):
    try:
        if os.path.isfile(language_filename):
            f = open(language_filename, "r")
            raw_text = f.read()
            f.close()
            freq_dict = compute_ngram_frequency(raw_text, n)
            if not freq_dict:
                return None
    except:
        return None
    if dict_filename:
        write_ngram_dict(freq_dict, dict_filename)
    return freq_dict


"""the function receives two dictionaries and compares the square distance between them. it first goes through all the
keys in dict1, then goes through all the keys in dict2 to check if any key from dict 1 is in dict 2. if the key exists, 
the function adds the difference between the two for the total distance of the dictionaries. if the key only exists in
dict1 it's value is added. then the function does the test for all the keys that are only in dict2 and adds them as well.
the function returns the square distance between both dictionaries."""
# 7. Compute distance between two dictionaries
def compute_ngram_distance(dict1, dict2):
    difference = 0
    for key in dict1:
        if key in dict2:
            difference += (dict1[key] - dict2[key]) ** 2
        else:
            difference += dict1[key] ** 2
    for key in dict2:
        if not key in dict1:
            difference += dict2[key] ** 2
    return difference


"""the function recieves a dictionary and a variable. first it defines a new dictionary, then goes through all keys and
vals of the given dictionary. if the length of the key in the dictionary the function returns an error message and returns
False. if the n is smaller than the keys in the dictionary, the function then checks if the key in the length of n is 
already in the new dictionary - if it is then it adds it to it's frequency, if it's not yet in the keys then it's
set as a new key. the function returns the new dictionary."""
# 8. Reduce n-gram frequencies
def reduce_ngram(d, n):
    new_d = {}
    for key, val in d.items():
        if len(key) < n:
            print("Error! Current ngram size smaller than %d"%n)
            return False
        if key[0:n] in new_d:
            new_d[key[0:n]] += val
        else:
            new_d[key[0:n]] = val
    return new_d


"""the function recieves a text file and a list of two dictionaries. it has a variable n and min_distance to be used
later. min_distance starts from the maximum it can be = 1 (frequency of appearence). it goes through each dictionary 
in the list and reads the text, appending it to the dict_list. it then goes through each key of the dictionary and checks
if n==0 or if len(key) < n, if one of these is true n becomes len key. after one value it stops the test for each 
dictionary. after going through all the dictionaries the function has the number of the minimum length of a value out
of all the dictionaries. this number is the number with which the function checks the text to classify it. it then goes
on to check which of the dictionaries has the smallest distance to the text. in the end the function returns the index
of the closest dictionary to the text."""
# 9. Build a language classifier
def classify_language(text_to_classify, list_of_dict_files):
    n = 0
    min_distance = 1
    dict_list = []
    for file_name in list_of_dict_files:
        dict = read_ngram_dict(file_name)
        dict_list.append(dict)
        for key in dict:
            if n == 0 or len(key) < n:
                n = len(key)
            break
    text_freq = build_language_model(text_to_classify, n)
    for i, dict in enumerate(dict_list):
        distance = compute_ngram_distance(text_freq, reduce_ngram(dict, n))
        if distance < min_distance:
            min_distance = distance
            min_index = i
    return min_index


######################## Helper Functions ########################
