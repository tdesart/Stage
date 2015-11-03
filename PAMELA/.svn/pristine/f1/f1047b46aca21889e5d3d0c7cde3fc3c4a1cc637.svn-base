package lu.uni.fstc.proactivity.messaging;

import org.json.JSONObject;

import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;

/**
 * Abstract representation of any object created as a Socket Message <br>
 * either on the client or the server to be treated according to its specificity...<p>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2013
 *
 */
public abstract class SocketMessage {

	/**
	 * the key that stores the information on the JSON message type
	 */
	public static final String KEY_MSG_TYPE = "msg_type";

	/**
	 * messages role, depending on where they where created
	 */
	public enum Role {
		/**
		 * messages role created on the client side
		 */
		CLIENT,
		/**
		 * messages role created on the server side
		 */
		SERVER
	}

	private long user_id;
	private Role role;
	private JSONObject json;
	private AbstractPAM2MoodleDbWrapper dbPam;

	/**
	 * @param dbPam 
	 * @param user_id
	 */
	public SocketMessage(final AbstractPAM2MoodleDbWrapper dbPam, final long user_id) {
		super();
		this.setUserId(user_id);
		this.setDbPam(dbPam);
	}

	/**
	 * @param dbPam 
	 * @param user_id
	 * @param json
	 */
	public SocketMessage(final AbstractPAM2MoodleDbWrapper dbPam, final long user_id, final JSONObject json) {
		this(dbPam, user_id);
		this.setJson(json);
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @param id the user_id to set
	 */
	private final void setUserId(long id) {
		this.user_id = id;
	}

	/**
	 * @return the user_id
	 */
	public long getUserId() {
		return this.user_id;
	}

	/**
	 * @return the role
	 */
	public Role getRole() {
		return this.role;
	}

	/**
	 * @return the json
	 */
	public JSONObject getJson() {
		return this.json;
	}

	/**
	 * @param json the json to set
	 */
	final private void setJson(JSONObject json) {
		this.json = json;
	}

	/**
	 * @return the dbPam
	 */
	public final AbstractPAM2MoodleDbWrapper getDbPam() {
		return this.dbPam;
	}

	/**
	 * @param dbPam the dbPam to set
	 */
	private final void setDbPam(AbstractPAM2MoodleDbWrapper dbPam) {
		this.dbPam = dbPam;
	}

	@Override
	public final String toString() {
		return this.toString(0);
	}

	/**
	 * @param indentFactor
	 * @return the String representation of the wrapped JSON Object
	 */
	public final String toString(final int indentFactor) {
        try {
        	return getJson().toString(indentFactor);
        } catch (final Exception e) {
            return "";
        }
	}
}