package lu.uni.fstc.proactivity.db;

import lu.uni.fstc.proactivity.parameters.ConnectionParameters;

/**
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2015
 *
 */
public abstract class GenericDataAccessOperations {

	/**
	 * CONSTANT that represents a non-existing connection number
	 */
	public static final int NO_CONNECTION = -1;

	/**
	 * CONSTANTs that represents the type of query to be executed, and the expected return type
	 * 1 - one SELECT query returning 1 field
	 */
	protected static final int QTYPE_SELECT_FIELD = 1; 
	/**
	 * 2 - one SELECT query returning a resultSet
	 */
	protected static final int QTYPE_SELECT_RS = 2; 
	/**
	 * 3 - one UPDATE/INSERT query
	 */
	protected static final int QTYPE_EXECUTE = 3; 
	/**
	 * 4 - a Batch of UPDATE/INSERT queries
	 */
	protected static final int QTYPE_BATCH = 4; 

	/**
	 * @param params a DbConnectionParameters object
	 * @return a connection index to the URL given by the params
	 */
	public abstract int openConnection(final ConnectionParameters params);

	/**
	 * @param connectionIndex the integer representing the index of the connection in the connection pool
	 */
	public abstract void getConnectionMetaData(final int connectionIndex);

	/**
	 * End the current transaction by rolling back the operations
	 * @param connectionIndex the integer representing the index of the connection in the connection pool
	 */
	public abstract void rollback(final int connectionIndex);

	/**
	 * End the current transaction by committing the operations
	 * @param connectionIndex the integer representing the index of the connection in the connection pool
	 */
	public abstract void commit(final int connectionIndex);

	/**
	 * The low level method that executes either ONE query, or a BATCH of them, whether its an UPDATE (Update + Insert) or a SELECT<br>
	 * The SQL exceptions are catch at this level, to be able to rollback the full execution automatically, and report back the number of affected rows.<br><br>
	 * 
	 * @param connectionIndex the integer representing the index of the connection in the connection pool
	 * @param q the String representing the Query
	 * @param batch an ARRAY of Strings representing the batch of Queries
	 * @param queryType possible values:<br>
	 *          <code><i>QTYPE_SELECT_FIELD</i></code> - one SELECT QUERY returning 1 field<br>
	 *          <code><i>QTYPE_SELECT_RS</i></code> - one SELECT query returning a resultSet<br> 
	 *          <code><i>QTYPE_EXECUTE</i></code> - one UPDATE/INSERT query<br>
	 *          <code><i>QTYPE_BATCH</i></code> - a Batch of UPDATE/INSERT queries<br> 
	 * @return Object  
	*/
	 protected abstract Object execute (final int connectionIndex, final String q, final String[] batch, final int queryType);

	/**
	 * Method that executes a given array of Query strings as a transaction by defining Query_TYPE as BATCH<br>
	 * @param conn the integer representing the index of the connection in the connection pool
	 * @param set an ARRAY of Strings representing the batch of Queries
	 * @return the array of results of type int[]
	 */
	public abstract int[] executeBatch(final int conn, final String[] set);

	/**
	 * Method that executes a given Query string  by defining Query_TYPE as EXECUTE<br>
	 * @param conn the integer representing the index of the connection in the connection pool
	 * @param sql the String representing the Query
	 * @return propagate the result from the actual sql execution
	//	 * @throws SQLException 
	 */
	public abstract long executeUpdate(final int conn, final String sql);

}