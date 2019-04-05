/**
 * 
 * @author Edgar Cardenas
 * 
 * This class implements the comparable interface to make a ResultNode object comparable to other objects of the same type.
 * It holds the result of an expression, the root of the result tree, and the string representation of the original expression.
 * Will be used to build a binary search tree of ResultNodes.
 */
public class ResultNode implements Comparable<ResultNode>{
	
	//attributes
	private CalcNode rootRes;
	private double result;
	private String origExpression;
	
	/**
	 * @param root of the expression tree
	 * @param result of the expression
	 * @param origExpression string representation of the original expression
	 * Constructor
	 */
	public ResultNode(CalcNode root, double result, String origExpression) {
		this.rootRes = root;
		this.result = result;
		this.origExpression = origExpression;
	}
	
	/**
	 * @return result of the expression
	 * This method is the getter for the result of the expression.
	 */
	public double getResult() {
		return result;
	}
	
	/**
	 * @return the root of the expression tree
	 * This method is the getter for the root of the expression tree.
	 */
	public CalcNode getRoot() {
		return rootRes;
	}
	
	/**
	 * @return the string representation of the original expression
	 * This method is the getter for the string representation of the original expression .
	 */
	public String getOrigExpression() {
		return origExpression;
	}
	
	/**
	 * @param time 0 to skip printing and just do the traversal.
	 * This method prints out the tree from the current node in 
	 * an inorder traversal and tacks on the result at the end.
	 */		
	public void printInOrder(int time) {
		rootRes.printInOrder(time);
		if(time != 0) {
			System.out.println("<=> " + result);
		}
	}
	
	/**
	 * @param time 0 to skip printing and just do the traversal.
	 * This method prints out the tree from the current node in 
	 * an preorder traversal and tacks on the result at the end.
	 */
	public void printPreOrder(int time) {
		rootRes.printPreOrder(time);
		if(time != 0) {
			System.out.println("<=> " + result);
		}
	}
	
	/**
	 * @param time 0 to skip printing and just do the traversal.
	 * This method prints out the tree from the current node in 
	 * an postorder traversal and tacks on the result at the end.
	 */	
	public void printPostOrder(int time) {
		rootRes.printPostOrder(time);
		if(time != 0) {
			System.out.println("<=> " + result);
		}
	}

	/**
	 * This method compares the results between two ResultNodes
	 * and returns 0 if they are equal, 1 if the current ResultNode is greater than the ResultNode it's being compared to, and
	 * -1 if the current ResultNode is less than the ResultNode it's being compared to.
	 */
	@Override
	public int compareTo(ResultNode node) {
		if(this.result > node.result) return 1;
		if(this.result < node.result) return -1;
		else return 0;
	}

}
