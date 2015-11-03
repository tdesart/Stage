package lu.uni.fstc.proactivity.db;

import java.sql.ResultSet;

import lu.uni.fstc.proactivity.db.AbstractHostDataAccessWrapper;
import lu.uni.fstc.proactivity.db.MySQLOperations;
import lu.uni.fstc.proactivity.parameters.DbConnectionParameters;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * Third level Abstract class that holds basic attributes and methods needed to operate with Moodle's database
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014
 * 
 */
public abstract class AbstractETeachDbWrapper extends AbstractHostDataAccessWrapper {

	 final MySQLOperations mso = MySQLOperations.getInstance ();

	 /**
	 * the connection parameters to the native moodle database <p> 
	 * It is <b>STATIC</b> because:<ul><li>It allows only one Connection Parameter to access Moodle.</li><br>
	 * <li> It is accessed in getInstance, a static method.</li></ul>
	 * <p><p>Overloads the AbstractDbWrapper.connParams attribute, <br>
	 * so that other DbWrappers types can have different Connection Parameters, otherwise they would reference the same one.
	 */
	public static DbConnectionParameters connParams = null;

	/**
	 * AbstractMoodleDbWrapper Constructor<br>
	 * <b>Private</b> (protected, so that it's accessed from 'children') to implement the "Singleton" Object 
	 */
	protected AbstractETeachDbWrapper() {
	}

	/* **********************************************************************************
	 * Events TABLE
	 * **********************************************************************************
	 */
	/**
	 * @param lastEvent
	 * @return true if there are new events 
	 */
	public abstract boolean isNewEvent(final long lastEvent);

	/**
	 * @return the LastEvent 
	 */
	public abstract long getLastEvent();

	/**
	 * @param lastEvent 
	 * @return the array of new events in descending order 
	 */
	public abstract ResultSet getNewEventsOrdered(final long lastEvent);

	/**
	 * @param lastEvent 
	 * @return the array of new events in descending order 
	 */
	public abstract ResultSet getNewEventsOrderedDesc(final long lastEvent);

	/**
	 * @param lastEvent
	 * @return true if there are new events 
	 */
	public abstract boolean isNewEventEOL(final long lastEvent);

	/**
	 * @param lastEvent 
	 * @return the LastEvent 
	 */
	public abstract long getNextEventEOL(final long lastEvent);

	/**
	 * @param lastEvent 
	 * @return the LastEvent 
	 */
	public abstract long getPreviousEOLEvent(final long lastEvent);

	/**
	 * @param lastPattern 
	 * @return the LastEvent 
	 */
	public abstract long getPreviousEOLEventFromPattern(final long lastPattern);

	/**
	 * @param eventEOL 
	 * @return the tag corresponding to the provided EOL event 
	 */
	public abstract String getTagFromEOL(final long eventEOL);
	
	/* **********************************************************************************
	 * Patterns TABLE
	 * **********************************************************************************
	 */
	/**
	 * @param lastPattern
	 * @return true if there are new Patterns 
	 */
	public abstract boolean isNewPatternS2orS3(final long lastPattern);
	
	/**
	 * @param lastPattern
	 * @return the nextPattern 
	 */
	public abstract long getNextPattern(final long lastPattern);

	@Override
	public AbstractETeachDbWrapper getThis() {
		Global.logger.finer("this:" + this);
		return this; 
	}

	/* **********************************************************************************
	 * Set TABLE
	 * **********************************************************************************
	 */

	/**
	 * @param id1
	 * @param tag1
	 * @param id2
	 * @param tag2
	 */
	public abstract void createSet(final long id1, final String tag1, final long id2, final String tag2);

	/**
	 * @param id 
	 * @param tag 
	 */
	public abstract void closeSet(final long id, final String tag);

	/**
	 * @param thisEOL
	 * @param tag1 
	 * @param tag2 
	 */
	public abstract void createAssociatedTag(long thisEOL, final String tag1, final String tag2);

	/**
	 * @param howMany the number of (distinct) tags to provide from the query
	 * @return the Result Set containing the list of tags
	 */
	public abstract ResultSet getLastDistinctAssociatedTag(final long howMany);

	/**
	 * @param tag to update 'shown' field
	 */
	public abstract void updateShownOnTag(final String tag);
}