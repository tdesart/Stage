package lu.uni.fstc.proactivity.rules.moodleCoPs;

import java.sql.ResultSet;
import java.sql.SQLException;

import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * Routine to get all the future groups by getting all the different study programs that start with 'Bachelor' or 'Master'
 * @author remus.dobrican
 *
 */
public class S101 extends AbstractRuleCops {
	private ResultSet categories;
	private String groupName;
	private long course_ID;
	
	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private S101() {
		setType(RuleType.SCENARIO);
	}
	
	/**
	 * @param course_ID 
	 */
	public S101(final long course_ID) {
		this();
		this.course_ID = course_ID;
	}
	
	@Override
	protected void dataAcquisition() {		
		categories = dataNativeSystem.getAllCategories(0);
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
		return false;
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#actions()
	 */
	@Override
	protected void actions() {
		// no actions
	}
	
	/**
	 * 	For each potential Social Study Group it creates 
	 * 			-	a new Rule to register the group 	-- createRule(new S101(course_ID, groupName));
	 * 			- 	a new Rule to inscribe users 		-- createRule(new S102(course_ID, groupName));
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer("--------------------- S101  rulesGeneration() ----------------- ");
		//Global.logger.finer(this.toString());		
		if(activationGuards()){
			try {
				this.categories.beforeFirst();
				
				while (!this.categories.isClosed() && this.categories.next()) {
					groupName = this.categories.getString("name");
					Global.logger.fine("Creating new S102 [" + groupName + ", " + 1 +"] ");
					createRule(new S102(course_ID, groupName, 1));
					
					Global.logger.fine("Creating new S103 [" + groupName + ", study ]");
					createRule(new S103(course_ID, groupName, "study"));
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else 
			return false;
		return true;
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return "S101 - Creating a list of possible Social Groups based on the available Study Programs";
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
}