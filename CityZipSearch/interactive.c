#include "lib.h"
#include "interactive.h"
#include "structs.h"

/*
This section produces a menu in the commandline allowing the user to find
a zip code by entering the town name, or the name of a town corresponding
to a given zipcode.
*/

//Menu
void doInteractive(zipTowns arrs, int length)
{

    char selection[1], *input = (char *)malloc(1024);
    int index;

    do
    {
        //Validation of selection
        if (selection[0] != '\n')
        {
            printf("Enter:\n1 to Quit\n2 to search for zip\n3 to search for town\n\n");

            fscanf(stdin, "%1s", selection);

            int valid = 0;

            if (selection[0] == '1' || selection[0] == '2' || selection[0] == '3')
            {
                printf("valid selection\n");
            }
            else
            {

                do
                {
                    printf("Invalid selection!\nEnter:\n1 to Quit\n2 to search for zip\n3 to search for town\n\n");

                    fflush(stdin);
                    fscanf(stdin, "%1s", selection);

                    if (selection[0] == '1' || selection[0] == '2' || selection[0] == '3')
                    {
                        valid = 1;
                        printf("valid selection\n");
                    }

                } while (valid == 0);
            }
        }

        //Selection actions
        switch (selection[0])
        {
        case '1':

            printf("Exiting program\n\n");
            break;

        case '2': //Find zip of the given town name

            printf("\nEnter town name to find zip:\n");
            fgetc(stdin);
            fgets(input, 1023, stdin);
            input[strlen(input) - 1] = '\0';
            index = FindZip(arrs, length, input);

            if (index == -1)
            {
                printf("Town name entered was not found\n\n");
            }
            else
            {
                printf("Has the zip: %d\n\n", arrs.cities[arrs.towns[index]].zip);
            }
            break;

        case '3': //Find the town of the given zip

            printf("\nEnter zip to find town:\n");
            fgetc(stdin);
            fgets(input, 1023, stdin);
            input[strlen(input) - 1] = '\0';
            index = FindTown(arrs, length, input);

            if (index == -1)
            {
                printf("Zip entered was not found\n\n");
            }
            else
            {
                printf("Belongs to the town of %s\n\n", arrs.zips[index]->town);
            }
            break;
        }

    } while (selection[0] != '1');

    free(input);
}

//This function uses binary search algorithm to search for zip given a town name.
int FindZip(zipTowns arrs, int length, char *target)
{
    int bottom = 0, mid, top = length - 1, result, len = strlen(target);

    while (bottom <= top)
    {
        mid = (bottom + top) / 2;

        char *toComp = arrs.cities[arrs.towns[mid]].town;

        result = strncmp(toComp, target, len);

        if (result == 0)
        {
            return mid;
        }
        else if (result > 0)
        {
            top = mid - 1;
        }
        else
        {
            bottom = mid + 1;
        }
    }

    return -1;
}

//This function using the same algorith finds the town name corresponding to the given zipcode
int FindTown(zipTowns arrs, int length, char *target)
{
    int bottom = 0, mid, top = length - 1;
    int zipEntered = atoi(target), zip;

    while (bottom <= top)
    {
        mid = (bottom + top) / 2;
        zip = arrs.zips[mid]->zip;

        if (zip == zipEntered)
        {
            return mid;
        }
        else if (zip > zipEntered)
        {
            top = mid - 1;
        }
        else
        {
            bottom = mid + 1;
        }
    }

    return -1;
}
