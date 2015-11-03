package lu.uni.fstc.proactivity.rules.moodleAssignments;

import java.sql.ResultSet;
import java.sql.SQLException;


import lu.uni.fstc.proactivity.rules.Importance;
import lu.uni.fstc.proactivity.rules.RuleMessageMoodle;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.Time;

/**
 * @author Sandro Reis
 * @author Gilles Neyens
 * @version 3.0 - Sandro Reis © 2012 
 * @version 3.1 - Gilles Neyens © 2013 
 * 
 * Notification rule which sends a notification about a new assignment to all students enrolled to the course.
 * 
 * This rule is executed once and doesn't clone itself, so there is no need to check whether the assignment has been modified or not.
 *
 */
public class NTF001m extends AbstractRuleMoodle {

	private long assignment_id;
	private long user_id;
	private String course_name;
	private String assignment_name;
	private ResultSet students;
	private String dateS = "-NA-";
	private String subject, text;
	private boolean enabled,exists;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private NTF001m() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * @param assignment_id 
	 * @param user_id 
	 */
	public NTF001m(final long assignment_id, final long user_id) {
		this();
		setAssignment_id(assignment_id);
		setUser_id(user_id);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		this.exists=dataNativeSystem.assignmentExists25(this.assignment_id);
		if(exists){
			this.enabled=dataNativeSystem.isScenarioEnabled(this.assignment_id,"newassignnotif");
			if(enabled){
				this.assignment_name = dataNativeSystem.getAssignmentName25(this.assignment_id);
				final long course_id = dataNativeSystem.getCourseIdFromAssignment25(this.assignment_id);
				this.course_name = dataNativeSystem.getCourseName(course_id);
				final long deadline = dataNativeSystem.getAssignmentDeadline25(this.assignment_id);
				this.dateS = Time.longDateSecondsToString(deadline);
				this.students = dataNativeSystem.getStudentsFromCourse(course_id);
			}
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		return true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#conditions()
	 */
	@Override
	protected boolean conditions() {
		return exists && enabled;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#actions()
	 */
	@Override
	protected void actions() {
		this.subject = "Notification: New assignment '" + this.assignment_name + "'";
		this.text = "This notification is related to your new assignment '" + this.assignment_name + "' for the course '" + this.course_name + "'. \n";
		this.text += "Please take into consideration the deadline: '" + this.dateS + "'!";
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		if(exists && enabled){
			long id;
			try {
				this.students.beforeFirst();
				while (!this.students.isClosed() && this.students.next()) {
					id = this.students.getLong("id");
					createRule(new RuleMessageMoodle(id, this.subject, this.text, Importance.NORMAL));
					Global.logger.info("Creating new MSG001 for student [" + id + "] ");
				}
			} catch (final SQLException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}else{
			return true;
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return "NTF001m - New assignment Notification [Students]. Assignment ID = " + this.assignment_id;
	}

	/**
	 * @param assignment_id the assignment_id to set
	 */
	public void setAssignment_id(final long assignment_id) {
		this.assignment_id = assignment_id;
	}

	/**
	 * @return the assignment_id
	 */
	public long getAssignment_id() {
		return this.assignment_id;
	}

	/**
	 * @return a long
	 */
	public long getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id
	 */
	public void setUser_id(final long user_id) {
		this.user_id = user_id;
	}
}
