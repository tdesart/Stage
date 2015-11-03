package lu.uni.fstc.proactivity.rules.moodleCoPs;

import java.sql.ResultSet;
import java.util.GregorianCalendar;

import lu.uni.fstc.proactivity.db.MySQLOperations;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * 	@author remus.dobrican
 *
 *	MTA 202 - checks for changes in the group activities 
 *			- runs each night
 */

public class M202 extends AbstractRuleCops{
	private long timeInterval;
//	private final int run_hour = 15; 
	private final int run_hour = 2; 
	private int thisDay, last_day_executed, actions;
	private GregorianCalendar cal;
	private ResultSet activeGroups;
	private String groupName;
	
	private M202() {
		setType(RuleType.META);
	}
	/**
	 * @param last_day_executed
	 */
	public M202(int last_day_executed) {
		this();
		this.last_day_executed = last_day_executed;
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		timeInterval = 24 * 60 * 60 ;	// = 1 day
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
		Global.logger.finer("--------------------- M202  actions() ----------------- ");
		MySQLOperations mso = MySQLOperations.getInstance();
		try {
			if (!mso.startTransaction(engine.dataEngine.getConn()))
				return;
		
			dataEngine.setAllGroupsToInactive();		
			activeGroups = dataNativeSystem.getAllActiveGroups(timeInterval);
	
			this.activeGroups.beforeFirst();
			while (!this.activeGroups.isClosed() && this.activeGroups.next()) {
				groupName = this.activeGroups.getString("name");
				actions = this.activeGroups.getInt("actions");
				
				dataEngine.setGroupActivity(groupName, 1);
				dataEngine.registerGroupActions(groupName, thisDay, cal.get(GregorianCalendar.WEEK_OF_YEAR), actions);
			} 
			
			mso.commit(engine.dataEngine.getConn());
		}
		catch (final Exception e){
			e.printStackTrace();
			mso.rollback(engine.dataNativeSystem.getConn());
			Global.logger.severe("Error in the transaction: rollback executed!");
		}
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		
		
		Global.logger.finer("--------------------- M202  rulesGeneration() ----------------- ");
		
		if (getActivated()) {
			createRule(new M202(thisDay));
			Global.logger.fine("Creating new M202  [" + thisDay + "] ");
		}
		else
			createRule(this);		
		return true;
	}

	@Override
	public String toString() {
		return "MTA202 - Checking for the activity of the groups";
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