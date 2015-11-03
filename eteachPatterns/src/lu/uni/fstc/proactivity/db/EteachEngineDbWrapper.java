package lu.uni.fstc.proactivity.db;

import lu.uni.fstc.proactivity.db.AbstractEngineMySQLDataAccessWrapper;
import lu.uni.fstc.proactivity.parameters.DbConnectionParameters;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * Concrete class that holds the attributes and methods needed to operate with the local database
 * This dbWrapper was used for the Eteach project (Denis Shirnin's 2nd Proof of Concept)
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014
 *
 */
public class EteachEngineDbWrapper extends AbstractEngineMySQLDataAccessWrapper {

	private static EteachEngineDbWrapper theInstance = null;

	/**
	 * PAMDbWrapper Constructor<br>
	 * <b>Private</b> to implement the "Singleton" Object
	 */
	private EteachEngineDbWrapper() throws Exception {
		super();
		conn = mso.openConnection(connParams);

		if (conn == NO_CONNECTION) {
			setConnected(false);
			Global.logger.severe("Error opening DB connection!");
		} else
			setConnected(true);
	}

	/**
	 * the public method to get the objetc's instance
	 * @return AbstractPAMDbWrapper
	 */
	public static synchronized EteachEngineDbWrapper getInstance(final String configFilePath) throws Exception {
		if (theInstance == null) {
			connParams = new DbConnectionParameters(configFilePath);
			theInstance = new EteachEngineDbWrapper();
		}
		return theInstance;
	}

	/**
	 * method that save H to the database
	 * @param H represents the High Relevance of the current state, at them moment of the creation of the record 
	 */
	public void saveHValue (final double H) {
		String q = "INSERT INTO CurrentHighRelevance (H, datetime) ";
		q += "VALUES (" + H + ", UNIX_TIMESTAMP())";
		mso.executeUpdate(conn, q);
	}

	/**
	 * method that save H to the database
	 * @param name the class name that represents the pattern to save
	 * @param type the type of pattern D/S
	 * @param id the event_id  that finalised the pattern
	 */
	public void saveNewPattern (final String name, final String type, long id) {
		String q = "INSERT INTO PatternsFound (name, type, last_event_ID, datetime) ";
		q += "VALUES ('" + name + "', '" + type + "', " + id + ", UNIX_TIMESTAMP())";
		mso.executeUpdate(conn, q);
	}
}