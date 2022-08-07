all: tweets snake
tweets: tweets_generator
tweets_generator: tweets_generator.o markov_chain.o linked_list.o
	gcc -o tweets_generator tweets_generator.o markov_chain.o linked_list.o
snake: snakes_and_ladders
snakes_and_ladders: snakes_and_ladders.o markov_chain.o linked_list.o
	gcc -o snakes_and_ladders snakes_and_ladders.o markov_chain.o linked_list.o
