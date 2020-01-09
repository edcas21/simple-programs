CC = gcc
CFLAGS = -g -Wall

source : source.o readfile.o interactive.o

source.o : readfile.h structs.h lib.h interactive.h

readfile.o : readfile.h structs.h lib.h

interactive.o : interactive.h structs.h lib.h

clean:
	rm -f *.o source

run:
	./source zip_code_sample.txt 30 < test-menu-input.txt
