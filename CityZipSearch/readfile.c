#include "lib.h"
#include "readfile.h"

/*
This section reads a text file that is tokenized into city structs that are
stored and sorted in a zipTowns struct.
*/

//This function reads the text file and sorts structs using insertion sort
void readFile(zipTowns arrs, FILE *infile, int *length)
{

    char *buff = (char *)malloc(sizeof(char) * 4096);
    int i = 0, j, res;

    do
    {
        arrs.cities[i].town = (char *)malloc(sizeof(char) * 256);

        res = fscanf(infile, "%u   %s", &arrs.cities[i].zip, arrs.cities[i].town);

        if (arrs.zips[0] != NULL)
        {
            j = i - 1; //left index
            //compare left index zip to zip of struct to be inserted
            while (j >= 0 && arrs.zips[j]->zip > arrs.cities[i].zip)
            {
                //shift greater zip to the right
                arrs.zips[j + 1] = arrs.zips[j];
                //move to the next left index
                j--;
            }

            //everything is sorted, now insert into the correct spot
            arrs.zips[j + 1] = &arrs.cities[i];
            // printf("here\n");

            //sort the indexes from main array by town name
            j = i - 1;
            while (j >= 0 && strcmp(arrs.cities[arrs.towns[j]].town, arrs.cities[i].town) > 0)
            {
                arrs.towns[j + 1] = arrs.towns[j];
                j--;
            }
            arrs.towns[j + 1] = i;
        }
        else
        {
            //add the first item to the first index of the array
            arrs.zips[0] = &arrs.cities[i];
            arrs.towns[0] = 0;
        }

        i++;

    } while (res == 2);

    *length = i + 1; //set length

    /*for (int i = 0; i < *length; i++)
    {
        printf("town: %s zip: %u\n", arrs.zips[i]->town, arrs.zips[i]->zip);
    }*/

    free(buff);
}
