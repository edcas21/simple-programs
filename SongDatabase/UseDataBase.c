/*
Edgar Cardenas
CS 3240
*/
#include "apue.h"
#include <errno.h>
#define BUFFSIZE 4096
int sizeOfDir;

typedef struct Song{

	char *artist;
	char *songName;
	char *albumName;
	float duration;
	int releaseYr;
	double hottness;
} Song;

typedef struct{

	char *title;
	int offset;
	int len_title;
	int len_artist;
	int len_album;
} Song_dir;

int binary_search(Song_dir *dir, int size, char *target);

//creates a song directory struct
Song_dir recordCreate(int len_title, int len_artist, int len_album, int offset, char *title);

//prints song information
void printSongInfo(int offset, FILE *binarySongs, int index, Song_dir *dir);

int main(int argc, char *argv[]){

	FILE *binarySongs, *directory;
	Song_dir *songDir;

	//Open the files
	binarySongs = fopen("BinarySongData.bin", "rb+");
	if(binarySongs == NULL){
		perror("Error Occured");
		printf("Error Code: %d\n", errno);
	}

	directory = fopen("directory.bin", "rb+");
	if(binarySongs == NULL){
		perror("Error Occured");
		printf("Error Code: %d\n", errno);
	}

	int index = -1;

	//read directory and store in array
	while(!feof(directory) && index < sizeOfDir){

		if(index == -1){
			fread(&sizeOfDir, sizeof(int), 1, directory);
			songDir = malloc(sizeof(Song_dir) * sizeOfDir);
		}else{

			int titleInt;
			fread(&titleInt, sizeof(int), 1, directory);

			int albumInt;
			fread(&albumInt, sizeof(int), 1, directory);

			int artistInt;
			fread(&artistInt, sizeof(int), 1, directory);

			int offset;
			fread(&offset, sizeof(int), 1, directory);

			char *song_name = malloc(1024);
			fread(song_name, sizeof(char), titleInt, directory);

			songDir[index] = recordCreate(titleInt, artistInt, albumInt, offset, song_name);

		}


		index++;
	}


	/*	for(int i = 0; i < sizeOfDir; i++){
	printf("[%d]\n", i);
	printf("strlen title: %d\n", songDir[i].len_title);
	printf("strlen album: %d\n", songDir[i].len_album);
	printf("strlen artist: %d\n", songDir[i].len_artist);
	printf("offset: %d\n", songDir[i].offset);
	printf("songName: %s\n", songDir[i].title);
	printf("\n");
}*/
//-------------------------------------------------------------------------------
char *targetSong = malloc(1024), *exit = "ZZZ";
int len = 0;

char* blank = "";
char* cop = malloc(1024);
int blen = strlen(blank);

//Interface
do{

	printf("\nEnter the song name to search. 'ZZZ' to quit:\n\n");
	fgets(targetSong, 1024, stdin);
	printf("\n");

	len = strlen(targetSong);
	targetSong[len - 1] = '\0';

	if(strcmp(targetSong, exit) != 0){

		int ind = binary_search(songDir, sizeOfDir, targetSong);
		if(ind == -1){

			printf("\"%s\" was not found\n", targetSong);
		}else{

			printSongInfo(songDir[ind].offset, binarySongs, ind, songDir);
		}
	}

	printf("------------------------------------------------\n");

}while(strcmp(targetSong, exit) != 0);

fclose(binarySongs);
fclose(directory);

return 0;
}

//Function that creates a song directory struct
Song_dir recordCreate(int len_title, int len_artist, int len_album, int offset, char *title){

	Song_dir record;
	record.title = malloc(1024);

	record.len_title = len_title;
	//printf("%d\n", record.len_title);
	record.len_album = len_album;
	//printf("%d\n", record.len_album);
	record.len_artist = len_artist;
	//printf("%d\n", record.len_artist);
	record.offset = offset;
	//printf("%d\n", record.offset);
	strcpy(record.title, title);
	//printf("%s\n\n", record.title);

	return record;

}

//Search function
int binary_search(Song_dir *dir, int size, char *target){
	int bottom= 0;
	int mid;
	int top = size - 1;

	while(bottom <= top){
		mid = (bottom + top)/2;
		if (strcmp(dir[mid].title, target) == 0){
			return mid;
		} else if (strcmp(dir[mid].title, target) > 0){
			top = mid - 1;
		} else if (strcmp(dir[mid].title, target) < 0){
			bottom = mid + 1;
		}
	}

	return -1;
}

//Function that prints out the information of the song in question and prints out the info stored in the binary song file
void printSongInfo(int offset, FILE *binarySongs, int index, Song_dir *dir){

	char *artistName = malloc(1024);
	char *albumName = malloc(1024);
	float duration;
	int releaseYr;
	double hottness;

	fseek(binarySongs, offset, SEEK_SET);

	fread(artistName, sizeof(char), dir[index].len_artist, binarySongs);
	fread(albumName, sizeof(char), dir[index].len_album, binarySongs);
	fread(&duration, sizeof(float), 1, binarySongs);
	fread(&releaseYr, sizeof(int), 1, binarySongs);
	fread(&hottness, sizeof(double), 1, binarySongs);

	printf("Song Name: %s\nArtist: %s\nAlbum: %s\nDuration: %f\nYear of Release: %d\nHottness: %f\n", dir[index].title, artistName, albumName, duration, releaseYr, hottness);

}
