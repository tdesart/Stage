package lu.uni.fstc.proactivity.rules.moodleCoPs;

import java.sql.ResultSet;
import java.sql.SQLException;

import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Remus Dobrican
 * @version 1.0 - Remus Dobrican © 2013 
 *
 *	Sends notifications to the members of a group
 */
public class S105 extends AbstractRuleCops {
	
	private ResultSet inscribedUsers_IDs;
	private long student_ID, group_ID;
	private String subject, text, groupName;
	private int importance;

	private S105() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * @param groupName
	 * @param subject
	 * @param text
	 * @param importance
	 */
	public S105(final String groupName, final String subject, final String text, final int importance) {
		this();
		this.groupName = groupName;
		this.subject = subject;
		this.text = text;
		this.importance = importance;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
			this.group_ID = dataNativeSystem.getGroupID(groupName);
			inscribedUsers_IDs = dataNativeSystem.getStudentsFromGroup(group_ID);
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
		//Global.logger.finer("--------------------- S105  actions() ----------------- ");
		Global.logger.finer(this.toString());
		try {
			this.inscribedUsers_IDs.beforeFirst();
			while (!this.inscribedUsers_IDs.isClosed() && this.inscribedUsers_IDs.next()) {
				student_ID = this.inscribedUsers_IDs.getLong("userid");

				createRule(new lu.uni.fstc.proactivity.rules.RuleMessageMoodle(student_ID, this.subject, this.text, importance));
				//Global.logger.fine("Creating new MSG001 for student [" + student_ID + "] ");
				//Global.logger.info("Creating new MSG001 for student [" + student_ID + ", "+subject+", "+importance+"] ");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 *	Doesn't generate other rules 
	 * */
	@Override
	protected boolean rulesGeneration() {	
		Global.logger.finer("--------------------- S105  rulesGeneration() ----------------- ");
		return true;
	}

	@Override
	public String toString() {
		return "S105 - Broadcast this message to all users of a group: " + this.subject;
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
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @return the importance
	 */
	public final int getImportance() {
		return this.importance;
	}
	/**
	 * @param importance the importance to set
	 */
	public final void setImportance(int importance) {
		this.importance = importance;
	}
}