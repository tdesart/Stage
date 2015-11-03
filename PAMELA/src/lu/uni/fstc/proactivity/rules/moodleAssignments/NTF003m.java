package lu.uni.fstc.proactivity.rules.moodleAssignments;


import lu.uni.fstc.proactivity.rules.Importance;
import lu.uni.fstc.proactivity.rules.RuleMessageMoodle;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @author Gilles Neyens
 * @version 3.0 - Sandro Reis © 2012 
 * @version 3.1 - Gilles Neyens © 2013 
 *
 */
public class NTF003m extends AbstractRuleMoodle {

	private long assignment_id;
	private long user_id;
	private String course_name;
	private String assignment_name;
	private String subject, text;
	private boolean enabled,exists;
	private int submission_level;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	public NTF003m() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * 
	 * @param assignment_id
	 * @param user_id
	 * @param submission_level 
	 */
	public NTF003m(final long assignment_id,final long user_id,final int submission_level) {
		this();
		setAssignment_id(assignment_id);
		setUser_id(user_id);
		setSubmission_level(submission_level);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		this.exists=dataNativeSystem.assignmentExists25(this.assignment_id);
		if(exists){
			this.enabled=dataNativeSystem.isScenarioEnabled(this.assignment_id,"notification");
			if(enabled){
				this.assignment_name = dataNativeSystem.getAssignmentName25(this.assignment_id);
				final long course_id = dataNativeSystem.getCourseIdFromAssignment25(this.assignment_id);
				this.course_name = dataNativeSystem.getCourseName(course_id);
			}
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		return (this.user_id != 0 && exists && enabled);
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
		this.subject = "Notification: Assignment Submission level ("+getSubmission_level()+"%)";
		this.text = "This notification is related to your assignment '" + this.assignment_name + "' for the course '" + this.course_name + "'.\n";
		this.text += "The submission level for this assignment has reached "+getSubmission_level()+"%.";
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
					createRule(new RuleMessageMoodle(this.user_id, this.subject, this.text, Importance.NORMAL));
					Global.logger.info("Creating new MSG001 for assignment creator [" + this.user_id + "] ");
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
		return "NTF003m - Submission Notification has reached "+getSubmission_level()+" (or more). Assignment ID = " + this.assignment_id;
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

	/**
	 * @return a int
	 */
	public int getSubmission_level() {
		return submission_level;
	}

	/**
	 * @param submission_level
	 */
	public void setSubmission_level(final int submission_level) {
		this.submission_level = submission_level;
	}
}
