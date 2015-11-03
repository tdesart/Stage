package lu.uni.fstc.proactivity.db;

import java.sql.ResultSet;

/**
 * Forth level Abstract class that holds basic attributes and methods needed to operate with PAM's database<br>
 * This dbWrapper was used for the Assignments System as well as Social Group System
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011-2013
 *
 */
abstract public class AbstractPAM2MoodleDbWrapper extends AbstractEngineMySQLDataAccessWrapper {

	/* **********************************************************************************
	 * PAM User TABLE
	 * **********************************************************************************
	 */
	/**
	 * @param user_ID
	 * @return boolean
	 */
	@Deprecated
	abstract public boolean isUserOnline(final long user_ID);

	/* **********************************************************************************
	 * PAM Countries and Cities TABLEs
	 * **********************************************************************************
	 */
	/**
	 * @return a ResultSet
	 */
	abstract public ResultSet getAllCountries();

	/**
	 * @return a ResultSet
	 */
	abstract public ResultSet getAllCities();

	/**
	 * @param country
	 * @return true if the country (simple String comparison)
	 */
	abstract public boolean validateCountry(final String country);

	/**
	 * @param city
	 * @return true if the country (simple String comparison)
	 */
	abstract public boolean validateCity(final String city);

	/**
	 * @param country
	 * @param city
	 * @return the city name as it is in the database if the city exists in the country (simple String comparison with ignore case)
	 */
	@Deprecated
	abstract public String validateCity(final String country, final String city);

	/* **********************************************************************************
	 * PAM Questions & Answers TABLEs
	 * **********************************************************************************
	 */
	/**
	 * @param user_IDs
	 * @return boolean
	 */
	abstract public boolean doUsersHaveQuestions(final String user_IDs);
	
	/**
	 * @param user_IDs
	 * @return a ResultSet
	 */
	abstract public ResultSet getUsersWithQuestions(final String user_IDs);

	/**
	 * @param question_ID
	 * @return a ResultSet
	 */
	abstract public ResultSet getPossibleAnswersFromQuestion(final long question_ID);

	/**
	 * @param ID
	 * @return the long that results form the query execution
	 */
	abstract public long updateUserQuestionTime(final long ID);

	/**
	 * @param user_ID
	 * @param question_id 
	 * @param country
	 * @return the long that results form the query execution
	 */
	abstract public long updateUserAnswerCountryOK(final long user_ID, final long question_id, final String country);

	/**
	 * @param user_ID
	 * @param question_id 
	 * @param city
	 * @return the long that results form the query execution
	 */
	abstract public long updateUserAnswerCityOK(final long user_ID, final long question_id, final String city);

	/**
	 * @param user_ID
	 * @param country
	 * @param city
	 * @return the long that results form the query execution
	 */
	@Deprecated
	abstract public long updateUserAnswerOK(final long user_ID, final String country, final String city);

	/**
	 * @param user_ID
	 * @param question_id 
	 * @return the long that results form the query execution
	 */
	abstract public long updateUserAnswerNO(final long user_ID, final long question_id);
	
	/* **********************************************************************************
	 * PAM Hint TABLE
	 * **********************************************************************************
	 */
	/**
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @return boolean true if there is a Hint to be sent at the time represented by the parameters
	 */
	abstract public boolean hasHintsToSend(final int year, final int month, final int day, final int hour);

	/**
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @return the list of Hints to be sent at the time represented by the parameters
	 */
	abstract public ResultSet getHintsToSend(final int year, final int month, final int day, final int hour);

	/**
	 * @param hint_id
	 * @return the Assignment ID
	 */
	abstract public long getAssignmentFromHint(final long hint_id);

	/**
	 * @param hint_id
	 * @return the Hint Title
	 */
	abstract public String getTitleFromHint(final long hint_id);

	/**
	 * @param hint_id
	 * @return the Hint Subject
	 */
	abstract public String getTextFromHint(final long hint_id);

	/**
	 * @param hint_id
	 * @return the long that results form the query execution
	 */
	abstract public long setSentToHint (final long hint_id);

	/**
	 * @param hint_id 
	 * @param text 
	 * @return the long that results form the query execution
	 */
	abstract public long updateHintText(final long hint_id, final String text); 

	/* **********************************************************************************
	 * PAM Queries that execute commands from the user interface
	 * **********************************************************************************
	 */
	/**
	 * Deletes a message for a certain user<br>
	 * and Logs the operation 'Delete message'
	 * @param user_ID  
	 * @param message_ID
	 */	
	abstract public void deleteMessage(final long user_ID, final long message_ID);

	/**
	 * 
	 * Updates a message as 'read' by a certain user<br>
	 * and Logs the operation 'View message'
	 * @param user_ID  
	 * @param message_ID
	 */	
	abstract public void markAsReadMessage(final long user_ID, final long message_ID);

	/**
	 * 
	 * Logs the operation 'Leave group'
	 * @param user_ID  
	 * @param grp_ID
	 */	
	abstract public void markAsLeaveGroup(final long user_ID, final long grp_ID);

	/**
	 * Returns all messages for a user
	 * @param user_ID  
	 * @return a ResultSet with the users messages
	 */
	abstract public ResultSet getAllMessages(final long user_ID);

	/**
	 * Returns the last (5) messages for a user
	 * @param user_ID
	 * @return a ResultSet with the users last (5) messages 
	 */
	abstract public ResultSet getLastMessages(final long user_ID);

	/* **********************************************************************************
	 * PAM Queries that for weekly statistics
	 * **********************************************************************************
	 */
	/**
	 * Returns all the Statistics since last execution
	 * @param old_Stat_ID
	 * @return the array of statistics since last execution
	 */
	abstract ResultSet getStats(final long old_Stat_ID);

	/* **********************************************************************************
	 * TEST Methods
	 * **********************************************************************************
	 */
	/**
	 * A Test Method to test and understand how batch executions work<br>
	 * As it turns out, if there is an error, it will <b>not ROLLBACK</b> to the previous state<br>
	 * <br><b><i>DO NOT  IMPLEMENT TRANSACTIONS THIS WAY</i></b>
	 */
	abstract public void testBatchExecution();

	/**
	 * A Test Method to test and understand how batch executions work with Transactions<br>
	 * If there is an error, it will <b>ROLLBACK</b> to the previous state<br>
	 * <br><b><i>WE CAN IMPLEMENT TRANSACTIONS THIS WAY</i></b>
	 */
	abstract public void testBatchExecutionWithTransaction();
	
	/**
	 * A Test Method to test and understand how Transactions work<br>
	 */
	abstract public void testTransactionExecution1();
	
	/**
	 * A Test Method to test how to get an integer from the select<br>
	 * before, it threw an illegal cast, 
	 */
	abstract public void testdbGetIntFromSelect();
	
	/* **********************************************************************************
	 * Added by ReMuS
	 * **********************************************************************************
	 * common methods
	 * **********************************************************************************
	 */
	/**
	 * Gets online users
	 * @return a ResultSet
	 */
	abstract public ResultSet getOnlineUsers();
	
	/*
	 * **********************************************************************************
	 * S103 methods
	 * **********************************************************************************
	 * Generic scenario for inscribing users into groups and groupings
	 * **********************************************************************************
	 */
	
	/**
	 * Gets a ResultSet of users by their country
	 * @param country 
	 * @param answer_id 
	 * @return a ResultSet
	 */
	abstract public ResultSet getUsersByCountry(final String country, final long answer_id);
	
	/**
	 * Gets a ResultSet of users by their city
	 * @param city 
	 * @param answer_id 
	 * @return a ResultSet
	 */
	abstract public ResultSet getUsersByCity(String city, long answer_id);
	
	/**
	 * Is updating users that joined a group
	 * @param option
	 * @param name 
	 * @param answer_id 
	 */
	abstract public void updateJoinedField(String option, String name, long answer_id);
	
	/**
	 * Gets a ResultSet of users by their city
	 * @param groupName
	 * @return int
	 */
	abstract public long getActivity(String groupName);

	/*
	 * **********************************************************************************
	 * S301 methods
	 * **********************************************************************************
	 * Scenario that builds the questions, answers and the questions for each user
	 * **********************************************************************************
	 */

	/**
	 * Checks if a question was created already for a certain user
	 * @param question_ID 
	 * @param user_ID 
	 * @return boolean
	 */
	abstract public boolean questionExists(final long question_ID, final long user_ID);

	/**
	 * Registers a question for a certain user in the dbPam
	 * @param question_ID 
	 * @param user_ID 
	 */
	abstract public void registerQuestion_User(final long question_ID, final long user_ID);

	/**
	 * Registers an answer in the database - dbPam
	 * @param answer 
	 */
	abstract public void registerAnswer(final String answer);

	/**
	 * Registers a question in the database - dbPam
	 * @param type 
	 * @param content 
	 * @param ordr 
	 */
	abstract public void registerQuestion(final String type, final String content, final long ordr);

	/**
	 * Registers the answers associated to one question in the database - dbPam
	 * @param question_ID 
	 * @param answer_ID 
	 */
	abstract public void registerQuestionPossibleAnswer(final long question_ID, final long answer_ID);

	/**
	 * Checks a user answered a certain question
	 * @param question_ID 
	 * @param user_ID 
	 * @return boolean
	 */
	abstract public boolean userHasAnswered(final long question_ID, final long user_ID);

	/**
	 * Return the question id
	 * @param ordr 
	 * @return long
	 */
	abstract public long getQuestionID(final long ordr);

	/**
	 * Return the question id
	 * @param answerName 
	 * @return long
	 */
	abstract public long getAnswerID(final String answerName);

	/*
	 * **********************************************************************************
	 * M301 methods
	 * **********************************************************************************
	 * Meta-scenario that checks for new answers and for new users
	 * **********************************************************************************
	 */

	/**
	 * Checks for new country-based groups that need to be created
	 * @param aswer_id 
	 * @return a boolean
	 */
	abstract public boolean checkForNewCountryGroups(final long aswer_id);
	
	/**
	 * Checks for new city-based groups that need to be created
	 * @param aswer_id 
	 * @return a boolean
	 */
	abstract public boolean checkForNewCityGroups(final long aswer_id);

	/**
	 * Returns the uncreated groups that are ready to be created
	 * @param type
	 * @param aswer_id  
	 * @return  ResultSet
	 */
	abstract public ResultSet getUncreatedGroups(String type, long aswer_id);

	/**
	 * Checks for NewCountryStudents ready to join city-based groups 
	 * @param aswer_id 
	 * @return a boolean
	 */
	abstract public boolean checkForNewCountryStudents(final long aswer_id);
	
	/**
	 * Checks for NewCityStudents ready to join city-based groups 
	 * @param aswer_id 
	 * @return a boolean
	 */
	abstract public boolean checkForNewCityStudents(final long aswer_id);

	/**
	 * Returns the un-inscribed users that want to join a group AND were the group is already created
	 * @param type 
	 * @param aswer_id 
	 * @return a ResultSet
	 */
	abstract public ResultSet getCountriesCitiesFromUninscribedUsers(String type, long aswer_id);

	/**
	 * Register a group when it was created 
	 * @param groupName 
	 * @param type 
	 */
	public abstract void registerGroup(final String groupName, final String type);

	/*
	 * **********************************************************************************
	 * M201 methods
	 * **********************************************************************************
	 * Meta-scenario that builds the statistics for each group
	 * **********************************************************************************
	 */
		
	/**
	 * Returns how many groups there are for each type - 'study','city','country'
	 * @return ResultSet
	 */
	public abstract ResultSet getAllCurrentGroupsAndTheirType();

	/**
	 * Gets the number of 'Ok' or 'No, thanks!' answers 
	 * @param typ
	 * @param end_time
	 * @param begin_time  
	 * @return long
	 */
	public abstract long getNumberOfAnswers(String typ, long begin_time, long end_time);
	
	/**
	 * Gets all the members that left a group from a time interval
	 * @param group_ID
	 * @param begin_time
	 * @param end_time
	 * @return long
	 */
	public abstract long getMembersWhoLeave(long group_ID, long begin_time, long end_time);
	
	/**
	 * Gets all the members that joined a group from a time interval
	 * @param group_ID
	 * @param begin_time
	 * @param end_time
	 * @return long
	 */
	public abstract long getMembersWhoJoined(long group_ID, long begin_time, long end_time);
	
	/**
	 * Returns all the names of all the groups
	 * @return ResultSet
	 */
	public abstract ResultSet getAllGroups();
	
	/*
	 * **********************************************************************************
	 * M202 methods
	 * **********************************************************************************
	 * Meta-scenario that checks for groups activity
	 * **********************************************************************************
	 */
	
	/**
	 * Sets all groups in PAM.groups to inactive 
	 */	
	abstract public void setAllGroupsToInactive();
	
	/**
	 * Sets a certain group's activity to '1', meaning 'active' 
	 * @param groupName
	 * @param activity
	 */	
	abstract public void setGroupActivity(String groupName, int activity);

	/**
	 * Sets a certain group's activity to '1', meaning 'active' 
	 * @param groupName
	 * @param dayOfYear
	 * @param weekOfYear
	 * @param actions
	 */	
	abstract public void registerGroupActions(String groupName, int dayOfYear, int weekOfYear, int actions);
	
	/*
	 * **********************************************************************************
	 * M203 methods
	 * **********************************************************************************
	 * Meta-scenario that checks for inactive groups
	 * **********************************************************************************
	 */
	
	/**
	 * Returns all the inactive groups
	 * @param start_day
	 * @param end_day
	 * @return ResultSet
	 */
	public abstract ResultSet getAllInactiveGroups(int start_day, int end_day);

	/**
	 * public abstract void insertIntoLog(); 
	 */
}