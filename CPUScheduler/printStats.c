#include <stdio.h>
#include "a2.h"
#define P_COUNT 48
// a[] must have P_COUNT number of valid structs normally 48 or 0-47 indexes
void printStats(process **a, os *system)
{
    int i, j;
    struct averages
    {
        double average;
        ui priority;
    } avg, avgs[P_COUNT];
    printf("\nTime quantum %u,  Maximum Wait %u\n", system->quantum, system->wait);
    for (int i = 0; i < P_COUNT; i++)
    {
        printf("Process[%d] cpuTotal: %u, ioTotal: %u, waitSum: %u, waitCount: %u\nwaitMin: %u, waitMax: %u\n", i, a[i]->cpuTotal, a[i]->ioTotal, a[i]->waitSum, a[i]->waitCount, a[i]->waitMin, a[i]->waitMin);
    }
    /*
    printf("\nAverage Wait   Priority\n");
    for (i = 0; i < P_COUNT; i++)
    {
        avg.priority = a[i]->priority;
        avg.average = (double)a[i]->waitSum / (double)a[i]->waitCount;
        // sorted insert
        j = i - 1;
        while (j >= 0 && avgs[j].average > avg.average)
        {
            avgs[j + 1] = avgs[j];
            j--;
        }
        avgs[j + 1] = avg;
    } // end for each process
    for (i = 0; i < P_COUNT; i++)
    {
        printf("%12.2lf   %8u\n", avgs[i].average, avgs[i].priority);
    }*/
}
