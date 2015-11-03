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
 */
public class PRB001m extends AbstractRuleMoodle{

	private long assignment_id;
	private long user_id;
	private String assignment_name;
	private String course_name;
	private ResultSet students;
	private String dateS = "-NA-";
	private String subject, text;

	private boolean enabled,exists;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	public PRB001m() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * 
	 * @param assignment_id
	 * @param user_id
	 */
	public PRB001m(final long assignment_id,final long user_id) {
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
			this.enabled=dataNativeSystem.isScenarioEnabled(this.assignment_id,"problemdetection");
			if(enabled){
				this.assignment_name = dataNativeSystem.getAssignmentName25(this.assignment_id);
				final long course_id = dataNativeSystem.getCourseIdFromAssignment25(this.assignment_id);
				this.course_name = dataNativeSystem.getCourseName(course_id);
				final long deadline = dataNativeSystem.getAssignmentDeadline25(this.assignment_id);
				this.dateS = Time.longDateSecondsToString(deadline);
				this.students = dataNativeSystem.getAllStudentsNotSubmitted25(course_id, this.assignment_id);
			}
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		return ((this.user_id != 0) && exists && enabled);
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
		final StringBuffer buf = new StringBuffer();

		this.subject = "Notification: Assignment Submission Problem";
		buf.append("This notification is related to your assignment '" + this.assignment_name + "' for the course '" + this.course_name + "'.\n");
		buf.append("There is a problem with the assignment submission.\n");
		buf.append("The deadline for this assignment expires in less than one day and its submission level is less than 100%!\n");
		if (this.dateS.compareTo("-NA-") != 0)
			buf.append("The referred deadline is : '" + this.dateS + "'!");
		buf.append("Please consider reviewing the list of students who haven't submitted their work yet:\n");
		
		long id;
		String name;
		try {
			this.students.beforeFirst();
			while (!this.students.isClosed() && this.students.next()) {
				id = this.students.getLong(1);
				name = dataNativeSystem.getUserFullName(id);
				buf.append("\t - " + name + "\n");
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		} finally {
			this.text = buf.toString();
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());

		if(exists){
			if(enabled){
				if (getActivated()) {
					createRule(new RuleMessageMoodle(this.user_id, this.subject, this.text, Importance.VERY_HIGH));
					Global.logger.info("Creating new MSG001 for the assignment creator [" + this.user_id + "] ");
				}
					return true;
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
		return "PRB001_old - inform the teacher about the problem concerning the assignment ID = " + this.assignment_id;
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
