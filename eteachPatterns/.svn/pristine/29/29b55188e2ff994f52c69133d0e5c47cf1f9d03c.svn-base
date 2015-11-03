package lu.uni.fstc.proactivity.eteach.events;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashMap;

import lu.uni.fstc.proactivity.db.AbstractETeachDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * The class that takes care of Events, Tags & Sets 
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014 
 *
 */
public class Operations {
	static private AbstractETeachDbWrapper db;
	static private final String SITE = "SITE=";
	static private final String TAG = "TAG=";
	static private final String SUBTHEME = " -> ";
	
	/**
	 * @param rs
	 * @return the string corresponding to the TAG
	 */
	public static String getThemeFromTag(final ResultSet rs) {
		String theme = lu.uni.fstc.proactivity.utils.StringUtils.EMPTY_STRING;

		try {
			String fieldValue;
			fieldValue = rs.getString("value");

			String tag = lu.uni.fstc.proactivity.utils.StringUtils.stringAfter(fieldValue, TAG);
			theme = lu.uni.fstc.proactivity.utils.StringUtils.stringBefore(tag, SUBTHEME);

		} catch (SQLException e) {
			Global.logger.severe("Unable to retrieve the 'value' field.");
			e.printStackTrace();
		}
		return theme;
	}

	/**
	 * @param rs
	 * @return the string corresponding to the TAG
	 */
	public static String getSiteFromTag(final ResultSet rs) {
		String site = lu.uni.fstc.proactivity.utils.StringUtils.EMPTY_STRING;

		try {
			String fieldValue;
			fieldValue = rs.getString("value");

			String siteAndtag = lu.uni.fstc.proactivity.utils.StringUtils.stringAfter(fieldValue, SITE);
			site = lu.uni.fstc.proactivity.utils.StringUtils.stringBefore(siteAndtag, " "+TAG);

		} catch (SQLException e) {
			Global.logger.severe("Unable to retrieve the 'value' field.");
			e.printStackTrace();
		}
		return site;
	}

	/**
	 * @param rs
	 * @return the long value corresponding to the record set field 'systemtime'. If exception is found then returns the current time.
	 */
	public static long getTimeFromRecord(final ResultSet rs) {
		long fieldValue;
		try {
			fieldValue = rs.getLong("systemtime")*1000; // because it comes in seconds, not milliseconds
		} catch (SQLException e) {
			GregorianCalendar cal = new GregorianCalendar();
			fieldValue = cal.getTimeInMillis();
			Global.logger.severe("Unable to retrieve the 'systemtime' field from the result set. Returning current tie instead!");
			e.printStackTrace();
		}
		return fieldValue;
	}

	/**
	 * @param twinTags
	 * @param theme the tag-theme to check
	 * @return true if tag exists on the HashMap
	 */
	static public boolean checkTagExiting(HashMap<String, Boolean> twinTags, final String theme) {
		Boolean res;
		boolean exists = false;

//		String theme = getThemeFromTag(rs);
		if (lu.uni.fstc.proactivity.utils.StringUtils.isEmptyOrNullString(theme))
			return false;

		res = twinTags.get(theme);
		// code might be simplified, but this way is more readable, and easier to debug
		if (res != null) {
			exists = true;
		} else {
			//twinTags.put(theme, Boolean.TRUE);
			exists = false;
		}

		twinTags.put(theme, Boolean.valueOf(exists));
		return exists;
	}

	/**
	 * @param twinTags
	 * @param theme the tag-theme to check
	 * @return the number of times the tag has been found on the HashMap
	 */
	static public long checkTagCount(HashMap<String, Long> twinTags, final String theme) {
		Long res;
		long value;

//		String theme = getThemeFromTag(rs);
		if (lu.uni.fstc.proactivity.utils.StringUtils.isEmptyOrNullString(theme))
			return 0;

		res = twinTags.get(theme);
		if (res != null)
			value = res.longValue() + 1;
		else
			value = 1;

		twinTags.put(theme, Long.valueOf(value));
		return value;
	}

	/**
	 * @param siteList
	 * @param site the site to check
	 * @return the number of times the tag has been found on the HashMap
	 */
	static public long checkSiteCount(HashMap<String, Long> siteList, final String site) {
		Long res;
		long value;

//		String site = getSiteFromTag(rs);
		if (lu.uni.fstc.proactivity.utils.StringUtils.isEmptyOrNullString(site))
			return 0;

		res = siteList.get(site);
		if (res != null)
			value = res.longValue() + 1;
		else
			value = 1;

		siteList.put(site, Long.valueOf(value));
		return value;
	}

	/**
	 * Transform an EOL event into a tag - <br>
	 * Given an Event ID (an EOL event), go to the database and extract the corresponding TAG
	 */
	static private String extractFullTag(final long EOLEventID) {
		return getDb().getTagFromEOL(EOLEventID);
	}

	/**
	 * Given an Event ID (an EOL event), go to the database and previous  EOL event
	 */
	static private long getPreviousEOLEvent(final long EOLEventID) {
		return getDb().getPreviousEOLEvent(EOLEventID);
	}

	/**
	 * Given an Event ID (an EOL event), go to the database and extract the associated TAGs
	 * @param thisEOL  
	 */
	static public void collectAndSaveAssotiatedTags(final long thisEOL) {
		String thisTag     = Operations.extractFullTag(thisEOL);
		long previousEOL   = getPreviousEOLEvent(thisEOL);
		String previousTag = Operations.extractFullTag(previousEOL);

		Global.logger.info("Collecting and Saving Assotiated Tags to '" + thisTag + 	"'");
		getDb().createAssociatedTag(thisEOL, thisTag, previousTag);
	}

	/**
	 * on db 
	 * @param thisEOL 
	 */
	static public void createSet (final long thisEOL) {
		String thisTag     = Operations.extractFullTag(thisEOL);
		long previousEOL   = getPreviousEOLEvent(thisEOL);
		String previousTag = Operations.extractFullTag(previousEOL);

		getDb().createSet(previousEOL, previousTag, thisEOL, thisTag);
	}

	/**
	 * The method that receives the final tag that closes the Set
	 * @param thisEOL the EOL that is closing the set
	 */
	static public void closeSet (final long thisEOL) {
		String thisTag = Operations.extractFullTag(thisEOL);

		getDb().closeSet(thisEOL, thisTag);
	}

	/**
	 * @param inputDb the db to set
	 */
	static public void setDb(AbstractETeachDbWrapper inputDb) {
		db = inputDb;
	}

	/**
	 * @return the db
	 */
	static public AbstractETeachDbWrapper getDb() {
		return db;
	}
}