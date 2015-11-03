package lu.uni.fstc.proactivity.rules;


import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.messaging.server.UserMessage;
import lu.uni.fstc.proactivity.ruleRunningSystem.ProactiveEngine;
import lu.uni.fstc.proactivity.utils.Global;


/**
 * <b>Test a mail sent from Moodle by calling:</b><br>
 * https://moodle-test.uni.lux/message/PAS_test_mail.php?from=2&to=2307&subject=Mail Title&txt=Test mail n.3<br>Created from code on a php script
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011-2013
 *
 */
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "MF_CLASS_MASKS_FIELD", justification = "I need to define a field with the same name as a visible instance field in a superclass, because I need to redefine it to a more specific type!")
public class RuleMessageMoodle extends AbstractRuleMessage {

	/**
	 * this 're-cast' is necessary to allow to access specific 'Moodle' methods 
	 */
	protected AbstractMoodleDbWrapper dataNativeSystem;
	protected ProactiveEngine engine;

	private boolean isOnline = false;
	private boolean userExists = false;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	public RuleMessageMoodle() {
		super();
	}

	/**
	 * Constructor that uses the default importance and delegates to the constructor with all parameters
	 * @param user_id
	 * @param subject
	 * @param body
	 */
	public RuleMessageMoodle(long user_id, String subject, String body) {
		this (user_id, subject, body, Importance.NORMAL);
	}

	/**
	 * When creating this, fill the local attributes with the values provided in the constructor
	 * @param user_id 
	 * @param subject 
	 * @param body 
	 * @param importance 
	 */
	public RuleMessageMoodle(long user_id, String subject, String body, final int importance) {
		super (user_id, subject, body, importance);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		Global.logger.fine("this.user_id " + this.user_id);
		Global.logger.fine("this.dataNativeSystem " + this.dataNativeSystem);
		
		this.userExists = dataNativeSystem.userExists(this.user_id);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		// activate only if user_id is valid (<>null and 0) 
		// and if user really exists (also includes <>0)
		if (this.userExists)
			Global.logger.finer("User exists " + this.user_id);
		else
			Global.logger.warning("User does NOT exist " + this.user_id);

		return (this.user_id != 0 && this.userExists);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#conditions()
	 */
	@Override
	protected boolean conditions() {
		return true;
	}

	/**
	 * STEP 1 - Save the message to the database
	 * STEP 2 - Verify if the user is Online 
	 * STEP 2a - If the user is ONLINE, send the message through the socket
	 * STEP 2b - If the user is OFFLINE, send him/her an email
	 * 
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#actions()
	 */
	@Override
	protected void actions() {

		UserMessage msg = new UserMessage(this.user_id, this.subject, this.body, this.importance);
		msg.saveMsgToDb();
		Global.logger.fine("Message saved to db, id=" + this.user_id);
		
		this.isOnline = engine.socketServer.isUserOnline(this.user_id);
		if (this.isOnline) {
			msg.sendMsg(engine);
			Global.logger.fine("Socket Message Sent to id=" + this.user_id);
		}
		else {
			msg.sendEmail();
			Global.logger.fine("EMAIL Sent (socket) to id=" + this.user_id);
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		return true;
	}
	/**
	 * @see lu.uni.fstc.proactivity.rules.IRuleMessage#toString()
	 */
	@Override
	public String toString() {
		return "RuleMessageMoodle - To UserID: " + this.user_id + ", Subject: " + this.subject;
	}

}
