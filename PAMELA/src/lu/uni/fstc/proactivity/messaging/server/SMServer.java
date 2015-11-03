package lu.uni.fstc.proactivity.messaging.server;

import org.json.JSONException;
import org.json.JSONObject;

import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.messaging.SocketMessage;
import lu.uni.fstc.proactivity.utils.Global;


/**
 * Abstract representation of any object created on the server to build a JSON to be sent to the client<p>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2013
 *
 */
public abstract class SMServer extends SocketMessage {

	private final String JSON_UPPER_LEVEL = "messageServer";

	protected SMServer(final AbstractPAM2MoodleDbWrapper dbPam, final long user_id) {
		super(dbPam, user_id);
		setRole(Role.SERVER);
	}

	// building a JSON to be sent to the client appropriately, depending on the object type
	protected abstract JSONObject buildJSON();

	// wrap all up, in an JSON object called "messageServer"
	protected final JSONObject addLastLevelJSON(Object inJson) {
		JSONObject jsonfinal = new JSONObject();
		try {
			jsonfinal.put(JSON_UPPER_LEVEL, inJson);
		} catch (JSONException e) {
			Global.wssLogger.severe(e.getMessage());
			e.printStackTrace();
		}
		return jsonfinal;
	}
}