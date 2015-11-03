package lu.uni.fstc.proactivity.rules.moodleAssignments;


import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @author Gilles Neyens
 * @version 3.0 - Sandro Reis © 2012 
 * @version 3.1 - Gilles Neyens © 2013 
 *
 */
public class MTA002cm extends AbstractRuleMoodle{

	private long assignment_id;
	private long user_id;
	private boolean sent;

	private boolean enabled,exists;
	
	private long course_id;
	private double submissionLevel;
	private long deadLine,lastdeadline;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	public MTA002cm() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * 
	 * @param assignment_id
	 * @param user_id
	 */
	public MTA002cm(final long assignment_id, final long user_id) {
		this();
		setAssignment_id(assignment_id);
		setUser_id(user_id);
		setSent(false);
		setLastdeadline(0);
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
				this.deadLine = dataNativeSystem.getAssignmentDeadline25(assignment_id);
				if(deadLine!=lastdeadline){
					sent=false;
					lastdeadline=deadLine;
				}
				course_id = dataNativeSystem.getCourseIdFromAssignment25(this.assignment_id);
				final long totalEnrol = dataNativeSystem.countAllEnrolledStudents25(course_id);
				final long accomplishedAssign = dataNativeSystem.countSubmitted25(assignment_id);

				if (totalEnrol == 0)
					this.submissionLevel = 1.00;
				else
					this.submissionLevel = (double) accomplishedAssign / totalEnrol;
			}
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		final long now = System.currentTimeMillis()/1000;
		if (this.deadLine == 0)
			return false;
		return ((now > (this.deadLine - lu.uni.fstc.proactivity.utils.Time._1DayInSecs)) &&
				(now < this.deadLine) && exists && enabled && !sent);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#conditions()
	 */
	@Override
	protected boolean conditions() {
		return (this.submissionLevel < 1.00);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#actions()
	 */
	@Override
	protected void actions() {
		Global.logger.info("Creating PRB001m for the new assignment [" + assignment_id + "]");
		createRule(new PRB001m(assignment_id,user_id));
		sent=true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		if(exists){
			if(enabled){
				if ((this.deadLine != 0) && (this.submissionLevel < 1.00)) {
					createRule(this);
				}
				return true;
				
			} else {
				createRule(this);
				return true;
			}
		} else {
			return true;
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return "MTA002cm - Meta-scenario: Monitoring the accomplishment of an assignment when deadline is today. Assignment ID = " + this.assignment_id;
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
	 * @return a boolean
	 */
	public boolean isSent() {
		return sent;
	}

	/**
	 * @param sent
	 */
	public void setSent(final boolean sent) {
		this.sent = sent;
	}

	/**
	 * @return a long
	 */
	public long getLastdeadline() {
		return lastdeadline;
	}

	/**
	 * @param lastdeadline
	 */
	public void setLastdeadline(final long lastdeadline) {
		this.lastdeadline = lastdeadline;
	}
}
