import java.io.*;

/**
 * This class implements an object that represents a line or record from a csv
 * @author Edgar Cardenas
 *
 */
public class Data implements Comparable<Data>{
	
	private String[] record;//Array of string attributes that make up the whole record
	private int type;//1 for sort by order id, 2 for sort by profit
	private int fileIndex;//file from which the record comes from
	private Long filePointer;//place in file from which the record was read from
	private String fileName;
	//index 6 for orderID
	//index 13 for profit
	
	/*
	 * Constructor
	 * 
	 * @param type type 1 for sort by orderID, 2 for sort by profit
	 * @param record Array of Strings that holds the line read from the file separated at the commas
	 * @param fileIndex Index in an array of file names from which the record was read from
	 * @param filePointer Place in the file it was read from
	 * @param fileName Name of the file it came from
	 */
	public Data(int type, String[] record, int fileIndex, Long filePointer, String fileName) {
		
		this.type = type;
		this.record = record;
		this.fileIndex = fileIndex;
		this.filePointer = filePointer;
		this.fileName = fileName;
	}
	
	/**
	 * Constructor override that takes in less paramater arguments for the purpose of creating the temporary file
	 * @param type Type 1 for sort by orderID, 2 for sort by profit
	 * @param record String array of the attributes in the record
	 */
	public Data(int type, String[] record) {
		this.type = type;
		this.record = record;
	}
	
	/**
	 * Getter for the record attribute
	 * @return The record
	 */
	public String[] getRecord() {
		return this.record;
	}
	
	/**
	 * Getter for the file index
	 * @return The file index
	 */
	public int getfileIndex() {
		return this.fileIndex;
	}
	
	/**
	 * Getter for the file pointer
	 * @return The file pointer
	 */
	public Long getFilePointer() {
		return this.filePointer;
	}
	
	/**
	 * Getter for the file name attribute
	 * @return The file name
	 */
	public String getFileName() {
		return this.fileName;
	}
	
	/**
	 * This method concatenates the String array record attribute back into a String that can be easily written or printed out
	 * @return Formatted string containing the record attributes
	 */
	public String toString() {
		return record[0] + "," + record[1] + "," + record[2] + "," + record[3] + "," + record[4] + "," + record[5] + "," + record[6] + "," + record[7] + "," + record[8] + "," + record[9] + "," + record[10] + "," + record[11] + "," + record[12] + "," + record[13];
	}
	
	/**
	 * Getter for the OrderID, parses String into long
	 * @return The OrderID
	 */
	private long getOrderID() {
		return Long.parseLong(this.record[6]);
	}

	/**
	 * Getter for the profit, parses String in to double
	 * @return
	 */
	private double getProfit() {
		return Double.parseDouble(this.record[13]);
	}

	@Override
	/**
	 * This comparator takes into account the type passed into the object to become comparable at the desired record attribute
	 * 
	 * @param arg0 Object to which the current object will be compared to
	 * @return 0 if the current object and the object it's being compared to is equal, -1 if the current is less than the other, 1 if the current is greater than the other
	 */
	public int compareTo(Data arg0) {
		
		//compare at the orderID which is a long
		if(this.type == 1) {
			if(this.getOrderID() > arg0.getOrderID()) {
				return 1;
			}else if(this.getOrderID() < arg0.getOrderID()) {
				return -1;
			}
		//compare at the profit which is a double	
		}else if(this.type == 2) {
			if(this.getProfit() > arg0.getProfit()) {
				return 1;
			}else if(this.getProfit() < arg0.getProfit()) {
				return -1;
			}
			
		}
		
		return 0;
	}

	
	
	

}
