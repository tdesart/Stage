package lu.uni.fstc.proactivity.messaging.client;

import org.json.JSONException;
import org.json.JSONObject;

import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * Representation of an object of type ANSWER to the question "City of origin" created on the client.<p>
 * Examples of possible answers and their expected results: <br>
 *    <code>{"msg_type":"question_city", "question_id" = 20, "answer":"CANCEL"}</code> <br>
 *     case: user declined to answer<p>
 *    <code>{"msg_type":"question_city", "question_id" = 20, "answer":"OK", "city":"Luxembourg-Ville"}</code> <br>
 *     case: answer was given, country is provided<p>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2013
 *
 */
public class SMClientAnswerCity extends SMClientAnswer {

	private static final String KEY_ANSWER_CITY = "city";

	/**
	 * @param dbPam
	 * @param id
	 * @param json
	 */
	public SMClientAnswerCity(final AbstractPAM2MoodleDbWrapper dbPam, final long id, final JSONObject json) {
		super(dbPam, id, json);
	}

	@Override
	public boolean execute() {
		boolean hasResult = false;
		String answer;
		long question_id;
		try {
			answer = getJson().getString(KEY_ANSWER);
		} catch (JSONException e) {
		    Global.wssLogger.warning("'" + KEY_ANSWER + "' key not found in the JSON object: " + getJson());
			return false;
		}
		try {
			question_id = getJson().getLong(KEY_QUESTION_ID);
		} catch (JSONException e) {
		    Global.wssLogger.warning("'" + KEY_QUESTION_ID + "' key not found in the JSON object: " + getJson());
			return false;
		}

		if (answer.equalsIgnoreCase(ANSWER_OK)) {
			String city;
			try {
				city = getJson().getString(KEY_ANSWER_CITY);
			} catch (JSONException e) {
			    Global.wssLogger.warning("'" + KEY_ANSWER_CITY + "' key not found in the JSON object: " + getJson());
				return false;
			}

			// Verify if the city is valid
			boolean existsCity = getDbPam().validateCity(city);
			if (!existsCity) {
				//if not, a error is logged
				Global.wssLogger.warning("The city does not exist! It comes from a pre-filled combo-box, so it must be right! Is this a man-in-the-middle attack ??");
			} else {
				// If OK, save the info to the db
				Global.wssLogger.finer("The city exists! Updating answer info on database.");
				getDbPam().updateUserAnswerCityOK(getUserId(), question_id, city);
				hasResult = true;
			}
		}
		else if (answer.equalsIgnoreCase(ANSWER_CANCEL)) {
			getDbPam().updateUserAnswerNO(getUserId(), question_id);
			hasResult = true;
		}
		else {
			Global.wssLogger.warning("Unexpected answer received (JSON object): " + answer);
		}

		return hasResult;
	}
}