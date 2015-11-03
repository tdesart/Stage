package lu.uni.fstc.proactivity.queue;

import lu.uni.fstc.proactivity.db.Persistence;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * <b>Manages a simple queue of (generic) Items with two Nodes</b><br>
 * With the basic operations extended with an extra one:<br> 
 * <b>enqueue(Queue<Item> q)</b> receiving a <b>Queue</b>, not an <b>Item</b>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011
 * @version 2.0 - Sergio Marques Dias <br>
 * @version 1.0 - Yann Milin
 * @param <Item> Generic
 * 
 */
public class Queue<Item> {
	/**
	 * number of elements in queue
	 */
	private int N;

	/**
	 * pointer to the beginning of the queue (first Node)
	 */
	public Node<Item> first;

	/**
	 * pointer to the end of the queue (last Node)
	 */
	public Node<Item> last;

	/**
	 * Create an empty queue
	 */
	public Queue() {
		initializeQueue();
		Global.logger.finer("Queue created!");
	}

	/**
	 * Initialise the queue by setting the first and the last to null.<p>
	 * To be used instead of creating a new instance of the queue.<br>
	 * The garbage collector (hopefully) will that care of the rest.
	 */
	public void initializeQueue() {
		first = null;
		last  = null;
		N=0;
	}

	/**
	 * Tests the list to know queue already contains an item or not
	 * @return boolean
	 */
	public boolean isEmpty() { 
		return first == null; 
	}

	/**
	 * Returns the size of the queue (N is a counter)
	 * @return N
	 */
	public int length() {
		return N;
	}

	/**
	 * Deletes the whole Queue (List)<p>
	 * <b>WARNING:</b> using this method is very dangerous,<br> 
	 * because we don't know who else is referencing the nodes,<br> 
	 * and we're invalidating them ...
	 */

	public void deleteQueue() {
		// if queue is empty, do nothing
		if (isEmpty())
			return;

		Node<Item> x = first;
		// I think there is an error here: using twice the getNext in the same iteration
		// what happens if the queue has only one item?
		while (x.getNext() != null){
			first = x.getNext();
			x = null;
			x = first; // error here?
		}

		N = 0;
		Global.logger.finer("Queue deleted!");
	}

	/**
	 * Removes and returns the first item of the list FIFO
	 * @return item - a generic Item
	 */
	public Item dequeue() {
		if (isEmpty()) throw new RuntimeException("Queue underflow");
		
		Item item = first.getItem();
		first = first.getNext();
		N--;

		Global.logger.finer("Item dequeued. Total items in Queue: " + N);
		return item;
	}

	/**
	 * Add an item at the end of the queue
	 * @param item - a generic Item
	 */
	public void enqueue(Item item) {
		// if parameter is empty, do nothing
		if (item == null)
			return;

		Node<Item> x = new Node<Item>();
		x.setItem(item);

		if (isEmpty())
			first = x;
		else
			last.setNext(x);

		last = x; // wrong if Item has a next node
		N++;

		Global.logger.finer("Item enqueued. Total items in Queue: " + N);
	}

	/**
	 * Add an queue of items at the end of the queue
	 * @param q - a queue of generic Items
	 */
	public void enqueue(Queue<Item> q) {
		// if parameter is empty, do nothing
		if (q == null) 
			return;
		else if (q.isEmpty())
			return;

		Node<Item> x = q.first;

		if (isEmpty())
			first = x;
		else
			last.setNext(x);

		last = q.last;
		N += q.N;

		Global.logger.finer("Queue enqueued. Total items in Queue: " + N);
	}

	/**
	 * Goes through the Queue (Nodes) and prints their Items
	 * @return s
	 */
	public String iterateQueue() {
		// best practice to use a String buffer, instead of directly adding to a string using '+'
		StringBuffer buf = new StringBuffer();

		for (Node<Item> x = first; x != null; x = x.getNext())
			buf.append(x.getItem().toString() + " »» ");

		String s = buf.toString();
		Global.logger.finer("Queue iterated: " + s);
		return s;
	}

	/**
	 * Goes through the Queue (Nodes) and saves their Items
	 * @return true if successful, false if not
	 */
	public boolean saveQueue() {
		for (Node<Item> x = first; x != null; x = x.getNext())
			Persistence.save(x.getItem(), true);

		Global.logger.finer("Queue saved!");
		return true;
	}
}