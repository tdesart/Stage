package lu.uni.fstc.proactivity.rules;

import lu.uni.fstc.proactivity.utils.StringUtils;

/**
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2015
 *
 */
public abstract class AbstractRuleMessage extends AbstractRule implements IRuleMessage {

	protected String subject = "[no subject]";
	protected String body = "[empty message body]";
	protected long user_id;

	/**
	 * Has to be provided by each individual rule that calls this include here a default value, just in case...
	 */
	protected int importance;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	protected AbstractRuleMessage() {
		setType(RuleType.MESSAGE);
	}

	/**
	 * Constructor that uses the default importance and delegates to the constructor with all parameters
	 * @param user_id
	 * @param subject
	 * @param body
	 */
	public AbstractRuleMessage(long user_id, String subject, String body) {
		this (user_id, subject, body, Importance.NORMAL);
	}

	/**
	 * When creating MSG001, fill the local attributes with the values provided in the constructor
	 * @param user_id 
	 * @param subject 
	 * @param body 
	 * @param importance 
	 */
	public AbstractRuleMessage(long user_id, String subject, String body, final int importance) {
		this();
		setUser_id(user_id);
		if (!StringUtils.isEmptyOrNullString(this.subject))
			setSubject(subject);
		if (!StringUtils.isEmptyOrNullString(this.body))
			setBody(body);
		setImportance(importance);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.IRuleMessage#getSubject()
	 */
	@Override
	public String getSubject() {
		return this.subject;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.IRuleMessage#setSubject(java.lang.String)
	 */
	@Override
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.IRuleMessage#getBody()
	 */
	@Override
	public String getBody() {
		return this.body;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.IRuleMessage#setBody(java.lang.String)
	 */
	@Override
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.IRuleMessage#getUser_id()
	 */
	@Override
	public long getUser_id() {
		return this.user_id;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.IRuleMessage#setUser_id(long)
	 */
	@Override
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.IRuleMessage#setImportance(int)
	 */
	@Override
	public void setImportance(int importance) {
		this.importance = importance;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.IRuleMessage#getImportance()
	 */
	@Override
	public int getImportance() {
		return this.importance;
	}
}