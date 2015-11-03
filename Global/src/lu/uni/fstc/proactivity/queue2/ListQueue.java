package lu.uni.fstc.proactivity.queue2;

//ListQueue class
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void enqueue( x )	  --> Insert x
// Object getFront( )	 --> Return least recently inserted item
// Object dequeue( )	  --> Return and remove least recent item
// boolean isEmpty( )	 --> Return true if empty; else false
// void makeEmpty( )	  --> Remove all items
// ******************ERRORS********************************
// getFront or dequeue on empty queue

/**
 * List-based implementation of the queue.
 * @author Mark Allen Weiss
 */
public class ListQueue implements IQueue {
	private ListNode front;
	
	private ListNode back;
	
	/**
	 * Construct the queue.
	 */
	public ListQueue( ) {
		front = back = null;
	}
	
	/**
	 * Return and remove the least recently inserted item
	 * from the queue.
	 * @return the least recently inserted item in the queue.
	 * @throws UnderflowException if the queue is empty.
	 */
	@Override
	public Object dequeue( ) {
		if( isEmpty( ) )
			throw new UnderflowException( "ListQueue dequeue" );
		
		Object returnValue = front.element;
		front = front.next;
		return returnValue;
	}
	
	/**
	 * Insert a new item into the queue.
	 * @param x the item to insert.
	 */
	@Override
	public void enqueue( Object x ) {
		if( isEmpty( ) )	// Make queue of one element
			back = front = new ListNode( x );
		else				// Regular case
			back = back.next = new ListNode( x );
	}
	
	/**
	 * Get the least recently inserted item in the queue.
	 * Does not alter the queue.
	 * @return the least recently inserted item in the queue.
	 * @throws UnderflowException if the queue is empty.
	 */
	@Override
	public Object getFront( ) {
		if( isEmpty( ) )
			throw new UnderflowException( "ListQueue getFront" );
		return front.element;
	}
	
	/**
	 * Test if the queue is logically empty.
	 * @return true if empty, false otherwise.
	 */
	@Override
	public boolean isEmpty( ) {
		return front == null;
	}
	/**
	 * Make the queue logically empty.
	 */
	@Override
	public void makeEmpty( ) {
		front = null;
		back = null;
	}
}