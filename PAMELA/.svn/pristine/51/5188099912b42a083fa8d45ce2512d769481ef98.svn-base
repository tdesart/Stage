package lu.uni.fstc.proactivity.messaging.client;

import org.json.JSONException;
import org.json.JSONObject;

import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.messaging.server.SMServerResult;
import lu.uni.fstc.proactivity.messaging.server.SMServerResultGetAllMsgs;
import lu.uni.fstc.proactivity.messaging.server.SMServerResultGetCOPSCourseId;
import lu.uni.fstc.proactivity.messaging.server.SMServerResultGetGroups;
import lu.uni.fstc.proactivity.messaging.server.SMServerResultGetLastMsgs;
import lu.uni.fstc.proactivity.utils.Global;


/**
 * Representation of an object of type COMMAND created on the client<p>
 * Existing commands: <br>
 * {"msg_type":"command", "command":"get_latest_messages"}<br>
 * {"msg_type":"command", "command":"get_all_messages"}<br>
 * {"msg_type":"command", "command":"delete_message", "msg_id":12}<br>
 * {"msg_type":"command", "command":"read_message", "msg_id":12}<br>
 * 
 * {"msg_type":"command", "command":"leave_group", "group_id":563}<br>
 * {"msg_type":"command", "command":"get_groups"}<br>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2013
 *
 */
public class SMClientCommand extends SMClient {

	private JSONObject result;
	protected final String KEY_COMMAND = "command";
	protected final String COMMAND_GET_GROUPS = "get_groups";
	protected final String COMMAND_LEAVE_GROUP = "leave_group";
	protected final String COMMAND_COURSE_ID = "cops_course_id";
	protected final String COMMAND_GET_LAST_MESSAGES = "get_latest_messages";
	protected final String COMMAND_GET_ALL_MESSAGES = "get_all_messages";
	protected final String COMMAND_DELETE_MESSAGE = "delete_message";
	protected final String COMMAND_READ_MESSAGE = "read_message";
	protected final String KEY_GROUP = "group_id";
	protected final String KEY_MESSAGE = "msg_id";

	private AbstractMoodleDbWrapper dbMoodle;
	
	/**
	 * @param dbMoodle 
	 * @param dbPam 
	 * @param user_Id 
	 * @param json 
	 */
	public SMClientCommand(final AbstractMoodleDbWrapper dbMoodle, final AbstractPAM2MoodleDbWrapper dbPam, final long user_Id, final JSONObject json) {
		super(dbPam, user_Id, json);
		setDbMoodle(dbMoodle);
	    Global.wssLogger.finer("SMClientCommand: " + json);
	}

	@Override
	public boolean execute() {
		boolean hasResult = false;
		String command;
		try {
			command = getJson().getString(KEY_COMMAND);
		} catch (JSONException e) {
		    Global.wssLogger.warning("'" + KEY_COMMAND + "' key not found in the JSON object: " + getJson());
			return false;
		}

		SMServerResult msgSrv = null;
		if (command.equalsIgnoreCase(COMMAND_GET_GROUPS)) {
			msgSrv = new SMServerResultGetGroups(getDbMoodle(), getDbPam(), getUserId());
			hasResult = true;
		} else if (command.equalsIgnoreCase(COMMAND_COURSE_ID)) {
			msgSrv = new SMServerResultGetCOPSCourseId(getDbMoodle(), getDbPam(), getUserId());
			hasResult = true;
		} else if (command.equalsIgnoreCase(COMMAND_GET_LAST_MESSAGES)) {
			msgSrv = new SMServerResultGetLastMsgs(getDbPam(), getUserId());
			hasResult = true;
		} else if (command.equalsIgnoreCase(COMMAND_GET_ALL_MESSAGES)) {
			msgSrv = new SMServerResultGetAllMsgs(getDbPam(), getUserId());
			hasResult = true;
		} else if (command.equalsIgnoreCase(COMMAND_LEAVE_GROUP)) {
			long g_id;
			try {
				// get the group id that we want to un-enrol the user from
				g_id = getJson().getLong(KEY_GROUP);
			} catch (JSONException e) {
			    Global.wssLogger.warning("'" + KEY_GROUP + "' key not found in the JSON object: " + getJson());
				return false;
			}
			// FIXME leave group didn't work on 16th April 2013. Test to see if this is fixed
			// Execute action
			getDbMoodle().leaveGroup(getUserId(), g_id);
			// Log action in PAM
			getDbPam().markAsLeaveGroup(getUserId(), g_id);
			// no result expected
			hasResult = false;
		} else if (command.equalsIgnoreCase(COMMAND_DELETE_MESSAGE)) {
			long msg_id;
			try {
				// get the message id that we want to delete
				msg_id = getJson().getLong(KEY_MESSAGE);
			} catch (JSONException e) {
			    Global.wssLogger.warning("'" + KEY_MESSAGE + "' key not found in the JSON object: " + getJson());
				return false;
			}
			// Execute action
			getDbPam().deleteMessage(getUserId(), msg_id);
			// no result expected
			hasResult = false;
		} else if (command.equalsIgnoreCase(COMMAND_READ_MESSAGE)) {
			long msg_id;
			try {
				// get the message id that we want to read
				msg_id = getJson().getLong(KEY_MESSAGE);
			} catch (JSONException e) {
			    Global.wssLogger.warning("'" + KEY_MESSAGE + "' key not found in the JSON object: " + getJson());
				return false;
			}
			getDbPam().markAsReadMessage(getUserId(), msg_id);
			hasResult = false;
		} else {
			Global.wssLogger.warning("Unexpected answer received (JSON object): " + command);
		}

		// in case a result was expected 
		// if hasResult is true, msgSrv is not null!
		if (hasResult) {
			// Execute action and build JSON result 
			JSONObject json = msgSrv.buildJSON();
			// Then set it to the internal attribute 'result'
			setResult(json);
		}

		return hasResult;	
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(JSONObject result) {
		this.result = result;
	}

	/**
	 * @return the result
	 */
	public JSONObject getResult() {
		return this.result;
	}

	/**
	 * @return the dbMoodle
	 */
	private final AbstractMoodleDbWrapper getDbMoodle() {
		return this.dbMoodle;
	}

	/**
	 * @param db the dbMoodle to set
	 */
	private final void setDbMoodle(AbstractMoodleDbWrapper db) {
		this.dbMoodle = db;
	}
}