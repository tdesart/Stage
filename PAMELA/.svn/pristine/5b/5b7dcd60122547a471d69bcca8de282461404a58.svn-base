package lu.uni.fstc.proactivity.rules.moodleAssignments;


import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011
 *
 */
public class MTA004 extends AbstractRuleMoodle {

	private long assignment_id;
	private long course_id;
	private long previousAccess;
	private long thisAccess;
	private boolean newAccess;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private MTA004() {
		setType(RuleType.META);
	}

	/**
	 * @param assignment_id 
	 * @param previousAccess 
	 */
	public MTA004(long assignment_id, long previousAccess) {
		this();
		setAssignment_id(assignment_id);
		setPreviousAccess(previousAccess);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		this.thisAccess = System.currentTimeMillis()/1000; // transform it to seconds -- moodle's way
		this.course_id = dataNativeSystem.getCourseIdFromAssignment(this.assignment_id);
		this.newAccess = dataNativeSystem.hasFirstAccess (this.assignment_id, this.course_id, this.previousAccess, this.thisAccess);
		Global.logger.finest("NewAccess [" + this.newAccess + "] ");
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		return this.newAccess;
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
		// no actions, only Rule Generation
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer("Assignment_id [" + this.assignment_id + "] previousAccess  [" + this.previousAccess + "] thisAccess [" + this.thisAccess + "] ");

		// check if the rule should die or clone itself use assignment deadline 
		if (isAssignmentValid(assignment_id, course_id)) {
			if (getActivated()) {
				Global.logger.info("Creating HNT001, HNT003 for the new assignment [" + assignment_id + "]");
				// not implemented! With Denis' accord, because Moodle sends automatically messages to students about new posts in forums.			
				//createRule(new HNT001(this.assignment_id, this.previousAccess, this.thisAccess));
				createRule(new HNT003(this.assignment_id, this.previousAccess, this.thisAccess));
			}
		
			createRule(new MTA004(this.assignment_id, this.thisAccess));
		}
		return true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return "MTA004 - Meta-scenario: Link access detection. Assignment ID = " + this.assignment_id;
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

	/**
	 * @param previousAccess the previousAccess to set
	 */
	public void setPreviousAccess(long previousAccess) {
		this.previousAccess = previousAccess;
	}

	/**
	 * @return the previousAccess
	 */
	public long getPreviousAccess() {
		return this.previousAccess;
	}
}
