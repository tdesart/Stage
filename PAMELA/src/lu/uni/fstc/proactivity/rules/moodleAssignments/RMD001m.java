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
public class RMD001m extends AbstractRuleMoodle{
	private long assignment_id;
	private boolean enabled,exists;
//	private int year,year2,month,month2,day,day2;
	private String assignment_name;
	private String course_name;
	private ResultSet students;
	private String dateS = "-NA-";
	private String subject, text;
	
	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private RMD001m() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * @param assignment_id 
	 */
	public RMD001m(final long assignment_id) {
		this();
		setAssignment_id(assignment_id);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
//		year=0;
//		year2=1;
		this.exists=dataNativeSystem.assignmentExists25(this.assignment_id);
		if(exists){
			this.enabled=dataNativeSystem.isScenarioEnabled(this.assignment_id,"reminder");
			if(enabled){
				this.assignment_name = dataNativeSystem.getAssignmentName25(this.assignment_id);
				final long course_id = dataNativeSystem.getCourseIdFromAssignment25(this.assignment_id);
				this.course_name = dataNativeSystem.getCourseName(course_id);
				final long deadline = dataNativeSystem.getAssignmentDeadline25(this.assignment_id);
				this.dateS = Time.longDateSecondsToString(deadline);
				this.students=dataNativeSystem.getAllStudentsNotSubmitted25(course_id, assignment_id);
			}
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		return exists && enabled;
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
		this.subject = "Reminder: Assignment Submission '" + this.assignment_name + "'";
		this.text = "This reminder is related to your assignment '" + this.assignment_name + "' for the course '" + this.course_name + "'. \n";
		this.text += "The assignment is due shortly. Please consider to submit your work by the deadline: '" + this.dateS + "'!";
		dataNativeSystem.setSentToReminder(this.assignment_id);
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
					long id;
					try {
						this.students.beforeFirst();
						while (!this.students.isClosed() && this.students.next()) {
							id = this.students.getLong("id");
							createRule(new RuleMessageMoodle(id, this.subject, this.text, Importance.HIGH));
							Global.logger.info("Creating new MSG001 for student [" + id + "] ");
						}
					} catch (final SQLException e) {
						e.printStackTrace();
						return false;
					}
				}
				return true;
			}
			return true;
		} else {
			return true;
		}
		
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return "RMD001m - Remind the student(s) about the deadline of an assignment if they have not yet submitted their works by the specified time for the assignment ID = " + this.assignment_id;
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
}
