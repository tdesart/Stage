package lu.uni.fstc.proactivity.rules;

/**
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2015
 *
 */
public interface IRuleMessage {

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	String toString();

	/**
	 * @return the subject
	 */
	String getSubject();

	/**
	 * @param subject the subject to set
	 */
	void setSubject(String subject);

	/**
	 * @return the body
	 */
	String getBody();

	/**
	 * @param body the body to set
	 */
	void setBody(String body);

	/**
	 * @return the user_id
	 */
	long getUser_id();

	/**
	 * @param user_id the user_id to set
	 */
	void setUser_id(long user_id);

	/**
	 * @param importance the importance to set
	 */
	void setImportance(int importance);

	/**
	 * @return the importance
	 */
	int getImportance();
}