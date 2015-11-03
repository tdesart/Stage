package lu.uni.fstc.proactivity.rules.moodleCoPs;

import java.sql.ResultSet;

import org.json.JSONObject;

import lu.uni.fstc.proactivity.db.MySQLOperations;
import lu.uni.fstc.proactivity.messaging.server.SMServerNewGroup;
import lu.uni.fstc.proactivity.rules.Importance;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Remus Dobrican
 * @version 1.0 - Remus Dobrican © 2013 
 *
 *	Inscribes users (which have only a role of 'student' in all courses ) 
 *		into a certain group
 */
public class S103 extends AbstractRuleCops{
	
	final private String subject = "Social Group Enrollment";
	private String text = "";
	private long course_ID, group_ID, category_ID, user_ID;
	private String groupName, opt;
	private ResultSet users;
	private long activity;
	final private int importance = Importance.NORMAL;
	
	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private S103() {
		setType(RuleType.SCENARIO);
	}
	
	/**
	 * @param course_ID 
	 * @param groupName 
	 * @param opt 
	 */
	public S103(final long course_ID, final String groupName, final String opt) {
		this();
		this.course_ID = course_ID;
		this.groupName = groupName;			
		this.opt = opt;
		text = "You were inscribed in a new social group called " + groupName + ".";
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
			group_ID = dataNativeSystem.getGroupID(groupName);
			category_ID = dataNativeSystem.getCategoryIDFromCategoryName(groupName); // groupName contains the categoryName
			activity = dataEngine.getActivity(groupName);
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
		
			if(opt.equals("study")){		
				dataNativeSystem.inscribeUsersIntoGroup(course_ID, group_ID, category_ID);
				//engine.db.inscribeUsersIntoPamLog(course_ID, group_ID, category_ID);
			}
			else if(opt.equals("country")){
				// get all the users that want to be part of a group (said YES) and that are not yet inscribed
				users = dataEngine.getUsersByCountry(groupName, dataEngine.getAnswerID("Ok"));
				
				// create a new command for the clients
				SMServerNewGroup grpSrv = new SMServerNewGroup(dataEngine, 0, groupName, group_ID, activity); // groupId and activity are ‘long’
	            JSONObject json = grpSrv.buildJSON();
	
				this.users.beforeFirst();
				while (!this.users.isClosed() && this.users.next()) {
					user_ID = this.users.getLong("user_id");				// gets the user's ID				
									
					dataNativeSystem.inscribeUserIntoGroup_2(group_ID, user_ID);
					//engine.db.inscribeUsersIntoPamLog(course_ID, group_ID, category_ID);
					
					
					dataEngine.updateJoinedField(opt, groupName, dataEngine.getAnswerID("Ok"));
					
					createRule(new lu.uni.fstc.proactivity.rules.RuleMessageMoodle(user_ID, subject, text, importance));
					
					// sends the 'result' to the client
		            engine.socketServer.sendToId(user_ID, json.toString());
				}
			}
			else if(opt.equals("city")){
				// get all the users that want to be part of a group (said YES) and that are not yet inscribed
				users = dataEngine.getUsersByCity(groupName, dataEngine.getAnswerID("Ok"));
				
				// create a new 'result' for the clients
				SMServerNewGroup grpSrv = new SMServerNewGroup(dataEngine, 0, groupName, group_ID, activity); // groupId and activity are ‘long’
	            JSONObject json = grpSrv.buildJSON();
	
				this.users.beforeFirst();
				while (!this.users.isClosed() && this.users.next()) {
					user_ID = this.users.getLong("user_id");				// gets the user's ID				
									
					dataNativeSystem.inscribeUserIntoGroup_2(group_ID, user_ID);
					//engine.db.inscribeUsersIntoPamLog(course_ID, group_ID, category_ID);
					
					dataEngine.updateJoinedField(opt, groupName, dataEngine.getAnswerID("Ok"));

					createRule(new lu.uni.fstc.proactivity.rules.RuleMessageMoodle(user_ID, subject, text, importance));

					// sends the 'result' to the client
		            engine.socketServer.sendToId(user_ID, json.toString());
				}
			}
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

		if (this.getActivated() && opt.equals("study")) {
			//Global.logger.info("Creating new S105 [" + groupName + ", " + subject +"] ");
			Global.logger.fine("Creating new S105 [" + groupName + ", " + subject +"] ");
			createRule(new S105(groupName, subject, text, importance));		
		}
		else
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "S103 - Inscribes users into the following group " + this.groupName;
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
	}
	
	/**
	 * @return a String
	 */
	public String getOpt() {
		return opt;
	}
	
	/**
	 * @param opt
	 */
	public void setOpt(final String opt) {
		this.opt = opt;
	}
}