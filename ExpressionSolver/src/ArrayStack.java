/**
 * @author Edgar Cardenas
 *
 * @param <T> Generic object type
 * This class is the interface to stack holding any generic object type
 */
public interface ArrayStack <T>{
	
	/**
	 * @param item to insert into the stack
	 * This method inserts an item onto the stack.
	 */
	void push(T item);
	
	/**
	 * @return the last item that was added onto the stack.
	 * This method returns and deletes the last item added onto the stack.
	 */
	public T pop();
	
	/**
	 * @return the value of the last item added to into the stack.
	 * This method returns information about the next item that could be popped off the stack.
	 */
	public T peek();
	
	/**
	 * @return true if the stack is empty, false if it's not.
	 * This method checks if the stack is empty.
	 */
	public boolean isEmpty();
}
