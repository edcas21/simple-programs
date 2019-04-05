
import java.util.Random;

public class WordSearch {

	// fields
	/*
	 * seed (int) genratedWordTable (first table without search info)
	 * (char[4][4]) searchedWordTable (boolean type table) (boolean[4][4] word
	 * (char[])
	 */

	private int seed;
	private char[][] generatedWordTable = new char[4][4];
	private int[][] searchedWordTable = new int[4][4];
	private int letterNumAndTrack = 0;
	private String word;

	// constructor
	// will only ask for seed. Two dimensional char arrays will always be the
	// same size
	public WordSearch(int seed) {
		this.seed = seed;
	}

	// methods
	/*
	 * ^printOutTable (void) (prints out search table, both unchecked and
	 * checked) searchTable (boolean) (uses recursion to search through ^table)
	 * createTable (void) (generates the wordSearch table) printOutResult(void)
	 * (Prints out the result of the search) resetSearchedTable(void) (resets
	 * the searchedWordTable array) search(boolean) (recursive search)
	 */

	public void createTable() {
		Random rand = new Random(seed);

		// fill in wordSearch table
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				generatedWordTable[i][j] = (char) (rand.nextInt(26) + 65);
			}
		}
	}

	/*
	 * prints out the desired table. Regular table or searched table. 1 in
	 * parameter prints out the regular table, 2 prints out the searched table,
	 * 3 does some debugging for the searchedWordTable
	 */
	public void printOutTable(int table) {

		System.out.println();
		/* main loop */ for (int i = 0; i < 4; i++) {
			///////////////////////////////////////////////////////////////////////////////////////////
			// top side
			/* loop 1 */ for (int t = 0; t < 4; t++) {
				System.out.print("+---+ ");
			}
			///////////////////////////////////////////////////////////////////////////////////////////
			System.out.println();
			// left and right sides with letter in the middle
			/* loop 2 */ for (int j = 0; j < 4; j++) {

				// prints out path in searchedWordTable array

				if (searchedWordTable[i][j] > 0 && table == 2) {

					System.out.print("|<" + generatedWordTable[i][j] + ">| ");

				} else if (searchedWordTable[i][j] > 0 && table == 3) {

					System.out.print("|<" + searchedWordTable[i][j] + ">| ");

				} else {

					System.out.print("| " + generatedWordTable[i][j] + " | ");

				}
			}
			///////////////////////////////////////////////////////////////////////////////////////////
			// bottom side
			System.out.println();
			/* loop 3 */ for (int b = 0; b < 4; b++) {
				System.out.print("+---+ ");
			}
			System.out.println();
			///////////////////////////////////////////////////////////////////////////////////////////
		}

		// Print out the table that has been evaluated

	}

	// Prints out the result of the search. Whether the word was found or not
	// and then resets the searchedWordTable array for the next word9
	public void printOutResult() {
		if (searchTable() == true) {

			System.out.println("'" + word + "' was found on the board!");
		} else {

			System.out.println("'" + word + "' was not found on the board!");
		}
		printOutTable(2);
		letterNumAndTrack = 0;

		// reset boolean array to search for next word
		resetSearchedWordTable();

	}

	// reset the searchedWordTable array
	public void resetSearchedWordTable() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				searchedWordTable[i][j] = 0;
			}
		}
	}

	// accesses recursive search
	public boolean searchTable() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (search(i, j, 0)) {
					return true;
				}
			}
		}
		return false;
	}

	// recursive search
	private boolean search(int row, int col, int index) {

		// check if current cell not already used or character in it is not not

		if (searchedWordTable[row][col] != 0 || word.toUpperCase().charAt(index) != generatedWordTable[row][col]) {
			return false;
		}

		if (index == word.length() - 1 || (word.length() == 1 && index == 0)) {

			// special when searching for only one letter
			if (word.length() == 1) {
				searchedWordTable[row][col] = 1;
			} else {
				// word is found, return true
				searchedWordTable[row][col] = letterNumAndTrack++;
			}
			return true;
		}

		// mark the current cell as 1
		searchedWordTable[row][col] = letterNumAndTrack++;
		// check if cell is already used

		if (row < 4 && search(row, col, index)) { // check at current index
			return true;
		}

		if (col + 1 < 4 && search(row, col + 1, index + 1)) { // go right
			return true;
		}
		if (col - 1 >= 0 && search(row, col - 1, index + 1)) { // go  left
			return true;
		}

		if (row + 1 < 4 && search(row + 1, col, index + 1)) { // go down
			return true;
		}
		if (row - 1 >= 0 && search(row - 1, col, index + 1)) { // go up
			return true;
		}

		if (row - 1 >= 0 && col + 1 < 4 && search(row - 1, col + 1, index + 1)) {
			// go diagonally up to the right
			return true;
		}
		if (row - 1 >= 0 && col - 1 >= 0 && search(row - 1, col - 1, index + 1)) {
			// go diagonally up to the left
			return true;
		}
		if (row + 1 < 4 && col - 1 >= 0 && search(row + 1, col - 1, index + 1)) {
			// go diagonally down and left
			return true;
		}
		if (row + 1 < 4 && col + 1 < 4 && search(row + 1, col + 1, index + 1)) {
			// go diagonally down right
			return true;
		}

		// if it can't find the letter then it will go in reverse and returns false
		searchedWordTable[row][col] = 0;
		letterNumAndTrack--;
		return false;
	}

	// setters and getters

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public char[][] getGeneratedWordTable() {
		return generatedWordTable;
	}

	public void setGeneratedWordTable(char[][] generatedWordTable) {
		this.generatedWordTable = generatedWordTable;
	}

	public int[][] getSearchedWordTable() {
		return searchedWordTable;
	}

	public void setSearchedWordTable(int[][] searchedWordTable) {
		this.searchedWordTable = searchedWordTable;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getLetterNumAndTrack() {
		return letterNumAndTrack;
	}

	public void setLetterNumAndTrack(int num) {
		letterNumAndTrack = num;
	}

}

