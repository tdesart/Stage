package lu.uni.fstc.proactivity.messaging.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * Representation of an object of type NEW_MESSAGE on the server<p>
 * The server sends one message for this user<br>
 * {"messageServer": {<br>
 *   "msg_type": "new_message",<br>
 *   "messages": [{<br>
 *     "body": "Test mail n.2<br>Created from code on a TEST JAVA Class<br>Hello Sergio, This is Sandro from inside a JAVA program, calling the Messenger RULE!",<br>
 *     "importance": 0,<br>
 *     "subject": "Test Mail Title",<br>
 *     "time_created": 1362656837,<br>
 *     "msg_id": 31,<br>
 *     "isRead": 0<br>
 *   }]<br>
 * }}<br>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2013
 *
 */
public class SMServerNewMessage extends SMServer {

	private final String RESULT_TYPE = "new_message";
	private final String KEY_MSG_ID = "msg_id";
	private final String KEY_SUBJECT = "subject";
	private final String KEY_BODY = "body";
	private final String KEY_ISREAD = "isRead";
	private final String KEY_TIME_CREATED = "time_created";
	private final String KEY_IMPORTANCE = "importance";
	private final String KEY_MSGS = "messages";

	private UserMessage msg = null;

	/**
	 * @param dbPam 
	 * @param user_id 
	 * @param msg 
	 */
	public SMServerNewMessage(final AbstractPAM2MoodleDbWrapper dbPam, final long user_id, final UserMessage msg) {
		super(dbPam, user_id);
		setMsg(msg);
	}

	private String getType() {
		return RESULT_TYPE;
	}

	/**
	 * @param msg the msg to set
	 */
	private void setMsg(UserMessage msg) {
		this.msg = msg;
	}

	/**
	 * @return the msg
	 */
	private UserMessage getMsg() {
		return this.msg;
	}

	/**
	 * @return building the JSON Object with the specific keys and result from the specific query 
	 */
	@Override
	public final JSONObject buildJSON() {
		if (msg == null)
			Global.wssLogger.severe("Cannot build JSON unless message is set beforehand (msg is NULL)!");

		JSONObject jsonfinal = new JSONObject();
		try {
			// Insert all the message fields in a JSON object
			JSONObject jsonInside = new JSONObject();
			jsonInside.put(KEY_MSG_ID, getMsg().getId());
			jsonInside.put(KEY_SUBJECT, getMsg().getSubject());
			jsonInside.put(KEY_BODY, getMsg().getBody());
			jsonInside.put(KEY_ISREAD, 0);
			jsonInside.put(KEY_TIME_CREATED, getMsg().getSent());
			jsonInside.put(KEY_IMPORTANCE, getMsg().getImportance());

			// Insert this JSON object in a JSONArray
			JSONArray jsonInsideArray = new JSONArray();
			jsonInsideArray.put(jsonInside);

			// wrap the message content in an JSON object called "messages"
			JSONObject json = new JSONObject();
			json.put(KEY_MSG_TYPE, getType());
			json.put(KEY_MSGS, jsonInsideArray);
			
			// wrap all up, in an JSON object called "messageServer"
			jsonfinal = addLastLevelJSON(json);
			Global.wssLogger.finer("Message:\n" + jsonfinal.toString(2));
		} catch (JSONException e) {
			Global.wssLogger.severe(e.getMessage());
			e.printStackTrace();
		}

		return jsonfinal;
	}
}