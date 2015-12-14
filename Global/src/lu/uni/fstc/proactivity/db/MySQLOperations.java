package lu.uni.fstc.proactivity.db;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

// Changed from java.sql.Connection to com.mysql.jdbc.Connection, in order to be able to use the setContinueBatchOnError method.
// import java.sql.Connection;
import com.mysql.jdbc.Connection;

import lu.uni.fstc.proactivity.parameters.ConnectionParameters;
import lu.uni.fstc.proactivity.parameters.DbConnectionParameters;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.StringUtils;

/**
 * This class provides static methods that implement the basic MySQL operations.
 * <p>
 * We provide an <code>open</code>, <code>reopen</code> (reuses the same
 * connectionPool slot) and <code>check connection</code>.<br>
 * We provide different <b><i>executes</i></b>, to hide the specifics of the
 * different <b>types of executes</b> (batch update/single update/select);<br>
 * and <b>types of</b> expected <b>return values</b> (resultSet, integer,
 * boolean, long, etc.)<br>
 * <br>
 * The low level <code>execute(connectionIndex, q, batch, queryType)</code>
 * method accepts a QueryType as parameter with the following values and
 * meanings:<br>
 *     <code>QTYPE_SELECT_FIELD = 1</code> - one SELECT query returning 1 field
 * <br>
 *     <code>QTYPE_SELECT_RS = 2</code> - one SELECT query returning a resultSet
 * <br>
 *     <code>QTYPE_EXECUTE = 3</code> - one UPDATE/INSERT query<br>
 *     <code>QTYPE_BATCH = 4</code> - a Batch of UPDATE/INSERT queries<br>
 * <br>
 * We also provide the mechanism to manage transactions in case of batch
 * updates: start transaction, commit and rollback.<br>
 * <br>
 * <b>NOTE 1:</b> In order to use transactions, all tables must have the engine
 * defined as <b>InnoDB</b>.<br>
 * <b>NOTE 2:</b> In order to use the <code>setContinueBatchOnError</code>
 * method, needed to change <code>import</code> from
 * <code>java.sql.Connection</code> <code>to com.mysql.jdbc.Connection</code>.
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2012-2013
 *
 */
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE", justification = "")
public final class MySQLOperations extends GenericDataAccessOperations {

	/*
	 * *************************************************************************
	 * *********
	 * *************************************************************************
	 * ********* Attributes and Methods to implement the "Singleton" Object
	 * *************************************************************************
	 * *********
	 * *************************************************************************
	 * *********
	 */

	private static MySQLOperations theInstance = null;

	/**
	 * the Constructor is <b>Private</b> to implement the "Singleton" Object
	 */
	private MySQLOperations() {

	}

	/**
	 * the public method to get the objetc's instance
	 * 
	 * @return MySQLOperations
	 */
	public static synchronized MySQLOperations getInstance() {
		if (theInstance == null) {
			theInstance = new MySQLOperations();
		}
		return theInstance;
	}

	/*
	 * *************************************************************************
	 * *********
	 * *************************************************************************
	 * *********
	 * *************************************************************************
	 * ********* Connection Management Methods and Attributes
	 * *************************************************************************
	 * *********
	 * *************************************************************************
	 * *********
	 * *************************************************************************
	 * *********
	 */

	/**
	 * Attributes to manage the several connections needed in a system
	 */
	private static final int maxPoolSize = 20;
	// current size
	private static int connectionPoolSize = 0;

	// one for NORMAL use (connectionPool),
	private static Connection connectionPool[] = new Connection[maxPoolSize];
	// another to be able to RECONNECT (connectionParameterPool)
	private static DbConnectionParameters connectionParameterPool[] = new DbConnectionParameters[maxPoolSize];

	/**
	 * @see lu.uni.fstc.proactivity.db.GenericDataAccessOperations#openConnection(lu.uni.fstc.proactivity.parameters.ConnectionParameters)
	 */
	@Override
	public int openConnection(final ConnectionParameters params) {
		// ask for a new connection to be added in the last position of the
		// connection pool
		return openConnection((DbConnectionParameters) params, connectionPoolSize);
	}

	/**
	 * This method simulates a connection opened in a command line like: <br>
	 * <code>$> mysql moodle_test -uroot -h moodle-test.uni.lux -pmoodle <br></code>
	 * <b>Template:</b><br>
	 * <code>String url = "jdbc:mysql://computer_name:port/schema";</code><br>
	 * <br>
	 * <b>Hard Coded Example:</b><br>
	 * <code>String url = "jdbc:mysql://moodle-test.uni.lux:3306/PAS"; <br>
	 * 		String user = "root"; <br>
	 * 		String password = "moodle";</code><br>
	 * <br>
	 * <b>With parameters:</b><br>
	 * <code>String url = "jdbc:mysql://" + computer + ":" + port + "/" + schema;</code>
	 * <br>
	 * <br>
	 * This method also sets the connection flag
	 * <code>continueBatchOnError</code> to stop executing queries as soon as
	 * the first error occurs.<br>
	 * <i>It only applies to Batch executions.</i><br>
	 * It is verified during the statement execution, but only set at the
	 * statement constructor,<br>
	 * Thus, it MUST be set before calling any
	 * <code>conn.createStatement()</code>
	 * 
	 * @param params
	 *            a DbConnectionParameters object
	 * @param index
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @throws SQLException
	 */
	private synchronized int openConnection(final DbConnectionParameters params, final int index) {
		try {
			// refuse to try an open a new connection if parameters are empty
			if (params == null) {
				Global.logger.warning("Invalid Parameters!");
				return NO_CONNECTION;
			}

			// refuse to try an open a new connection if index is out of range
			if (index > connectionPoolSize || index < 0) {
				Global.logger.warning("Invalid index [" + index + "]");
				return NO_CONNECTION;
			}

			Connection conn = null;

			final String url = "jdbc:mysql://" + params.hostName + ":" + params.portNumber + "/" + params.schemaName;
			conn = (Connection) java.sql.DriverManager.getConnection(url, params.userName, params.password);

			Global.logger.finest("BEFORE getContinueBatchOnError()=" + conn.getContinueBatchOnError());
			try {
				conn.setContinueBatchOnError(false);
			} catch (Exception e) {
				Global.logger.warning("UNABLE to setContinueBatchOnError()!");
			}
			Global.logger.finest("AFTER getContinueBatchOnError()=" + conn.getContinueBatchOnError());

			// Store connection and parameters in the pool
			connectionPool[index] = conn;
			connectionParameterPool[index] = params;
			if (index == connectionPoolSize) // meaning, it is a new connection
				connectionPoolSize++;

			Global.logger.config("opened connection in connection's pool slot " + index + "; Pool Size: "
					+ connectionPoolSize + "!");
			return index;
		} catch (final Exception e) {
			String msg = "ERROR openning connection [" + index + "]";
			Global.logger.severe(msg);
			e.printStackTrace();
			// throw new RuntimeException(msg);
			return NO_CONNECTION;
		}
	}

	/**
	 * @param connectionIndex
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 */
	private boolean reOpenConnection(final int connectionIndex) {
		if (openConnection(connectionParameterPool[connectionIndex], connectionIndex) == NO_CONNECTION) {
			Global.logger.severe(
					"FAILED to re-open the database connection [" + connectionIndex + "] before the query execution.");
			throw new RuntimeException(
					"FAILED to re-open the database connection [" + connectionIndex + "] before the query execution.");
			// return false;
		} else {
			Global.logger.info(
					"SUCCEDED to re-open the database connection [" + connectionIndex + "] before the query execution");
			return true;
		}
	}

	/**
	 * Check if the connection is valid.<br>
	 * If not, wait for 30 seconds and try to reopen it
	 * 
	 * @param connectionIndex
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @param conn
	 * @param q
	 * @return a boolean, TRUE if the connection is valid, FALSE if it failed to
	 *         reopen it
	 * @throws SQLException
	 */
	private boolean checkConnection(final int connectionIndex, final Connection conn, final String q)
			throws SQLException {
		boolean hasConnection = false;

		if (!conn.isValid(0)) {
			Global.logger.severe("Database connection [" + connectionIndex + "]. ERROR detected executing: " + q);
			lu.uni.fstc.proactivity.utils.Time.sleepNSeconds(30);
			hasConnection = reOpenConnection(connectionIndex);

			// the connection has changed, save it on the same slot as the 'old'
			// invalid one
			// if (hasConnection)
			// conn = connectionPool[connectionIndex];

		} else {
			hasConnection = true;
		}
		return hasConnection;
	}

	/*
	 * *************************************************************************
	 * *********
	 * *************************************************************************
	 * *********
	 * *************************************************************************
	 * ********* Transaction management Methods
	 * *************************************************************************
	 * *********
	 * *************************************************************************
	 * *********
	 * *************************************************************************
	 * *********
	 */

	/**
	 * @see lu.uni.fstc.proactivity.db.GenericDataAccessOperations#getConnectionMetaData(int)
	 */
	@Override
	public void getConnectionMetaData(final int connectionIndex) {
		Connection conn = connectionPool[connectionIndex];
		java.sql.DatabaseMetaData metaData;
		try {
			metaData = conn.getMetaData();
			Global.logger.info("Supports Batch Operations  : " + metaData.supportsBatchUpdates());
			Global.logger.info("Supports Transactions      : " + metaData.supportsTransactions());
			Global.logger.info("Supports Stored Procedures : " + metaData.supportsStoredProcedures());
			Global.logger.info("Supports Column Aliasing   : " + metaData.supportsColumnAliasing());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * @param connectionIndex
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @return a SQL statement associated with the connection
	 */
	public static java.sql.Statement getStatement(final int connectionIndex) {
		try {
			Connection conn = connectionPool[connectionIndex];
			return conn.createStatement();
		} catch (SQLException e) {
			Global.logger.severe("Error in providing a statement from connection [" + connectionIndex + "]");
			return null;
		}
	}

	/**
	 * @param connection
	 *            a Connection
	 * @return a boolean indicating the success of the operation
	 */
	public synchronized boolean startTransaction(final Connection connection) {
		if (connection == null)
			return false;
		try {
			connection.setAutoCommit(false);
			return true;
		} catch (SQLException e) {
			Global.logger.warning("Transaction start failed!!");
			return false;
		}
	}

	/**
	 * @param connectionIndex
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @return a boolean indicating the success of the operation
	 */
	public boolean startTransaction(final int connectionIndex) {
		Connection conn = connectionPool[connectionIndex];
		return startTransaction(conn);
	}

	/**
	 * Centralised end-point method to manage the end of a transaction.<br>
	 * Valid for <code>rollback</code> and <code>commit</code>.
	 * 
	 * @param connection
	 *            the current Connection
	 * @param rollback
	 *            <br>
	 *                  true if <b>rollback</b> intended, <br>
	 *                  false if <b>commit</b> intended.
	 */
	public synchronized void endTransaction(final Connection connection, final boolean rollback) {
		if (connection == null)
			return;

		// check if AutoCommit is true (= not a transaction),
		// so rollback and commit are not valid, and cannot not be executed
		try {
			if (connection.getAutoCommit()) {
				Global.logger.warning(
						"The explicit call to 'Rollback' or 'Commit' was ignored, because a Transaction is not in use (AutoCommit is set to 'true')!!");
				return;
			}
		} catch (SQLException ignore) {
		}

		try {
			if (rollback) {
				Global.logger.fine("Rollback Transaction!!");
				connection.rollback();
			} else {
				Global.logger.fine("Commit Transaction!!");
				connection.commit();
			}
		} catch (SQLException e) {
			Global.logger.severe("Transaction Ending failed on connection " + connection);
			e.printStackTrace();
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException ignore) {
			}
		}
	}

	/**
	 * End the current transaction by rolling back the operations
	 * 
	 * @param connection
	 *            the current Connection
	 */
	private synchronized void rollback(final Connection connection) {
		endTransaction(connection, true);
	}

	/**
	 * @see lu.uni.fstc.proactivity.db.GenericDataAccessOperations#rollback(int)
	 */
	@Override
	public void rollback(final int connectionIndex) {
		Connection conn = connectionPool[connectionIndex];
		endTransaction(conn, true);
	}

	/**
	 * End the current transaction by committing the operations
	 * 
	 * @param connection
	 *            the current Connection
	 */
	private synchronized void commit(final Connection connection) {
		endTransaction(connection, false);
	}

	/**
	 * @see lu.uni.fstc.proactivity.db.GenericDataAccessOperations#commit(int)
	 */
	@Override
	public void commit(final int connectionIndex) {
		Connection conn = connectionPool[connectionIndex];
		endTransaction(conn, false);
	}

	/*
	 * *************************************************************************
	 * *********
	 * *************************************************************************
	 * *********
	 * *************************************************************************
	 * ********* THE Query Execution Method
	 * *************************************************************************
	 * *********
	 * *************************************************************************
	 * *********
	 * *************************************************************************
	 * *********
	 */

	/**
	 * The low level method that executes either ONE query, or a BATCH of them,
	 * whether its an UPDATE (Update + Insert) or a SELECT<br>
	 * The SQL exceptions are catch at this level, to be able to rollback the
	 * full execution automatically, and report back the number of affected
	 * rows.<br>
	 * <br>
	 * After the Query execution, the statement is closed, except if the Query
	 * Type is QTYPE_SELECT_RS<br>
	 * Since the ResultSet is returned by the method, nor the Statement, nor the
	 * ResultSet can be closed,<br>
	 * otherwise the data would no longer be accessible.<br>
	 * <br>
	 * The concrete connections are managed by the DriverManager's using a
	 * connection pool.<br>
	 * Tests have been conducted that showed that the connection(s) was(were)
	 * closed after the program finished execution.<br>
	 * Even using cache(s) to store the ResultSet!
	 * 
	 * @param connectionIndex
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @param q
	 *            the String representing the Query
	 * @param batch
	 *            an ARRAY of Strings representing the batch of Queries
	 * @param queryType
	 *            possible values:<br>
	 *                     <code><i>QTYPE_SELECT_FIELD</i></code> - one SELECT
	 *            QUERY returning 1 field<br>
	 *                     <code><i>QTYPE_SELECT_RS</i></code> - one SELECT
	 *            query returning a resultSet<br>
	 *                     <code><i>QTYPE_EXECUTE</i></code> - one UPDATE/INSERT
	 *            query<br>
	 *                     <code><i>QTYPE_BATCH</i></code> - a Batch of
	 *            UPDATE/INSERT queries<br>
	 * @return Object
	 */
	@Override
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "ODR_OPEN_DATABASE_RESOURCE", justification = "Statement has to be left open to be able to cache (and re-read) the ResultSet at a later time")
	protected synchronized Object execute(final int connectionIndex, final String q, final String[] batch,
			final int queryType) {
		Object ret = Long.valueOf(0);

		// Verify if the correct parameter is filled, depending on the QueryType
		// TODO re-test this
		if ((queryType == QTYPE_SELECT_FIELD || queryType == QTYPE_SELECT_RS || queryType == QTYPE_EXECUTE)
				&& (StringUtils.isEmptyOrNullString(q))) {
			Global.logger.warning("Empty query string sent to execute!!");
			return ret;
		}

		// TODO re-test this
		if ((queryType == QTYPE_BATCH) && (batch.length == 0 || StringUtils.isEmptyOrNullString(batch[0]))) {
			Global.logger.warning("Empty set of query strings sent to batch execution on a transaction!!");
			return ret;
		}

		java.sql.Statement st = null;
		Connection conn = null;

		try {
			conn = connectionPool[connectionIndex];
			if (checkConnection(connectionIndex, conn, q)) {
				st = conn.createStatement();

				if (queryType == QTYPE_SELECT_FIELD || queryType == QTYPE_SELECT_RS) {
					Global.logger.finest("QUERY = " + q);
					ResultSet rs;
					rs = st.executeQuery(q);
					if (queryType == QTYPE_SELECT_FIELD) {
						if (rs.next())
							ret = rs.getObject(1); // column index
					} else // queryType == QTYPE_SELECT_RS
						ret = rs;
				} else if (queryType == QTYPE_EXECUTE) {
					ret = Long.valueOf(0); // initialised with zero, to comply
											// with the requirements of the
											// executeUpdate
					ret = Long.valueOf(st.executeUpdate(q));
				} else if (queryType == QTYPE_BATCH) {
					if (!startTransaction(conn))
						return Long.valueOf(0);

					// initialise with an array of integers, of size 0
					ret = Integer.valueOf(0); // new int[0];
					// add all query strings to the Batch
					for (int i = 0; i < batch.length; i++)
						st.addBatch(batch[i]);

					// execute everything (on the Batch)
					Global.logger.finest("BATCH QUERY = " + st.toString());
					ret = st.executeBatch();

					// unless an exception was thrown, commit the work
					commit(conn);
					Global.logger.finer("Transaction ended with a COMMIT!!");
				} else
					Global.logger.warning("Local attribute queryType has a an illegal value =" + queryType);
			}
		} catch (final com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e) {
			Global.logger
					.severe("Database connection [" + connectionIndex + "]. SQL EXCEPTION detected executing: " + q);
			e.printStackTrace();
			Global.logger.info("Pausing for 5 seconds, before trying to re-open the connection...");
			lu.uni.fstc.proactivity.utils.Time.sleepNSeconds(5);
			reOpenConnection(connectionIndex);
		} catch (final java.sql.BatchUpdateException e) {
			// Not all of the statements were successfully executed
			Global.logger.severe("Error executing batch of SQL queries !");

			int[] updateCounts = e.getUpdateCounts();
			ret = updateCounts;
			for (int i = 0; i < updateCounts.length; i++) {
				if (updateCounts[i] >= 0)
					Global.logger.fine("Query [" + i + "] successfully executed; number of affected rows:"
							+ updateCounts[i] + "\n" + batch[i] + "\n");
				else if (updateCounts[i] == java.sql.Statement.SUCCESS_NO_INFO)
					Global.logger.warning("Query [" + i
							+ "] successfully executed; number of affected rows not available :\n" + batch[i] + "\n");
				else if (updateCounts[i] == java.sql.Statement.EXECUTE_FAILED)
					Global.logger.severe("Query [" + i + "] failed to execute :\n" + batch[i] + "\n");
			}
			e.printStackTrace();
			rollback(conn);
			Global.logger.finer("Transaction ended with a ROLLBACK!");
		} catch (final java.sql.SQLException e) {
			if (!StringUtils.isEmptyOrNullString(q))
				Global.logger.severe("SQLException with query:\n" + q.toString());
			e.printStackTrace();
			try {
				if (!conn.getAutoCommit())
					rollback(conn);
			} catch (SQLException e1) {
				Global.logger.finer("Unable to verify the Autocommit of the database, thus Rollback is not possible!");
				e1.printStackTrace();
			}
		} finally {
			try {
				if (st != null) {
					if (queryType != QTYPE_SELECT_RS)
						st.close();

					// clear batch if the Query Type is QTYPE_BATCH
					if (queryType == QTYPE_BATCH)
						st.clearBatch();
				}
			} catch (Exception e) {
				// if Statement.close or Statement.clearBatch fails, ignore the
				// exception and move on...
			}
		}

		Global.logger.finest("Returning " + ret);
		return ret;
	}

	/*
	 * *************************************************************************
	 * *********
	 * *************************************************************************
	 * *********
	 * *************************************************************************
	 * ********* PUBLIC STATIC Methods come here, to access data from the data
	 * source, that call the centralised execute method
	 * *************************************************************************
	 * *********
	 * *************************************************************************
	 * *********
	 * *************************************************************************
	 * *********
	 */

	/**
	 * @see lu.uni.fstc.proactivity.db.GenericDataAccessOperations#executeBatch(int,
	 *      java.lang.String[])
	 */
	@Override
	public int[] executeBatch(final int conn, final String[] set) {
		return (int[]) execute(conn, null, set, QTYPE_BATCH);
	}

	/**
	 * @see lu.uni.fstc.proactivity.db.GenericDataAccessOperations#executeUpdate(int,
	 *      java.lang.String)
	 */
	@Override
	public long executeUpdate(final int conn, final String sql) {
		return ((Long) execute(conn, sql, null, QTYPE_EXECUTE)).longValue();

		// The following code was integrated in execute() in November 2012
		/*
		 * / try { Connection conn = connectionPool[connectionIndex]; Statement
		 * st = conn.createStatement(); st.executeUpdate(sql); st.close(); }
		 * catch (SQLException e) { Global.logger.severe("Query: " + sql + "\n"+
		 * e.getMessage()); } /
		 **/
	}

	/**
	 * @param conn
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @param sql
	 *            the String representing the Query
	 * @return one integer value from the execution of the select (first record
	 *         read)
	 */
	public int getIntFromSelect(final int conn, final String sql) {
		try {
			return ((Integer) execute(conn, sql, null, QTYPE_SELECT_FIELD)).intValue();
		} catch (ClassCastException e) {
			Global.logger.severe(
					"Error trying to cast the result into an 'int'.\n\tTry calling 'getLongFromSelect', 'getDoubleFromSelect' or 'getBIntFromSelect' instead! - the one that best suits your needs.");
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * @param conn
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @param table
	 * @param whereClause
	 * @param column
	 * @return one integer value from the execution of the select (first record
	 *         read)
	 */
	public int getIntFromSelect(final int conn, final String table, final String whereClause, final String column) {
		String q = "Select " + column + " from " + table + " where " + whereClause;
		return getIntFromSelect(conn, q);
	}

	/**
	 * Instead of casting the result of the execution to a boolean, this method
	 * needs to do some more validations,<br>
	 * in order to treat special cases (like empty result sets), thus avoiding
	 * throwing exceptions.
	 * 
	 * @param conn
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @param sql
	 *            the String representing the Query
	 * @return one boolean value from the execution of the select (first record
	 *         read)
	 */
	public boolean getBooleanFromSelect(final int conn, final String sql) {
		// return ((Boolean) executeQuery(conn, sql)).booleanValue(); //
		Object received = execute(conn, sql, null, QTYPE_SELECT_FIELD);
		if (received == null)
			return false;
		long ret = ((Number) received).longValue();
		boolean b = (ret == 0) ? false : true;
		Global.logger.finest("long [" + ret + "] = boolean [" + b + "] ");
		return b;
	}

	/**
	 * @param conn
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @param table
	 * @param whereClause
	 * @param column
	 * @return one boolean value from the execution of the select (first record
	 *         read)
	 */
	public boolean getBooleanFromSelect(final int conn, final String table, final String whereClause,
			final String column) {
		String q = "Select " + column + " from " + table + " where " + whereClause;
		// Global.logger.fine(q);
		return getBooleanFromSelect(conn, q);
	}

	/**
	 * @param conn
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @param sql
	 *            the String representing the Query
	 * @return one long integer value from the execution of the select (first
	 *         record read)
	 */
	public java.math.BigInteger getBIntFromSelect(final int conn, final String sql) {
		return ((java.math.BigInteger) execute(conn, sql, null, QTYPE_SELECT_FIELD));
	}

	/**
	 * @param conn
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @param table
	 * @param whereClause
	 * @param column
	 * @return one long integer value from the execution of the select (first
	 *         record read)
	 */
	public java.math.BigInteger getBIntFromSelect(final int conn, final String table, final String whereClause,
			final String column) {
		String q = "Select " + column + " from " + table + " where " + whereClause;
		return getBIntFromSelect(conn, q);
	}

	/**
	 * @param conn
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @param sql
	 *            the String representing the Query
	 * @return one long integer value from the execution of the select (first
	 *         record read)
	 */
	public long getLongFromSelect(final int conn, final String sql) {
		return ((Number) execute(conn, sql, null, QTYPE_SELECT_FIELD)).longValue();
	}

	/**
	 * @param conn
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @param table
	 * @param whereClause
	 * @param column
	 * @return one long integer value from the execution of the select (first
	 *         record read)
	 */
	public long getLongFromSelect(final int conn, final String table, final String whereClause, final String column) {
		String q = "Select " + column + " from " + table + " where " + whereClause;
		return getLongFromSelect(conn, q);
	}

	/**
	 * @param conn
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @param sql
	 *            the String representing the Query
	 * @return one long integer value from the execution of the select (first
	 *         record read)
	 */
	public double getDoubleFromSelect(final int conn, final String sql) {
		return ((Double) execute(conn, sql, null, QTYPE_SELECT_FIELD)).doubleValue();
	}

	/**
	 * @param conn
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @param table
	 * @param whereClause
	 * @param column
	 * @return one long integer value from the execution of the select (first
	 *         record read)
	 */
	public double getDoubleFromSelect(final int conn, final String table, final String whereClause,
			final String column) {
		String q = "Select " + column + " from " + table + " where " + whereClause;
		return getDoubleFromSelect(conn, q);
	}

	/**
	 * @param conn
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @param sql
	 *            the String representing the Query
	 * @return one String value from the execution of the select (first record
	 *         read)
	 */
	public String getStringFromSelect(final int conn, final String sql) {
		return execute(conn, sql, null, QTYPE_SELECT_FIELD).toString();
	}

	/**
	 * @param conn
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @param table
	 * @param whereClause
	 * @param column
	 * @return one String value from the execution of the select (first record
	 *         read)
	 */
	public String getStringFromSelect(final int conn, final String table, final String whereClause,
			final String column) {
		String q = "Select " + column + " from " + table + " where " + whereClause;
		return getStringFromSelect(conn, q);
	}

	/**
	 * @param conn
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @param q
	 * @return the array resulting from the execution of the select (first
	 *         record read)
	 */
	public ResultSet getArrayFromSelect(final int conn, final String q) {
		return (ResultSet) execute(conn, q, null, QTYPE_SELECT_RS);
		// The following code was integrated in execute() in November 2012
		/*
		 * / Connection conn = connectionPool[connectionIndex];
		 * java.sql.Statement st = null; ResultSet rs = null; try { st =
		 * conn.createStatement(); rs = st.executeQuery(q); } catch (Exception
		 * e) { Global.logger.severe(e.getMessage()); e.printStackTrace(); if
		 * (st != null) { try { st.close(); } catch (Exception e2) { //if
		 * Statement.close fails, ignore the exception and move on... } } }
		 * 
		 * return rs; /
		 **/
	}

	/**
	 * @param conn
	 *            the integer representing the index of the connection in the
	 *            connection pool
	 * @param table
	 * @param whereClause
	 * @param column
	 * @return the array resulting from the execution of the select (first
	 *         record read)
	 */
	public ResultSet getArrayFromSelect(final int conn, final String table, final String whereClause,
			final String column) {
		String q = "Select " + column + " from " + table + " where " + whereClause;
		return getArrayFromSelect(conn, q);
	}
	
	/* ------------------------------------------------------------------------------------
	 * ------------------------------------------------------------------------------------
	 * ACTIVE DIRECTORY METHODS
	 * ------------------------------------------------------------------------------------
	 * ------------------------------------------------------------------------------------
	 */

	public void createTable(final int conn) {
		String q = "CREATE  TABLE IF NOT EXISTS `Group_delete` ("
				+ "`idGroup_delete` INT NOT NULL AUTO_INCREMENT ,"
				+ "`name` VARCHAR(256) NOT NULL ,"
				+ "`delete` TINYINT(1)  NOT NULL ,"
				+ "`reason` VARCHAR(256) NULL ,"
				+ "PRIMARY KEY (`idGroup_delete`) )";
		executeUpdate(conn, q);
	}

	public long tableExist(final int conn, String tableName) {
		String q = "SHOW TABLES LIKE '" + tableName+"'";
		return executeUpdate(conn, q);
	}

	public long countLines(final int conn, String tableName) {
		String q = "SELECT COUNT(*) FROM `" + tableName+"`";
		return getLongFromSelect(conn, q);
	}

	public void insertInto(final int conn, String groupeName, boolean result,String reason) {
		String q = "INSERT INTO `Group_delete` (name, `delete`, reason ) VALUES ('" + groupeName + "'," + result + ",'"+reason+"')";
		executeUpdate(conn, q);
	}

	public void dropTable(final int conn,String tableName) {
		String q = "DROP TABLE IF EXISTS `"+tableName+"`";
		executeUpdate(conn, q);		
	}

	public void createTable(int conn, String tableName) {
		String q = "CREATE  TABLE IF NOT EXISTS `"+tableName+"` (" 
				+ "Name VARCHAR(256) NOT NULL ," 
				+ "Age BIGINT NULL ,"
				+ "Role VARCHAR(45) NULL)";
		executeUpdate(conn, q);
	}

	public void insertInto(int conn, String tableName, ArrayList memberOf,String distinguishedName) {
		String q = "INSERT INTO `"+tableName+"` (Name) VALUES ";
		for(int i = 0; i<memberOf.size()-1;i++){
			String s = (String) memberOf.get(i);
			if(!s.equals(distinguishedName))
				q = q + "('"+s+"'),";
		}
		q = q + "('" +memberOf.get(memberOf.size()-1)+"');"; 
		executeUpdate(conn, q);
		
	}
	
	public void insertInto(int conn, String tableName, String s, String s1, String role) {
		String q = "INSERT INTO `"+tableName+"` (Name, Age,Role) VALUES ('"+s+"',"+s1+",'"+role+"')";
		executeUpdate(conn, q);
		
	}
	
	public void insertInto(int conn, String tableName, String s, String s1) {
		deleteFrom(conn, tableName, s);
		String q = "INSERT INTO `"+tableName+"` (Name, Age) VALUES ('"+s+"',"+s1+")";
		executeUpdate(conn, q);
	}
	
	public void deleteFrom(int conn, String tableName, String name){
		String q = "DELETE FROM `"+tableName+"`WHERE name ='"+name+"'";
		executeUpdate(conn, q);
	}
	
	public String getManager(final int conn, String tableName){
		String q = "SELECT Name from("
				+ "SELECT COUNT(Name)AS nbr_doublon, Name "
				+ "FROM     `"+tableName+"` "
				+ "GROUP BY Name "
				+ "ORDER BY nbr_doublon DESC "
				+ "LIMIT 1) as T";
		return getStringFromSelect(conn, q);
	}
	
	public String getFinish(final int conn, String tableName){
		String q = "SELECT Name"
				+" FROM `"+tableName+"`"
				+" WHERE Name = 'Finish'";
		return getStringFromSelect(conn, q);
	}
	
	public ResultSet getTopGroup(final int conn, String tableName){
		String q = "SELECT Name from("
				+ "SELECT COUNT(Name)AS nbr_doublon, Name "
				+ "FROM     `"+tableName+"` "
				+ "WHERE Name NOT IN (SELECT name FROM Exclusion_suggestion)"
				+ "GROUP BY Name "
				+ "ORDER BY nbr_doublon DESC "
				+ "LIMIT 3) as T";
		return getArrayFromSelect(conn, q);
	}
	
	public long getMean(final int conn, String tableName){
		String q = "SELECT AVG(Age)"
				+" FROM `"+tableName+"`";
		return getLongFromSelect(conn, q);
	}
	
	public long getMax(final int conn, String tableName){
		String q = "SELECT MAX(Age)"
				+" FROM `"+tableName+"`";
		return getLongFromSelect(conn, q);
	}
	
	public String getRole(final int conn, String tableName){
		String q = "SELECT Role from("
				+ "SELECT COUNT(Role)AS nbr_doublon, Role "
				+ "FROM     `"+tableName+"` "
				+ "GROUP BY Role "
				+ "ORDER BY nbr_doublon DESC "
				+ "LIMIT 1) as T";
		return getStringFromSelect(conn, q);
	}
	
	public void createNewProfile(final int conn ,Profil group){
		String[] t= group.getTopGroup();
		String q = "INSERT INTO TopGroups (Group1, Group2, Group3) VALUES('"+t[0]+"','"+t[1]+"','"+t[2]+"')";
		executeUpdate(conn,q);
		q = "INSERT INTO Profil (manager,activity,size,role,topGroups) VALUES ('"+group.getManager()+"',0,"+group.getSizeMeasure()+",'"+group.getRole()+"',LAST_INSERT_ID())";
		executeUpdate(conn, q);
		q = "INSERT INTO Groups (name,whenCreated,nbrMembers,whenChanged,manager,ageMembers,youngestMember,role,profil) VALUES ("
				+ "'"+group.getDistinguishedName()+"',"
				+ group.getWhenCreated()+","
				+ group.getMembersSize()+","
				+ group.getWhenChanged()+","
				+ "'"+group.getManager()+"',"
				+ group.getMeanCreationDate()+","
				+ group.getMostRecentCreationDate()+","
				+ "'"+group.getRole()+"',"
				+ "LAST_INSERT_ID())";
		executeUpdate(conn,q);
	}
	
	public ResultSet checkProfile(final int conn, Profil group){
		String q = "SELECT idProfil "
				+ "FROM Profil,TopGroups "
				+ "WHERE manager = '"+group.getManager()+"' AND "
				+ "topGroups = idTopGroups AND "
				+ "role = '"+group.getRole()+"' AND "
				+ "size = "+group.getSizeMeasure()+" AND "				
				+ "'"+group.getTopGroup()[0] +"' IN (Group1,Group2,Group3) AND "
				+ "'"+group.getTopGroup()[1] +"' IN (Group1,Group2,Group3) AND "
				+ "'"+group.getTopGroup()[2] +"' IN (Group1,Group2,Group3);";
		return getArrayFromSelect(conn, q);
	}
	
	public void insertGroup(final int conn, Profil group, int idProfil){
		String q = "INSERT INTO Groups (name,whenCreated,nbrMembers,whenChanged,manager,ageMembers,youngestMember,role,profil) VALUES ("
				+ "'"+group.getDistinguishedName()+"',"
				+ group.getWhenCreated()+","
				+ group.getMembersSize()+","
				+ group.getWhenChanged()+","
				+ "'"+group.getManager()+"',"
				+ group.getMeanCreationDate()+","
				+ group.getMostRecentCreationDate()+","
				+ "'"+group.getRole()+"',"
				+ idProfil+")";
		executeUpdate(conn,q);
	}
	
	public void cleanDb(final int conn){
		String q = "CREATE TABLE IF NOT EXISTS `TopGroups` ("
				+ "`idTopGroups` int(11) NOT NULL AUTO_INCREMENT,"
				+ "`Group1` varchar(256) NOT NULL,"
				+ "`Group2` varchar(256) NOT NULL,"
				+ "`Group3` varchar(256) NOT NULL,"
				+ "PRIMARY KEY (`idTopGroups`)"
				+ ")"; 
		executeUpdate(conn, q);
		q = "CREATE TABLE IF NOT EXISTS `Profil` ("
				+ "`idProfil` int(11) NOT NULL AUTO_INCREMENT,"
				+ "`manager` varchar(256) NOT NULL,"
				+ "`activity` int(11) NOT NULL,"
				+ "`size` int(11) NOT NULL,"
				+ "`topGroups` int(11) NOT NULL,"
				+ "`role` varchar(45) NOT NULL,"
				+ "PRIMARY KEY (`idProfil`),"
				+ "KEY `topGroup` (`topGroups`),"
				+ "CONSTRAINT `topGroup` FOREIGN KEY (`topGroups`) REFERENCES `TopGroups` (`idTopGroups`) ON DELETE NO ACTION ON UPDATE NO ACTION"
				+ ")";
		executeUpdate(conn, q);
		q = "CREATE TABLE IF NOT EXISTS `Groups` ("
			+ "`name` varchar(256) NOT NULL,"
			+ "`whenCreated` bigint(20) NOT NULL,"
			+ "`nbrMembers` int(11) NOT NULL,"
			+ "`whenChanged` bigint(20) NOT NULL,"
			+ "`manager` varchar(256) NOT NULL,"
			+ "`ageMembers` bigint(20) NOT NULL,"
			+ "`youngestMember` bigint(20) NOT NULL,"
			+ "`role` varchar(45) NOT NULL,"
			+ "`profil` int(11) NOT NULL,"
			+ "PRIMARY KEY (`name`),"
			+ "KEY `profil` (`profil`),"
			+ "CONSTRAINT `profil` FOREIGN KEY (`profil`) REFERENCES `Profil` (`idProfil`) ON DELETE NO ACTION ON UPDATE NO ACTION"
			+ ")" ;
		executeUpdate(conn, q);
		q = "DELETE FROM `Groups`";
		executeUpdate(conn, q);
		q = "DELETE FROM `Profil`";
		executeUpdate(conn, q);
		q = "ALTER TABLE `Profil` AUTO_INCREMENT = 1";
		executeUpdate(conn, q);
		q = "DELETE FROM `TopGroups`";
		executeUpdate(conn, q);
		q = "ALTER TABLE `TopGroups` AUTO_INCREMENT = 1";
		executeUpdate(conn, q);
	}
	
	public ResultSet findProfil(final int conn, String manager, String role){
		String q = "SELECT Group1,Group2,Group3 "
				+ "FROM `Profil`,`TopGroups` "
				+ "WHERE manager = '"+manager+"' AND role = '"+role+"' AND "
				+ "topGroups = idTopGroups "
				+ "ORDER BY size DESC";
		return getArrayFromSelect(conn, q);
	}
	
	public ResultSet findProfil(final int conn, String manager){
		String q = "SELECT Group1,Group2,Group3 "
				+ "FROM `Profil`,`TopGroups` "
				+ "WHERE manager = '"+manager+"' AND "
				+ "topGroups = idTopGroups "
				+ "ORDER BY size DESC";
		return getArrayFromSelect(conn, q);
	}
	
	public void createTableSuggestion(final int conn){
		String q = "CREATE  TABLE IF NOT EXISTS `Suggestion` ("
				+ "`idSuggestion` INT NOT NULL AUTO_INCREMENT ,"
				+ "`name` VARCHAR(256) NULL ,"
				+ "`groupe` VARCHAR(256) NULL ,"
				+ "`reason` VARCHAR(256) NULL ,"
				+ "PRIMARY KEY (`idSuggestion`) );";
		executeUpdate(conn, q);
	}
	
	public void insertSuggestion(final int conn, HashSet<String> hs, String userName, String reason){
		String q = "INSERT INTO `Suggestion` (`name`, `groupe`,`reason`) "
				+ "VALUES ";
		Iterator<String> i = hs.iterator();
		while(i.hasNext()){
			q = q + "('"+userName+"','"+i.next()+"','"+reason+"'),";
		}
		q=q+"('"+userName+"','No Suggestion', '');";
		executeUpdate(conn, q);
		q = "DELETE FROM Suggestion "
				+ "WHERE`name` in ( "
				+ "SELECT * FROM (SELECT `name` FROM Suggestion WHERE groupe <> 'No Suggestion' Group BY `name`)as t1) "
				+ "AND groupe = 'No Suggestion'	";	
		executeUpdate(conn, q);
	}
	
	public long countSuggestion(final int conn){
		String q  = "SELECT COUNT(DISTINCT name) FROM Suggestion";
		return getLongFromSelect(conn, q);
	}
	
	public ResultSet getSuggestion(final int conn){
		String q = "SELECT `Suggestion`.`name`, `Suggestion`.`reason`,`Suggestion`.`groupe` FROM `active_dir_system`.`Suggestion`";
		return getArrayFromSelect(conn, q);
	}

	public ResultSet getGroupDelete(final int conn) {
		String q = "SELECT name,reason FROM Group_delete WHERE `delete` = TRUE AND name NOT IN (SELECT name FROM Exclusion_delete)";
		return getArrayFromSelect(conn, q);
	}
	
	public ResultSet getExpiredPassword(final int conn){
		String q = "SELECT name FROM Password30 WHERE name <> 'Finish'";
		return getArrayFromSelect(conn, q);
	}

	public long getFrom(int conn, String tableName, String name) {
		String q = "SELECT age FROM `"+tableName+"` WHERE name = '"+name+"'";
		return getLongFromSelect(conn, q);
	}
	
	

}