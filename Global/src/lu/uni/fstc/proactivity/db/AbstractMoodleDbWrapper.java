package lu.uni.fstc.proactivity.db;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import lu.uni.fstc.proactivity.parameters.MoodleConnectionParameters;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * Third level Abstract class that holds basic attributes and methods needed to operate with Moodle's database
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011/2012
 *
 */
public abstract class AbstractMoodleDbWrapper extends AbstractHostDataAccessWrapper {

	 final MySQLOperations mso = MySQLOperations.getInstance ();

	 /**
	 * the connection parameters to the native moodle database <p> 
	 * It is <b>STATIC</b> because:<ul><li>It allows only one Connection Parameter to access Moodle.</li><br>
	 * <li> It is accessed in getInstance, a static method.</li></ul>
	 * <p><p>Overloads the AbstractHostDataAccessWrapper.connParams attribute, <br>
	 * so that other DbWrappers types can have different Connection Parameters, otherwise they would reference the same one.
	 */
	public static MoodleConnectionParameters connParams = null;

	/**
	 * AbstractMoodleDbWrapper Constructor<br>
	 * <b>Private</b> (protected, so that it's accessed from 'children') to implement the "Singleton" Object 
	 */
	protected AbstractMoodleDbWrapper() {
	}

	/* **********************************************************************************
	 * User TABLE
	 * **********************************************************************************
	 */

	/**
	 * @param user_ID
	 * @return true if the user exists on the database
	 */
	public abstract boolean userExists(final long user_ID);
	
	/**
	 * @param user_ID
	 * @return the user's full name
	 */
	public abstract String getUserFullName(final long user_ID);

	/**
	 * @param email
	 * @return the user id that corresponds to that email
	 */
	abstract public long getUserIDFromEmail(final String email);
	
	/**
	 * @param course_ID
	 * @return the array of students Id's 
	 */
	public abstract ResultSet getStudentsFromCourse(final long course_ID);

	/**
	 * @param course_ID
	 * @return the array of students Id's 
	 */
	public abstract long countAllEnrolledStudents(final long course_ID);

	/**
	 * @param course_ID
	 * @return the array of students Id's 
	 */
	public abstract long countEnrolledStudents(final long course_ID);
	
	/**
	 * @param course_ID
	 * @return the array of teachers Id's 
	 */
	public abstract ResultSet getTeachersFromCourse(final long course_ID);

	/**
	 * @param course_ID
	 * @param assignment_ID
	 * @return the array of students Id's 
	 */
	public abstract ResultSet getAllStudentsNotSubmitted(final long course_ID, final long assignment_ID);

	/**
	 * @param course_ID
	 * @param assignment_ID
	 * @return the array of students Id's 
	 */
	public abstract ResultSet getStudentsNotSubmitted(final long course_ID, final long assignment_ID);
	
	/* **********************************************************************************
	 * Course TABLE
	 * **********************************************************************************
	 */
	/**
	 * @param course_ID
	 * @return the array of teachers Id's 
	 */
	public abstract String getCourseName(final long course_ID);

	/**
	 * @param course_ID
	 * @return the start date of the course 
	 */
	public abstract long getCourseStartDate(final long course_ID);

	/**
	 * @return the course ID for the COPs course 
	 */
	public abstract long getCopsCourseId();

	/* **********************************************************************************
	 * Forum TABLE
	 * **********************************************************************************
	 */
	/**
	 * @param course_ID
	 * @return the array of forums 
	 */
	public abstract ResultSet getForumsFromCourse(final long course_ID);

	/* **********************************************************************************
	 * Assignment TABLE
	 * **********************************************************************************
	 */
	/**
	 * @param lastAssignment
	 * @return true if there are new assignments 
	 */
	public abstract boolean isNewAssignment(final long lastAssignment);

	/**
	 * @param assignment_ID
	 * @return true if assignment still exists 
	 */
	public abstract boolean assignmentExists(final long assignment_ID);
	
	/**
	 * @param lastAssignment
	 * @return the array of students Id's 
	 */
	public abstract ResultSet getNewAssignmentsOrdered(final long lastAssignment);

	/**
	 * @param lastAssignment
	 * @return the array of students Id's 
	 */
	public abstract ResultSet getNewAssignmentsOrderedAllFields(final long lastAssignment);

	/**
	 * @param assignment_ID
	 * @return the array of teachers Id's 
	 */
	public abstract long getCourseIdFromAssignment(final long assignment_ID);

	/**
	 * @param assignment_ID
	 * @return the array of teachers Id's 
	 */
	public abstract String getAssignmentName(final long assignment_ID);

	/**
	 * @param assignment_ID
	 * @return the deadline of the assignment 
	 */
	public abstract long getAssignmentDeadline(final long assignment_ID);

	/**
	 * @param assignment_ID
	 * @return the deadline of the assignment 
	 */
	public abstract long countSubmitted(final long assignment_ID);

	/**
	 * @param assignment_ID
	 * @return true if the assignment type is 'offline' 
	 */
	public abstract boolean isAssignmentOffline(final long assignment_ID);

	/**
	 * Get the last assignment
	 * @return the last assignment ID
	 */	
	abstract public long getLastAssignment();

	/**
	 * Get the last proactive assignment
	 * @return the last assignment ID
	 */	
	abstract public long getLastProactiveAssignment();

	/**
	 * Are there new proactive assignments since last execution
	 * @param old_Assign_ID
	 * @return true if there were proactive assignments since last execution
	 */
	abstract public boolean isNewProactiveAssignments (final long old_Assign_ID);

	/* **********************************************************************************
	 * Event TABLE
	 * **********************************************************************************
	 */
	/**
	 * @param lastEvent
	 * @return true if there are new assignments 
	 */
	public abstract boolean isNewEvent(final long lastEvent);

	/**
	 * @param lastEvent
	 * @return the array of students Id's 
	 */
	public abstract ResultSet getNewEventsOrdered(final long lastEvent);

	/**
	 * @param event_ID
	 * @return the event name 
	 */
	public abstract String getEventName(final long event_ID);

	/**
	 * @param event_ID
	 * @return the event date 
	 */
	public abstract long getEventDate(final long event_ID);

	/**
	 * @param event_ID
	 * @return the event module name 
	 */
	public abstract String getEventModule(final long event_ID);

	/**
	 * @param event_ID
	 * @return the event type 
	 */
	public abstract String getEventType(final long event_ID);
	
	/**
	 * @param event_ID
	 * @return the course Id 
	 */
	public abstract long getCourseIdFromEvent(final long event_ID);

	/**
	 * @return true if there are events for next week
	 */
	public abstract boolean isEventNextWeek();

	/**
	 * @return the array of Students who have events for next week
	 */
	public abstract ResultSet getStudentsForNextWeekEvents();

	/**
	 * @param student_ID
	 * @return the array of events related to that student
	 */
	public abstract ResultSet getNextWeekEventsFromStudent(final long student_ID);

	/* **********************************************************************************
	 * Log TABLE
	 * **********************************************************************************
	 */
	/**
	 * @param course_ID 
	 * @param assignment_ID
	 * @return the array of user Id's 
	 */
	public abstract long getAssignmentCreator(final long course_ID, final long assignment_ID);

	/**
	 * @param assignment_ID
	 * @param course_ID
	 * @param previousAccess 
	 * @param thisAccess 
	 * @return true if there are new accesses to the assignment 
	 */
	public abstract boolean hasFirstAccess (final long assignment_ID, final long course_ID, final long previousAccess, final long thisAccess);	
	
	/**
	 * @param assignment_ID
	 * @param course_ID
	 * @param previousAccess 
	 * @param thisAccess 
	 * @return true if there are new accesses to the assignment 
	 */
	public abstract ResultSet getStudentsWithFirstAccess (final long assignment_ID, final long course_ID, final long previousAccess, final long thisAccess);
	
	/* **********************************************************************************
	 * Moodle scripts
	 * **********************************************************************************
	 */
	/**
	 * @param user_id
	 * @param subject
	 * @param body
	 * @return boolean success
	 */
//	public abstract boolean sendEmail(final long user_id, final String subject, final String body);
	
	/* **********************************************************************************
	 * **********************************************************************************
	 * **********************************************************************************
	 * TEST Methods and fields, come here
	 * **********************************************************************************
	 * **********************************************************************************
	 * **********************************************************************************
	 */

	/** 
	 * @param n input value for the compute method
	 * @return String representing the final result of the computing
	 */
	public abstract String testComputing (final int n);

	/**
	 * Test method to insert one record into the usuarios table 
	 * @param value 
	 */
	public abstract void testConnectionInsert (final String value);

	/**
	 * Test method to retrieve all records from the usuarios table 
	 */
	public abstract void testConnectionRead();

	/**
	 * Test method to delete all records from the testcalc table
	 */
	public abstract void testDeleteNumTable();

	/**
	 * Test method to retrieve all records from the testcalc table
	 */
	public abstract void testGetAllNumTable();
	
	/* **********************************************************************************
	 * Added by ReMuS
	 * **********************************************************************************
	 * common methods
	 * **********************************************************************************
	 */
	
	/**
	 * Inserts a line in the Context Table
	 * @param type 
	 * @param ID 
	 */
	public abstract void insertIntoContextTable(final String type, final long ID);
	
	/**
	 * Returns a context_ID for a specific instanceid
	 * @param instanceid 
	 * @param depth 
	 * @return long
	 */
	public abstract long getContextID(final long instanceid, final int depth);
	
	/**
	 * Updates The Context Table
	 * @param path 
	 * @param context_ID 
	 */
	public abstract void updateContext(final String path, final long context_ID);
	
	/**
	 * Inserts a line in the CacheFlags
	 * @param name 
	 */
	public abstract void insertCacheFlags(final String name);
	
	/**
	 * Returns a categoryID from the categoryName
	 * @param categoryName 
	 * @return long
	 */
	public abstract long getCategoryIDFromCategoryName(final String categoryName);
		
	/**
	 * Returns the ID of the last row that was inserted
	 * @return long
	 */
	public abstract long getLastId();
	
	/**
	 * Returns the path of a certain element in the Context Table
	 * @param context_ID 
	 * @param depth 
	 * @return String
	 */
	public abstract String getContextPath(final long context_ID, final int depth);
	
	/**
	 * Inscribes data in the course_modules Table
	 * @param type 
	 * @param course_ID 
	 * @param grouping_ID 
	 */
	public abstract void insertIntoCourseModules(final long type, final long course_ID, final long grouping_ID);
	
	/**
	 * Returns the group_id by the groupName
	 * @param groupName 
	 * @return long 
	 */
	public abstract long getGroupID(final String groupName);
	
	/**
	 * Gets the admin ID
	 * @return long
	 */
	public abstract long getAdminUserID();
	
	/*
	 * **********************************************************************************
	 * S001 methods
	 * **********************************************************************************
	 * Category Creation
	 * **********************************************************************************
	 */

	/**
	 * Checks if a certain course was already created or not
	 * @param courseShortName 
	 * @param courseName 
	 * @return a boolean
	 */
	public abstract boolean courseExist(final String courseShortName, final String courseName);

	/**
	 * Creates a new Category
	 * @param categoryName 
	 * @param sortorder 
	 */
	public abstract void createCourseCategory(final String categoryName, final long sortorder);

	/**
	 * Updates a Category
	 * @param category_ID 
	 */
	public abstract void updateCourseCategory(final long category_ID);

	/*
	 * **********************************************************************************
	 * Course Creation
	 * **********************************************************************************
	 */	
	
	/**
	 * Creates a new Course
	 * @param courseFullName 
	 * @param courseShortName 
	 * @param categorySortorder 
	 * @param categoryID 
	 * @param startdate 
	 */
	public abstract void createCourse(final String courseFullName, final String courseShortName, final long categorySortorder, final long categoryID, final long startdate);
	
	/**
	 * Returns the course_ID from the courseFullName 
	 * @param courseFullName 
	 * @return long
	 */
	public abstract long getCourseIdFromName(final String courseFullName);
		
	/**
	 * Inserts a line into the block_instance table
	 * @param blockname 
	 * @param parentcontext_ID 
	 * @param defaultweight 
	 */
	public abstract void insertBlockInstance(final String blockname, final long parentcontext_ID, final int defaultweight);
	
	/**
	 * Returns the ID of a block_instance
	 * @param blockname 
	 * @param parentcontext_ID 
	 * @return long
	 */
	public abstract long getBlockInstanceID(final String blockname, final long parentcontext_ID);
	
	/**
	 * Creates Course Categories for a specific Course 
	 * @param course_ID 
	 * @param courseDescription 
	 * @param sectionCountryDescription 
	 * @param sectionStudyGroupDescription
	 * @param sectionCityDescription 
	 */
	public abstract void createCourseSection(final long course_ID, final String courseDescription, final String sectionStudyGroupDescription, final String sectionCountryDescription, final String sectionCityDescription);
	
	/**
	 * Inserts a line into mdl_enrol table
	 * @param course_ID 
	 */
	public abstract void insertEnrolMethods(final long course_ID);
	
	/**
	 * Sets the initial modinfo of the CoPs course to 'a:0:{}'
	 * @param course_ID 
	 */ 
	public abstract void updateFirstModInfo(final long course_ID);
	
	/**
	 * Returns the groupName from its group_ID 
	 * @param group_ID 
	 * @return String
	 */ 
	public abstract String getGroupName(final long group_ID);
	
	/*
	 * **********************************************************************************
	 * S002 methods
	 * **********************************************************************************
	 */
	
	/**
	 * Creates the "manual" enrolment method for a specific course
	 * @param course_ID 
	 */
	public abstract void createEnrolmentMethodForCourse(final long course_ID);
	
	/**
	 * Returns the enrolment_ID for a specific course
	 * @param course_ID 
	 * @return long 
	 */
	public abstract long getEnrolmentIDFromCourseID(final long course_ID);
	
	/**
	 * Enrolls users into a specific course
	 * @param enrol_ID 
	 */
	public abstract void userEnrolments(final long enrol_ID);
	
	/**
	 * Creates ROLES for the users inscribed in a specific course
	 * @param context_ID 
	 */
	public abstract void roleAssignments(final long context_ID);
	
	/*
	 * **********************************************************************************
	 * S101 methods
	 * **********************************************************************************
	 * checks for all study programs and creates a group for each one
	 * **********************************************************************************
	 */
	
	/**
	 * Returns all the categories with parent 0 
	 * 		(e.g. Bachelor in Informatics, Masters in Informatics, ...)
	 * @param parent 
	 * @return ResultSet 
	 */
	public abstract ResultSet getAllCategories(final int parent);
	
	/*
	 * **********************************************************************************
	 * S102 methods
	 * **********************************************************************************
	 * Generic scenario for creating groups and groupings 
	 * **********************************************************************************
	 */
	
	/**
	 * Checks if a group exists
	 * @param course_ID 
	 * @param groupName 
	 * @return boolean
	 */
	public abstract boolean groupExist(final long course_ID, final String groupName);
	
	/**
	 * Checks if a grouping exists
	 * @param course_ID 
	 * @param groupingName 
	 * @return boolean
	 */
	public abstract boolean groupingExist(final long course_ID, final String groupingName);
	
	/**
	 * Checks if a group is inside a certain grouping
	 * @param groupName 
	 * @param groupingName 
	 * @return boolean
	 */
	public abstract boolean groupIsInGrouping(final long groupName, final long groupingName);
	
	/**
	 * Creates a group
	 * @param course_ID 
	 * @param groupName 
	 */
	public abstract void createGroup(final long course_ID, final String groupName);
	
	/**
	 * Creates a grouping 
	 * @param course_ID 
	 * @param groupingName 
	 */
	public abstract void createGrouping(final long course_ID, final String groupingName);
	
	/**
	 * Inserts a group into a grouping
	 * @param group_ID 
	 * @param grouping_ID 
	 */
	public abstract void insertGroupIntoGrouping(final long group_ID, final long grouping_ID);
	
	/**
	 * Returns the group id by the groupingName
	 * @param groupingName 
	 * @return long
	 */
	public abstract long getIDFromGroupingName(final String groupingName);
	
	/*
	 * **********************************************************************************
	 * S103 methods
	 * **********************************************************************************
	 * Generic scenario for inscribing users into groups and groupings
	 * **********************************************************************************
	 */
	
	/**
	 * Inscribes user into a specific group
	 * @param course_ID 
	 * @param group_ID 
	 * @param category_ID 
	 */
	public abstract void inscribeUsersIntoGroup(final long course_ID, final long group_ID, final long category_ID);
	
	/**
	 * Inscribes user to a city-based group
	 * @param groupID 
	 * @param user_ID 
	 */
	public abstract void inscribeUserIntoGroup_2(final long groupID, final long user_ID);
	
	
	/*
	 * **********************************************************************************
	 * S104 methods
	 * **********************************************************************************
	 * Generic scenario for creating resources for a certain group
	 * **********************************************************************************
	 */
	
	/**
	 * Creates a new forum
	 * @param name 
	 * @param type 
	 * @param course_ID 
	 * @param forum_description 
	 */
	public abstract void insertIntoForum(final String name, final String type, final long course_ID, final String forum_description);
	
	/**
	 * Returns the course section_ID
	 * @param course_ID 
	 * @param section 
	 * @return long
	 */
	public abstract long getCourseSectionID(final long course_ID, final int section);
	
	
	/**
	 * Returns the ModInfor field of a certain course
	 * @param course_ID 
	 * @return String
	 */
	public abstract String getCourseModinfo(final long course_ID);
	
	/**
	 * Updates the content of the field 'modinfo' from a certain course
	 * @param modinfo 
	 * @param course_ID 
	 */
	public abstract void updateCourseModinfo(final String modinfo, final long course_ID);
	
	/**
	 * Updates fields of the course_modules Table
	 * @param id 
	 * @param course_section_ID 
	 * @param course_modules_ID 
	 */
	public abstract void updateCourseModule(final long id, final long course_section_ID, final long course_modules_ID);
	
	/**
	 * Updates fields of the course_sections Table
	 * @param sequence 
	 * @param course_section_ID 
	 */
	public abstract void updateCourseSection(final String sequence, final long course_section_ID);
	
	/**
	 * Creates a new chat
	 * @param name 
	 * @param course_ID 
	 * @param chat_description 
	 */
	public abstract void insertIntoChat(final String name, final long course_ID, final String chat_description);
	
	/**
	 * Creates a new event
	 * @param chat_ID 
	 * @param chatName 
	 * @param course_ID 
	 * @param chat_description 
	 */
	public abstract void insertIntoEvent(final long chat_ID, final String chatName, final long course_ID, final String chat_description);
	
	/**
	 * Creates a new folder
	 * @param name 
	 * @param course_ID 
	 * @param folder_description 
	 */
	public abstract void insertIntoFolder(final String name, final long course_ID, final String folder_description);
	
	/**
	 * Creates a new forum discussion
	 * @param admin_ID 
	 * @param subject 
	 * @param message 
	 */
	public abstract void addForumPost(final long admin_ID, final String subject, final String message);
	
	/**
	 * Creates a new forum discussion
	 * @param course_ID 
	 * @param forum_ID 
	 * @param group_ID
	 * @param subject 
	 * @param forum_post_ID
	 * @param admin_ID 
	 */
	public abstract void addForumDiscussion(final long course_ID, final long forum_ID, final long group_ID, final String subject, final long forum_post_ID, final long admin_ID);
	
	/**
	 * Creates a new forum discussion
	 * @param forum_post_ID 
	 * @param forum_discussion_ID
	 */
	public abstract void updateForumPost(final long forum_post_ID, final long forum_discussion_ID);
	
	/**
	 * Inserts into mdl_log when a forum, chat or folder is created
	 * @param user_ID 
	 * @param course_ID
	 * @param cmid
	 * @param info
	 */
	public abstract void insertIntoLog(final long user_ID, final long course_ID, final long cmid, final String info );
	
	/**
	 * Returns the forum id from the mdl_modules table
	 * @return the forum id
	 */
	public abstract long getForumID();
	
	/**
	 * Returns the chat id from the mdl_modules table
	 * @return the chat id
	 */
	public abstract long getChatID();
	
	/**
	 * Returns the folder id from the mdl_modules table
	 * @return the folder id
	 */
	public abstract long getFolderID();
	
	/*
	 * **********************************************************************************
	 * S105 methods
	 * **********************************************************************************
	 * Generic scenario for creating messages and announcing users
	 * **********************************************************************************
	 */
	
	/**
	 * Returns all the users inscribed in a certain group
	 * @param group_ID 
	 * @return ResultSet
	 */
	public abstract ResultSet getStudentsFromGroup(final long group_ID);
	
	/*
	 * **********************************************************************************
	 * S301 methods
	 * **********************************************************************************
	 * Scenario that builds the questions, answers and the questions for each user
	 * **********************************************************************************
	 */
	
	/**
	 * Returns if a user is inscribed in a certain group
	 * @param user_ID 
	 * @param course_ID 
	 * @return boolean
	 */
	public abstract boolean isUserInGroup(final long user_ID, final long course_ID);

	/*
	 * **********************************************************************************
	 * M301 methods
	 * **********************************************************************************
	 * Meta-scenario that checks for new answers and for new users
	 * **********************************************************************************
	 */
	
	/**
	 * Returns the city of a certain user
	 * @param user_ID 
	 * @return String 
	 */
	public abstract String getCity(final long user_ID);
	
	/*
	 * **********************************************************************************
	 * M201 methods
	 * **********************************************************************************
	 * Meta-scenario that builds the statistics for each group
	 * **********************************************************************************
	 */
	
	/**
	 * Records a resource
	 * @param fileName 
	 * @param course_ID 
	 * @param fileDescription 
	 * @param displayOptions 
	 */
	public abstract void insertIntoResources(final String fileName, final long course_ID, final String fileDescription, final String displayOptions);
	
	/**
	 * Return all groups where there was some activity
	 * @param groupName
	 * @param begin_time
	 * @param end_time
	 * @return long containing the activity level of a group
	 */
	public abstract long getActivityLevel(final String groupName, final long begin_time, final long end_time);
		
	/**
	 * Gets all the new members of a group from a time interval
	 * @param group_ID
	 * @param begin_time
	 * @param end_time
	 * @return long with the number of new members
	 */
	public abstract long getNewMembers(final long group_ID, final long begin_time, final long end_time);
	
	/**
	 * Inserts a file into mdl_files
	 * 
	 * @param type 
	 * @param contenthash 
	 * @param pathnamehash 
	 * @param pdf_context_ID 
	 * @param pdfFileName 
	 * @param pdfFileSize 
	 */
	public abstract void insertIntoFiles(final String type, final String contenthash, final String pathnamehash, final long pdf_context_ID, final String pdfFileName, final long pdfFileSize);
	
	/* **********************************************************************************
	 * Groups TABLEs
	 * Queries that execute commands from the user interface
	 * **********************************************************************************
	 */
	/**
	 * Gets all the groups for one user
	 * @param user_ID
	 * @return ResultSet
	 */
	abstract public ResultSet getGroupsFromUser(final long user_ID);

	/**
	 * Removes a user from a certain group
	 * @param user_ID
	 * @param group_ID
	 */
	public abstract void leaveGroup(final long user_ID, final long group_ID);
	
	/*
	 * **********************************************************************************
	 * M202 methods
	 * **********************************************************************************
	 * Meta-scenario that checks for groups activity
	 * **********************************************************************************
	 */
	
	/**
	 * Returns all active groups from Moodle
	 * @param time
	 * @return ResultSet
	 */
	public abstract ResultSet getAllActiveGroups(final long time);
	
	/*
	 * New methods for moodle 2.5
	 * 
	 */
	
	/* **********************************************************************************
	 * User TABLE
	 * **********************************************************************************
	 */

	//no changes required if moodle admin enters role names
	/**
	 * @param course_ID
	 * @return a long
	 */
	public abstract long countAllEnrolledStudents25(final long course_ID);

	/**
	 * @param course_ID
	 * @param assignment_ID
	 * @return the array of students Id's 
	 */
	public abstract ResultSet getAllStudentsNotSubmitted25(final long course_ID, final long assignment_ID);

	/**
	 * @param course_ID
	 * @param assignment_ID
	 * @return the array of students Id's 
	 */
	public abstract ResultSet getStudentsNotSubmitted25(final long course_ID, final long assignment_ID);
	
	/* **********************************************************************************
	 * Assignment TABLE
	 * **********************************************************************************
	 */

	/**
	 * @param lastAssignment
	 * @return a boolean
	 */
	public abstract boolean isNewAssignment25(final long lastAssignment);

	/**
	 * @param assignment_ID
	 * @return true if assignment still exists 
	 */
	public abstract boolean assignmentExists25(final long assignment_ID);

	/**
	 * @param lastAssignment
	 * @return a ResultSet
	 */
	public abstract ResultSet getNewProactiveAssignmentsOrdered(final long lastAssignment);

	/**
	 * @param lastAssignment
	 * @return a ResultSet
	 */
	public abstract ResultSet getNewProactiveAssignmentsOrderedAllFields(final long lastAssignment);

	/**
	 * @param assignment_ID
	 * @return the array of teachers Id's 
	 */
	public abstract long getCourseIdFromAssignment25(final long assignment_ID);

	/**
	 * @param assignment_ID
	 * @return the array of teachers Id's 
	 */
	public abstract String getAssignmentName25(final long assignment_ID);

	/**
	 * @param assignment_ID
	 * @return the deadline of the assignment 
	 */
	public abstract long getAssignmentDeadline25(final long assignment_ID);

	/**
	 * @param assignment_ID
	 * @return the deadline of the assignment 
	 */
	public abstract long countSubmitted25(final long assignment_ID);

	/**
	 * @param course_ID 
	 * @param assignment_ID
	 * @return the array of user Id's 
	 */
	public abstract long getAssignmentCreator25(final long course_ID, final long assignment_ID);

	/*
	 * **********************************************************************************
	 * M203 methods
	 * **********************************************************************************
	 * Meta-scenario that checks for inactive groups
	 * **********************************************************************************
	 * nothing in moodleDbWrapper
	 * **********************************************************************************
	*/
	
	/**
	 * Returns the value of a Scenario's parameter as a String
	 * @param assignment_id	
	 * @param scenarioname
	 * @param param_name
	 * @return String
	 */
	public abstract String getStringScenarioConfigParam(final long assignment_id,final String scenarioname,final String param_name);
	
	/**
	 * Returns the value of a Scenario's parameter as a GregorianCalendar
	 * @param assignment_id	
	 * @param scenarioname
	 * @param param_name
	 * @return GregorianCalendar
	 */
	public abstract GregorianCalendar getDateScenarioConfigParam(final long assignment_id,final String scenarioname,final String param_name);
	
	/**
	 * Returns the value of a Scenario's parameter as a boolean
	 * @param assignment_id	
	 * @param scenarioname
	 * @param param_name
	 * @return boolean
	 */
	public abstract boolean getBooleanScenarioConfigParam(final long assignment_id,final String scenarioname,final String param_name);
	
	/**
	 * Returns the value of a Scenario's parameter as an integer
	 * @param assignment_id	
	 * @param scenarioname
	 * @param param_name
	 * @return int
	 */
	public abstract int getIntegerScenarioConfigParam(final long assignment_id, final String scenarioname, final String param_name);
	
	/**
	 * Returns the value of a Scenario's parameter as a long
	 * @param assignment_id	
	 * @param scenarioname
	 * @param param_name
	 * @return long
	 */
	public abstract long getLongScenarioConfigParam(final long assignment_id,final String scenarioname,final String param_name);
	
	/**
	 * Checks whether a scenario is enabled or not
	 * @param assignment_id
	 * @param scenarioname
	 * @return boolean
	 */
	public abstract boolean isScenarioEnabled(final long assignment_id,final String scenarioname);
	
	/**
	 * @param assignment_id
	 * @param year
	 * @param month
	 * @param day
	 * @return a boolean
	 */
	public abstract boolean hasHintsToSend(final long assignment_id, final int year, final int month, final int day);
	
	/**
	 * @param assignment_id
	 * @param year
	 * @param month
	 * @param day
	 * @return an ArrayList
	 */
	public abstract ArrayList<Long> getHintsToSend(final long assignment_id, final int year, final int month, final int day);
	
	/**
	 * @param assignment_id
	 * @param hint_id
	 * @return a long
	 */
	public abstract long setSentToHint(final long assignment_id,long hint_id);
	
	/**
	 * @param assignment_id
	 * @return a long
	 */
	public abstract long setSentToReminder(final long assignment_id);

	@Override
	public AbstractMoodleDbWrapper getThis() {
		Global.logger.fine("this:" + this);
		return this; 
	}
}