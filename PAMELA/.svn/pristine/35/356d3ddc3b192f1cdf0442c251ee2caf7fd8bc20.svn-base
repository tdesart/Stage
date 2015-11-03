package lu.uni.fstc.proactivity.rules.moodleAssignments;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;


import lu.uni.fstc.proactivity.messaging.server.SMServerQuestion;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.StringUtils;


/**
 * A Meta Scenario dedicated to check the database for new messages.<br>
 * It's only activated if there are new messages for the online users.<br>
 * Send the new messages to the users via a JSON message created by a <code>SMServerQuestion</code> object.
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2013
 *
 */
public class MTA_PushQuestion extends AbstractRuleMoodle {

	private String idString;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	//private MTA_PushQuestion() {
	public MTA_PushQuestion() {
		setType(RuleType.META);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		idString = this.engine.socketServer.getActiveIdsStr();

		if (StringUtils.isEmptyOrNullString(idString)) 
			Global.logger.finer("NO Users Online!");
		else
			Global.logger.info("Online Users : " + idString);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		return (!StringUtils.isEmptyOrNullString(idString) && dataEngine.doUsersHaveQuestions(idString));
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#conditions()
	 */
	@Override
	protected boolean conditions() {
		return true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#actions()
	 */
	@Override
	protected void actions() {
		try {
			// get the unanswered questions for the online users
			Global.logger.finer("Online Users : " + idString);
			ResultSet usersQuestions = dataEngine.getUsersWithQuestions(idString);
			usersQuestions.beforeFirst();
			while (!usersQuestions.isClosed() && usersQuestions.next()) {
				long user_ID = usersQuestions.getLong("user_id");

				// For each user-Question, 
				// create the server Question object and build the JSON the right way, 
				// using the ResultSet data
				SMServerQuestion msgServer = new SMServerQuestion(dataEngine, user_ID);
				JSONObject json = new JSONObject();
				json = msgServer.buildJSON(usersQuestions);
				Global.wssLogger.info("Sending JSON to the client :\n" + json.toString(2));

				// send it to the appropriate user via its socket (one rule per user?)
				this.engine.socketServer.sendToId(user_ID, json.toString());
			}
		} catch (SQLException e) {
			Global.logger.severe(e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			Global.logger.severe(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		createRule(this);

		return true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return "MTA_PushQuestion - Meta-scenario: Push the un-answered questions to the online users.";
	}
}