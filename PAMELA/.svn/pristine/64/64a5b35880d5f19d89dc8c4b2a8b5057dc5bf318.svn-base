package lu.uni.fstc.proactivity.rules.moodleCoPs;

import lu.uni.fstc.proactivity.db.MySQLOperations;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Remus Dobrican
 * @version 1.0 - Remus Dobrican © 2013 
 *
 * Creates a basic set resources for a certain Group
 */
public class S104 extends AbstractRuleCops {
	
	private long course_ID, group_ID, forum_ID, course_modules_ID, grouping_ID, course_section_ID, chat_ID, folder_ID;
	private String groupName, folderName, path, forumName, chatName, modinfo,
			forum_description = "<p>In this forum students are encouraged to discuss matters related to their courses.</p>",
			chat_description = "<p>Use this chat to exachange information with your colleagues inscribed in the same group</p>",
			folder_description = "<p>Use this folder to upload all your files that you share with your community.</p>";
	private int section;
	
	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private S104() {
		setType(RuleType.SCENARIO);
	}
	
	/**
	 * @param course_ID 
	 * @param groupName 
	 * @param section 
	 */
	public S104(final long course_ID, final String groupName, final int section) {
		this();
		this.course_ID = course_ID;		
		this.groupName = groupName;
		this.forumName = groupName;
		this.chatName = groupName;
		this.folderName = groupName;
		this.setSection(section);
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		MySQLOperations mso = MySQLOperations.getInstance();
		try {
			if (!mso.startTransaction(engine.dataNativeSystem.getConn()))
				return;
			
			this.group_ID = dataNativeSystem.getGroupID(groupName);
			course_section_ID = dataNativeSystem.getCourseSectionID(course_ID, section);
			grouping_ID = dataNativeSystem.getIDFromGroupingName(groupName);
			
			mso.commit(engine.dataNativeSystem.getConn());
		}
		catch (final Exception e){
			e.printStackTrace();
			mso.rollback(engine.dataNativeSystem.getConn());
			Global.logger.severe("Error in the transaction: rollback executed!");
		}
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
	 * creates a specific set of resources for a certain group
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#actions()
	 */
	@Override
	protected void actions() {		
		MySQLOperations mso = MySQLOperations.getInstance();
		try {
			if (!mso.startTransaction(engine.dataNativeSystem.getConn()))
				return;
		
			/*
			 * 	Creating a FORUM
			 */		
			//addACourseModule(5);
			addACourseModule(dataNativeSystem.getForumID());
			
			dataNativeSystem.insertIntoForum("Forum - "+forumName, "general", course_ID, forum_description);
			forum_ID = dataNativeSystem.getLastId();			

			insertAndUpdateContextTable(4, 3, "forum");			
			updateCourseModulesAndCourseSections(forum_ID);
			createModInfo(forum_ID, 13, "forum", forumName);
			
			addForumPost();
			insertIntoLog("forum", course_modules_ID);
			
			/*
			 *  CHAT
			 */
			addACourseModule(dataNativeSystem.getChatID());
			
			// INSERT INTO mdl_chat
			dataNativeSystem.insertIntoChat("Chat - "+chatName, course_ID, chat_description);
			chat_ID = dataNativeSystem.getLastId();			
			
			insertAndUpdateContextTable(4, 3, "chat");
			dataNativeSystem.insertIntoEvent(chat_ID, chatName, course_ID, chat_description);			
			updateCourseModulesAndCourseSections(chat_ID);
			createModInfo(chat_ID, 13, "chat", chatName);			
			
			insertIntoLog("chat", course_modules_ID);
			
			/*	
			 * Creating a FOLDER	
			 */
			
			addACourseModule(dataNativeSystem.getFolderID());
						
			dataNativeSystem.insertIntoFolder("Document Folder - "+folderName, course_ID, folder_description);
			folder_ID = dataNativeSystem.getLastId();
			
			insertAndUpdateContextTable(4, 3, "folder");			
			updateCourseModulesAndCourseSections(folder_ID);
			createModInfo(folder_ID, 13, "folder", folderName);

			insertIntoLog("folder", course_modules_ID);
			
			mso.commit(engine.dataNativeSystem.getConn());
		}
		catch (final Exception e){
			e.printStackTrace();
			mso.rollback(engine.dataNativeSystem.getConn());
			Global.logger.severe("Error in the transaction: rollback executed!");
		}
	}
	
	private void addACourseModule(long type){
		dataNativeSystem.insertIntoCourseModules(type, course_ID, grouping_ID);
		this.course_modules_ID = dataNativeSystem.getLastId();
	}
	
	private void insertAndUpdateContextTable(int depth1, int depth2, String resource){
		dataNativeSystem.insertIntoContextTable(resource, course_modules_ID);
		long context_ID = dataNativeSystem.getContextID(course_modules_ID, depth1);
		path = dataNativeSystem.getContextPath(course_ID, depth2) + "/" + context_ID;
		dataNativeSystem.updateContext(path, context_ID);
	}
	
	private void updateCourseModulesAndCourseSections(long id){
		dataNativeSystem.updateCourseModule(id, course_section_ID, course_modules_ID);
		dataNativeSystem.updateCourseSection(","+course_modules_ID, course_section_ID);
	}
	
	private void createModInfo(long id, int stdClass, String resource, String resourceName){
		modinfo = dataNativeSystem.getCourseModinfo(course_ID);
		
		ModInfo newModInfo = new ModInfo(modinfo, String.valueOf(id), stdClass, String.valueOf(course_modules_ID), resource, String.valueOf(section), String.valueOf(grouping_ID), resourceName);
		dataNativeSystem.updateCourseModinfo(newModInfo.getModinfo(), course_ID);
	}
	
	private void addForumPost(){
		
		final String subject = "First Topic", message = "Open new topics when you want to discuss a subject with more participants, otherwise use the chat.";
		
		dataNativeSystem.addForumPost(dataNativeSystem.getAdminUserID(), subject, message);
		long forum_post_ID = dataNativeSystem.getLastId();
		
		dataNativeSystem.addForumDiscussion(this.course_ID, this.forum_ID, this.group_ID, subject, forum_post_ID, dataNativeSystem.getAdminUserID());
		long forum_discussion_ID = dataNativeSystem.getLastId();
		
		dataNativeSystem.updateForumPost(forum_post_ID, forum_discussion_ID);
	}
	
	private void insertIntoLog(String type, long cmid){
		dataNativeSystem.insertIntoLog(dataNativeSystem.getAdminUserID(), course_ID, cmid, type);
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		// no rules to be generated
		Global.logger.finer(this.toString());
		return true;
	}

	@Override
	public String toString() {
		return "S104 - Specific resources created for the group " + this.group_ID;
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
	/**
	 * @return
	 */
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
		this.forumName = groupName;
		this.chatName = groupName;
		this.folderName = groupName;
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