package lu.uni.fstc.proactivity.rules.moodleCoPs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;

import lu.uni.fstc.proactivity.rules.Importance;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * 	@author remus.dobrican
 *
 *	MTA 203 - checks for inactive groups
 *			- announces users of the inactivity
 */

public class M203 extends AbstractRuleCops {
//	private final int run_hour = 15; 
	private final int run_hour = 4; 
	private int thisDay, last_day_executed;
	private GregorianCalendar cal;
	private ResultSet inactiveGroups;
	private String groupName, text, subject, subjectComplete;
	
	private M203() {
		setType(RuleType.META);
	}
	/**
	 * @param last_day_executed
	 */
	public M203(int last_day_executed) {
		this();
		this.last_day_executed = last_day_executed;
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		subject = "Groups Inactive";
		text = "Your group has been inactive since more than 3 days. Please take a look!";
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		cal = new GregorianCalendar(); // Initialised by default with the current time. Perfect! Just what I need.
		thisDay = cal.get(GregorianCalendar.DAY_OF_YEAR);
		if (thisDay != this.last_day_executed
				&& cal.get(GregorianCalendar.HOUR_OF_DAY) == run_hour) {
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
		Global.logger.finer("--------------------- M203 actions() ----------------- ");
		
		inactiveGroups = dataEngine.getAllInactiveGroups(thisDay, thisDay-3);		
		try {			
			this.inactiveGroups.beforeFirst();
			while (!this.inactiveGroups.isClosed() && this.inactiveGroups.next()) {
				groupName = this.inactiveGroups.getString("group_name");
				subjectComplete = subject + " " + groupName;
				
				createRule(new S105( groupName, this.subjectComplete, this.text, Importance.HIGH));
				Global.logger.fine("Creating new S105 for group [" + groupName + "] ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		
		//Global.logger.finer(this.toString());
		Global.logger.finer("--------------------- M203 rulesGeneration() ----------------- ");
		if (getActivated()) {
			createRule(new M203(thisDay));
			Global.logger.fine("Creating new M203  [" + thisDay + "] ");
		}
		else
			createRule(this);
		return true;
	}

	@Override
	public String toString() {
		return "MTA203 - Checking for inactive groups";
	}
	/**
	 * @return the last_day_executed
	 */
	public final int getLast_day_executed() {
		return this.last_day_executed;
	}

	/**
	 * @param last_day_executed the last_day_executed to set
	 */
	public final void setLast_day_executed(int last_day_executed) {
		this.last_day_executed = last_day_executed;
	}
}
