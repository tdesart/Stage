package lu.uni.fstc.proactivity.api;

import java.sql.Connection;
import java.sql.DriverManager;

import lu.uni.fstc.proactivity.db.MoodleDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * <b>"Singleton" Class that connects the RRS (Rules Running System) to the Moodle DB</b><br>
 * All methods for collecting data should be written here and called in a 
 * rule template, to keep a high level writing in rules template
 * -
 * @deprecated <br>
 * This class has been replaced by lu.uni.fstc.db.AbstractMoodleDbWrapper,  <br>
 * an abstract class to implement Singleton connections to the database <br>
 *  
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 * @version 2.0 - Sergio Marques Dias <br> 
 * @version 1.0 - Yann Milin
 * @see  MoodleDbWrapper#getInstance(String configFilePath)
 */
@Deprecated
@SuppressWarnings("deprecation")
public class API {

	/**
	 * API <<Singleton>> Object
	 */
	private static volatile API theApiInstance = null;
	private Connection dbConn = null;
	
	/**
	 * The ID of the user
	 */
//	protected int user_id;
	
	/**
	 * DB connection info
	 */
	// 
	private static final String DB_NAME = "jdbc:mysql://moodle-test.uni.lux:3306";
	// lu.uni.fstc.db instance moodle-test.uni.lux:3306
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "moodle";
	// v. 2.0
//	private static final String DB_NAME = "jdbc:mysql://localhost/efstc";
//	private static final String DB_USER = "root";
//	private static final String DB_PASSWORD = "";

	/**
	 * Creates ONCE a <<Singleton>> Object
	 * @return the "Singleton" instance 
	 */
	public static API getAPI() {
		if (theApiInstance == null)
			theApiInstance = new API();
		Global.logger.finer("API Object created!");
		return theApiInstance;
	}

	private API() {
		dbConn = OpenDBConnection();
	}

	/**
	 * closes the instance's connection to the database
	 */
	public void closeDBConnection() {
		closeDBConnection(dbConn);
	}

	/**
	 * Closes connection to a MySql DB
	 * @param c the connection to be closed
	 */
	private void closeDBConnection(Connection c) {
		try {
			if (c != null) {
				c.close();
				Global.logger.finer("Database connection interrupted");
			} 
		}
		catch (Exception e) {
		}
	}

	/**
	 * Gets the course of the user from the Moodle DB
	 * @param course_id
	 * @return null
	 */
	public String get_course(int course_id) {
		return null;
	}

	/**
	 * Gets the first name of the user from the Moodle DB
	 * @param user_id
	 * @return null
	 */
	public String get_firstName(int user_id) {
		return null;
	}
	
/**
	 * Gets the name of the user from the Moodle DB
	 * @param user_id
	 * @return null
	 */
	public String get_name(int user_id) {
		return null;
	}

	/**
	 * @param user_id
	 * @return null
	 */
	public SocketClientThread getClientThread(int user_id) {
		return null;	
	}

	//---------------- DB QUERIES -----------------------
	/**
	 * Gets the status of the user whether he's connected or not
	 * @param user_id
	 * @param course_id
	 * @return false
	 */
	public boolean isConnectedToCourse(int user_id, int course_id) {
		return false;
	}

	/**
	 * Opens the connection to a MySql DB
	 */
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "DMI_CONSTANT_DB_PASSWORD", justification = "The whole class is deprecated, since it was only used as a prototype, it was never used in production, so no security issues here!")
	private Connection OpenDBConnection() {
		try {
			Global.logger.finest("Database connection initialised");
			//Class.forName("com.mysql.jdbc.Driver").newInstance ();
//			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); 
			Global.logger.finest("Database connection driver created");
			Connection c = DriverManager.getConnection(DB_NAME, DB_USER, DB_PASSWORD);
			Global.logger.finer("Database connection established");
			return c;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
