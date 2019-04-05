import java.io.*;
import java.util.*;

/**
 * This class implements the external k_way merge sort.
 * 
 * @author Edgar Cardenas
 *
 */

public class FileSorter {

	private String fileName;//file that will be processed
	private int bufferSize;//size of the buffer that will be used for the merge
	private String header;//Header line in the file
	private String sortedFileName;//Name of the final sorted file

	/**
	 * Constructor
	 * @param fileName The file name of the csv to be sorted
	 */
	public FileSorter(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * This method sorts the file by the Order ID
	 * @param k In how many ways the original file will be split
	 * @throws IOException Catches file exceptions
	 */
	public void ExternalMergeSortByOrderID(int k) throws IOException {
		this.sortedFileName = "ExternalMergeSortByOrderID" + k + "ways.csv";
		Kmerge(k, 1);
	}

	/**
	 * This method sorts the file by the profit
	 * @param k In how many ways the original file will be split
	 * @throws IOException Handles file exceptions
	 */
	public void ExternalMergeSortByTotalProfit(int k) throws IOException {
		this.sortedFileName = "ExternalMergeSortByProfit" + k + "ways.csv";
		Kmerge(k, 2);

	}

	/**
	 * This method reads the smallest order number from the final sorted file
	 * @return A string formatted for output in main
	 * @throws IOException Catches file exceptions.
	 */
	public String getsmallestOrderNum() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(this.sortedFileName));
		String orderNum = reader.readLine();//smallest order number should be the first line of the file sorted by orderNum
		reader.close();

		return orderNum.split(",")[6] + " (profit: " + orderNum.split(",")[13] + ")";
	}

	/**
	 * This method reads the highest profit from the sorted file
	 * @return A formatted string for output in main
	 * @throws IOException Handles file exceptions
	 */
	public String getHighestProfit() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(this.sortedFileName));
		String profit = reader.readLine();//highest profit should be the first line of the file sorted by profit
		reader.close();

		return profit.split(",")[13] + " (order #: " + profit.split(",")[6] + ")";
	}

	/**
	 * This method prepares the file for sorting and then calls the necessary methods to actually carry it out
	 * @param k The number of ways that the original file will be split
	 * @param type 1 to indicate sorting by order number, 2 to indicate sorting by profit
	 * @throws IOException Handles file exceptions
	 */
	private void Kmerge(int k, int type) throws IOException {

		File file = new File(this.fileName);
		BufferedReader reader = null;
		int numRecords = 0;
		try {
			numRecords = count(this.fileName) - 1;// count number of lines in the file to find number of records
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int portionSize = (int) Math.ceil((double) numRecords / k);// number of records to be distributed among the k files, or the portion
		String splitRecord[] = null, line;//to hold line read from file that is split at the commas into an array of strings
		Data record = null;//temporary Data object
		ArrayList<String> fileNames = new ArrayList<String>();//ArrayList that will contain the names of the K files into which the original file will be split into

		try {
			reader = new BufferedReader(new FileReader(file));//open file for reading

			ArrayList<Data> toSort = new ArrayList<Data>();//initialize temp list to hold the portion of the file to be read
			int lineCount = 0, fileCount = 0;//counters

			while ((line = reader.readLine()) != null) {//read until the end of the file
				splitRecord = line.split(",");//split line read at the commas
				record = new Data(type, splitRecord);//create a Data object from the line read

				if (lineCount == portionSize) {//when the line count reaches the portion size

					String fileName = "temp" + fileCount + ".csv";//create a temporary file
					FileWriter writer = new FileWriter(fileName);//set up for writing to that file
					fileNames.add(fileName);//add file to the list of file names
					//decide what way to sort based off of the attribute to be sorted by
					if (type == 1) {
						Collections.sort(toSort);
					} else if (type == 2) {
						toSort.sort(Collections.reverseOrder());
					}

					//write sorted list of records to the temporay file
					for (Data obj : toSort) {
						writer.append(obj.toString());
						writer.append("\n");
						writer.flush();
					}
					writer.close();

					//reset and increment file count
					toSort = new ArrayList<Data>();
					lineCount = 0;
					fileCount++;
				}

				//watch out for the header of the csv
				if (!splitRecord[13].matches(".*[a-zA-Z].*")) {
					toSort.add(record);
				} else {
					this.header = line;
				}

				lineCount++;
			}

			//last file may not be filled up to the portion size, so flush and write what is left in the temporary list
			if (lineCount <= portionSize && lineCount != 0) {

				String fileName = "temp" + fileCount + ".csv";
				FileWriter writer = new FileWriter(fileName);
				fileNames.add(fileName);
				if (type == 1) {
					Collections.sort(toSort);
				} else if (type == 2) {
					toSort.sort(Collections.reverseOrder());
				}

				for (Data obj : toSort) {
					writer.append(obj.toString());
					writer.append("\n");
					writer.flush();
				}
				writer.close();
			}
			//exception handling
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

		//merge the temporary files together
		mergeLists(fileNames, k, type, numRecords);
	}

	/**
	 * This method uses a min heap to merge the sorted temporary files into one big sorted file
	 * @param fileNames List of names of the files that will be merged into one sorted file
	 * @param k Reference to the number of ways the data was split
	 * @param type Reference to the way in which the data will be sorted
	 * @param numOfRecords Reference to the number of records in the file
	 * @throws IOException Handles file exceptions
	 */
	private void mergeLists(ArrayList<String> fileNames, int k, int type, int numOfRecords) throws IOException {

		PriorityQueue<Data> PQ = new PriorityQueue<Data>();//min heap that will be used to merge the k number of files
		RandomAccessFile fileMutator;
		File finalFile = new File(this.sortedFileName);//create the final sorted file
		
		//append the header to the final file
		/*FileWriter appender1 = new FileWriter(finalFile, true);
		appender1.append(this.header);
		appender1.append("\n");
		appender1.flush();
		appender1.close();*/

		// load the PQ, getting the first line from each
		for (int i = 0; i < fileNames.size(); i++) {

			fileMutator = new RandomAccessFile(fileNames.get(i), "r");
			String line = fileMutator.readLine();
			Data record = createRecord(type, line, i, fileMutator.getFilePointer(), fileNames.get(i));
			if (record != null) {
				PQ.add(record);
			}
			fileMutator.close();
		}

		int bufferSize = PQ.size();//set buffer size to the initial size of the min heap
		ArrayList<Data> output = new ArrayList<Data>();//output buffer list
		int index = 0;//temp variable to hold the index of the file to which the polled record belongs to

		for (int i = 0; i < numOfRecords; i++) {
			try {
				Data polled = PQ.poll();//remove min at root of the heap
				addToOut(polled, output, bufferSize, finalFile);//put into the ouput buffer
				/*create a RandomAccessFile object to read a new record from the file that the most recently polled record belongs to.
				There must be a record from each file in the heap at all times, so when it is removed, it must be replaced by another from
				the same location*/
				fileMutator = new RandomAccessFile(polled.getFileName(), "r");
				fileMutator.seek(polled.getFilePointer());//get file position to read the next record from
				Data record = createRecord(type, fileMutator.readLine(), polled.getfileIndex(), fileMutator.getFilePointer(), polled.getFileName());//create record
				if (record != null) {//make sure it is not null in the case that it reads a blank line. Depending on how many records there are in the original file, the ceiling of the portion
					//will result in an extra file
					PQ.add(record);//if there is a valid record generated, then go ahead and add it to the heap
				}

				fileMutator.close();
			} catch (EOFException e) {//handle reading till the end of one of the temporary files

			}
		}

		//flush whatever is left at the in the buffer it didn't become full reading the last few records
		FileWriter appender = new FileWriter(finalFile, true);

		//append to the final file
		for (Data element : output) {
			appender.append(element.toString());
			appender.append("\n");
			appender.flush();
		}

		appender.close();

		//attempt to delete temp files
		for (int i = 0; i < fileNames.size(); i++) {
			File temp = new File(fileNames.get(index));
			temp.delete();
		}

	}

	/**
	 * This method manages the output buffer. If it becomes full
	 * then it is flushed and written to the final sorted file.
	 * 
	 * @param record Record to be added to the buffer.
	 * @param output Reference to the output buffer list.
	 * @param bufferSize Reference to the buffer size.
	 * @param file Reference to the file object of the final sorted file.
	 * @throws IOException Handles file exceptions.
	 */
	private void addToOut(Data record, ArrayList<Data> output, int bufferSize, File file) throws IOException {

		//check if the buffer is empty
		if (output.size() < bufferSize) {
			output.add(record);
		//if empty flush buffer and append to the final file
		} else {

			FileWriter appender = new FileWriter(file, true);

			for (Data element : output) {
				appender.append(element.toString());
				appender.append("\n");
				appender.flush();
			}

			//reset buffer
			output.removeAll(output);
			output.add(record);

			appender.close();
		}
	}

	/**
	 * This method instantiates a Data object and returns it
	 * @param type Reference to the way that the data will be sorted.
	 * @param line String that will be split by the commas
	 * @param fileIndex Index in the list of filenames from which the data was read from
	 * @param filePointer Place in the file in from which the record was read from
	 * @param fileName Name of the file from which the record was read from
	 * @return The instantiated Data object
	 * @throws NullPointerException Catches null pointer exception in the case at which the whole file is read
	 */
	private Data createRecord(int type, String line, int fileIndex, Long filePointer, String fileName)throws NullPointerException {
		try {
			String record[] = line.split(",");//split the line
			Data data = new Data(type, record, fileIndex, filePointer, fileName);//create object
			return data;
		} catch (NullPointerException e) {
			return null;
		}
	}

	/**
	 * This method does an initial read of the file to count the number of lines
	 * to show how many records there are within the file
	 * @param fileName Name of the file to be processed
	 * @return The number of lines in the file
	 * @throws IOException Handles file exceptions
	 */
	int count(String fileName) throws IOException {

		BufferedReader counter = new BufferedReader(new FileReader(fileName));
		String line;
		int count = 0;
		//read until end of file and count the number of lines
		while ((line = counter.readLine()) != null) {
			count++;
		}

		return count;
	}

}
