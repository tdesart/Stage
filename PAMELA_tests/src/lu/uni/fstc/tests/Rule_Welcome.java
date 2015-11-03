package lu.uni.fstc.tests;

import lu.uni.fstc.proactivity.utils.Global;

import lu.uni.fstc.proactivity.rules.AbstractRule;

/**
 * <b>Rule_Welcome rule</b>
 *  
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 * @version 2.0 - Sergio Marques Dias <br> 
 * @version 1.0 - Yann Milin
 * 
 */
public class Rule_Welcome extends AbstractRule {

	private int course_id;
	private int user_id;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	public Rule_Welcome() {
	}

	/**
	 * When creating a Rule_Welcome Rule, the user name and course id are needed
	 * @param user_id
	 * @param course
	 */
	public Rule_Welcome(int user_id, int course) {
		this.setUser_id(user_id);
		this.course_id = course;
		Global.logger.info("Rule_Welcome rule created (object) for course number " + this.course_id + ".");
	}

	@Override
	protected void actions() {
		Global.logger.info(toString());
	}

	@Override
	protected boolean activationGuards() {
		return true;
	}

	@Override
	protected boolean conditions() {
		return true;
	}

	@Override
	protected void dataAcquisition() {
	}

	/**
	 * @return the course_id
	 */
	public int getCourse_id() {
		return course_id;
	}
	
	/**
	 * @return the user_id
	 */
	public int getUser_id() {
		return user_id;
	}

	@Override
	protected boolean rulesGeneration() {
		return false;
	}

	/**
	 * @param course_id the course_id to set
	 */
	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}
	
	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	@Override
	public String toString() {
		return "Rule_Welcome rule. Course_id=" + this.course_id + ". User_id=" + this.user_id + ".";
	}
}