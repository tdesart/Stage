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
 * Representation of an object of type QUESTION created on the server<p>
 * The server sends one question for this user<br>
* {"messageServer": {<br>
 *   "message": "Do you want to be part of country-based social groups ?",<br>
 *   "msg_type": "question_country",<br>
 *   "answers": [<br>
 *     {"answer": "No, thanks!"},<br>
 *     {"answer": "Ok"}<br>
 *   ],<br>
 *   "question_id": 19,<br>
 *   "countries": [<br>
 *     {"country_name": "Afghanistan"},<br>
 *     {"country_name": "Albania"},<br>
 *     {"country_name": "Zimbabwe"},<br>
 *     {"country_name": "Åland Islands"}<br>
 *   ]<br>
 * }}<br>
 * <br>
 * {"messageServer": {<br>
 *   "message": "Do you want to be part of city-based social groups ?",<br>
 *   "cities": [<br>
 *     {"city_name": "Bettembourg"},<br>
 *     {"city_name": "Dudelange"},<br>
 *     {"city_name": "Esch-sur-Alzette"},<br>
 *     {"city_name": "Luxembourg-Ville"},<br>
 *     {"city_name": "Metz"},<br>
 *     {"city_name": "Nancy"},<br>
 *     {"city_name": "Schifflange"},<br>
 *     {"city_name": "Thionville"}<br>
 *   ],<br>
 *   "msg_type": "question_city",<br>
 *   "answers": [<br>
 *     {"answer": "No, thanks!"},<br>
 *     {"answer": "Ok"}<br>
 *   ],<br>
 *   "question_id": 20<br>
 * }}<br>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2013
 *
 */
public class SMServerQuestion extends SMServer {

	private final String QUESTION_TYPE = "typ";
	private final String QUESTION_TYPE_COUNTRY = "question_country";
	private final String QUESTION_TYPE_CITY = "question_city";
	private final String QUESTION_ID = "question_id";
	private final String KEY_QUESTION_ID = "question_id";
	private final String KEY_COUNTRIES = "countries";
	private final String KEY_CITIES = "cities";
	private final String KEY_MESSAGE = "message";
	private final String KEY_ANSWERS = "answers";
	
	/**
	 * @param dbPam 
	 * @param user_id 
	 */
	public SMServerQuestion(final AbstractPAM2MoodleDbWrapper dbPam, final long user_id) {
		super(dbPam, user_id);
	}

	@Override
	/**
	 * Because it has no parameters, it has nothing to put inside<br>
	 * returns a NULL JSON Object wrapped up in a higher level Message
	 */
	public JSONObject buildJSON() {
		return addLastLevelJSON(JSONObject.NULL);
	}

	/**
	 * @param usersQuestions
	 * @return the JSON Object already built
	 * @throws SQLException 
	 */
	public final JSONObject buildJSON(ResultSet usersQuestions) throws SQLException {
		JSONObject jsonfinal = new JSONObject();
		try {
			long question_ID = usersQuestions.getLong(QUESTION_ID);
			String question_type = usersQuestions.getString(QUESTION_TYPE);

			JSONObject json = new JSONObject();
			json.put(KEY_MSG_TYPE, question_type);
			json.put(KEY_QUESTION_ID, question_ID);

			// NOT generic anymore
			// Exceptions for these specific Question Types
			if (question_type.equals(QUESTION_TYPE_COUNTRY)) {
				ResultSet countries = getDbPam().getAllCountries();
				JSONArray ja = ResultSetConverter.toJSONArray(countries);
				json.put(KEY_COUNTRIES, ja);
			}
			else if (question_type.equals(QUESTION_TYPE_CITY)) {
				ResultSet cities = getDbPam().getAllCities();
				JSONArray ja = ResultSetConverter.toJSONArray(cities);
				json.put(KEY_CITIES, ja);
			}

			json.put(KEY_MESSAGE, usersQuestions.getString("content"));

			// get the possible answers to each question / user
			ResultSet answersQuestions = getDbPam().getPossibleAnswersFromQuestion(question_ID);
			JSONArray ja = ResultSetConverter.toJSONArray(answersQuestions);
			json.put(KEY_ANSWERS, ja);

			// wrap all up, in an JSON object called "messageServer"
			jsonfinal = addLastLevelJSON(json);
			Global.wssLogger.finer(jsonfinal.toString(2));

			// register that this question was read (Question_User) and sent to the users Socket connection
			long userquestion_ID = usersQuestions.getLong("id");
			getDbPam().updateUserQuestionTime(userquestion_ID);
		} catch (JSONException e) {
			Global.wssLogger.severe(e.getMessage());
			e.printStackTrace();
		}
		return jsonfinal;
	}
}