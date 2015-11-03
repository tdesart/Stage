package lu.uni.fstc.proactivity.messaging.server;

import java.sql.ResultSet;

import org.json.JSONException;
import org.json.JSONObject;

import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * Representation of an object of type QUERY RESULT "cops_course_id" created on the server<p>
 * The server sends ALL groups for this user<br>
 * {"messageServer": {<br>
 *   "msg_type": "cops_course_id",<br>
 *   "course_id": 1552<br>
 * }}<br>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2013
 *
 */
public class SMServerResultGetCOPSCourseId extends SMServerResult {

	protected final String RESULT_TYPE = "cops_course_id";
	protected final String KEY_COURSE = "course_id";

	private AbstractMoodleDbWrapper db;

	/**
	 * @param db 
	 * @param dbPam
	 * @param id
	 */
	public SMServerResultGetCOPSCourseId(final AbstractMoodleDbWrapper db, AbstractPAM2MoodleDbWrapper dbPam, long id) {
		super(dbPam, id);
		setDb(db);
	}

	/** 
	 * <b>Not implemented on this class.</b><p>
	 * Reason: we need a long, not a full ResultSet, as the return value from the db query<br>
	 * @see lu.uni.fstc.proactivity.messaging.server.SMServerResult#executeQuery()
	 */
	@Override
	protected ResultSet executeQuery() {
		return null;
	}

	@Override
	public JSONObject buildJSON() {
		JSONObject jsonfinal = new JSONObject();
		try {
			JSONObject json = new JSONObject();
			json.put(KEY_MSG_TYPE, getType());

			// get the COPs course Id
			long copsId = getDb().getCopsCourseId();
			json.put(getKey(), copsId);

			jsonfinal = addLastLevelJSON(json);
			Global.wssLogger.finer(jsonfinal.toString(2));
		} catch (JSONException e) {
			Global.wssLogger.severe(e.getMessage());
			e.printStackTrace();
		}
		return jsonfinal;
	}

	@Override
	protected String getType() {
		return RESULT_TYPE;
	}

	@Override
	protected String getKey() {
		return KEY_COURSE;
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