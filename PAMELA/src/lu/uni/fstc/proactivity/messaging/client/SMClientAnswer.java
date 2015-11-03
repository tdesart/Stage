package lu.uni.fstc.proactivity.messaging.client;

import org.json.JSONObject;

import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;

/**
 * Representation of an object of type ANSWER created on the client<p>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2013
 *
 */
public class SMClientAnswer extends SMClient {

	protected final String KEY_ANSWER = "answer";
	protected final String ANSWER_OK = "OK";
	protected final String ANSWER_CANCEL = "CANCEL";
	protected final String KEY_QUESTION_ID = "question_id";

	/**
	 * @param dbPam
	 * @param id
	 * @param json
	 */
	public SMClientAnswer(AbstractPAM2MoodleDbWrapper dbPam, long id, JSONObject json) {
		super(dbPam, id, json);
	}

	/**
	 * @see lu.uni.fstc.proactivity.messaging.client.SMClient#execute()
	 */
	@Override
	public boolean execute() {
		return false;
	}
}