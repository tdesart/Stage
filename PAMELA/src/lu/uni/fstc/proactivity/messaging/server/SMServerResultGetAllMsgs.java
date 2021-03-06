package lu.uni.fstc.proactivity.messaging.server;

import java.sql.ResultSet;

import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;

/**
 * Representation of an object of type QUERY RESULT "get_all_messages" created on the server<p>
 * The server sends ALL messages for this user<br>
 * {"messageServer": {<br>
 *   "msg_type": "message_page",<br>
 *   "messages": []<br>
 * }}<br>
 * -OR-<br>
 * {"messageServer": {<br>
 *   "msg_type": "message_page",<br>
 *   "messages": [{<br>
 *     "body": "You were inscribed in a new social group called Bachelor en Informatique (professionnel).",<br>
 *     "importance": -50,<br>
 *     "subject": "Social Group Enrollment",<br>
 *     "time_created": 1362752448,<br>
 *     "msg_id": 4,<br>
 *     "isRead": 0<br>
 *   }]<br>
 * }}<br>
 * -OR-<br>
 * {"messageServer": {<br>
 *   "msg_type": "message_page",<br>
 *   "messages": [<br>
 *     {<br>
 *       "body": "You were inscribed in a new social group called Bachelor en Informatique (professionnel).",<br>
 *       "importance": -50,<br>
 *       "subject": "Social Group Enrollment",<br>
 *       "time_created": 1362752448,<br>
 *       "msg_id": 5,<br>
 *       "isRead": 0<br>
 *     },<br>
 *     {<br>
 *       "body": "e",<br>
 *       "importance": -50,<br>
 *       "subject": "Social Group Enrollment",<br>
 *       "time_created": 1362752448,<br>
 *       "msg_id": 11,<br>
 *       "isRead": 0<br>
 *     }<br>
 *   ]<br>
 * }}<br>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis � 2013
 *
 */
public class SMServerResultGetAllMsgs extends SMServerResult {

	protected final String RESULT_TYPE = "message_page";
	protected final String KEY_GROUPS = "messages";

	/**
	 * @param dbPam
	 * @param id
	 */
	public SMServerResultGetAllMsgs(AbstractPAM2MoodleDbWrapper dbPam, long id) {
		super(dbPam, id);
	}

	@Override
	protected ResultSet executeQuery() {
		return getDbPam().getAllMessages(getUserId());
	}

	@Override
	protected String getType() {
		return RESULT_TYPE;
	}

	@Override
	protected String getKey() {
		return KEY_GROUPS;
	}
}