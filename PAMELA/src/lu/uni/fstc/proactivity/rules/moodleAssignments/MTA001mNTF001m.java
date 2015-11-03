package lu.uni.fstc.proactivity.rules.moodleAssignments;


import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.rules.moodleAssignments.AbstractRuleMoodle;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @author Gilles Neyens
 * @version 3.0 - Sandro Reis © 2012 
 * @version 3.1 - Gilles Neyens © 2013 
 *
 */
public class MTA001mNTF001m extends AbstractRuleMoodle{

	private long assignment_id;
	private long user_id;
	private boolean sent;

	private boolean enabled,exists;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	public MTA001mNTF001m() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * 
	 * @param assignment_id
	 * @param user_id
	 */
	public MTA001mNTF001m(final long assignment_id, final long user_id) {
		this();
		setAssignment_id(assignment_id);
		setUser_id(user_id);
		setSent(false);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		this.exists = dataNativeSystem.assignmentExists25(this.assignment_id);
		if (exists) {
			this.enabled=dataNativeSystem.isScenarioEnabled(this.assignment_id, "newassignnotif");
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		return (exists && enabled && !sent);
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
		Global.logger.info("Creating NTF001m for the new assignment [" + assignment_id + "]");
		createRule(new NTF001m(assignment_id,user_id));
		sent = true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		if (exists && !enabled) {
			createRule(this);
		}
		return true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return "MTA001mNTF001m - Check new assignment notification. Assignment ID = " + this.assignment_id;
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
}
