package lu.uni.fstc.proactivity.db;

import lu.uni.fstc.proactivity.parameters.DbConnectionParameters;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * Third level Abstract class that holds the implementation of the basic attributes and methods needed to operate with the local database.<br>
 * This level implements the methods in MySQL queries. 
 * 
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis � 2015
 *
 */
 abstract public class AbstractEngineMySQLDataAccessWrapper extends AbstractEngineDataAccessWrapper {

	 /**
	 * The connection parameters to the local database <p> 
	 * It is <b>STATIC</b> because:<ul><li>It allows only one Connection Parameter to access the local db.</li><br>
	 * <li> It is accessed in getInstance, a static method.</li></ul>
	 * <p><p><b>Overrides</b> the AbstractDataAccessWrapper.connParams attribute, <br>
	 * so that it has access to its particular fields and methods, <br> 
	 * and other DbWrappers types can have different Connection Parameters, otherwise they would reference the same one.
	 */
	 public static DbConnectionParameters connParams = null;

	 final MySQLOperations mso = MySQLOperations.getInstance ();

	 /**
	 * AbstractLocalDbWrapper Constructor<br>
	 * <b>Private</b> (protected, so that it's accessed from 'children') to implement the "Singleton" Object 
	 */
	protected AbstractEngineMySQLDataAccessWrapper() {
	}

	/* **********************************************************************************
	 * Engine Signals Management (Implementation)
	 * **********************************************************************************
	 */
	/**
	 * @return boolean
	 */
	@Override
	public boolean isScheduleToStop() {
		// Replaced with a call to UserOnline table in PAS schema/database
		// select scheduledStop from `active_dir_engine`.`ScheduledStop`;
		Global.logger.finer("ENTER");
		return mso.getBooleanFromSelect(conn, "active_dir_engine.Signals", "id = 1", "scheduledStop=1");
	}

	/**
	 * @param stopSignal 
	 * @return the long that results from the query execution
	 */
	@Override
	public long setScheduleToStop (final boolean stopSignal) {
		String q = "UPDATE active_dir_engine.Signals SET scheduledStop = " + stopSignal + " WHERE id = 1";
		return mso.executeUpdate(conn, q);
	}

	/**
	 * @return a boolean with the value of the stop signal
	 */
	@Override
	public boolean isRunning() {
		// Replaced with a call to UserOnline table in PAS schema/database
		// select scheduledStop from `active_dir_engine`.`ScheduledStop`;
		return mso.getBooleanFromSelect(conn, "active_dir_engine.Signals", "id = 1", "running=1");
	}

	/**
	 * @param runSignal
	 * @return the long that results from the query execution
	 */
	@Override
	public long setRunning (final boolean runSignal) {
		String q = "UPDATE active_dir_engine.Signals SET running = " + runSignal + " WHERE id = 1 and running <> " + runSignal;
		return mso.executeUpdate(conn, q);
	}

	@Override
	public AbstractEngineMySQLDataAccessWrapper getThis() {
		Global.logger.finer("this:" + this);
		return this; 
	}
}