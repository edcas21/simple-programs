import java.io.IOException;
import java.util.*;

/**
 * 
// * This class is coordinates the functionalty of the program
 * @author Edgar Cardenas
 *
 */
public class Main {

	/**
	 * This method calls the UserInterface method that drives the program.
	 * @param args argument lines of the program.
	 * @throws IOException Handles file exceptions.
	 */
	public static void main(String[] args) throws IOException {
		
		UserInterface();
		
	}
	
	/**
	 * This method gets the name of the csv file that the user would like to process,
	 * and then calls the driver method which carries out the functionality of the program
	 * @throws IOException Handles file exceptions
	 */
	public static void UserInterface() throws IOException {
		//get the file name
		Scanner scan = new Scanner(System.in);
		System.out.print("please enter a file name (do not enter file type at end): ");
		String fileName = scan.nextLine() + ".csv";
		System.out.println();
		
		driver(fileName);//start up the program using that filename
		
	}
	
	/**
	 * This method sets up the necessary objects and calls the right methods to
	 * carry out the functionality of the program. Also times how long it takes to
	 * process the files based off of the input for k.
	 * @param fileName The name of the csv file to be processed
	 * @throws IOException Handles file exceptions
	 */
	public static void driver(String fileName) throws IOException {
		long startTime; 
		double endTime; 
		String mainOrderNum;
		String mainProfit;
		
		FileSorter sorter = new FileSorter(fileName);//create the object to be used to sort
		
		//times and sample inputs of k
		//order ID runs
		startTime = System.nanoTime();
		sorter.ExternalMergeSortByOrderID(2);
		mainOrderNum = sorter.getsmallestOrderNum();
		endTime = ((double) ((System.nanoTime() - startTime))) / 1000000.0;
		System.out.println("time to sort by order ID with k = 2: " + endTime + " ms");

		startTime = System.nanoTime();
		sorter.ExternalMergeSortByOrderID(5);
		endTime = ((double) ((System.nanoTime() - startTime))) / 1000000.0;
		System.out.println("time to sort by order ID with k = 5: " + endTime + " ms");

		startTime = System.nanoTime();
		sorter.ExternalMergeSortByOrderID(9);
		endTime = ((double) ((System.nanoTime() - startTime))) / 1000000.0;
		System.out.println("time to sort by order ID with k = 9: " + endTime + " ms\n");
		 
		//Profit runs
		startTime = System.nanoTime();
		sorter.ExternalMergeSortByTotalProfit(1);
		endTime = ((double) ((System.nanoTime() - startTime))) / 1000000.0;
		System.out.println("time to sort by profit with k = 1: " + endTime + " ms");
		
		startTime = System.nanoTime();
		sorter.ExternalMergeSortByTotalProfit(6);
		mainProfit = sorter.getHighestProfit();
		endTime = ((double) ((System.nanoTime() - startTime))) / 1000000.0;
		System.out.println("time to sort by profit with k = 6: " + endTime + " ms");

		startTime = System.nanoTime();
		sorter.ExternalMergeSortByTotalProfit(8);
		endTime = ((double) ((System.nanoTime() - startTime))) / 1000000.0;
		System.out.println("time to sort by profit with k = 8: " + endTime + " ms");
		
		System.out.println("______________\n");
		
		System.out.println("The smallest order number was " + mainOrderNum);

		System.out.println("The highest profit was " + mainProfit);
	}

}
