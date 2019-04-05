import java.util.Scanner;
import java.util.regex.Pattern;

public class WordSearchTest {

	public static void main(String[] args) {
		// declare variables
		int seed;
		String wordCheck;
		String continueSearching = null;
		String continueCreatingTables;
		Scanner scan = new Scanner(System.in);

		do {
			// Ask for the seed
			System.out.println();
			System.out.print("Enter seed: ");

			// input validation - check if it's an integer
			while (!scan.hasNextInt()) {
				System.out.println();
				System.out.println("Invalid response. Please enter an integer.");
				scan.next();
			}
			seed = scan.nextInt();

			// input validation - check if it's within the range
			while (seed < 1 || seed > 9999) {
				System.out.println();
				System.out.println("Invalid input. Please enter a seed within the range of [1,999]");
				seed = scan.nextInt();
			}

			// create WordSearch Object
			WordSearch search = new WordSearch(seed);

			// create table
			search.createTable();
			do {
				// print out table
				search.printOutTable(1);

				// Ask for the word and send it in to the object through the
				// setter
				System.out.println();
				System.out.println("Enter a word to search for on the word search board:\n");
				wordCheck = scan.next();
				
				//searchedWordTable debugging;
				if (wordCheck.charAt(wordCheck.length() - 1) == '3') {
					wordCheck = wordCheck.substring(0, wordCheck.length() - 1);
					search.setWord(wordCheck);
					System.out.println(search.getWord());
					
					search.searchTable();
	
					search.printOutTable(3);
					search.resetSearchedWordTable();
					System.out.println();

				}

				search.setWord(wordCheck);
				System.out.println();
				// search for word and print out result
				search.printOutResult();

				System.out.println("--------------------------------------------------------\n");
				System.out.println();

				System.out.println("Do you want to search for another word? (Y / N)");
				// input validation - check if it's a String and if it's an
				// upper case or lower case Y or N
				while (!scan.hasNext("[YNyn]")) {
					System.out.println("Invalid response. Y / N?");
					scan.next();
				}
				continueSearching = scan.next();

				// continue searching for word as long as their answer is Y
			} while (continueSearching.toUpperCase().compareTo("N") != 0);

			System.out.println();
			System.out.println("Do you want to do another word search? (Y / N)");
			// input validation - check if it's a String and if it's an upper
			// case or lower case Y or N
			while (!scan.hasNext("[YNyn]")) {
				System.out.println("Invalid response. Y / N?");
				scan.next();
			}
			continueCreatingTables = scan.next();
			System.out.println();

			// If Y, then make a new board and start a new search, otherwise end
			// program
		} while (continueCreatingTables.toUpperCase().compareTo("N") != 0);

		scan.close();

		System.out.println("Terminating.....");
	}
}

