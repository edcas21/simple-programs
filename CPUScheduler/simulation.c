#include "a2.h"
#include <stdio.h>
#include <stdlib.h>

typedef struct queue
{
    process **array;
    int *size;
    int maxSize;

} q;

void scheduler(q *, q *, process *, os *);
void sort_descending(q *);
void sort_ascending(q *);
//cpu_ready_search(q *);
//io_ready_search(q *);
int isEmpty(q *);
int isFull(q *);
void read(q *, FILE *);
process *dequeue(q *);
void enqueue_descending(q *, process *);
void enqueue_ascending(q *, process *);

int main(int argc, char **argv)
{
    //os parameters stored in os struct
    ui param1 = 70, param2 = 30;
    os *parameters = malloc(sizeof(os));
    parameters->quantum = param1;
    parameters->wait = param2;
    //---------------------------------

    //SCHEDULER COMPONENTS
    // Core
    process *CPU = malloc(sizeof(process));
    CPU->cpu = 0;

    // I/O
    q *IO = malloc(sizeof(q *));
    IO->size = malloc(sizeof(int *));
    *(IO->size) = 0;
    IO->array = malloc(sizeof(process) * 48);
    IO->maxSize = 48;

    // ready queue
    q *ready = malloc(sizeof(q *));
    ready->size = malloc(sizeof(int *));
    *(ready->size) = 0;
    ready->array = malloc(sizeof(process) * 48);
    ready->maxSize = 48;
    //---------------------------------

    //attempt to open file
    FILE *sourceFile;
    if ((sourceFile = fopen(argv[1], "r")) == NULL)
    {
        printf("Cannot open file provided");
    }
    //---------------------------------

    //Load the ready queue
    read(ready, sourceFile);
    printf("ready size = %d\n", *(ready->size));
    /* for (int i = 0; i < *(ready->size); i++)
    {
        printf("%u %u %u %u\n", ready->array[i]->priority, ready->array[i]->cpu, ready->array[i]->io, ready->array[i]->pid);
    }*/

    //Start up the cpu
    scheduler(IO, ready, CPU, parameters);

    printf("finished simulation\n");

    enqueue_descending(ready, CPU);
    for (int i = 0; i < *(IO->size); i++)
    {
        enqueue_descending(ready, IO->array[i]);
    }

    printf("ready size = %d\nIO size = %d\n", *(ready->size), *(IO->size));

    printf("\nPrinting stats: \n");

    //printStats(ready->array, parameters);

    return 0;
}

void scheduler(q *IO, q *ready, process *CPU, os *parameters)
{
    int time = 0;

    while (time < 10000)
    {
        //Put the top process in the CPU at the start of the simulation
        if (time == 0)
        {
            CPU = dequeue(ready);
            printf("Loading cpu for the first time: pid = %u\n", CPU->pid);
        }

        //Increment time, followed by the update of fields in each process
        time++;

        //curCPU
        CPU->curCpu += 1;

        //wait
        //increment the wait in each process in the ready queue
        for (int i = 0; i < *(ready->size); i++)
        {
            ready->array[i]->wait += 1;
        }

        //if 70 ticks have gone by
        if ((time % parameters->quantum) == 0)
        {
            //Check if the process in the CPU has to go to IO
            if (CPU->curCpu >= CPU->cpu)
            {
                //reset curCPU back to 0
                CPU->curCpu = 0;
                //Update the total time spent in the cpu now that it has spent the time that it needs to in the CPU
                CPU->cpuTotal += CPU->cpu;
                //Reset the priority back to its original priority
                CPU->curPrior = CPU->priority;
                //Update the wait min and wait max
                if (CPU->waitMin > CPU->wait)
                {
                    CPU->waitMin = CPU->wait;
                }

                if (CPU->waitMax < CPU->wait)
                {
                    CPU->waitMax = CPU->wait;
                }
                //Update total wait time for that process
                CPU->waitSum += CPU->wait;
                //Reset wait time
                CPU->wait = 0;
                //Send process to IO
                enqueue_ascending(IO, CPU);
                //Send a new process from the ready queue over to the CPU
                CPU = dequeue(ready);
                printf("Process in CPU has completed its time: pid = %u\n", CPU->pid);
            }
            //If not, then it needs to go back to the ready queue
            else
            {
                //Increment the count for amount of times in the ready queue
                CPU->waitCount += 1;
                //Send back into the ready queue
                enqueue_descending(ready, CPU);
                //Get the next process at the top of the ready queue
                CPU = dequeue(ready);
            }
        }

        //Always check if the current process in the CPU is ready to get sent over to the ready queue
        if (CPU->curCpu >= CPU->cpu)
        {
            //reset curCPu back to 0
            CPU->curCpu = 0;
            //Update the total time spent in the CPU
            CPU->cpuTotal += CPU->cpu;
            //Reset priority back to the original priority
            CPU->curPrior = CPU->priority;
            //Update the wait min and wait max

            //Update the total wait time
            CPU->waitSum += CPU->wait;
            //Reset wait time
            CPU->wait = 0;
            //Send process in the CPU to IO
            enqueue_ascending(IO, CPU);
            //Get the next process from the ready queue
            CPU = dequeue(ready);
        }

        //curIO
        //Check if there is any process in IO that is ready to get sent back into the ready queue
        for (int i = 0; i < *(IO->size); i++)
        {
            //Go ahead and increment the time spent in IO for each process inside of it
            IO->array[i]->curIo += 1;
            //Check the IO time and see if it is over
            if (IO->array[i]->curIo >= IO->array[i]->io)
            {
                //Process that is going to be sent to the ready queue
                process *toGo = dequeue(IO);
                //Update the total amount of time spent in IO
                toGo->ioTotal += toGo->io;
                //Reset the curIO back to 0
                toGo->curIo = 0;
                //Send process into the ready queue
                enqueue_descending(ready, toGo);
            }
        }

        //curPrior
        //Check to see if the priority of the processes in the ready queue need to be increased
        if ((time % parameters->wait) == 0)
        {
            //Iterate through each process
            for (int i = 0; i < *(ready->size); i++)
            {
                //Go ahead and update the wait time for each one
                ready->array[i]->wait += 1;

                //Then check if that individual process is ready for a priority increase
                if ((ready->array[i]->wait % parameters->wait) == 0)
                {
                    //Increment the priority
                    ready->array[i]->curPrior += 1;
                }
            }
        }
    }
}

void read(q *queue, FILE *source)
{
    int trash;

    while (!feof(source))
    {
        process *newProcess = malloc(sizeof(process));
        fscanf(source, "%u %u %u %u", &newProcess->priority, &newProcess->cpu, &newProcess->io, &trash);

        //insert into IO by wait time
        if (newProcess->cpu != 0 && newProcess->io != 0)
        {
            //currents
            newProcess->curPrior = newProcess->priority;
            newProcess->curCpu = 0;
            newProcess->curIo = 0;
            newProcess->wait = 0;

            //totals
            newProcess->ioTotal = 0;
            newProcess->cpuTotal = 0;

            newProcess->waitCount = 0;
            newProcess->waitSum = 0;
            newProcess->waitMin = 0;
            newProcess->waitMax = 0;

            enqueue_descending(queue, newProcess);
        }
    }

    fclose(source);

    printf("succesfully read the file\n");
}

process *dequeue(q *queue)
{
    process *ret = malloc(sizeof(process));
    ret = queue->array[0];
    //Shift elements
    for (int i = *(queue->size) - 1; i >= 0; i--)
    {

        if (i > 0)
        {
            queue->array[i] = queue->array[i - 1];
        }
    }

    *(queue->size) -= 1;

    printf("pid = %u\n", ret->pid);

    return ret;
}

void sort_descending(q *queue)
{

    int j;
    process *temp;
    for (int i = 0; i < *(queue->size); i++)
    {
        for ((j = i + 1); j < *(queue->size); j++)
        {
            if (queue->array[i]->priority <= queue->array[j]->priority)
            {
                //swap
                temp = queue->array[i];
                queue->array[i] = queue->array[j];
                queue->array[j] = temp;
            }
        }
    }
}

void sort_ascending(q *queue)
{

    int j;
    process *temp;
    for (int i = 0; i < *(queue->size); i++)
    {
        for ((j = i + 1); j < *(queue->size); j++)
        {
            if (queue->array[i]->io > queue->array[j]->io)
            {
                //swap
                temp = queue->array[i];
                queue->array[i] = queue->array[j];
                queue->array[j] = temp;
            }
        }
    }
}

void enqueue_ascending(q *queue, process *toInsert)
{
    int j = 0;

    if (isEmpty(queue) == 1)
    {
        queue->array[0] = toInsert;
        queue->array[0]->pid = 0; //update pid
    }
    else
    {
        j = *(queue->size);

        while (j >= 0 && queue->array[j - 1]->io > toInsert->io)
        {

            queue->array[j] = queue->array[j - 1];
            queue->array[j]->pid = j; //update pid

            j--;
        }

        queue->array[j] = toInsert;
        queue->array[j]->pid = j;
    }

    *(queue->size) += 1;
}

void enqueue_descending(q *queue, process *toInsert)
{
    int j = 0;

    if (isEmpty(queue) == 1)
    {
        queue->array[0] = toInsert;
        queue->array[0]->pid = 0; //update pid
    }
    else
    {

        j = *(queue->size);

        while (j > 0 && queue->array[j - 1]->priority <= toInsert->priority)
        {

            queue->array[j] = queue->array[j - 1];
            j--;
        }

        queue->array[j] = toInsert;
        queue->array[j]->pid = j;
    }

    *(queue->size) += 1;

    //Update the head and tail of queue
    //queue->head = queue->array[0];
    //queue->tail = queue->array[*(queue->size) - 1];
}

int isEmpty(q *queue)
{
    return (*(queue->size) == 0); //1 if ==, 0 if !=
}

int isFull(q *queue)
{
    return (*(queue->size) == queue->maxSize); //1 if ==, 0 if !=
}
