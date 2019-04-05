/**
 * @author Edgar Cardenas
 * This class takes a given expression, calls the necessary methods
 * from corresponding classes to convert the expression to an expression tree.
 * It then solves the equation and produces a final ResultNode that will
 * go into a binary search tree.
 */
public class Calculator {
	
	//attributes
	private CalcTree tree;
	private String expression;
	private ResultNode finalResult;
	private double buildTime;
	private double solveTime;
	
	/**
	 * @param expression to be solved.
	 * Constructor
	 */
	public Calculator(String expression) {
		this.expression = expression;
		this.buildTime = buildTree();//construct the expression tree and get build time.
		this.solveTime = solve();//solve the given expression using the tree and get time it took to solve.
	}
	
	/**
	 * @return the time it takes for the expression tree to be built.
	 * This method builds the expression tree needed to solve
	 * the given expression.
	 */
	private double buildTree() {
		long startTime = System.nanoTime();
		this.tree = new CalcTree(this.expression);//initialize expression tree.
		this.tree.insert();//convert expression to the expression tree
		double endTime = ((double) ((System.nanoTime() - startTime)))/1000000.0;

		return endTime;
	}
	
	/**
	 * @return time to solve the given expression using the expression tree.
	 * This method solves the given expression using the expression tree and
	 * gives the time taken to do so.
	 */
	public double solve() {
		double result;
		long startTime = System.nanoTime();
		result = evaluate();//evaluate and solve expression
		if(Double.isNaN(result)) {//for whatever reason it returns, NaN, then just make it a 0
			result = 0.0;
		}
		//initialize and set the ResultNode
		this.finalResult = new ResultNode(tree.getRoot(), result, this.expression);
		double endTime = ((double) ((System.nanoTime() - startTime)))/1000000.0;
		
		return result;		
	}
	
	/**
	 * @return the calculated result given by the calculate method
	 * This method sends a copy of the root of the expression tree to the calculate method
	 */
	private double evaluate() {
		CalcNode rootCopy = tree.getRoot();
		return calculate(rootCopy);
	}
	
	/**
	 * @param node to be evaluated
	 * @return true if it's a leaf, false if it's not.
	 * This method checks whether a particular node is a leaf in a tree.
	 */
	private boolean isLeaf(CalcNode node) {
		if(node.getLeftChild() == null || node.getRightChild() == null) {
			return true;
		}
		return false;
	}
	
	/**
	 * @param root of the expression tree
	 * @return the calculated result
	 * This method traverses the expression tree to calculate the result of the expression
	 */
	private double calculate(CalcNode root) {
		
		//get content of the root
		String content = root.getContent();
		//variable to keep track of the running total result
		double total = 0.0;
		
		//check if the node is a leaf, meaning it's an operand
		if(isLeaf(root)){
			//parse the operand to a double
			return Double.parseDouble(content);
		}else{//it's an operator
			//recursively calculate the result of the right and left branches, keeping a running total between the sub-branches
			double leftResult = calculate(root.getLeftChild());
			double rightResult = calculate(root.getRightChild());
			
			//apply the operator at the root of the current tree
			switch(content) {
			case "+":
				total = leftResult + rightResult;
				break;
			case "-":
				total = leftResult - rightResult;
				break;
			case "*":
				total = leftResult * rightResult;
				break;
			case "/":
				total = leftResult / rightResult;
				break;
			case "^":
				total = Math.pow(leftResult, rightResult);
				break;
			}
		}
		
		return total;
	}
	
	/**
	 * @return build time
	 * This method is the getter for the time taken to build the expression tree.
	 */
	public double getBuildTime() {
		return buildTime;
	}
	
	/**
	 * This method id the getter for the time taken to solve the expression using the expression tree.
	 * @return solve time
	 */
	public double getSolveTime() {
		return solveTime;
	}
	
	/**
	 * @return the ResultNode containing the result of the expression
	 * This method is the getter for the final ResultNode.
	 */
	public ResultNode getResultNode() {
		return finalResult; 
	}
}
