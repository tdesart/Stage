package lu.uni.fstc.proactivity.messaging.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;


/**
 * Representation of an object of type NEW_GROUP on the server<p>
 * The server sends one group for this user<br>
 * {"messageServer": {<br>
 *   "msg_type": "new_group",<br>
 *   "groups": [{<br>
 *     "group_name": "groupName",
 *     "group_id": 1,
 *     "activity": 0
 *   }]<br>
 * }}<br>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2013
 *
 */
public class SMServerNewGroup extends SMServer {

	private final String RESULT_TYPE = "new_group";
	private final String KEY_GRP_ID = "group_id";
	private final String KEY_NAME = "group_name";
	private final String KEY_ACT = "activity";

	private final String KEY_GROUPS = "groups";
	private String grpName;
	private long grpId;
	private long grpActivity;

	/**
	 * @param dbPam 
	 * @param user_id 
	 * @param grpName 
	 * @param grpId 
	 * @param grpActivity 
	 */
	public SMServerNewGroup(final AbstractPAM2MoodleDbWrapper dbPam, final long user_id, final String grpName, final long grpId, final long grpActivity) {
		super(dbPam, user_id);
		setGrpName(grpName);
		setGrpId(grpId);
		setGrpActivity(grpActivity);
	}

	private String getType() {
		return RESULT_TYPE;
	}

	/**
	 * @return building the JSON Object with the specific keys and result from the specific query 
	 */
	@Override
	public final JSONObject buildJSON() {
		JSONObject jsonfinal = new JSONObject();
		try {
			// Insert all the message fields in a JSON object
			JSONObject jsonInside = new JSONObject();
			jsonInside.put(KEY_GRP_ID, getGrpId());
			jsonInside.put(KEY_NAME, getGrpName());
			jsonInside.put(KEY_ACT, getGrpActivity());
/*			jsonInside.put(KEY_ISREAD, 0);
			jsonInside.put(KEY_TIME_CREATED, getMsg().getSent());
			jsonInside.put(KEY_IMPORTANCE, getMsg().getImportance());
*/
			// Insert this JSON object in a JSONArray
			JSONArray jsonInsideArray = new JSONArray();
			jsonInsideArray.put(jsonInside);

			// wrap the message content in an JSON object called "messages"
			JSONObject json = new JSONObject();
			json.put(KEY_MSG_TYPE, getType());
			json.put(KEY_GROUPS, jsonInsideArray);
			
			// wrap all up, in an JSON object called "messageServer"
			jsonfinal = addLastLevelJSON(json);
			Global.wssLogger.finer("Message:\n" + jsonfinal.toString(2));
		} catch (JSONException e) {
			Global.wssLogger.severe(e.getMessage());
			e.printStackTrace();
		}

		return jsonfinal;
	}

	/**
	 * @return the grpName
	 */
	private final String getGrpName() {
		return this.grpName;
	}

	/**
	 * @param grpName the grpName to set
	 */
	private final void setGrpName(String grpName) {
		this.grpName = grpName;
	}

	/**
	 * @return the grpId
	 */
	private final long getGrpId() {
		return this.grpId;
	}

	/**
	 * @param grpId the grpId to set
	 */
	private final void setGrpId(long grpId) {
		this.grpId = grpId;
	}

	/**
	 * @return the grpActivity
	 */
	private final long getGrpActivity() {
		return this.grpActivity;
	}

	/**
	 * @param grpActivity the grpActivity to set
	 */
	private final void setGrpActivity(long grpActivity) {
		this.grpActivity = grpActivity;
	}
}