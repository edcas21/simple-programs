import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Edgar Cardenas
 *
 *This program generates an expression tree based off of equations read from an
 *input file. The equations are then solved and the results are stored into a binary
 *search tree to find the min, mix, and median result.
 */

public class Main {

	/**
	 * Main method that calls the user the interface to start the program.
	 * @param args lines of code
	 */
	public static void main(String[] args) {
		Interface();
	}
	
	/**
	 * This method prints out the main user interface and calls the Input method to read
	 * the file entered by the user and carry out the rest of the program.
	 */
	public static void Interface() {
		
		ResultTree results;
		Scanner scan = new Scanner(System.in);
		//get file name
		String message = "Please enter a file (w/o file type): ", input;
		System.out.print(message);
		input = scan.nextLine();
		System.out.println("Building trees...");
		results = Input(input + ".txt");//read file entered
	}

	/**
	 * @param fileName of file to be read 
	 * This method reads the input text file, reading line by line containing
	 * the equation to be solved. Calls the right methods and displays the results.
	 * 
	 * @return the ResultTree built from the results of the equations
	 */
	public static ResultTree Input(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		String line;
		Calculator calc = null;
		ResultTree results = null;
		int calcNum = 1;

		try {
			reader = new BufferedReader(new FileReader(file));

			// read line by line
			while ((line = reader.readLine()) != null) {
				System.out.println();
				calc = new Calculator(line);//send in equation to be calculated
				if(calcNum == 1) {//initialize results
					results = new ResultTree();
				}
				results.insert(calc.getResultNode());//insert result to ResultTree
				//print info
				System.out.println("time to build CalcTree " + calcNum + ": " + calc.getBuildTime() + " ms");
				System.out.println("time to find result " + calcNum + ": " + calc.getSolveTime() + " ms");
				System.out.println(calc.getResultNode().getResult());
				calcNum++;
			}
			results.printAllResults();//print the rest of the results
			// error handling
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return results;
	}
}
