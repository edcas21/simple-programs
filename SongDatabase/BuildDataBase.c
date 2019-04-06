/*
Edgar Cardenas
CS 3240
*/
#include "apue.h"
#include <math.h>
#define BUFFSIZE 4096
int sizeOfDir;

typedef struct Song{//Song struct

	char *artist;
	char *songName;
	char *albumName;
	float duration;
	int releaseYr;
	double hottness;
} Song;

typedef struct{//Song directory struct

	char *title;
	int offset;
	int len_title;
	int len_artist;
	int len_album;
} Song_dir;

//function declarations

//function used when sorting song directory structs
int comparator(const void *struct1, const void *struct2);

char* cleanString(char *string);//gets rid of quotation marks

Song* separator(char *line);//tokenizes a line of content

Song_dir recordCreate(Song *song);//creates a directory struct from a song struct

//Main part of the program that reads the song file and prepares for writing to a song directory binary file
Song_dir* reader(FILE *source, FILE *dest, FILE *dirFile, Song_dir *directory, int size);

Song_dir* grow(Song_dir *directory, int origSize);//Grows a given array

Song_dir* dynamArrayOpt(Song_dir *directory, int origSize);//Gets rid of empty space in an array

void dirFileCreate(FILE *dirFile, Song_dir *directory);//writes sorted directory struct array to a binary file

void writer(FILE *dest, Song *song);//writes song structs to a binary file

//------------------------------------------------------
int main(int argc, char *argv[]){

	FILE *destFile , *sourFile, *dirFile;//File pointers
	Song_dir* directory = malloc(sizeof(Song_dir) * 1000);//directory array
	int dirSize = 1000;//initial size
	char buf[BUFFSIZE];

	//error checking
	//make sure song csv exists
	if((sourFile = fopen("SongCSV.csv", "r")) == NULL){
		fprintf(stderr, "Can't open source file");
		exit(2);
	}
	
	//Create buffer for content that will be read from the csv
	if(setvbuf(sourFile, NULL, _IOFBF, BUFFSIZE) != 0){
		fprintf(stderr, "Can't create buffer");
		exit(3);
	}


	//create binary files
	destFile = fopen("BinarySongData.bin", "wb");
	dirFile = fopen("directory.bin", "wb");

	//read from csv and create directory
	directory = reader(sourFile, destFile, dirFile, directory, dirSize);
	printf("size of directory: %d\n\n", sizeOfDir);


	//deallocate memory
	free(destFile);
	free(sourFile);
	free(dirFile);
	free(directory);

	return 0;
}
//------------------------------------------------------

//Function used for sorting the song directory structs
int comparator(const void *struct1, const void *struct2){
	return strcmp(((Song_dir *)struct1) -> title, ((Song_dir*)struct2) -> title);
}

//Function used to remove quotation marks from tokens
char* cleanString(char *string){

	int size = strlen(string), ind = 0;
	char *fixed = malloc((sizeof(char) * (size - 2)));

	for(int i = 1; i < (size - 1); i++){

		fixed[ind] = string[i];
		ind++;
	}

	return fixed;
}

//Function used to tokenize content read from song file, which then is converted to song structs
Song* separator(char *line){

	int size, c = 0;
	char *token;
	Song *song = malloc(sizeof(Song));
	
	//Fields for the struct are identified based off of the position within the line
	while((token = strsep(&line, ",\t\n")) != NULL){

		switch(c){
					//Artist
			case 8:
			song->artist = malloc(1024);
			strcpy(song->artist, cleanString(token));
				//printf("Artist: %s\n", song->artist);
			break;
					//Name of Song
			case 17:
			song->songName = malloc(1024);
			strcpy(song->songName, cleanString(token));
				//printf("Song: %s\n", song->songName);
			break;
					//Album Name
			case 3:
			song->albumName = malloc(1024);
			strcpy(song->albumName, cleanString(token));
				//printf("Album: %s\n", song->albumName);
			break;
					//Duration
			case 10:
			song->duration = atof(token);
				//printf("Duration: %f\n", song->duration);
			break;
					//Year of Release
			case 18:
			song->releaseYr = atoi(token);
				//printf("Release: %d\n", song->releaseYr);
			break;
					//Hottness
			case 14:
			if(isnan(atof(token))){
				song->hottness = 0.0;
			}else{
				song->hottness = atof(token);
			}
				//printf("Hottness: %f\n", song->hottness);
			break;
		}
		c++;
	}
	free(line);
	return song;
}

//Function used to create a song directory struct
Song_dir recordCreate(Song *song){

	int offset = 0;
	
	//extract the information needed from the song struct
	Song_dir record;
	record.title = malloc(1024);

	strcpy(record.title, song->songName);
	record.offset = offset;
	record.len_album = strlen(song->albumName);
	record.len_artist = strlen(song->artist);
	record.len_title = strlen(song->songName);

	return record;
}

//Main part that reads the song file and prepares song directory structs so that they can be written to the directory binary file
Song_dir* reader(FILE *source, FILE *dest, FILE *dirFile, Song_dir *directory, int size){

	size_t bytes;
	char buffer[BUFFSIZE];
	char *buffCopy = (char*)malloc(sizeof(char) * BUFFSIZE);
	Song *song = malloc(sizeof(Song));
	Song_dir record;
	int dirSize = size, recordNum = 0, delete = 0;
	
	//Reading
	while((bytes = fread(buffer, sizeof(char), BUFFSIZE, source)) > 0){

		int i = 0, offset;

		while(buffer[i] != '\n' && i < BUFFSIZE){
			buffCopy[i] = buffer[i];
			i++;
		}
		offset += i + 1;
		buffCopy[i] = '\0';
		
		//Create a song
		song = separator(buffCopy);

		if(delete > 0){//skip the first line of the file
		
			//Create a song directory struct based off of the song struct created
			record = recordCreate(song);
			
			//Make sure the new struct can go into the array
			if(recordNum < dirSize){
				directory[recordNum] = record;
			}else{
			//Otherwise dynamically grow that array
				directory = grow(directory, dirSize);
				dirSize += 1000;
				directory[recordNum] = record;
			}
			recordNum++;
			//write the song to the binary file
			writer(dest, song);
		}

		if(delete == 0){
			delete++;
		}

		fseek(source, offset, SEEK_SET);
	}

	sizeOfDir = dirSize;//keep track of directory size

	//optimize directory. Get rid of empty indices, prepare for write
	directory = dynamArrayOpt(directory, sizeOfDir);

	//calculate offset, sort array and then write to dirrectory file
	dirFileCreate(dirFile, directory);

	free(song);
	free(buffCopy);
	fclose(source);
	fclose(dest);
	fclose(dirFile);

	return directory;
}

//Function used to dynamically grow an array of song directory structs
Song_dir* grow(Song_dir *directory, int origSize){

	Song_dir * newDir = malloc(sizeof(Song_dir) * (origSize + 1000));
	memcpy(newDir, directory, (origSize * sizeof(Song_dir)));
	free(directory);
	directory = newDir;

	return directory;
}

//Function used to get rid of unused space in an array
Song_dir* dynamArrayOpt(Song_dir *directory, int origSize){

	int tempSize = origSize;
	int actualSize = 0;

	while(directory[tempSize].title == NULL){

		actualSize = tempSize--;
	}

	Song_dir * newDir = malloc(sizeof(Song_dir) * (actualSize));
	memcpy(newDir, directory, (actualSize * sizeof(Song_dir)));
	free(directory);
	directory = newDir;
	sizeOfDir = actualSize;

	return directory;
}

//Function used to write song structs to a binary file
void writer(FILE *dest, Song *song){

	char *artistWrite = malloc(sizeof(char) * (strlen(song->artist) + 1));
	char *albumNameWrite = malloc(sizeof(char) * (strlen(song->albumName) + 1));
	float writeDuration;
	int writeReleaseYr;
	double writeHottness;

	//-------------------------------------------------------
	artistWrite = song->artist;
	//printf("Artist: %s\n", artistWrite);
	fwrite(artistWrite, strlen(artistWrite), 1, dest);
	//-------------------------------------------------------
	albumNameWrite = song->albumName;
	//printf("Album: %s\n", albumNameWrite);
	fwrite(albumNameWrite, strlen(albumNameWrite), 1, dest);
	//-------------------------------------------------------
	writeDuration = song->duration;
	//printf("Duration: %f\n", writeDuration);
	fwrite(&writeDuration, sizeof(float), 1, dest);
	//-------------------------------------------------------
	writeReleaseYr = song->releaseYr;
	//printf("Year of release: %d\n", writeReleaseYr);
	fwrite(&writeReleaseYr, sizeof(int), 1, dest);
	//-------------------------------------------------------
	writeHottness = song->hottness;
	//printf("Hottness: %f\n", writeHottness);
	fwrite(&writeHottness, sizeof(double), 1, dest);
	//-------------------------------------------------------
	//printf("\n");

	free(artistWrite);
	free(albumNameWrite);
}

//Function used to write a sorted array of song directory structs to a binary file
void dirFileCreate(FILE *dirFile, Song_dir *directory){

	char *title = malloc(1024);
	int offset = 0, titleInt, albumInt, artistInt;
	int dirSize = sizeOfDir;

	//calculate and set offset
	for(int i = 0; i < sizeOfDir; i++){
		directory[i].offset = offset;
		offset += directory[i].len_artist + directory[i].len_album + sizeof(int) + sizeof(float) + sizeof(double);
	}

	//sort directory
	qsort((void *)directory, sizeOfDir, sizeof(Song_dir), comparator);

	//write directory to file
	for(int i = -1; i < sizeOfDir; i++){

		if(i == -1){
			fwrite(&dirSize, sizeof(int), 1, dirFile);
		}else{
			strcpy(title, directory[i].title);
			offset = (int)directory[i].offset;
			titleInt = (int)directory[i].len_title;
			albumInt = (int)directory[i].len_album;
			artistInt = (int)directory[i].len_artist;

			fwrite(&titleInt, sizeof(int), 1, dirFile);
			fwrite(&albumInt, sizeof(int), 1, dirFile);
			fwrite(&artistInt, sizeof(int), 1, dirFile);
			fwrite(&offset, sizeof(int), 1, dirFile);
			fwrite(title, strlen(title), 1, dirFile);
		}

	}

	free(title);
}
