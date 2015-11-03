package lu.uni.fstc.proactivity.db;

import java.sql.ResultSet;

import lu.uni.fstc.proactivity.parameters.DbConnectionParameters;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * A "Singleton" Object to wrap the MySql connection to PAM
 * on version 4, the attribute DbConnectionParameters and the methods that use the PAM Signals TABLE where extracted to the newly created LocalDbWrapper
 *  
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2011-2014
 *
 */
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE", justification = "SQL Statement are dinamic, but created inside the program, not based on user input. Therefore, no risk of SQL injectios exist.")
public class PAMDbWrapper extends AbstractPAM2MoodleDbWrapper {

	private static PAMDbWrapper theInstance = null;

	/**
	 * PAMDbWrapper Constructor<br>
	 * <b>Private</b> to implement the "Singleton" Object
	 */
	private PAMDbWrapper() throws Exception {
		super();
		conn = mso.openConnection(connParams);

		if (conn == NO_CONNECTION) {
			setConnected(false);
			Global.logger.severe("Error opening DB connection!");
		} else
			setConnected(true);
	}

	/**
	 * the public method to get the objetc's instance
	 * @return AbstractPAMDbWrapper
	 */
	public static synchronized PAMDbWrapper getInstance(final String configFilePath) throws Exception {
		if (theInstance == null) {
			connParams = new DbConnectionParameters(configFilePath);
			theInstance = new PAMDbWrapper();
		}
		return theInstance;
	}

	/* **********************************************************************************
	 * PAM User TABLE
	 * **********************************************************************************
	 */
	@SuppressWarnings("deprecation")
	//@Deprecated
	@Override
	public boolean isUserOnline(final long user_ID) {
		// Replaced with a call to UserOnline table in PAS schema/database
		// select (UNIX_TIMESTAMP() - lastaccess) < 60 from mdl_user where id = 2306
		return mso.getBooleanFromSelect(conn, "PAM.UsersOnline", "userid = " + user_ID, "online");
	}

	/* **********************************************************************************
	 * PAM Countries and Cities TABLEs
	 * **********************************************************************************
	 */
	@Override
	public ResultSet getAllCountries() {
		String q = "select country_name from country order by country_name";
		return mso.getArrayFromSelect(conn, q);
	}

	@Override
	public ResultSet getAllCities() {
		String q = "select city_name from cities order by city_name";
		return mso.getArrayFromSelect(conn, q);
	}

	@Override
	public boolean validateCountry(final String country) {
		String q = "select count(1) ";
		q += "from  country c ";
		q += "where country_name = '" + country + "'";
		return mso.getBooleanFromSelect(conn, q);
	}

	@Override
	public boolean validateCity(final String city) {
		String q = "select count(1) ";
		q += "from  cities c ";
		q += "where city_name = '" + city + "'";
		return mso.getBooleanFromSelect(conn, q);
	}

	@SuppressWarnings("deprecation")
	@Override
	public String validateCity(final String country, final String city) {
		String q = "select full_name_nd ";
		q += "from world_cities_free w, country c ";
		q += "where w.cc_fips = c.cc_fips ";
		q += "and country_name = '" + country + "' ";
		q += "and full_name_lower = lower('" + city + "')";
		return mso.getStringFromSelect(conn, q);
	}

	/* **********************************************************************************
	 * PAM Questions & Answers TABLEs
	 * **********************************************************************************
	 */
	@Override
	public boolean doUsersHaveQuestions(final String user_IDs) {
		String q = "select count(1)>0 ";
		q += "from PAM.Question_User "; 
		q += "where user_id in (" + user_IDs + ") and answer_id is null and UNIX_TIMESTAMP() > question_time+30";
		return mso.getBooleanFromSelect(conn, q);
	}

	@Override
	public ResultSet getUsersWithQuestions(final String user_IDs) {
		String q = "select qu.id id, q.id question_id, q.content, q.typ, qu.user_id ";
		q += "from PAM.Question_User qu, PAM.Question q ";
		q += "where qu.question_id = q.id and user_id in (" + user_IDs + ") and answer_id is null and UNIX_TIMESTAMP() > question_time+30";
		return mso.getArrayFromSelect(conn, q);
	}

	@Override
	public ResultSet getPossibleAnswersFromQuestion(final long question_ID) {
		String q = "select qa.answer answer ";
		q += "from PAM.Question_Possible_Answer qpa, PAM.Question_Answer qa ";
		q += "where qpa.answer_id = qa.id and question_id = " + question_ID;
		return mso.getArrayFromSelect(conn, q);
	}

	@Override
	public long updateUserQuestionTime(final long ID) {
		String q = "update Question_User set question_time = UNIX_TIMESTAMP() where id = " + ID;
		return mso.executeUpdate(conn, q);
	}

	@Override
	public long updateUserAnswerCountryOK(final long user_ID, final long question_id, final String country) {
		String q = "update Question_User ";
		q += "SET country = '" + country + "', answer_time = UNIX_TIMESTAMP(), ";
		q += "answer_id =  (select id from Question_Answer where answer = 'Ok') ";
		q += "where user_id = " + user_ID + " and question_id = " + question_id;
		return mso.executeUpdate(conn, q);
	}
	
	@Override
	public long updateUserAnswerCityOK(final long user_ID, final long question_id, final String city) {
		String q = "update Question_User ";
		q += "SET city = '" + city + "', answer_time = UNIX_TIMESTAMP(), ";
		q += "answer_id =  (select id from Question_Answer where answer = 'Ok') ";
		q += "where user_id = " + user_ID + " and question_id = " + question_id;
		return mso.executeUpdate(conn, q);
	}

	@SuppressWarnings("deprecation")
	@Override
	@Deprecated
	public long updateUserAnswerOK(final long user_ID, final String country, final String city) {
		String q = "update Question_User ";
		q += "SET country = '" + country + "', city = '" + city + "', answer_time = UNIX_TIMESTAMP(), ";
		q += "answer_id =  (select id from Question_Answer where answer = 'Ok') ";
		q += "where user_id = " + user_ID;
		return mso.executeUpdate(conn, q);
	}

	@Override
	public long updateUserAnswerNO(final long user_ID, final long question_id) {
		String q = "update Question_User ";
		q += "SET answer_id = (select id from Question_Answer where answer = 'No, thanks!'), ";
		q += "answer_time = UNIX_TIMESTAMP(), country = NULL, city = NULL ";
		q += "where user_id = " + user_ID + " and question_id = " + question_id;
		return mso.executeUpdate(conn, q);
	}

	/* **********************************************************************************
	 * PAM Hint TABLE
	 * **********************************************************************************
	 */
	@Override
	public boolean hasHintsToSend(final int year, final int month, final int day, final int hour) {
		return mso.getBooleanFromSelect(conn, "PAM.Hint", "exec_year = "+year+" and exec_month = "+month+" and exec_day = "+day+" and exec_hour = "+hour+" and sent = false", "count(1)");
	}

	@Override
	public ResultSet getHintsToSend(final int year, final int month, final int day, final int hour) {
		return mso.getArrayFromSelect(conn, "PAM.Hint", "exec_year = "+year+" and exec_month = "+month+" and exec_day = "+day+" and exec_hour = "+hour+" and sent = false", "id");
	}

	@Override
	public long getAssignmentFromHint(final long hint_id) {
		return mso.getLongFromSelect(conn, "PAM.Hint", "id = " + hint_id, "assignment_id");
	}

	@Override
	public String getTitleFromHint(final long hint_id) {
		return mso.getStringFromSelect(conn, "PAM.Hint", "id = " + hint_id, "title");
	}

	@Override
	public String getTextFromHint(final long hint_id) {
		return mso.getStringFromSelect(conn, "PAM.Hint", "id = " + hint_id, "body");
	}

	@Override
	public long setSentToHint (final long hint_id) {
		String q = "UPDATE PAM.Hint SET sent = 1 WHERE id = " + hint_id;
		return mso.executeUpdate(conn, q);
	}

	@Override
	public long updateHintText(final long hint_id, final String text) {
		String q = "UPDATE PAM.Hint SET body = '"+text+"' WHERE id = " + hint_id;
		return mso.executeUpdate(conn, q);
	}

	/* **********************************************************************************
	 * PAM Queries that execute commands from the user interface
	 * **********************************************************************************
	 */
	@Override
	public void deleteMessage(final long user_ID, final long message_ID) {
/*/		String q = "INSERT INTO PAM.Log (user_id, instance_id, action, subject, time) VALUES ("
				+ user_ID + ", " + message_ID + ", 'delete messsage', '', UNIX_TIMESTAMP())";
		mso.executeUpdate(conn, q);

		q = "DELETE FROM PAM.Messages WHERE msg_id = " + message_ID
				+ " AND userid = " + user_ID;
		mso.executeUpdate(conn, q);
/**/
		String[] set = new String[2];
		if (!mso.startTransaction(conn))
			return;
		
		String q = "INSERT INTO PAM.Log (user_id, instance_id, action, subject, time) VALUES ("
			+ user_ID + ", " + message_ID + ", 'delete messsage', '', UNIX_TIMESTAMP())";
		set[0]=q;

		q = "DELETE FROM PAM.Messages WHERE msg_id = " + message_ID
		+ " AND userid = " + user_ID;
		set[1]=q;

		// commit and rollback are managed in this call
		mso.executeBatch(conn, set);
	}

	@Override
	public void markAsReadMessage(final long user_ID, final long message_ID) {
		String[] set = new String[2];
		if (!mso.startTransaction(conn))
			return;
		
		String q = "INSERT INTO PAM.Log (user_id, instance_id, action, subject, time) VALUES ("
				+ user_ID + ", " + message_ID + ", 'view message', '', UNIX_TIMESTAMP())";
//		mso.executeUpdate(conn, q);
		set[0]=q;

		q = "UPDATE PAM.Messages SET isRead = 1 WHERE msg_id = " + message_ID
				+ " AND userid = " + user_ID;
//		mso.executeUpdate(conn, q);
		set[1]=q;

		// commit and rollback are managed in this call
		mso.executeBatch(conn, set);
	}

	@Override
	public void markAsLeaveGroup(final long user_ID, final long grp_ID) {
		String q = "INSERT INTO PAM.Log (user_id, instance_id, action, subject, time) VALUES ("
				+ user_ID + ", " + grp_ID + ", 'leave group', '', UNIX_TIMESTAMP())";
		mso.executeUpdate(conn, q);
	}

	@Override
	public ResultSet getAllMessages(final long user_ID) {
		String q = "SELECT msg_id, subject, body, isRead, sent time_created, importance FROM Messages WHERE userid = "
				+ user_ID + " order by time_created ";
		return mso.getArrayFromSelect(conn, q);
	}

	@Override
	public ResultSet getLastMessages(final long user_ID) {
		String q = "select msg_id, subject, body, isRead, sent time_created, importance from PAM.Messages where userid = "
				+ user_ID + " order by time_created desc LIMIT 5";
		return mso.getArrayFromSelect(conn, q);
	}

	/* **********************************************************************************
	 * PAM Queries that for weekly statistics
	 * **********************************************************************************
	 */
	@Override
	public ResultSet getStats(final long old_Stat_ID) {
		String q = "select * from Stats where Stat_id = Parent_Execution and Total_Activated <> 0 and Stat_id > " + old_Stat_ID + " order by Stat_id desc";
		return mso.getArrayFromSelect(conn, q);
	}
	
	/* **********************************************************************************
	 * TEST Methods
	 * **********************************************************************************
	 */

	/**
	 * A Test Method to test and understand how batch executions work<br>
	 * As it turns out, if there is an error, it will <b>not ROLLBACK</b> to the previous state<br>
	 * The fisrt sql statement is committed to the db, even with the error caused by the second!<br>
	 * <br><b><i>DO NOT  IMPLEMENT TRANSACTIONS THIS WAY</i></b>
	 * we have to start a transaction beforehand
	 */
	@Override
	public void testBatchExecution() {
		String[] set = new String[2];
		mso.getConnectionMetaData(conn);
		String sql = "INSERT INTO Log (user_id, instance_id, action, subject, time) VALUES (998, 666, 'delete', 'SUBJECT NAME HERE', UNIX_TIMESTAMP())";
		set[0]=sql;
		//this second line is causing an error on purpose to test if it will rollback!
		sql = "INSERT INTO Log (id, user_id, instance_id, action, subject, time) VALUES (6, 999, 667, 'delete', 'SUBJECT NAME 2 HERE', UNIX_TIMESTAMP())";
		set[1]=sql;
		Global.logger.info("2x SQL added to batch set");

		int [] retvals = mso.executeBatch(conn, set);
		if (retvals!=null) {
			// append the return values to a string
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < retvals.length; ++i)
				buf.append(retvals[i] + "+");
			Global.logger.info("Batch executed; affected lines: " + buf.toString());
		}
		else {
			Global.logger.warning("NO results to report!");
		}
	}

	/**
	 * A Test Method to test and understand how batch executions work with Transactions<br>
	 * If there is an error, it will <b>ROLLBACK</b> to the previous state<br>
	 * <br><b><i>WE CAN IMPLEMENT TRANSACTIONS THIS WAY</i></b>
	 */
	@Override
	public void testBatchExecutionWithTransaction() {
		if (!mso.startTransaction(conn))
			return;
		testBatchExecution(); 					// delegate logic to the other test method
	}

	/**
	 * A Test Method to test and understand how Transactions work<br>
	 * FIXME transactions DON'T work unless the error is in the last query.
	 * FIXME cannot set auto-commit to true after the end of the transaction 
	 * FIXME commit AND ROLLBACK must be managed outside query execution in one single try/catch block
	 */ 
	@Override
	public void testTransactionExecution1() {
		if (!mso.startTransaction(conn))
			return;
		Global.logger.info("Transaction created!");

		Global.logger.info("Execute SQL 1 - no error expected");
		String sql = "INSERT INTO Log (id, user_id, instance_id, action, subject, time) VALUES (6, 999, 667, 'delete', 'SUBJECT NAME 2 HERE2222', UNIX_TIMESTAMP())";
		mso.executeUpdate(conn, sql);

		//this second line is causing an error on purpose to test if it will rollback!
		Global.logger.info("Execute SQL 2 - ERROR expected");
		sql = "INSERT INTO Log (user_id, instance_id, action, subject, time) VALUES (998, 666, 'delete', 'SUBJECT NAME HERE', UNIX_TIMESTAMP())";
		mso.executeUpdate(conn, sql);

		mso.commit(conn);
		Global.logger.info("ALL done!");
	}

	/**
	 * A Test Method to test how to get an integer from the select<br>
	 * before, it threw an illegal cast.
	 * After testing I realised the code was OK. It works, as long as the value FITS an int..., if not -> Illegal cast exception
	 * Updated code of 'getIntFromSelect' to catch the exception and provide more info on the LOG!
	 */
	@Override
	public void testdbGetIntFromSelect() {
		Global.logger.info("START!");
		String sql;
		int i;

		sql= "select id from Log limit 1";
		i = mso.getIntFromSelect(conn, sql);
		Global.logger.info(sql + " = " + i);

		sql = "select exec_year from `PAM`.`Hint`";
		i = mso.getIntFromSelect(conn, sql);
		Global.logger.info(sql + " = " + i);

		sql = "select sent from `PAM`.`Messages`";
		i = mso.getIntFromSelect(conn, sql);
		Global.logger.info(sql + " = " + i);

		Global.logger.info("ALL done!");
	}

	/* **********************************************************************************
	 * STUB Methods
	 * **********************************************************************************
	 */

	/**
	 * Method stub/skeleton/template that shows how to execute several queries in a Single Transaction
	 */
	public void executeSeveralQueriesInASingleTransaction() {
		try {
			if (!mso.startTransaction(conn))
				return;

			String sql = ""; // dummy query 1
			mso.executeUpdate(conn, sql);

			sql = ""; // dummy query 2
			mso.executeUpdate(conn, sql);
			// repeat as many queries as the program requires

			mso.commit(conn);
			Global.logger.info("ALL DONE: commit executed!");
		}
		catch (final Exception e){
			e.printStackTrace();
			mso.rollback(conn);
			Global.logger.severe("Error in the transaction: rollback executed!");
		}
	}

	/**
	 * Method stub/skeleton/template that shows how to execute a batch of queries in a Single Transaction
	 */
	public void executeABatchInASingleTransaction() {
		String[] set = new String[2];
		// TODO Not yet decided if mso.startTransaction should be here or in execute()
//		if (!mso.startTransaction(conn))
//			return;

		String sql = ""; // dummy query 1
		set[0]=sql;

		sql = ""; // dummy query 2
		set[1]=sql;
		// repeat as many queries as the program requires

		// commit and rollback are managed in this call
//			int [] retvals = mso.executeBatch(st); 
		int [] retvals = mso.executeBatch(conn, set);

		if (retvals != null) {
			// treat the results
		}
		else {
			// treat the absence of results
			Global.logger.warning("NO results to report!");
		}
	}

	/* **********************************************************************************
	 * Added by ReMuS
	 * **********************************************************************************
	 * common methods
	 * **********************************************************************************
	 */

	@Override
	public ResultSet getOnlineUsers(){
		final String q = "SELECT userid, time FROM PAM.UsersOnline WHERE online = 1";
		return mso.getArrayFromSelect(conn, q);
	}

	/*
	 * **********************************************************************************
	 * S103 methods
	 * **********************************************************************************
	 * Generic scenario for inscribing users into groups and groupings
	 * **********************************************************************************
	 */
	
	@Override
	public ResultSet getUsersByCountry(final String country, final long answer_id){
		String q = "SELECT user_id FROM PAM.Question_User WHERE country = '"+ country +"' AND joined = 0 AND answer_id = " + answer_id;
		return mso.getArrayFromSelect(conn, q);
	}
	
	@Override
	public ResultSet getUsersByCity(final String city, final long answer_id){
		String q = "SELECT user_id FROM PAM.Question_User WHERE city = '"+ city +"' AND joined = 0 AND answer_id = " + answer_id;
		return mso.getArrayFromSelect(conn, q);
	}
	
	@Override
	public void updateJoinedField(String option, final String name, final long answer_id){
		String q = "";
		if(option.equals("country"))
			q = "UPDATE PAM.Question_User SET joined = 1 WHERE country = '"+ name +"' AND joined = 0 AND answer_id = " + answer_id;
		else if(option.equals("city"))
			q = "UPDATE PAM.Question_User SET joined = 1 WHERE city = '"+ name +"' AND joined = 0 AND answer_id = " + answer_id;
		mso.executeUpdate(conn, q);
	}
	
	@Override
	public long getActivity(String groupName){
		String q = "SELECT activity FROM PAM.groups WHERE group_name = '"+ groupName +"'";
		return mso.getLongFromSelect(conn, q);
	}

	/*
	 * **********************************************************************************
	 * S301 methods
	 * **********************************************************************************
	 * Scenario that builds the questions, answers and the questions for each user
	 * **********************************************************************************
	 */
	
	@Override
	public boolean questionExists(final long question_ID, final long user_ID){
		String q = "SELECT count(1)>0 FROM PAM.Question_User WHERE question_ID = "+ question_ID +" AND user_ID = " + user_ID;
		return mso.getBooleanFromSelect(conn, q);
	}
	
	@Override
	public void registerQuestion_User(final long question_ID, final long user_ID){
		String q = "INSERT INTO PAM.Question_User (question_id, user_id) VALUES ("+ question_ID +"," + user_ID +")";
		mso.executeUpdate(conn, q);
	}
	
	@Override
	public void registerAnswer(final String answer){
		String q = "INSERT INTO PAM.Question_Answer (answer)  VALUES('"+ answer +"');";
		mso.executeUpdate(conn, q);
	}
	
	@Override
	public void registerQuestion(final String type, final String content, final long ordr){
		String q = "INSERT INTO PAM.Question (content, typ, ord)  VALUES('"+ content +"','"+type+"', "+ ordr +");";
		mso.executeUpdate(conn, q);
	}
	
	@Override
	public void registerQuestionPossibleAnswer(final long question_ID, final long answer_ID){
		String q = "INSERT INTO PAM.Question_Possible_Answer (question_id, answer_id)  VALUES("+ question_ID +", "+ answer_ID +");";
		mso.executeUpdate(conn, q);
	}

	@Override
	public boolean userHasAnswered(final long question_ID, final long user_ID){
		String q = "SELECT count(1)>0 FROM PAM.Question_User WHERE question_ID = "+ question_ID +" AND user_ID = " + user_ID;		// && answerIsNotEmpty
		return mso.getBooleanFromSelect(conn, q);
	}

	@Override
	public long getQuestionID(final long ordr){
		String q = "SELECT id FROM PAM.Question WHERE ord = "+ ordr;		// && answerIsNotEmpty
		return mso.getLongFromSelect(conn, q);
	}

	@Override
	public long getAnswerID(final String answerName){
		String q = "SELECT id FROM PAM.Question_Answer WHERE answer = '"+ answerName + "'";
		return mso.getLongFromSelect(conn, q);
	}

	/*
	 * **********************************************************************************
	 * M301 methods
	 * **********************************************************************************
	 * Meta-scenario that checks for new answers and for new users
	 * **********************************************************************************
	 */
	
	@Override
	public boolean checkForNewCountryGroups(final long aswer_id){
		String q = "select count(1) from (SELECT count(country) > 2 FROM PAM.Question_User where joined = 0 and answer_id = "+ aswer_id +" and country <> '' and country not in (SELECT group_name FROM PAM.groups) group by country having count(1)>2) fake";
		return mso.getBooleanFromSelect(conn, q);
	}
	
	@Override
	public boolean checkForNewCityGroups(final long aswer_id){
		String q = "select count(1) from (SELECT count(city) > 2 FROM PAM.Question_User where joined = 0 and answer_id = "+ aswer_id +" and city <> '' and city not in (SELECT group_name FROM PAM.groups) group by city having count(1)>2) fake";
		return mso.getBooleanFromSelect(conn, q);
	}

	@Override
	public ResultSet getUncreatedGroups(final String type, final long aswer_id){
		String q = "";
		if(type.equals("country"))
			q = "SELECT country FROM PAM.Question_User where joined = 0 and answer_id = " + aswer_id +" and country <> '' and country not in (SELECT group_name FROM PAM.groups)group by country having count(1)>2";
		else if(type.equals("city"))
			q = "SELECT city FROM PAM.Question_User where joined = 0 and answer_id = " + aswer_id +" and city <> '' and city not in (SELECT group_name FROM PAM.groups)group by city having count(1)>2";
		return mso.getArrayFromSelect(conn, q);
	}
 
	@Override
	public boolean checkForNewCountryStudents(final long aswer_id){
		String q = "select count(1) from (SELECT count(country) > 0 FROM PAM.Question_User where joined = 0 and answer_id = "+ aswer_id +" and country <> '' and country in (SELECT group_name FROM PAM.groups)group by country having count(1)>0 ) fake";
		return mso.getBooleanFromSelect(conn, q);
	}
	
	@Override
	public boolean checkForNewCityStudents(final long aswer_id){
		String q = "select count(1) from (SELECT count(city) > 0 FROM PAM.Question_User where joined = 0 and answer_id = "+ aswer_id +" and city <> '' and city in (SELECT group_name FROM PAM.groups)group by city having count(1)>0 ) fake";
		return mso.getBooleanFromSelect(conn, q);
	}
	
	@Override
	public ResultSet getCountriesCitiesFromUninscribedUsers(final String type, final long aswer_id){
		String q = "";
		if(type.equals("country"))
			q = "SELECT country FROM PAM.Question_User where joined = 0 and answer_id = "+ aswer_id +" and country <> '' and country in (SELECT group_name FROM PAM.groups)group by country having count(1)>0";
		else if(type.equals("city"))
			q = "SELECT city FROM PAM.Question_User where joined = 0 and answer_id = "+ aswer_id +" and city <> '' and city in (SELECT group_name FROM PAM.groups)group by city having count(1)>0";		
		return mso.getArrayFromSelect(conn, q);
	}

	@Override
	public void registerGroup(final String groupName, final String type){
		String q = "INSERT INTO PAM.groups (type, group_name, timestamp, activity)  VALUES('"+ type +"','"+ groupName +"', UNIX_TIMESTAMP(), 1);";
		mso.executeUpdate(conn, q);
	}

	/*
	 * **********************************************************************************
	 * M201 methods
	 * **********************************************************************************
	 * Meta-scenario that builds the statistics for each group
	 * **********************************************************************************
	 */

	@Override
	public ResultSet getAllCurrentGroupsAndTheirType(){
		String q = "SELECT type, count(*) number FROM PAM.groups GROUP BY type";
		return mso.getArrayFromSelect(conn, q);
	}

	@Override
	public long getNumberOfAnswers(String typ, long begin_time, long end_time){
		String q = "select count(1) from PAM.Question_User where answer_id = (select id from PAM.Question_Answer where answer = '"+typ+"') and answer_time >= "+begin_time+" and answer_time < " + end_time;
		return mso.getLongFromSelect(conn, q);
	}

	@Override
	public long getMembersWhoLeave(long group_ID, long begin_time, long end_time){
		String q = "select count(1) from PAM.Log where action = 'leave group' and instance_id = "+group_ID+"  and time < "+end_time+" and time > "+begin_time;
		return mso.getLongFromSelect(conn, q);
	}

	@Override
	public long getMembersWhoJoined(long group_ID, long begin_time, long end_time){
		String q = "select count(1) from PAM.Log where action = 'join group' and instance_id = "+group_ID+"  and time < "+end_time+" and time > "+begin_time;
		return mso.getLongFromSelect(conn, q);
	}

	@Override
	public ResultSet getAllGroups(){
		String q = "select group_name from PAM.groups";
		return mso.getArrayFromSelect(conn, q);
	}

	/*
	 * **********************************************************************************
	 * M202 methods
	 * **********************************************************************************
	 * Meta-scenario that checks for groups activity
	 * **********************************************************************************
	 */

	@Override
	public void setAllGroupsToInactive(){
		String q = "Update PAM.groups set activity = 0";
		mso.executeUpdate(conn, q);
	}

	@Override
	public void setGroupActivity(String groupName, int activity){
		String q = "Update PAM.groups set activity = "+activity+" where group_name = '"+groupName+"'";
		mso.executeUpdate(conn, q);
	}

	@Override	
	public void registerGroupActions(String groupName, int dayOfYear, int weekOfYear, int actions){
		String q = "INSERT INTO PAM.group_stats (group_name, day_of_year, week_of_year, activities_count) VALUES ('"+groupName+"', "+dayOfYear+", "+weekOfYear+", "+actions+")";
		mso.executeUpdate(conn, q);
	}

	/*
	 * **********************************************************************************
	 * M203 methods
	 * **********************************************************************************
	 * Meta-scenario that checks for inactive groups
	 * **********************************************************************************
	 */

	@Override
	public ResultSet getAllInactiveGroups(int start_day, int end_day){
		String q = "select group_name from PAM.groups where group_name <> '' and group_name not in ( select group_name from PAM.group_stats where day_of_year <= "+start_day+" and day_of_year > "+end_day+" )";
		return mso.getArrayFromSelect(conn, q);
	}

	/*
	@Override
	public void insertIntoLog(){
		String q = "INSERT INTO `moodle_test`.`mdl_log` (`time`, `userid`, `ip`, `course`, `module`, `cmid`, `action`, `url`, `info`) VALUES ('1362044849', '2310', '10.91.1.9', '1544', 'forum', '15424', 'view forum', 'view.php?id=15424', '2559')";
		//System.out.println(q);
		mso.executeUpdate(conn, q);	
	}
	*/
}