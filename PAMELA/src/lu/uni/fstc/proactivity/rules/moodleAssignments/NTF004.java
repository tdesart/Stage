package lu.uni.fstc.proactivity.rules.moodleAssignments;

import java.sql.ResultSet;


import lu.uni.fstc.converter.ResultSetConverter;
import lu.uni.fstc.proactivity.rules.Importance;
import lu.uni.fstc.proactivity.rules.RuleMessageMoodle;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014
 * Notification rule which sends a email with htis weeks statistics on new assignments and proactive assignments.
 * 
 * This rule is executed once and doesn't clone itself, so there is no need to check anything.
 *
 */
public class NTF004 extends AbstractRuleMoodle {

	private static final String PAM_ADMIN_EMAIL = "sandro.reis@uni.lu";
	private long old_Assign_ID, old_ProAssign_ID;
	private long user_ID;
	private ResultSet rsAssignments;
	private ResultSet rsProAssignments;
	private String subject, text;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	public NTF004() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * @param last_Assign_ID 
	 * @param last_ProAssign_ID 
	 */
	public NTF004(final long last_Assign_ID, final long last_ProAssign_ID) {
		this();
		setOld_Assign_ID(last_Assign_ID);
		setOld_ProAssign_ID(last_ProAssign_ID);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		this.rsAssignments	  = dataNativeSystem.getNewAssignmentsOrderedAllFields(getOld_Assign_ID());
		this.rsProAssignments = dataNativeSystem.getNewProactiveAssignmentsOrderedAllFields(getOld_ProAssign_ID());
		this.user_ID 		  = dataNativeSystem.getUserIDFromEmail(PAM_ADMIN_EMAIL);
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
		this.subject = "Notification: Assignments Statistics";
		this.text = "Assignments since last execution were: \n";

		StringBuffer buf = new StringBuffer();
		try {
			buf.append("<b>List of NORMAL assignments detected since assignment id = " + getOld_Assign_ID() + ".</b><p>");
			if (!this.rsAssignments.isClosed() && this.rsAssignments.next())
				buf.append (ResultSetConverter.toHTML(this.rsAssignments));
			else
				buf.append ("<p><< no assignments to report >><p>");

			buf.append("<b>List of PROACTIVE assignments detected since assignment id = " + getOld_ProAssign_ID() + ".</b><p>");
			if (!this.rsProAssignments.isClosed() && this.rsProAssignments.next())
				buf.append (ResultSetConverter.toHTML(this.rsProAssignments));
			else
				buf.append ("<p><< no proactive assignments to report >><p>");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.text = buf.toString();
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		createRule(new RuleMessageMoodle(user_ID, this.subject, this.text, Importance.HIGH));
		Global.logger.info("Creating new MSG001 for user [" + PAM_ADMIN_EMAIL + "] ");
		return true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return "NTF004 - Notification with this weeks statistics since assignment " + getOld_Assign_ID() + " and proactive assignment = " + getOld_ProAssign_ID();
	}

	/**
	 * @param last_Assign_ID the last_Assign_ID to set
	 */
	public void setOld_Assign_ID(long last_Assign_ID) {
		this.old_Assign_ID = last_Assign_ID;
	}

	/**
	 * @return the last_Assign_ID
	 */
	public long getOld_Assign_ID() {
		return this.old_Assign_ID;
	}

	/**
	 * @param old_ProAssign_ID the old_ProAssign_ID to set
	 */
	public void setOld_ProAssign_ID(long old_ProAssign_ID) {
		this.old_ProAssign_ID = old_ProAssign_ID;
	}

	/**
	 * @return the old_ProAssign_ID
	 */
	public long getOld_ProAssign_ID() {
		return this.old_ProAssign_ID;
	}
}
