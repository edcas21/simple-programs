//Author: Edgar Cardenas

#include<stdio.h>
#include<pthread.h>
#include<stdlib.h>
#include "apue.h"
#include <dirent.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <sys/time.h>
//-------------------------------------------------------
typedef struct Record{

  char *username;
  char *password;
  char *bloodType;
  char *domain;
  int index;
} Record;
//-------------------------------------------------------
typedef struct DBPart{

  Record *records;
  int size;
} DBPart;
//function declarations---------------------------------
void *process(void*);//thread process

Record separator(char*);//string tokenizer

int comparator(const void *struct1, const void *struct2);//for qsort

void writer(Record *fullDataBase, int size);//for writing sorted database

void mergeSort(Record *arr, int l, int r);//main sorting function

void merge(Record *arr, int l, int m, int r);//auxillary function for merge sort
//-------------------------------------------------------
//Globals
int thread_num;
char newPath[1024];
DBPart **DataBase;
int curIndex = 0;
//-------------------------------------------------------
int main(int argc, char *argv[]){

  struct timeval begin, end;
  gettimeofday(&begin, NULL);

  char *directory;
  thread_num = 0;

  if(!argv[1]) {
    printf("No path given\n");
    return 0;
  }

  directory = argv[1];
  char workdir[1024];
  //get the working directory
  getcwd(workdir, sizeof(workdir));
  
  //Concatenate the new directory to the working directory
  snprintf(newPath, sizeof(newPath), "%s/%s", workdir, directory);

  DIR *theFiles;
  
  //Change into the new directory
  if(chdir(newPath) == 0){

    theFiles = opendir(newPath);//open it
    struct stat buf1;
    struct dirent *d = NULL;

    //get number of files for the number of threads to be created
    while((d = readdir(theFiles)) != NULL){

      lstat(d->d_name, &buf1);
      
      //Make sure that the files are actually readable
      if(S_ISREG(buf1.st_mode) && strcmp(d->d_name, "sorted.yay") != 0){
        thread_num ++;
      }
    }

    closedir(theFiles);

    pthread_t allThreads[thread_num];
    char *fileNames[thread_num];

    DIR *theFiles2 = opendir(newPath);//Array of pathnames to all the files

    int t = 0;
    
    //Fill the array of pathnames
    while((d = readdir(theFiles2)) != NULL && t < thread_num){

      lstat(d->d_name, &buf1);

      if(S_ISREG(buf1.st_mode) && strcmp(d->d_name, "sorted.yay") != 0){
        char curdir[1024];
        getcwd(curdir, sizeof(curdir));
        char newPathology[1024];
        snprintf(newPathology, sizeof(newPath), "%s/%s", curdir, d->d_name);

        fileNames[t] = strdup(newPathology);
        t++;
      }
    }
    closedir(theFiles2);

    //create the threads
    for(int i = 0; i < thread_num; i++){

      if(pthread_create(&allThreads[i], NULL, process, fileNames[i]) != 0){
        perror("pthread_create() failure.");
        exit(1);
      }
    }

    //join the threads
    void *results[thread_num];
    for(int i = 0; i < thread_num; i++){

      if(pthread_join(allThreads[i], (void*)&results[i]) != 0){
        perror("thr_join() failure.");
        exit(1);
      }
    }

    int totalRecords = 0;
    DBPart *parts = malloc(sizeof(DBPart) * thread_num);
    for(int i = 0; i < thread_num; i++){
      DBPart *part = results[i];
      totalRecords += part->size;
      parts[i] = *part;
    }

    Record *fullDataBase = malloc(sizeof(Record) * totalRecords);
    curIndex = parts[0].size;
    memcpy(fullDataBase, parts[0].records, (totalRecords * sizeof(Record)));

    //combine and merge sort arrays
    for(int i = 1; i < thread_num; i++){
      if(curIndex < totalRecords){
        for(int j = 0; j < parts[i].size; j++){
          if((curIndex + j) < totalRecords){
          fullDataBase[curIndex + j] = parts[i].records[j];
         }
        }
        curIndex += parts[i].size;
      }
     }

     mergeSort(fullDataBase, 0, totalRecords - 1);
     writer(fullDataBase, totalRecords);//write the sorted records to the final file

  }else{
    printf("Cannot open directory");
  }

  gettimeofday(&end, NULL);
  double elapsed = (end.tv_sec - begin.tv_sec) + ((end.tv_usec - begin.tv_usec)/1000000.0);

  printf("Time elapsed: %f ms\n", elapsed);

  return 0;
}
//-------------------------------------------------------
//Function to write the sorted records to the final file
void writer(Record *fullDataBase, int size){

  FILE *file = fopen("sorted.yay", "w+");
  for(int i = 0; i < size; i++){
    if(fullDataBase[i].username != NULL || fullDataBase[i].password != NULL || fullDataBase[i].bloodType != NULL || fullDataBase[i].domain != NULL){
      fprintf(file, "%s,%s,%s,%s,%d\n", fullDataBase[i].username, fullDataBase[i].password, fullDataBase[i].bloodType, fullDataBase[i].domain, fullDataBase[i].index);
      fprintf(stdout, "%d,%s,%s,%s,%s\n", fullDataBase[i].index, fullDataBase[i].username, fullDataBase[i].password, fullDataBase[i].bloodType, fullDataBase[i].domain);

    }
  }
}
//-------------------------------------------------------
//Function that gives the process that will be done by each of the threads
void *process(void *filename) {

  char *file = filename;
  FILE *sourceFile;

  if((sourceFile = fopen(file, "r")) == NULL){
    fprintf(stderr, "Can't open source file\n");
    exit(2);
  }

  if(setvbuf(sourceFile, NULL, _IOFBF, 4096) != 0){
    fprintf(stderr, "Can't create buffer\n");
    exit(3);
  }

  int numOfRecords = 0;
  size_t bytes;
  char buffer[4096];
  char *buffCopy = (char *) malloc(sizeof(char) * 4096);

  //get number of records in the file
  while (!feof(sourceFile)) {
    numOfRecords++;
    fgets(buffer, 4096, sourceFile);
  }
  fclose(sourceFile);

  FILE *sourceFileRe = fopen(filename, "r");

  if(setvbuf(sourceFileRe, NULL, _IOFBF, 4096) != 0){
    fprintf(stderr, "Can't create buffer");
    exit(3);
  }

  DBPart *part = malloc(sizeof(DBPart));
  part->records = malloc(sizeof(Record) * numOfRecords);
  part->size = numOfRecords;
  int recordNum = 0;
  //start filling in the array
  while ((bytes = fread(buffer, sizeof(char), 4096, sourceFileRe)) > 0 && recordNum < part->size) {

    Record record;

    int i = 0, offset;
    while (buffer[i] != '\n' && i < 4096) {
      buffCopy[i] = buffer[i];
      i++;
    }
    offset += i + 1;
    buffCopy[i] = '\0';

    record = separator(buffCopy);
    if(record.username != NULL){
      part->records[recordNum] = record;
      recordNum++;
    }

    fseek(sourceFile, offset, SEEK_SET);
  }

  fclose(sourceFileRe);

  qsort((void *)part->records, part->size, sizeof(Record), comparator);

  return part;
}
//-------------------------------------------------------
//Function that tokenizes a line read from the files to create a record struct
Record separator(char *line){

  int c = 0;
  char *token;
  Record record;

  while((token = strsep(&line, ",\t\n")) != NULL){

    switch(c){
      //username
      case 0:
      record.username = malloc(1024);
      strcpy(record.username, token);

      break;
      //password
      case 1:
      record.password = malloc(1024);
      strcpy(record.password, token);

      break;
      //blood type
      case 2:
      record.bloodType = malloc(1024);
      strcpy(record.bloodType, token);

      break;
      //domain name
      case 3:
      record.domain = malloc(1024);
      strcpy(record.domain, token);

      break;
      //database index
      case 4:
      record.index = atoi(token);

      break;

    }
    c++;
  }

  free(line);
  return record;
}
//-------------------------------------------------------
//Function used to make the record structs comparable so that they are able to be sorted
int comparator(const void *struct1, const void *struct2){

  int result;

  if((((Record *)struct1) -> index) > (((Record *)struct2) -> index)){
    result = 1;
  }else{
    result = 0;
  }

  return result;
}
//-------------------------------------------------------
//Function used to merge all the databases into one file
void merge(Record *database, int left, int middle, int right)
{
    int i, j, k;
    int sub1 = middle - left + 1;
    int sub2 =  right - middle;

    //create temp arrays
    Record *mainL = malloc(sizeof(Record)*sub1), *mainR = malloc(sizeof(Record) * sub2);

    //Copy data to temp arrays L[] and R[]
    for (i = 0; i < sub1; i++)
        mainL[i] = database[left + i];
    for (j = 0; j < sub2; j++)
        mainR[j] = database[middle + 1+ j];

    //Merge the temp arrays back into arr[l..r]
    i = 0; // Initial index of first subarray
    j = 0; // Initial index of second subarray
    k = left; // Initial index of merged subarray
    while (i < sub1 && j < sub2)
    {
        if (mainL[i].index <= mainR[j].index)
        {
            database[k] = mainL[i];
            i++;
        }
        else
        {
            database[k] = mainR[j];
            j++;
        }
        k++;
    }

    //Copy the remaining elements of L[], if there are any
    while (i < sub1)
    {
        database[k] = mainL[i];
        i++;
        k++;
    }

    //Copy the remaining elements of R[], if there are any
    while (j < sub2)
    {
        database[k] = mainR[j];
        j++;
        k++;
    }
}
//-------------------------------------------------------
//Function used to sort arrays of record structs
void mergeSort(Record *database, int left, int right)
{
    if (left < right)
    {
        // Same as (l+r)/2, but avoids overflow for large l and h
        int middle = left+(right-left)/2;

        // Sort first and second halves
        mergeSort(database, left, middle);
        mergeSort(database, middle+1, right);

        merge(database, left, middle, right);
    }
}
