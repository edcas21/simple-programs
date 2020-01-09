CC = gcc
CFLAGS = -g -Wall

simulation : simulation.o printStats.o

simulation.o : a2.h

printStats.o : a2.h

clean:
	rm -f *.o source

run:
	./simulation a2in.txt