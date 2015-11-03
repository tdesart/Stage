package lu.uni.fstc.proactivity.db;

import lu.uni.fstc.proactivity.parameters.ConnectionParameters;

/**
 * Top level Abstract class that holds basic attributes and methods needed to operate with any database
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2012-2013
 *
 */
abstract public class AbstractDataAccessWrapper {

	/**
	 * CONSTANT that represents a non-existing connection number
	 */
	public static final int NO_CONNECTION = GenericDataAccessOperations.NO_CONNECTION;

	/**
	 * The object that holds the Connection Parameters<br>
	 * It should be overridden at the lowest level
	 */
	public static ConnectionParameters connParams = null;

	// instances must redefine theInstance as private static
	protected AbstractDataAccessWrapper theInstance = null;

	/**
	 * Messaging Rule needs some informations about the connection parameters
	 */
	private boolean connected = false;

	/**
	 * This field is only used for getting the connection to be used in transactions 
	 */
	protected int conn = NO_CONNECTION;

	/**
	 * AbstractDbWrapper Constructor<br>
	 * <b>Private</b> (protected, so that it's accessed from 'children') to implement the "Singleton" Object 
	 */
	protected AbstractDataAccessWrapper() {
	}

	/**
	 * It is declared here to make it visible<br>
	 * The idea is that the children should override it.<br>
	 * If they don't, a runtime exception will be thrown when trying to access it<br>
	 * Can not be abstract, because then it would prevent it to be static at the children level<br>
	 * @param configFilePath 
	 * @return the AbstractDbWrapper instance
	 * @throws Exception 
	 */
	protected static synchronized AbstractDataAccessWrapper getInstance (final String configFilePath) throws Exception {
		throw new UnsupportedOperationException("This method cannot be called directly (in this abstract class), it has to be overloaded at a lower, concrete level!");
	}

	// Prevent cloning, to be a real "Singleton"
	// Cloning the object can still copy it and result into a duplicate object
	@Override
	public final Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	/**
	 * @return boolean
	 */
	public final boolean isConnected() {
		return this.connected;
	}

	/**
	 * @param connected
	 */
	public final void setConnected(boolean connected) {
		this.connected = connected;
	}

 	/**
	 * to allow for the children to extend, if needed<br>
	 * ex: clearing cache at the end of a certain operation, or on demand  
	 */
	public void terminateOperation() {
	}

	/**
	 * @return the conn
	 */
	public int getConn() {
		return conn;
	}

	/**
	 * @return AbstractMySQLDbWrapper
	 */
	public abstract AbstractDataAccessWrapper getThis(); 
}
