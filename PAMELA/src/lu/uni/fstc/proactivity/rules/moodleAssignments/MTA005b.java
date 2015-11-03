package lu.uni.fstc.proactivity.rules.moodleAssignments;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;


import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011/2012
 */
public class MTA005b extends AbstractRuleMoodle {

/*/
 	// This block is for testing purposes only.
 	GregorianCalendar tcal = new GregorianCalendar(); // Initialised with the current time. Perfect!
	final private int run_day = tcal.get(GregorianCalendar.DAY_OF_WEEK);
	final private int run_hour = tcal.get(GregorianCalendar.HOUR_OF_DAY);
/*/
	final private int run_day = GregorianCalendar.FRIDAY;
	final private int run_hour = 16;
/**/
	private long last_week_executed;
	private boolean newEvents;
	private ResultSet events;
	private int thisWeek; 

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	public MTA005b() {
		setType(RuleType.META);
	}

	/**
	 * @param last_week_executed 
	 */
	public MTA005b(long last_week_executed) {
		this();
		setLast_week_executed(last_week_executed);
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

			this.newEvents = dataNativeSystem.isEventNextWeek();
			if (this.newEvents)
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
		// no actions, only Rule Generation
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		if (getActivated()) {
			this.events = dataNativeSystem.getStudentsForNextWeekEvents();
			try {
				long student_ID;
				this.events.beforeFirst();
				while (!this.events.isClosed() && this.events.next()) {
					student_ID = this.events.getLong("id");
					Global.logger.info("Creating RMD003 for next week events for student [" + student_ID + "]");
					createRule(new RMD003(student_ID));
				}
				createRule(new MTA005b(thisWeek));
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			Global.logger.finest("Next week Events treated!");
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
		return "MTA005b - Meta-scenario: Event list for next week (calendar scan). Last week it was activated = " + this.last_week_executed;
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
}
