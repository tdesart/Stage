package lu.uni.fstc.proactivity.messaging.server;

import java.sql.ResultSet;

import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;

/**
 * Representation of an object of type QUERY RESULT "get_groups" created on the server<p>
 * The server sends ALL groups for this user<br>
 * {"messageServer": {<br>
 *   "msg_type": "group_box",<br>
 *   "groups": []<br>
 * }}<br>
 * -OR-<br>
 * {"messageServer": {<br>
 *   "msg_type": "group_box",<br>
 *   "groups": [{<br>
 *    "group_name": "Master of Science in Information and Computer Sciences (académique)",<br>
 *     "group_id": 572,<br>
 *     "activity": 1<br>
 *   }]<br>
 * }}<br>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2013
 *
 */
public class SMServerResultGetGroups extends SMServerResult {

	protected final String RESULT_TYPE = "group_box";
	protected final String KEY_GROUPS = "groups";

	private AbstractMoodleDbWrapper db;

	/**
	 * @param db 
	 * @param dbPam
	 * @param id
	 */
	public SMServerResultGetGroups(final AbstractMoodleDbWrapper db, AbstractPAM2MoodleDbWrapper dbPam, long id) {
		super(dbPam, id);
		setDb(db);
	}

	@Override
	protected ResultSet executeQuery() {
		return getDb().getGroupsFromUser(getUserId());
	}

	@Override
	protected String getType() {
		return RESULT_TYPE;
	}

	@Override
	protected String getKey() {
		return KEY_GROUPS;
	}

	/**
	 * @return the db
	 */
	private final AbstractMoodleDbWrapper getDb() {
		return this.db;
	}

	/**
	 * @param db the db to set
	 */
	private final void setDb(AbstractMoodleDbWrapper db) {
		this.db = db;
	}
}