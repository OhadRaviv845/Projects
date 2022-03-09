"""the function receives a text as a string and a number n. it divides the long string into substrings in the length
of n. it then creates a dictionary, using a for loop to set the keys of the dictionary as the substrings, and the values
are the relative ammount the substrings appear in the dictionary compared to the total number of substrings. the second
for loop is used to determine the frequency in which each key appears."""
# 1. Compute ngram statistics for text
def compute_ngram_frequency(text, n):
    dict = {}
    total = 0
    for i in range(len(text)-n+1):
        key = text[i:i+n]
        if key.isalpha():
            total += 1
            key = key.lower()
            if key in dict.keys():
                dict[key] += 1
            else:
                dict[key] = 1
    for k in dict.keys():
        dict[k] /= total
        dict[k] = round(dict[k], 3)
    return dict


"""the function sets an empty string in the beginning, then loops through a dictionary. for each key and value,
the key and the value are added to the string in the end of it. the function then returns a string of the converted
dictionary."""
# 2. Concatenate items of dictionary to one string
def ngram_dict_to_string(d):
    s = ""
    for key, value in d.items():
        s += "%s:%.1f "%(key, value)
    return s

"""using two different splits, the function extracts the singular words out of the string. it then uses the list index
of the split output de set each couple of words as key:value in the dictionary. it returns the final dictionary."""
# 3. split string to dictionary
def string_to_ngram_dict(s):
    d = {}
    for e in s.split(" "):
        es = e.split(":")
        if len(es) >= 2:
            d[es[0]] = float(es[1])
    return d


"""the function receives a dictionary and converts it to string using a prior function. it then opens a file and writes
in to it the converted text. finally the file is closed and saved."""
# 4. Save to file
def write_ngram_dict(dict, filename):
    dict = ngram_dict_to_string(dict)
    f = open(filename, "w")
    f.write(dict)
    f.close()
    return


"""the function opens a file and reads its text. it then uses a prior function to convert the text into 
a new dictionary."""
# 5. Load from file
def read_ngram_dict(filename):
    f = open(filename, "r")
    text = f.read()
    dict = string_to_ngram_dict(text)
    f.close()
    return dict


######################## Helper Functions ########################
