package lu.uni.fstc.proactivity.db;

import java.sql.ResultSet;

import lu.uni.fstc.proactivity.parameters.DbConnectionParameters;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * A "Singleton" Object to wrap the MySql connection to Moodle
 * 
 * @author Sandro  Reis
 * @version 4.0 - Sandro Reis © 2014
 *
 */
public class ETeachDbWrapper extends AbstractETeachDbWrapper {

	private static ETeachDbWrapper theInstance = null;
//	private int conn = NO_CONNECTION;

	/**
	 * MoodleDbWrapper Constructor<br>
	 * <b>Private</b> to implement the "Singleton" Object 
	 */
	private ETeachDbWrapper() throws Exception {
		super();
		conn = mso.openConnection(connParams);

		if (conn == NO_CONNECTION) {
			setConnected(false);
			Global.logger.warning("Error opening DB connection.");
		}
		else 
			setConnected(true);
	}

	/**
	 * the public method to get the objetc's instance
	 * @return AbstractMoodleDbWrapper
	 */
	public static synchronized ETeachDbWrapper getInstance (final String configFilePath) throws Exception {
		if (theInstance == null) {
			connParams = new DbConnectionParameters(configFilePath);
			theInstance = new ETeachDbWrapper();
		}
		return theInstance;
	}

	/* **********************************************************************************
	 * Events TABLE
	 * **********************************************************************************
	 */
	@Override
	public boolean isNewEvent(final long lastEvent) {
		return mso.getBooleanFromSelect(conn, "eventStats", "id > " + lastEvent, "count(1) > 0");
	}

	@Override
	public long getLastEvent() {
		return mso.getLongFromSelect(conn, "Select max(id) from eventStats");
	}

	@Override
	public ResultSet getNewEventsOrdered(final long lastEvent) {
		// only consider records after the last SB2 (search button), because SB2 stops all patterns.
		String q = " select * from eventStats where ";
		q += " id > (select max(id) from eventStats where type = 'page' and element = 'search')"; 
		q += " and id > " + lastEvent + " order by id"; 
		return mso.getArrayFromSelect(conn, q);
	}

	@Override
	public ResultSet getNewEventsOrderedDesc(final long lastEvent) {
		// only consider records after the last SB2 (search button), because SB2 stops all patterns.
		String q = " select * from eventStats where ";
		q += " id > (select max(id) from eventStats where type = 'page' and element = 'search')"; 
		q += " and id > " + lastEvent + " order by id desc"; 
		return mso.getArrayFromSelect(conn, q);
	}

	@Override
	public boolean isNewEventEOL(final long lastEvent) {
		String q = "select count(1) > 0 from eventStats where ";
		q += " type = 'list' and element = 'result_list' and value not in ('scroll_arrow','scroll_slider','hovering stop','hovering start')";
		q += " and id > " + lastEvent; 
		return mso.getBooleanFromSelect(conn, q);
	}

	@Override
	public long getNextEventEOL(final long lastEvent) {
		String q = "select min(id) id from eventStats where "; 
		q += "  type = 'list' and element = 'result_list' and value not in ('scroll_arrow','scroll_slider','hovering stop','hovering start') ";
		q += "  and id > " + lastEvent;
		return mso.getLongFromSelect(conn, q);
	}

	@Override
	public long getPreviousEOLEvent(final long lastEvent) {
		String q = "select max(id) ID from eventStats where ";
		q += " type = 'list' and element = 'result_list' and value not in ('scroll_arrow','scroll_slider','hovering stop','hovering start')";
		q += " and id < " + lastEvent; 
		return mso.getLongFromSelect(conn, q);
	}

	@Override
	public long getPreviousEOLEventFromPattern(final long lastPattern) {
		String q = "select max(id) ID from eventStats where ";
		q += " type = 'list' and element = 'result_list' and value not in ('scroll_arrow','scroll_slider','hovering stop','hovering start')";
		q += " and id < (select last_event_ID from PatternsFound where id = "+ lastPattern + ")" ; 
		return mso.getLongFromSelect(conn, q);
	}

	@Override
	public String getTagFromEOL(final long eventEOL){
		return mso.getStringFromSelect(conn, "eventStats", "id = " + eventEOL, "SUBSTRING(value, POSITION('TAG' IN value)+4) TAG");
	}

	/* **********************************************************************************
	 * Patterns TABLE
	 * **********************************************************************************
	 */
	@Override
	public boolean isNewPatternS2orS3 (final long lastPattern) {
		return mso.getBooleanFromSelect(conn, "PatternsFound", "name in ('PatternS02', 'PatternS03') and  id > " + lastPattern, "count(1) > 0");
	}

	@Override
	public long getNextPattern(long lastPattern) {
//		return mso.getLongFromSelect(conn, "Select max(id) from PatternsFound");
		return mso.getLongFromSelect(conn, "PatternsFound", "name in ('PatternS02', 'PatternS03') and  id > " + lastPattern, "min(id)");
	}

	/* **********************************************************************************
	 * Set TABLE
	 * **********************************************************************************
	 */

	@Override
	public void createSet(final long id1, final String tag1, final long id2, final String tag2) {
		String q = "INSERT INTO TagsSets ";
		q+= "(eol_id_1, eol_tag_1, eol_id_2, eol_tag_2, time_creation) ";
		q+= "VALUES ( " + id1 + ", '" + tag1 + "', " + id2 + ", '" + tag2 + "', UNIX_TIMESTAMP() );";
		mso.executeUpdate(conn, q);
	}

	@Override
	public void closeSet(long id, String tag) {
		long updId = mso.getLongFromSelect(conn, "select min(id) from TagsSets where eol_id_3 is null");
		
		String q = "UPDATE TagsSets ";
		q+= "set eol_id_3 = " + id + ", eol_tag_3 = '" + tag + "', time_closing = UNIX_TIMESTAMP() ";
		q+= "where id = " + updId;
		mso.executeUpdate(conn, q);
	}

	@Override
	public void createAssociatedTag(final long thisEOL, final String tag1, final String tag2) {
		final String SUBTHEME = " -> ";
		String theme1 = lu.uni.fstc.proactivity.utils.StringUtils.stringBefore(tag1, SUBTHEME);
		String theme2 = lu.uni.fstc.proactivity.utils.StringUtils.stringBefore(tag2, SUBTHEME);

		String q = "SELECT id FROM TagsSets " +
				   "WHERE eol_id_1 = " + thisEOL + 
				   "   OR eol_id_2 = " + thisEOL +
				   "   OR eol_id_3 = " + thisEOL;
		long ignoreThisID = mso.getLongFromSelect(conn, q);
		String subTheme1 = "SUBSTRING(eol_tag_1, 1, locate ('" + SUBTHEME + "', eol_tag_1))";
		String subTheme2 = "SUBSTRING(eol_tag_2, 1, locate ('" + SUBTHEME + "', eol_tag_2))";
		String subTheme3 = "SUBSTRING(eol_tag_3, 1, locate ('" + SUBTHEME + "', eol_tag_3))";

		long tagsFound = 0; 
		q = "INSERT into TagsAssociated ";
		q+= " (eol_id_found, eol_tag_found, time) ";
		q+= " select eol_id_1, eol_tag_1, UNIX_TIMESTAMP() from TagsSets ";
		q+= " where (eol_tag_2 in ('" + theme1 + "', '" + theme2 + "') or eol_tag_3 in ('" + theme1 + "', '" + theme2 + "')) ";
		q+= " and (eol_id_1 not in ('" + theme1 + "', '" + theme2 + "') ";
		q+= " and " + subTheme1 + " not in ('" + theme1 + "', '" + theme2 + "')) ";
//		q+= " and '" + subTheme1 + "' not in ('" + theme1 + "', '" + theme2 + "')";
		q+= " and id <> " + ignoreThisID;
		tagsFound = mso.executeUpdate(conn, q);
		Global.logger.finer("Tag1 QUERY = " + q + "\nLines inserted = " + tagsFound);

		q = "INSERT into TagsAssociated ";
		q+= " (eol_id_found, eol_tag_found, time) ";
		q+= " select eol_id_2, eol_tag_2, UNIX_TIMESTAMP() from TagsSets ";
		q+= " where (eol_tag_1 in ('" + theme1 + "', '" + theme2 + "') or eol_tag_3 in ('" + theme1 + "', '" + theme2 + "')) ";
		q+= " and (eol_id_2 not in ('" + theme1 + "', '" + theme2 + "') ";
		q+= " and " + subTheme2 + " not in ('" + theme1 + "', '" + theme2 + "')) ";
//		q+= " and " + subTheme2 + " not in ('" + theme1 + "', '" + theme2 + "')";
		q+= " and id <> " + ignoreThisID;
		tagsFound = mso.executeUpdate(conn, q);
		Global.logger.finer("Tag2 QUERY = " + q + "\nLines inserted = " + tagsFound);

		q = "INSERT into TagsAssociated ";
		q+= " (eol_id_found, eol_tag_found, time) ";
		q+= " select eol_id_3, eol_tag_3, UNIX_TIMESTAMP() from TagsSets ";
		q+= " where (eol_tag_1 in ('" + theme1 + "', '" + theme2 + "') or eol_tag_2 in ('" + theme1 + "', '" + theme2 + "')) ";
		q+= " and (eol_id_3 not in ('" + theme1 + "', '" + theme2 + "') ";
		q+= " and " + subTheme3 + " not in ('" + theme1 + "', '" + theme2 + "')) ";
//		q+= " and " + subTheme3 + " not in ('" + theme1 + "', '" + theme2 + "')";
		q+= " and id <> " + ignoreThisID;
		tagsFound = mso.executeUpdate(conn, q);
		Global.logger.finer("Tag3 QUERY = " + q + "\nLines inserted = " + tagsFound);

		q = "INSERT into TagsAssociated ";
		q+= " (eol_id_found, eol_tag_found, time) ";
		q+= " select eol_id_1, eol_tag_1, UNIX_TIMESTAMP() from TagsSets ";
		q+= " where (" + subTheme2 + " in ('" + theme1 + "', '" + theme2 + "') or " + subTheme3 + " in ('" + theme1 + "', '" + theme2 + "')) ";
		q+= " and (eol_id_1 not in ('" + theme1 + "', '" + theme2 + "') ";
		q+= " and " + subTheme1 + " not in ('" + theme1 + "', '" + theme2 + "')) ";
		q+= " and id <> " + ignoreThisID;
		tagsFound = mso.executeUpdate(conn, q);
		Global.logger.finer("Tag1 (substring) QUERY = " + q + "\nLines inserted = " + tagsFound);

		q = "INSERT into TagsAssociated ";
		q+= " (eol_id_found, eol_tag_found, time) ";
		q+= " select eol_id_2, eol_tag_2, UNIX_TIMESTAMP() from TagsSets ";
		q+= " where (" + subTheme1 + " in ('" + theme1 + "', '" + theme2 + "') or " + subTheme3 + " in ('" + theme1 + "', '" + theme2 + "')) ";
		q+= " and (eol_id_2 not in ('" + theme1 + "', '" + theme2 + "') ";
		q+= " and " + subTheme2 + " not in ('" + theme1 + "', '" + theme2 + "')) ";
		q+= " and id <> " + ignoreThisID;
		tagsFound = mso.executeUpdate(conn, q);
		Global.logger.finer("Tag2 (substring) QUERY = " + q + "\nLines inserted = " + tagsFound);

		q = "INSERT into TagsAssociated ";
		q+= " (eol_id_found, eol_tag_found, time) ";
		q+= " select eol_id_3, eol_tag_3, UNIX_TIMESTAMP() from TagsSets ";
		q+= " where (" + subTheme1 + " in ('" + theme1 + "', '" + theme2 + "') or " + subTheme2 + " in ('" + theme1 + "', '" + theme2 + "')) ";
		q+= " and (eol_id_3 not in ('" + theme1 + "', '" + theme2 + "') ";
		q+= " and " + subTheme3 + " not in ('" + theme1 + "', '" + theme2 + "')) ";
		q+= " and id <> " + ignoreThisID;
		tagsFound = mso.executeUpdate(conn, q);
		Global.logger.finer("Tag3 (substring) QUERY = " + q + "\nLines inserted = " + tagsFound);
	}

	@Override
	public ResultSet getLastDistinctAssociatedTag(long howMany) {
		String q = " SELECT DISTINCT (eol_tag_found) tag FROM TagsAssociated " +
				"WHERE shown = 0 ORDER BY time DESC LIMIT " + howMany; 
		return mso.getArrayFromSelect(conn, q);
	}

	@Override
	public void updateShownOnTag(String tag) {
		String q = "UPDATE TagsAssociated SET shown = 1 " +
				"WHERE eol_tag_found = '" + tag + "'"; 
		mso.executeUpdate(conn, q);
	}
}