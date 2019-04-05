/**
 * @author Edgar Cardenas
 * 
 * This class encapsulates the elements of the expression into a node
 * so that it can be inserted into an expression tree.
 *
 */
public class CalcNode {
	
	//attributes
	private String content;
	private CalcNode rightChild, leftChild;
	
	/**
	 * @param content elements of the expression such as 
	 * operators and operands
	 * 
	 * Constructor of the class
	 */
	public CalcNode(String content){
		this.setContent(content);
		this.setRightChild(null);
		this.setLeftChild(null);
	}

	/**
	 * @return content of the node.
	 * Getter for the content of the node.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content element of the expression.
	 * Setter for thr content of the node.
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return left child of the node.
	 * Getter for the left child of the node.
	 */
	public CalcNode getLeftChild() {
		return leftChild;
	}

	/**
	 * @param leftChild left child of the node.
	 * Setter for the left child of the node.
	 */
	public void setLeftChild(CalcNode leftChild) {
		this.leftChild = leftChild;
	}

	/**
	 * @return right child of the node.
	 * Getter for the right child of the node.
	 */
	public CalcNode getRightChild() {
		return rightChild;
	}

	/**
	 * @param rightChild right child of the node.
	 * Setter for the right child of the node.
	 */
	public void setRightChild(CalcNode rightChild) {
		this.rightChild = rightChild;
	}
	
	/**
	 * @param time 0 to skip printing and just do the traversal.
	 * This method prints out the tree from the current node in 
	 * an inorder traversal.
	 */
		public void printInOrder(int time) {
			
			if(leftChild != null) {
				leftChild.printInOrder(time);
			}
			
			if(time != 0) {
				System.out.print(content + " ");
			}
			
			if(rightChild != null) {
				rightChild.printInOrder(time);
			}
		}
		
		/**
		 * @param time 0 to skip printing and just do the traversal.
		 * This method prints out the tree from the current node in 
		 * an preorder traversal.
		 */		
		public void printPreOrder(int time) {
			
			if(time != 0) {
				System.out.print(content + " ");
			}
			
			if(leftChild != null) {
				leftChild.printPreOrder(time);
			}
			
			if(rightChild != null) {
				rightChild.printPreOrder(time);
			}
		}

		/**
		 * @param time 0 to skip printing and just do the traversal.
		 * This method prints out the tree from the current node in 
		 * an postorder traversal.
		 */	
		public void printPostOrder(int time) {
			if(leftChild != null) {
				leftChild.printPostOrder(time);
			}
			if(rightChild != null) {
				rightChild.printPostOrder(time);
			}
			
			if(time != 0) {
				System.out.print(content + " ");
			}
		}
		
}
