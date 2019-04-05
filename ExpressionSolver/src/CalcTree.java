import java.util.*;

public class CalcTree {

	// attributes
	private CalcNode root;
	private String[] expression;
	private String strExpression;
	private Stack<String> stack1;
	private Stack<CalcNode> stack2;

	/**
	 * Constructor
	 * @param expression string of the expression to be converted to the a tree
	 */
	public CalcTree(String expression) {
		root = null;
		this.strExpression = expression;
		expression = expression.replace("(", "~(~");// replace ( with ~(~
		expression = expression.replace(")", "~)~");// replace ( with ~(~
		// split at ~ and blank spaces to allow a proper separation of the expression
		// elements
		this.expression = expression.split("\\s|~");
		// Initialize stack that will be used to help convert the expression to
		// postorder.
		stack1 = new Stack<String>(expression.length());
	}
	
	/**
	 * @return list of the broken up stack This method converts the given expression
	 *         from infix to postfix notation
	 */
	private ArrayList<String> toPost() {

		// list that will hold the elements of the expression in the converted order
		ArrayList<String> post = new ArrayList<String>();

		try {
			for (int i = 0; i < this.expression.length; i++) {
				String val = expression[i];//hold element of the expression to be evaluated
				//check if it's an operator
				if (isOperator(val)) {
					//check precedence and while it's higher, pop what's on the stack and add to the list. 
					while (precedenceCheck(val, stack1.peek())) {
						post.add(stack1.pop());
					}
					//push value with lower precedence onto the the stack.
					stack1.push(val);
				} else if (val.equals("(")) {//if it's an open parentheses, push onto the stack.
					stack1.push(val);
				} else if (val.equals(")")) {//if it's a closing parentheses, while the next item on the stack
					//is not the matching opening parentheses, add the element to the list.
					while (!stack1.peek().equals("(")) {
						post.add(stack1.pop());
					}
					stack1.pop();//pop the matching opening parentheses off the stack to get rid of it
				} else {
					//if it's a space or or a number, just add it to the list
					post.add(val);
				}
			}
			
			//pop and add the remaining elements on the stack to the list.
			while (!stack1.isEmpty()) {
				post.add(stack1.pop());
			}

			//catch expressions that have unbalanced parentheses
		} catch (ArrayIndexOutOfBoundsException exception) {
			System.out.println("Invalid Expression! Unbalanced parentheses.");
		}
		//return the postfix expression
		return post;
	}

	/**
	 * This method inserts the expression elements into the tree properly using a
	 * stack.
	 */
	public void insert() {

		ArrayList<String> post;// list to hold the separated out expression

		post = toPost();// store separated out expression into the list

		String number;
		CalcNode newNode;

		stack2 = new Stack<CalcNode>(post.size());

		for (int i = 0; i < post.size(); i++) {
			String val = post.get(i);// expression element

			if (isNumeric(val)) {// if it's a number push node onto the stack
				number = val;
				newNode = new CalcNode(number);
				stack2.push(newNode);

			} else if (isOperator(val)) {// if it's an operator

				// pop 2 consecutive items off the stack and make them children
				CalcNode right = stack2.pop();
				CalcNode left = stack2.pop();

				newNode = new CalcNode(val);
				newNode.setLeftChild(left);
				newNode.setRightChild(right);

				// push parent node onto stack
				stack2.push(newNode);
			}

		}
		root = stack2.pop();// keep track of final root as tree is built up
	}

	

	/**
	 * @param op1 first operator
	 * @param op2 second operator
	 * @return true if precedence of second operator is >= first operator, false if < than first operator.
	 * This method checks the precedence between two operators.
	 */
	private boolean precedenceCheck(String op1, String op2) {
		//check if precedence of first operator is greater than or equal to the the second operator
		if ((op2.equals("+") || op2.equals("-")) && (op1.equals("+") || op1.equals("-"))) {
			return true;
		} else if ((op2.equals("*") || op2.equals("/"))
				&& (op1.equals("+") || op1.equals("-") || op1.equals("*") || op1.equals("/"))) {
			return true;
		} else if ((op2.equals("^")) && (op1.equals("+") || op1.equals("-") || op1.equals("*") || op1.equals("/"))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param token string to be evaluated.
	 * @return true if the string is recognized as an operator, false if not.
	 * This method checks if a given element of the expression is an operator.
	 */
	private boolean isOperator(String token) {
		if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("^")) {
			return true;
		}
		return false;
	}

	/**
	 * @param strNum string to be evaluated.
	 * @return true if the string is a number, false if it's not.
	 * This method checks if a given string is a number.
	 */
	public static boolean isNumeric(String strNum) {
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException | NullPointerException nfe) {
			return false;
		}
		return true;
	}

	/**
	 * @return string of the original expression.
	 * Getter for the original string expression.
	 */
	public String getEquation() {
		return this.strExpression;
	}

	/**
	 * @return root of the expression tree.
	 * Getter for the root of the expression tree.
	 */
	public CalcNode getRoot() {
		return root;
	}

}
