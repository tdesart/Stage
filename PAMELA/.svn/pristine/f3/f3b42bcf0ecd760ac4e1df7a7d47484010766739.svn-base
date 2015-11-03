package lu.uni.fstc.proactivity.rules.moodleCoPs;

import lu.uni.fstc.proactivity.db.MySQLOperations;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Remus Dobrican
 * @version 1.0 - Remus Dobrican © 2013 
 *
 * Creates the groups and the groupings
 */
public class S102 extends AbstractRuleCops {

	private long course_ID, group_ID = 0, grouping_ID = 0;
	private String groupName, groupingName;
	private int section;
	
	private S102() {
		setType(RuleType.SCENARIO);
	}
	
	/**
	 * @param course_ID 
	 * @param groupName 
	 * @param section 
	 */
	public S102(final long course_ID, final String groupName, final int section) {
		this();
		this.groupName = groupName;
		this.groupingName = groupName;
		this.course_ID = course_ID;
		this.setSection(section);
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
		return true;
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
		MySQLOperations mso = MySQLOperations.getInstance();
	
		try {
			if (!mso.startTransaction(engine.dataNativeSystem.getConn()) || !mso.startTransaction(engine.dataEngine.getConn()))
				return;
			
			Global.logger.finer(this.toString());
			if (!dataNativeSystem.groupExist(course_ID, groupName)){
				dataNativeSystem.createGroup(course_ID, groupName);
				if(section == 1)
					dataEngine.registerGroup(groupName, "study");
				else if(section == 2)
					dataEngine.registerGroup(groupName, "country");
				else if(section == 3)
					dataEngine.registerGroup(groupName, "city");
			}
			else
				Global.logger.warning("group exists");
			
			if (!dataNativeSystem.groupingExist(course_ID, groupingName))
				dataNativeSystem.createGrouping(course_ID, groupingName);
			else
				Global.logger.warning("grouping exists");
			
			group_ID = dataNativeSystem.getGroupID(groupName);
			grouping_ID = dataNativeSystem.getIDFromGroupingName(groupingName);
			if(!dataNativeSystem.groupIsInGrouping(group_ID, grouping_ID) && group_ID != 0 && grouping_ID != 0)
				dataNativeSystem.insertGroupIntoGrouping(group_ID, grouping_ID);
	
			mso.commit(engine.dataNativeSystem.getConn());
			mso.commit(engine.dataEngine.getConn());
		}
		catch (final Exception e){
			e.printStackTrace();
			mso.rollback(engine.dataNativeSystem.getConn());
			mso.rollback(engine.dataEngine.getConn());
			Global.logger.severe("Error in the transaction: rollback executed!");
		}	
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		
		if (this.getActivated()) {
			//Global.logger.info("--------------------- Creating new S104 [" + groupName + ", " + section +"] ---------------------");
			Global.logger.fine("--------------------- Creating new S104 [" + groupName + ", " + section +"] ---------------------");
			createRule(new S104(course_ID, groupName, section));
		}
		else
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "S102 - Creates the groups and the groupings " + groupName;
	}
	
	/**
	 * @return a long
	 */
	public long getCourse_ID() {
		return course_ID;
	}
	
	/**
	 * @param course_ID
	 */
	public void setCourse_ID(final long course_ID) {
		this.course_ID = course_ID;
	}
	
	/**
	 * @return a String
	 */
	public String getGroupName() {
		return groupName;
	}
	
	/**
	 * @param groupName
	 */
	public void setGroupName(final String groupName) {
		this.groupName = groupName;
		this.groupingName = groupName;
	}
	
	/**
	 * @return an int
	 */
	public int getSection() {
		return section;
	}
	/**
	 * @param section
	 */
	public void setSection(final int section) {
		this.section = section;
	}
}