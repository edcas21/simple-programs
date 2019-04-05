//Author: Edgar Cardenas

#include "apue.h"
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <math.h>
#define BUFFSIZE 4096
int size = 1000;//Globally keep track of the size of the array of song structs

//Song Struct
typedef struct Song{
	//fields
	char *artist;
	char *songName;
	char *albumName;
	float duration;
	int releaseYr;
	double hottness;
} Song;

char* cleanString(char *string);//removes quotations from the information read

Song separator(char *line);//tokenizes the lines to create song structs from the informatioin

int binary_search(Song *songs, int size, char *target);//Searching song by name

Song* grow(Song *songs, int origSize);//Grows an existing array

Song* dynamArrayOpt(Song *songs, int origSize);//Optimizes array made in the function grow()

int comparator(const void *struct1, const void *struct2);//Comparator used for sorting

int main(int argc, char *argv[]){

	char *buffer = (char*)malloc(sizeof(char)* BUFFSIZE), line[BUFFSIZE], *test = (char *)malloc(BUFFSIZE);

	//Create an initial array to start holding songs read from the file
	Song *songs = malloc(1000 * sizeof(Song)), song;
	
	//Prepare file to be read
	int file = open("SongCSV.csv", O_RDONLY), reader;

	//Check to see if the file actually exists
	if(file < 0){
		fprintf(stderr, "Unable to open file or file does not exist");
		exit(1);
	}

	//Prepare to read file
	int s = 0, offset = 0, delete = 0, i;

	while((reader = read(file, buffer, BUFFSIZE)) != 0){

		i = 0;
		//Copy content from the buffer so that it can be processed and cleared
		while(buffer[i] != '\n' && i < BUFFSIZE){

			line[i] = buffer[i];

			i++;
		}

		offset += i + 1;
		
		//ensure there's a null terminating character at the end of the content string
		line[i] = '\0';

		//Song struct creation
		strcpy(test, line);//transfer contents to a pointer

		if(delete > 0){//Avoid the first line of the file which conatains the headers

			song = separator(test);//send pointer to the content string to be tokenize
			
			//make sure that the song array is not fill
			if(s < size){

				songs[s] = song;
			}else{//if it is, then grow the array

				songs = grow(songs, size);
				size += 1000;//increasing by 1000
				songs[s] = song;
			}

			s++;//song counter
		}

		if(delete == 0){
			delete++;//indicates that we're past the first line
		}
		
		//Start reading and placing content into the buffer where we last left off
		lseek(file, offset, SEEK_SET);
	}

	close(file);

	songs = dynamArrayOpt(songs, size);//Trim the final array of empty array spaces

	qsort((void *)songs, size, sizeof(Song), comparator);//sort the array

	/*for(int i = 0; i < size; i++){

		if(i != 0){
			printf(" #%d\n", i);
			printf("   song name: %s\n", songs[i].songName);
			printf("      artist: %s\n", songs[i].artist);
			printf("  album name: %s\n", songs[i].albumName);
			printf("    duration: %f\n", songs[i].duration);
			printf("release year: %d\n", songs[i].releaseYr);
			printf("    hottness: %f\n\n", songs[i].hottness);
		}
	}*/

	printf("total # of songs = %d\n", (s - 1));

	char *targetSong = malloc(1024), *temp = malloc(1024), *exit = "ZZZ";
	int len = 0;

	//Interface
	do{
		//Ask for the song name
		printf("\nEnter the song name to search. 'ZZZ' to quit:\n\n");
		fgets(targetSong, 1024, stdin);
		printf("\n");

		len = strlen(targetSong);
		strcpy(temp, targetSong);
		temp[len - 1] = '\0';
		targetSong[len - 1] = '\0';
		
		//Look for exit condition
		if(strcmp(targetSong, exit) != 0){
			
			//Otherwise search for song name entered
			int ind = binary_search(songs, size, temp);
			if(ind == -1){

				printf("\"%s\" was not found\n", targetSong);
			}else{
			//If found, then print the information
				printf(" Song Name: %s\n Artist: %s\n Album: %s\n Duration: %f\n Year of Release: %d\n Hottness: %f\n", songs[ind].songName, songs[ind].artist, songs[ind].albumName, songs[ind].duration, songs[ind].releaseYr, songs[ind].hottness);
			}
		}

		printf("------------------------------------------------\n");

	}while(strcmp(targetSong, exit) != 0);

	return 0;
}

//Function that gets rid of the quotations marks that come with each token
char* cleanString(char *string){

	int size = strlen(string);
	char *fixed = malloc((sizeof(char) * (size - 2)));

	int ind = 0;

	for(int i = 1; i < (size - 1); i++){

		fixed[ind] = string[i];

		ind++;

	}

	return fixed;
}

//Function that tokenizes the information read from the file in order to create the song structs
Song separator(char *line){
	int c = 0;//counter for keeping track of the position at which we are tokenizing

	char *token;

	Song song;
	
	//Mainly separating at commas
	while((token = strsep(&line, ",\t\n")) != NULL){
	
		//Information for the struct is identified based off of its position in the line
		switch(c){
						//Artist
			case 8:
			song.artist = malloc(1024);
			strcpy(song.artist, cleanString(token));
					//printf("Artist: %s\n", song->artist);

			break;
						//Name of Song
			case 17:
			song.songName = malloc(1024);
			strcpy(song.songName, cleanString(token));
					//printf("Song: %s\n", song->songName);

			break;
						//Album Name
			case 3:
			song.albumName = malloc(1024);
			strcpy(song.albumName, cleanString(token));
					//printf("Album: %s\n", song->albumName);

			break;
						//Duration
			case 10:
			song.duration = atof(token);
					//printf("Duration: %f\n", song->duration);

			break;
						//Year of Release
			case 18:
			song.releaseYr = atoi(token);
					//printf("Release: %d\n", song->releaseYr);

			break;
						//Hottness
			case 14:
			if(isnan(atof(token))){
				song.hottness = 0.0;
			}else{
				song.hottness = atof(token);
			}

					//printf("Hottness: %f\n", song->hottness);
			break;
		}

		c++;
	}
		//printf("\n\n");

	return song;
}

//Function used to search for songs inside the array of song structs
int binary_search(Song *songs, int size, char *target){
	int bottom= 0;
	int mid;
	int top = size - 1;

	while(bottom <= top){
		mid = (bottom + top)/2;
		if (strcmp(songs[mid].songName, target) == 0){
			return mid;
		} else if (strcmp(songs[mid].songName, target) > 0){
			top = mid - 1;
		} else if (strcmp(songs[mid].songName, target) < 0){
			bottom = mid + 1;
		}
	}

	return -1;
}

//Function used to dynamically grow the array of song structs
Song* grow(Song *songs, int origSize){

	Song *new = malloc(sizeof(Song) * (origSize + 1000));
	memcpy(new, songs, (origSize * sizeof(Song)));
	free(songs);
	songs = new;

	return songs;
}

//Function used to get rid of empty array cells
Song* dynamArrayOpt(Song *songs, int origSize){

	int tempSize = origSize;
	int actualSize = 0;

	while(songs[tempSize].songName == NULL){

		actualSize = tempSize--;
	}

	Song * new = malloc(sizeof(Song) * (actualSize));
	memcpy(new, songs, (actualSize * sizeof(Song)));
	free(songs);
	songs = new;
	size = actualSize;

	return songs;
}

//Function that makes structs comparable to enable sorting
int comparator(const void *struct1, const void *struct2){
	return strcmp(((Song *)struct1) -> songName, ((Song*)struct2) -> songName);
}
