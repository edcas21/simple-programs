#define _CRT_SECURE_NO_WARNINGS
#include "lib.h"
#include "structs.h"
#include "readfile.h"
#include "interactive.h"

/*
This section is the  main driver of the program.
*/

//This function allocates memory to the arrays within a zipTown struct
void getArrs(zipTowns *arrs, int size)
{ // mallocs arrays of size elements
	arrs->towns = (int *)malloc(sizeof(int) * size);
	arrs->zips = malloc(sizeof(city) * size);
	arrs->cities = (city *)malloc(sizeof(city) * size);
}

extern int getArgsInfoOpenFile(int argc, char *argv[], FILE **infile, int *size); // 0 ok, !0 error

int main(int argc, char *argv[])
{
	zipTowns arrs;  // all the arrays in one struct
	int length = 0; // current count of items in arrays

	FILE *infile = NULL;

	int size;

	if (getArgsInfoOpenFile(argc, argv, &infile, &size))
		printf("error in command line arguments\n");
	else
	{
		getArrs(&arrs, size); //Allocate
		length = size;

		readFile(arrs, infile, &length); //Read, fill arrays and sort
		fclose(infile);
		doInteractive(arrs, length); //Menu and search
	}								 // end else no error in command line
	printf("press any key: \n");
	getc(stdin);

	return 0;
}

//This function validates arguments from the commandlibe and attempts to open file entered by user.
int getArgsInfoOpenFile(int argc, char *argv[], FILE **infile, int *size) // 0 ok else !0 error
{
	int retval = 0;
	// test for correct arguments number 3: exename, filename, size
	if (argc < 3 || argc > 3)
	{
		retval = 1;
		return retval;
	}
	// attempt to open file
	if ((*infile = fopen(argv[1], "r")) == NULL)
	{
		printf("Cannot open file provided\n");
		retval = 1;
		return retval;
	}

	//store size entered by user
	int sizeConvert = atoi(argv[2]);
	*size = sizeConvert;

	// return file and size in parameters or error
	return retval;
}
