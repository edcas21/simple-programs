
/**
 * @author Edgar Cardenas
 *
 * @param <T> Generic object type
 * This class is the implementation of an interface that enables building a stack holding any generic object type.
 */
public class Stack<T> implements ArrayStack<T> {

	//attributes
	private T[] expression;//array based stack
	private int top, max;//first and last indices of the stack

	/**
	 * @param size initial size of the stack
	 * Constructor
	 */
	@SuppressWarnings("unchecked")
	public Stack(int size) {
		max = size + 1000;
		expression = (T[]) new Object[max];
		top = -1;
	}

	/**
	 * @param value item to insert into the stack
	 * This method inserts an item onto the stack.
	 */
	public void push(T value) {
		if (!isFull()) {
			expression[++top] = value;
		}

	}

	/**
	 * @return the last item that was added onto the stack.
	 * This method returns and deletes the last item added onto the stack.
	 */
	public T pop() {
		if (!isEmpty()) {
			return expression[top--];
		}
		return null;
	}

	/**
	 * @return the value of the last item added to into the stack.
	 * This method returns information about the next item that could be popped off the stack.
	 */
	public T peek() {
		if(isEmpty()) {
			
		}
		return expression[top];
	}
	
	/**
	 * @return true if there's one last element on the stack, false if there's more than one basically.
	 * This method checks if there's only one remaining element on the stack.
	 */
	public boolean last() {
		return top == 0;
	}

	/**
	 * @return true if the stack is empty, false if it's not.
	 * This method checks if the stack is empty.
	 */
	public boolean isEmpty() {
		return (top == -1);
	}

	/**
	 * @return true if the stack is full, false if not.
	 * This method checks of the stack is full.
	 */
	public boolean isFull() {
		return top == max;
	}

}
