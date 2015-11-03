package lu.uni.fstc.proactivity.messaging.server;

import org.json.JSONObject;


import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.db.Persistence;
import lu.uni.fstc.proactivity.ruleRunningSystem.GenericProactiveEngine;
import lu.uni.fstc.proactivity.ruleRunningSystem.ProactiveEngine;
import lu.uni.fstc.proactivity.rules.Importance;
import lu.uni.fstc.proactivity.utils.CallSecureURLThread;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.Utils;

/**
 * <b>Manages messages to send to users</b>
 * Its existence is mandatory, because its the object used to save the message by Hibernate
 *  
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011-2013 
 * 
 */
public class UserMessage {

	/**
	 * the internal message ID
	 */
	private long id;

	/**
	 * the user ID (TO) 
	 */
	private long user_id;

	/**
	 * the message subject 
	 */
	private String subject;

	/**
	 * the message body to be send 
	 */
	private String body;

	/**
	 * the time stamp that represents when the message was sent (in seconds) 
	 * NOTE: the initial value shouldn't be used if this class is to be read from the db using HIBERNATE.
	 *	 use the non-default constructors to set the value instead
	 */
	private long sent = System.currentTimeMillis()/1000;

	/**
	 * boolean that indicates if the message has already been read
	 * NOTE: the initial value shouldn't be used if this class is to be read from the db using HIBERNATE.
	 *	 use the non-default constructors to set the value instead
	 */
	private int read = 0;

	/**
	 * the message importance  
	 * NOTE: the initial value shouldn't be used if this class is to be read from the db using HIBERNATE.
	 *	 use the non-default constructors to set the value instead
	 */
	private int importance = Importance.NORMAL;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private UserMessage() {
	}

	/**
	 * @param user_id
	 * @param subject
	 * @param body
	 */
	public UserMessage(long user_id, String subject, String body) {
		this();

		setUser_id(user_id);
		setSubject(subject);
		setBody(body);
	}

	/**
	 * @param user_id
	 * @param subject
	 * @param body
	 * @param importance 
	 */
	public UserMessage(long user_id, String subject, String body, int importance) {
		this(user_id, subject, body);
		setImportance(importance);
	}

	/**
	 * PAM's method of sending an email:<br>
	 * Call Moodle's script to send an email to the user
	 */
	public void sendEmail() {
		try {
			java.net.URL url = Utils.buildSendEmailURL(AbstractMoodleDbWrapper.connParams, this.getUser_id(), this.getSubject(), this.getBody());
			Global.logger.finer("Email Subject: " + this.getSubject() + " To: " + this.getUser_id());
			Thread t = new CallSecureURLThread(url);
			t.start();
		} catch (Exception e) {
			Global.logger.warning("ERROR sending to user " +  this.getUser_id() + " the message with subject: '" + this.getSubject() + "'!");
			e.printStackTrace();
		}
	}

	/**
	 * PAM's method of sending a message to its interface:<br>
	 * Save it on the database (using Hibernate), so that the interface can read it at its own choice
	 */
	public void saveMsgToDb() {
		Persistence.beginTransaction();
		Global.logger.finer("Interface MSG Subject: " + this.getSubject() + " To: " + this.getUser_id());
		if (!Persistence.save(this, true)) {
			Global.logger.warning("ERROR saving the message '" + this.getSubject() + "' to : " + this.getUser_id() + " on the database!");
			Persistence.rollbackTransaction(false);
		}
		else {
			Persistence.commitTransaction(false);
		}
	}

	/**
	 * PAM's method of sending a message through the socket server:<br>
	 * Creating a SMServerNewMessage object,<br>
	 * building the JSON,<br>
	 * and sending it directly through the socket<br>
	 * @param engine the engine where the dbWrapper and the socket server are accessible
	 */
	public void sendMsg(final GenericProactiveEngine engine) {
		SMServerNewMessage msgSrv = new SMServerNewMessage ((AbstractPAM2MoodleDbWrapper) engine.dataEngine, getUser_id(), this);
		JSONObject json = msgSrv.buildJSON();
		((ProactiveEngine)engine).socketServer.sendToId(getUser_id(), json.toString());
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the user_id
	 */
	public long getUser_id() {
		return this.user_id;
	}

	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return this.subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return this.body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the sent
	 */
	public long getSent() {
		return this.sent;
	}

	/**
	 * @param sent the sent to set
	 */
	public void setSent(long sent) {
		this.sent = sent;
	}

	/**
	 * @return read flag
	 */
	public int isRead() {
		return this.read;
	}

	/**
	 * @param read the read flag to set
	 */
	public void setRead(int read) {
		this.read = read;
	}

	/**
	 * @return the importance
	 */
	public int getImportance() {
		return this.importance;
	}

	/**
	 * @param importance the importance to set
	 */ 
	public void setImportance(int importance) {
		this.importance = importance;
	}
}
