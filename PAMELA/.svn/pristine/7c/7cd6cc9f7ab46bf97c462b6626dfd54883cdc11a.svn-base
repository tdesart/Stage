package lu.uni.fstc.proactivity.messaging.server;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lu.uni.fstc.converter.ResultSetConverter;
import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;


/**
 * Representation of an object of type QUERY RESULT created on the server<p>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2013
 *
 */
public abstract class SMServerResult extends SMServer {

	/**
	 * @param dbPam 
	 * @param user_id 
	 */
	public SMServerResult(final AbstractPAM2MoodleDbWrapper dbPam, final long user_id) {
		super(dbPam, user_id);
	}

	/**
	 * Forcing the children to implement a method that calls the specific database query
	 */
	protected abstract ResultSet executeQuery();

	/**
	 * Forcing the children to implement a method that sets the JSON Result Type
	 */
	protected abstract String getType();

	/**
	 * Forcing the children to implement a method that sets the JSON key for the results
	 */
	protected abstract String getKey();

	/**
	 * Building the JSON Object with the specific keys and result from the specific query as deined in the implementation of <code>executeQuery()</code>
	 * This method should only be overwritten if the <code>SMServerResult</code><br>implementation requires a json with a structure different then the one provided here:<br>the ResultSet object resulting from the <code>executeQuery()</code> method.
	 * @return JSON Object representing the full ResultSet 
	 */
	@Override
	public JSONObject buildJSON() {
		JSONObject jsonfinal = new JSONObject();
		try {
			JSONObject json = new JSONObject();
			json.put(KEY_MSG_TYPE, getType());

			// get the possible answers to each question / user
			ResultSet rs = executeQuery();
			JSONArray ja = ResultSetConverter.toJSONArray(rs);
			json.put(getKey(), ja);

			jsonfinal = addLastLevelJSON(json);
			Global.wssLogger.finer(jsonfinal.toString(2));
		} catch (JSONException e) {
			Global.wssLogger.severe(e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			Global.wssLogger.severe(e.getMessage());
			e.printStackTrace();
		}
		return jsonfinal;
	}
}