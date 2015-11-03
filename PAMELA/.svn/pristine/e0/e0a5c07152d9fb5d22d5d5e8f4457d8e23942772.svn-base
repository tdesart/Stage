package lu.uni.fstc.proactivity.rules.moodleAssignments;


import lu.uni.fstc.proactivity.rules.Importance;
import lu.uni.fstc.proactivity.rules.RuleMessageMoodle;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 *
 */
public class NTF002 extends AbstractRuleMoodle {

	private long assignment_id;
	private String course_name;
	private String assignment_name;
	private long assignment_creator;
	private String subject, text;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private NTF002() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * @param assignment_id 
	 */
	public NTF002(long assignment_id) {
		this();
		setAssignment_id(assignment_id);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		this.assignment_name = dataNativeSystem.getAssignmentName(this.assignment_id);
		long course_id = dataNativeSystem.getCourseIdFromAssignment(this.assignment_id);
		this.course_name = dataNativeSystem.getCourseName(course_id);
		this.assignment_creator = dataNativeSystem.getAssignmentCreator(course_id, this.assignment_id);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		return (this.assignment_creator != 0);
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
		this.subject = "Notification: New assignment '" + this.assignment_name + "'";
		this.text = "This notification is related to your new assignment '" + this.assignment_name + "' for the course '" + this.course_name + "'. \n";
		this.text += "A notification has been sent to all concerned students.";
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
/*/		// If there is a list of Teachers to send the message to	
		Long id;
		try {
			this.assignment_creator.beforeFirst();
			while (!this.assignment_creator.isClosed() && this.assignment_creator.next()) {
				id = this.assignment_creator.getLong("id");
				createRule(new MSG001(this.subject, this.text, id));
				Global.logger.info("Creating new MSG001 for teacher [" + id.toString() + "] ");
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
/*/		// to send only to the creator (single user)
		if (getActivated()) {
			createRule(new RuleMessageMoodle(this.assignment_creator, this.subject, this.text, Importance.NORMAL));
			Global.logger.info("Creating new MSG001 for the assignment creator [" + this.assignment_creator + "] ");
		}
		return true;
/**/
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return "NTF002 - New assignment Notification [assignment creator]. Assignment ID = " + this.assignment_id;
	}

	/**
	 * @param assignment_id the assignment_id to set
	 */
	public void setAssignment_id(long assignment_id) {
		this.assignment_id = assignment_id;
	}

	/**
	 * @return the assignment_id
	 */
	public long getAssignment_id() {
		return this.assignment_id;
	}
}
