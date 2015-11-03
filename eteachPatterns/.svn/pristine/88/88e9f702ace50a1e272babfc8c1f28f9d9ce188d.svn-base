package lu.uni.fstc.proactivity.db;

import java.sql.ResultSet;
import java.util.HashMap;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * A "Singleton" Object to wrap the MySql connection to Moodle<br>
 * this wrapper uses the <b>Proxy Design Pattern</b> to add Cache to the initial MoodleDbWrapper object<p>
 * 
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014
 * 
 */
public class ETeachCacheDbWrapper extends AbstractETeachDbWrapper {

	private static ETeachCacheDbWrapper theInstance = null;
	private final ETeachDbWrapper theMySQLInstance;

	/**
	 * The permanent cache
	 */
	private final HashMap<String, Object> stableCache;

	/**
	 * The temporary cache
	 */
	private final HashMap<String, Object> tempCache;

	/**
	 * MoodleCacheDbWrapper Constructor<br>
	 * <b>Private</b> to implement the "Singleton" Object 
	 * @param configFilePath the path to the config file
	 * @throws Exception 
	 */
	private ETeachCacheDbWrapper (final String configFilePath) throws Exception {
		super();
		this.theMySQLInstance = ETeachDbWrapper.getInstance(configFilePath);
		this.setConnected (this.theMySQLInstance.isConnected()); // propagate boolean connected

//		this.connParams = this.theMySQLInstance.connParams;
		stableCache = new HashMap <String, Object>();
		tempCache   = new HashMap <String, Object>();
	}

	/**
	 * Creates ONCE a "Singleton" Object
	 * @param configFilePath the path to the config file
	 * @throws Exception 
	 */
	public static synchronized ETeachCacheDbWrapper getInstance (final String configFilePath) throws Exception {
		if (theInstance == null) {
			theInstance = new ETeachCacheDbWrapper(configFilePath);
		}
		return theInstance;
	}

	// Clears the temporary cache
	@Override
	public void terminateOperation() {
		tempCache.clear();
	}

	/* **********************************************************************************
	 * Events TABLE
	 * **********************************************************************************
	 */
	@Override
	public boolean isNewEvent(final long lastEvent) {
		return theMySQLInstance.isNewEvent(lastEvent);
	}

	@Override
	public long getLastEvent() {
		return theMySQLInstance.getLastEvent();
	}

	@Override
	public ResultSet getNewEventsOrdered(final long lastEvent) {
		return theMySQLInstance.getNewEventsOrdered(lastEvent);
	}
	
	@Override
	public ResultSet getNewEventsOrderedDesc(final long lastEvent) {
		return theMySQLInstance.getNewEventsOrderedDesc(lastEvent);
	}
	
	@Override
	public boolean isNewEventEOL(final long lastEvent) {
		return theMySQLInstance.isNewEventEOL(lastEvent);
	}

	@Override
	public long getNextEventEOL(final long lastEvent) {
		return theMySQLInstance.getNextEventEOL(lastEvent);
	}

	@Override
	public long getPreviousEOLEvent(final long lastEvent) {
		final String key = "getPreviousEOLEvent " + lastEvent;
		final Long res = (Long) stableCache.get(key);
		long res2;

		if (res != null) {
			res2 = res.longValue();
			Global.logger.finest("[in cache] key: " + key + ", value:" + res2);
		} else {
			res2 = theMySQLInstance.getPreviousEOLEvent(lastEvent);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			stableCache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	@Override
	public long getPreviousEOLEventFromPattern(final long lastPattern) {
		final String key = "getPreviousEOLEventFromPattern " + lastPattern;
		final Long res = (Long) stableCache.get(key);
		long res2;

		if (res != null) {
			res2 = res.longValue();
			Global.logger.finest("[in cache] key: " + key + ", value:" + res2);
		} else {
			res2 = theMySQLInstance.getPreviousEOLEventFromPattern(lastPattern);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			stableCache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	@Override
	public String getTagFromEOL(final long eventEOL){
		final String key = "getTagFromEOL " + eventEOL;
		String res = (String) stableCache.get(key);
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getTagFromEOL(eventEOL);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res);
			stableCache.put(key, res);
		}
		return res;
	}

	/* **********************************************************************************
	 * Patterns TABLE
	 * **********************************************************************************
	 */
	@Override
	public boolean isNewPatternS2orS3 (final long lastPattern) {
		final String key = "isNewPatternS2 " + lastPattern;
		final Boolean res = (Boolean) tempCache.get(key);
		boolean res2;

		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.booleanValue();
		} else {
			res2 = theMySQLInstance.isNewPatternS2orS3(lastPattern);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			tempCache.put(key, Boolean.valueOf(res2));
		}
		return res2;
	}

	@Override
	public long getNextPattern(final long lastPattern) {
		final String key = "getNextPattern" + lastPattern;
		final Long res = (Long) tempCache.get(key);
		long res2;
		
		if (res != null) {
			res2 = res.longValue();
			Global.logger.finest("[in cache] key: " + key + ", value:" + res2);
		} else {
			res2 = theMySQLInstance.getNextPattern(lastPattern);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			tempCache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	/* **********************************************************************************
	 * Set TABLE
	 * **********************************************************************************
	 */

	@Override
	public void createSet(final long id1, final String tag1, final long id2, final String tag2) {
		theMySQLInstance.createSet(id1, tag1, id2, tag2);
	}

	@Override
	public void closeSet(long id, String tag) {
		theMySQLInstance.closeSet(id, tag);
	}

	@Override
	public void createAssociatedTag(long thisEOL, String tag1, String tag2) {
		theMySQLInstance.createAssociatedTag(thisEOL, tag1, tag2);
	}

	@Override
	public ResultSet getLastDistinctAssociatedTag(long howMany) {
		return theMySQLInstance.getLastDistinctAssociatedTag(howMany);
	}

	@Override
	public void updateShownOnTag(String tag) {
		theMySQLInstance.updateShownOnTag(tag);
	}
}