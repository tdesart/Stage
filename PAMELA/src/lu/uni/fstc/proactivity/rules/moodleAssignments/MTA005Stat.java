package lu.uni.fstc.proactivity.rules.moodleAssignments;

import java.util.GregorianCalendar;


import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014
 */
public class MTA005Stat extends AbstractRuleMoodle {

/**/
 	// This block is for testing purposes only.
 	GregorianCalendar tcal = new GregorianCalendar(); // Initialised with the current time. Perfect!
	final private int run_day = tcal.get(GregorianCalendar.DAY_OF_WEEK);
	final private int run_hour = tcal.get(GregorianCalendar.HOUR_OF_DAY);
/*/
	final private int run_day = GregorianCalendar.MONDAY;
	final private int run_hour = 8;
/**/
	private long last_week_executed;
	private long last_Assign_ID, last_ProAssign_ID;
	private int thisWeek; 

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	public MTA005Stat() {
		setType(RuleType.META);
	}

	/**
	 * @param last_week_executed 
	 * @param old_Assign_ID 
	 * @param old_ProAssign_ID 
	 */
	public MTA005Stat(final long last_week_executed, final long old_Assign_ID, final long old_ProAssign_ID) {
		this();
		setLast_week_executed(last_week_executed);
		setLast_Assign_ID(old_Assign_ID);
		setLast_ProAssign_ID(old_ProAssign_ID);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		GregorianCalendar cal = new GregorianCalendar(); // Initialised by default with the current time. Perfect! Just what I need.
		thisWeek = cal.get(GregorianCalendar.WEEK_OF_YEAR);

		if (thisWeek != this.last_week_executed &&
			cal.get(GregorianCalendar.DAY_OF_WEEK) == run_day && 
			cal.get(GregorianCalendar.HOUR_OF_DAY) == run_hour) {

			Global.logger.info("Week= " + this.thisWeek + "; last_Assign_ID = " + getLast_Assign_ID()+ "; last_ProAssign_ID = " + getLast_ProAssign_ID());
			return true;
		}
		return false;
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
		Global.logger.finest("This week assignments statistics!");
		createRule(new NTF004(getLast_Assign_ID(), getLast_ProAssign_ID()));
		setLast_Assign_ID (dataNativeSystem.getLastAssignment()); 
		setLast_ProAssign_ID(dataNativeSystem.getLastProactiveAssignment()); 
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		if (getActivated()) {
			Global.logger.info("Week= " + this.thisWeek + "; getLast_Assign_ID() = " + getLast_Assign_ID()+ "; last_ProAssign_ID = " + getLast_ProAssign_ID());
			createRule(new MTA005Stat(thisWeek, getLast_Assign_ID(), getLast_ProAssign_ID()));
		}
		else
			createRule(this);
		return true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return "MTA005b - Meta-scenario: Last weeks statistics Report. Last week it was activated = " + this.last_week_executed + ", last assignment = " + getLast_Assign_ID() + " and last proactive assignment = " + getLast_ProAssign_ID();
	}

	/**
	 * @param last_week_executed the last_week_executed to set
	 */
	public void setLast_week_executed(long last_week_executed) {
		this.last_week_executed = last_week_executed;
	}

	/**
	 * @return the last_week_executed
	 */
	public long getLast_week_executed() {
		return this.last_week_executed;
	}

	/**
	 * @param old_Assign_ID the old_Assign_ID to set
	 */
	public void setLast_Assign_ID(long old_Assign_ID) {
		this.last_Assign_ID = old_Assign_ID;
	}

	/**
	 * @return the old_Assign_ID
	 */
	public long getLast_Assign_ID() {
		return this.last_Assign_ID;
	}

	/**
	 * @param old_ProAssign_ID the old_ProAssign_ID to set
	 */
	public void setLast_ProAssign_ID(long old_ProAssign_ID) {
		this.last_ProAssign_ID = old_ProAssign_ID;
	}

	/**
	 * @return the old_ProAssign_ID
	 */
	public long getLast_ProAssign_ID() {
		return this.last_ProAssign_ID;
	}
}
