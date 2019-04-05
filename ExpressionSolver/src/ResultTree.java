import java.util.*;

/**
 * @author Edgar Cardenas
 * 
 * This class builds an array based binary search tree out of ResultNodes and helps
 * find the min, max, and median result from the list of equations given to solve.
 *
 */
public class ResultTree {

	//attributes
	private ResultNode results[];
	private int maxIndex;
	private ResultNode median = null;

	/**
	 * Constructor
	 */
	public ResultTree() {
		maxIndex = -1;
		results = new ResultNode[100];
	}
	
	

	/**
	 * @param ResultNode to be inserted into the tree
	 * This method inserts a ResultNode into the array based binary search tree.
	 */
	public void insert(ResultNode node) {
		//make sure the array is big enough to hold an extra node
		ensureCapacity();

		//if the tree is empty, then insert the node at the root
		if (isEmpty()) {
			results[0] = node;
			this.maxIndex = 0;
		} else {
			boolean inserted = false;
			int curIndex = 0;

			while (!inserted) {//keep trying to insert
				if (node.compareTo(results[curIndex]) < 0) {//if the node is less than the root, then go to the left
					if (results[curIndex * 2 + 1] == null) {//if the left child index of the parent index is empty
						results[curIndex * 2 + 1] = node;//insert
						inserted = true;//exit loop
						if ((curIndex * 2 + 1) > maxIndex) {//update the maxIndex if the left child index is greater than the maxIndex
							maxIndex = curIndex * 2 + 1;//set maxIndex equal to the current left child index
						}
					} else {//if the left child index of the current index is occupied
						curIndex = curIndex * 2 + 1;//set the current index to the next left child index
					}
				} else {
					// if the node is greater than the root, go right
					if (results[curIndex * 2 + 2] == null) {//if the right child of the parent is empty
						results[curIndex * 2 + 2] = node;//insert
						inserted = true;//exit loop
						if ((curIndex * 2 + 2) > maxIndex) {//update the maxIndex if the right child index is greater than the maxIndex
							maxIndex = curIndex * 2 + 2;//set maxIndex equal to the current right child index
						}
					} else {//if the right child index of the current index is occupied
						curIndex = curIndex * 2 + 2;//set the current index to the next right child index
					}
				}
			}
		}

	}

	/**
	 * @return true if tree is empty, false if not
	 * This method checks if the tree is empty.
	 */
	private boolean isEmpty() {
		if (maxIndex == -1) {
			return true;
		}
		return false;
	}

	/**
	 * This method makes sure that tree has enough spots to insert a new node.
	 */
	private void ensureCapacity() {
		if (results.length < (maxIndex * 2 + 3)) {//check if it's large enough to store a new right child
			results = Arrays.copyOf(results, results.length * 2);//if not, then double the size of the tree
		}
	}

	/**
	 * @return the ResultNode that holds the min result
	 * This method finds ResultNode in the tree that holds the min result value.
	 * Also times the process taken to do so and then prints the results.
	 */
	private ResultNode findMin() {

		ResultNode result = null;//variable to store the ResultNode holding the min result

		long startTime = System.nanoTime();
		if (isEmpty()) {//check if the tree is empty, in which case there is no min
			System.out.println("No min result because tree is empty!");
		} else {
			int index = 0;
			while (((index * 2 + 1) <= maxIndex) && (results[index * 2 + 1]) != null) {//min is going to be in left subtree
				index = index * 2 + 1;//keep going left until leftmost leaf is reached
			}
			result = results[index];//store node at leftmost leaf
			double endTime = ((double) ((System.nanoTime() - startTime))) / 1000000.0;
			
			//print out info
			System.out.println("\nThe least result was " + result.getResult() + ", coming from the equation "
					+ result.getOrigExpression() + "\ntime to print CalcTree: " + timePrint(result) + " ms\n"
					+ "time to find the greatest result: " + endTime);
		}
		
		return result;
	}

	/**
	 * @return the ResultNode that holds the max result
	 * This method finds ResultNode in the tree that holds the max result value.
	 * Also times the process taken to do so and then prints the results.
	 */
	private ResultNode findMax() {

		ResultNode result = null;//variable to store the ResultNode holding the max result

		long startTime = System.nanoTime();
		if (isEmpty()) {//check if the tree is empty, in which case there is no max
			System.out.println("No max result because tree is empty!");
		} else {
			int index = 0;
			while (((index * 2 + 2) <= maxIndex) && (results[index * 2 + 2] != null)) {//max is going to be in right subtree
				index = index * 2 + 2;//keep going right until rightmost leaf is reached
			}
			result = results[index];//store node at rightmost leaf
			double endTime = ((double) ((System.nanoTime() - startTime))) / 1000000.0;
			
			//print info
			System.out.println("The greatest result was " + result.getResult() + ", coming from the equation "
					+ result.getOrigExpression() + "\ntime to print CalcTree: " + timePrint(result) + " ms\n"
					+ "time to find the greatest result: " + endTime);
		}
		
		return result;
	}

	/**
	 * @return the ResultNode that holds the median result
	 * This method finds the ResultNode in the tree that holds the median result value.
	 * Also times the process taken to do so and then prints the results.
	 */
	private void findMedian() {
		ArrayList<ResultNode> copyOfResults = new ArrayList<ResultNode>();//list to compile all result nodes
		int middle, size;

		long startTime = System.nanoTime();
		//store all ResultNodes in tree into the list, skipping over empty spots in the tree
		for (int i = 0; i <= maxIndex; i++) {
			if (results[i] != null) {
				copyOfResults.add(results[i]);//add where ResultNode exists
			}
		}

		Collections.sort(copyOfResults);//Sort the list of ResultNodes
		size = copyOfResults.size();//store size of the list
		middle = (size / 2);//calculate middle of the list
		double median;//variable that will store the median result

		if (size % 2 == 0) {//if the size of the list is even
			//ResultNode holding the median does not exist
			//therefore needs to be calculated by taking the average between the 2 middle ResultNode results
			median = (copyOfResults.get(middle).getResult() + copyOfResults.get(middle - 1).getResult()) / 2;
		} else {//if the size of the list is odd
			median = copyOfResults.get(middle).getResult();//the median is the ResultNode at the middle of the list
		}
		double endTime = ((double) ((System.nanoTime() - startTime))) / 1000000.0;

		//print info with respect to the size of the list and whether it is even or not
		if (size % 2 == 0) {
			System.out.println("\nResult tree is even. Calculated median is: " + median);
		} else {
			this.median = copyOfResults.get(middle);
			System.out.println("\nThe median result was " + this.median.getResult() + ", coming from the equation "
					+ this.median.getOrigExpression() + "\ntime to print CalcTree: " + timePrint(this.median) + " ms\n"
					+ "time to find the greatest result: " + endTime);
		}

	}

	/**
	 * @param node to be printed
	 * @return time it takes to traverse the expression tree of the node and print it.
	 * This method simulates the printing of an expression tree and times the process.
	 */
	private double timePrint(ResultNode node) {
		long startTime = System.nanoTime();
		node.printInOrder(0);
		double endTime = ((double) ((System.nanoTime() - startTime))) / 1000000.0;

		return endTime;
	}

	/**
	 * This method compiles all the find methods to print out the results, along
	 * with the traversals for those results and the time taken to do so.
	 */
	public void printAllResults() {
		long startTime;
		double endTime;
		ResultNode max, min;
		System.out.println("_______________________________________\n");
		max = findMax();
		min = findMin();
		findMedian();//find results
		System.out.println("\nALL RESULTS:\n");
		//print info
		//--------------------------------------------------------
		System.out.println("Preorder traversal of ResultTree:");
		startTime = System.nanoTime();
		min.printPreOrder(1);
		max.printPreOrder(1);
		this.median.printPreOrder(1);
		endTime = ((double) ((System.nanoTime() - startTime))) / 1000000.0;
		System.out.println("time to traverse: " + endTime + " ms");
		//--------------------------------------------------------
		System.out.println("\nPostorder traversal of ResultTree:");
		startTime = System.nanoTime();
		this.median.printPreOrder(1);
		max.printPreOrder(1);
		min.printPreOrder(1);
		endTime = ((double) ((System.nanoTime() - startTime))) / 1000000.0;
		System.out.println("time to traverse: " + endTime + " ms");
		//--------------------------------------------------------
		System.out.println("\nInorder traversal of ResultTree:");
		startTime = System.nanoTime();
		min.printPreOrder(1);
		this.median.printPreOrder(1);
		max.printPreOrder(1);
		endTime = ((double) ((System.nanoTime() - startTime))) / 1000000.0;
		System.out.println("time to traverse: " + endTime + " ms");
	}

}
