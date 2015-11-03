package lu.uni.fstc.proactivity.db;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import lu.uni.fstc.proactivity.parameters.MoodleConnectionParameters;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * A "Singleton" Object to wrap the MySql connection to Moodle
 * 
 * @author Sandro  Reis
 * @version 3.0 - Sandro Reis © 2011-2013
 *
 */
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE", justification = "SQL Statement are dinamic, but created inside the program, not based on user input. Therefore, no risk of SQL injectios exist.")
public class MoodleDbWrapper extends AbstractMoodleDbWrapper {

	private final String roleNameStudent, roleNameTeacher;

	private static MoodleDbWrapper theInstance = null;
//	private int conn = NO_CONNECTION;

	/**
	 * MoodleDbWrapper Constructor<br>
	 * <b>Private</b> to implement the "Singleton" Object 
	 */
	private MoodleDbWrapper() throws Exception {
		super();
		if (Global.EXPERIMENTS) {
			roleNameStudent = "Student proactiv";
			roleNameTeacher = "Teacher proactiv";
		}
		else {
			roleNameStudent = "Student";
			roleNameTeacher = "Teacher";
		}
		buildEventQueryString();

		conn = mso.openConnection(connParams);

		if (conn == NO_CONNECTION) {
			setConnected(false);
			Global.logger.warning("Error opening DB connection.");
		}
		else 
			setConnected(true);
	}

	/**
	 * the public method to get the objetc's instance
	 * @return AbstractMoodleDbWrapper
	 */
	public static synchronized MoodleDbWrapper getInstance (final String configFilePath) throws Exception {
		if (theInstance == null) {
			connParams = new MoodleConnectionParameters(configFilePath);
			theInstance = new MoodleDbWrapper();
		}
		return theInstance;
	}

	/* **********************************************************************************
	 * **********************************************************************************
	 * **********************************************************************************
	 * PUBLIC Methods that override methods from super(), come here
	 * **********************************************************************************
	 * **********************************************************************************
	 * **********************************************************************************
	 */
	/* **********************************************************************************
	 * User TABLE
	 * **********************************************************************************
	 */
	@Override
	public boolean userExists(final long user_ID) {
		return mso.getBooleanFromSelect(conn, "mdl_user", "id = " + user_ID, "id");
	}
	
	@Override
	public String getUserFullName(final long user_ID) {
		return mso.getStringFromSelect(conn, "mdl_user", "id = " + user_ID, "CONCAT(firstname,' ',lastname) AS fullname");
	}
	
	@Override
	public long getUserIDFromEmail(final String email) {
		return mso.getLongFromSelect(conn, "mdl_user", "email = '" + email + "'", "id");
	}

	// modified by ReMuS - exclude proactive students from query - and r.name = '"+ roleNameStudent + "
	
	@Override
	public ResultSet getStudentsFromCourse(final long course_ID) {
		String q = "select u.id ";
		q += "from mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r "; 
		q += "where x.instanceid = " + course_ID + " and x.id = ra.contextid and ";
		q += "u.id = ra.userid and ra.roleid = r.id";
		return mso.getArrayFromSelect(conn, q);
	}

	// In the experiment, count all students anyway, not just the proactive students
	// Decision made in agreement with Denis S.
	@Override
	public long countAllEnrolledStudents(final long course_ID) {
		String q;
		if (Global.EXPERIMENTS)
			q = "select count(1)-3 "; // we have 3 extra students for testing
		else
			q = "select count(1) ";
				
		q += "from mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r "; 
		q += "where x.instanceid = " + course_ID + " and x.id = ra.contextid and ";
		q += "u.id = ra.userid and ra.roleid = r.id and r.name = 'student'";
		return mso.getLongFromSelect(conn, q);
	}

	// In the experiment, count all students anyway, not just the proactive students
	// Decision made in agreement with Denis S.
	@Override
	public long countEnrolledStudents(final long course_ID) {
		String q;
		if (Global.EXPERIMENTS)
			q = "select count(1)-3 "; // we have 3 extra students for testing
		else
			q = "select count(1) ";
				
		q += "from mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r "; 
		q += "where x.instanceid = " + course_ID + " and x.id = ra.contextid and ";
		q += "u.id = ra.userid and ra.roleid = r.id and r.name = '"+ roleNameStudent + "'";
		return mso.getLongFromSelect(conn, q);
	}

	@Override
	public ResultSet getTeachersFromCourse(final long course_ID) {
		String q = "select u.id ";
		q += "from mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r "; 
		q += "where x.instanceid = " + course_ID + " and x.id = ra.contextid and ";
		q += "u.id = ra.userid and ra.roleid = r.id and r.name = '"+ roleNameTeacher + "'";
		return mso.getArrayFromSelect(conn, q);
	}

	// In the experiment, get all students to provide a list to the teacher anyway, not just the proactive students
	// Decision made in agreement with Denis S.
	@Override
	public ResultSet getAllStudentsNotSubmitted(final long course_ID, final long assignment_ID) {
	/*
		select u.id 
		from mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r 
		where x.instanceid = 1416 and x.id = ra.contextid and 
		u.id = ra.userid and ra.roleid = r.id and r.name = 'Student'
		and u.id not in (select userid from mdl_assignment_submissions where assignment = 388)
	 */
		String q = "select u.id ";
		q += "from mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r "; 
		q += "where x.instanceid = " + course_ID + " and x.id = ra.contextid and ";
		q += "u.id = ra.userid and ra.roleid = r.id and r.name = 'student'";
		q += "and u.id not in (select userid from mdl_assignment_submissions where assignment = " + assignment_ID + ")";
		return mso.getArrayFromSelect(conn, q);
	}

	// In the experiment, get all students to provide a list to the teacher anyway, not just the proactive students
	// Decision made in agreement with Denis S.
	@Override
	public ResultSet getStudentsNotSubmitted(final long course_ID, final long assignment_ID) {
	/*
		select u.id 
		from mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r 
		where x.instanceid = 1416 and x.id = ra.contextid and 
		u.id = ra.userid and ra.roleid = r.id and r.name = 'Student'
		and u.id not in (select userid from mdl_assignment_submissions where assignment = 388)
	 */
		String q = "select u.id ";
		q += "from mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r "; 
		q += "where x.instanceid = " + course_ID + " and x.id = ra.contextid and ";
		q += "u.id = ra.userid and ra.roleid = r.id and r.name = '"+ roleNameStudent + "'";
		q += "and u.id not in (select userid from mdl_assignment_submissions where assignment = " + assignment_ID + ")";
		return mso.getArrayFromSelect(conn, q);
	}

	/* **********************************************************************************
	 * Course TABLE
	 * **********************************************************************************
	 */
	@Override
	public String getCourseName(final long course_ID) {
		return mso.getStringFromSelect(conn, "mdl_course", "id = " + course_ID, "fullname");
	}

	@Override
	public long getCourseStartDate(final long course_ID) {
		return mso.getLongFromSelect(conn, "mdl_course", "id = " + course_ID, "startdate") ;
	}

	@Override
	public long getCopsCourseId() {
		return mso.getLongFromSelect(conn, "mdl_course", "shortname = 'cops'", "id") ;
	}

	/* **********************************************************************************
	 * Forum TABLE
	 * **********************************************************************************
	 */
	@Override
	public ResultSet getForumsFromCourse(final long course_ID) {
		// select * from mdl_forum where course = 136
		throw new sun.reflect.generics.reflectiveObjects.NotImplementedException();
//		return mso.getArrayFromSelect(conn, "mdl_forum", "course = " + course_ID, "*");
	}
	
	/* **********************************************************************************
	 * Assignment TABLE
	 * **********************************************************************************
	 */
	@Override
	public boolean isNewAssignment(final long lastAssignment) {
		if (Global.EXPERIMENTS) {
			String q = "select count(1)>0 ";
			q += "from mdl_assignment a, mdl_course c "; 
			q += "where a.course = c.id and a.duedate > UNIX_TIMESTAMP() and (shortname = 'BPINFOR-34' or shortname = 'BPINFOR-12') and a.id >" + lastAssignment;
			return mso.getBooleanFromSelect(conn, q);
		}
		else {
			// Moodle version 2.5 has a new assignments table + mdl_assign
			return mso.getBooleanFromSelect(conn, "mdl_assign", "id > " + lastAssignment + " and duedate > UNIX_TIMESTAMP()", "IF(count(1)>0, TRUE, FALSE) as isNew ")  ;
		}
	}

	@Override
	public boolean assignmentExists(final long assignment_ID) {
		return mso.getBooleanFromSelect(conn, "mdl_assign", "id = " + assignment_ID, "id");
	}

	@Override
	public ResultSet getNewAssignmentsOrdered(final long lastAssignment) {
		if (Global.EXPERIMENTS) {
			String q = "select a.id ";
			q += "from mdl_assign a, mdl_course c "; 
			q += "where a.course = c.id and duedate > UNIX_TIMESTAMP() and (shortname = 'BPINFOR-34' or shortname = 'BPINFOR-12') and a.id >" + lastAssignment ;
			q += " order by a.id ";
			return mso.getArrayFromSelect(conn, q);
		}
		else {
			return mso.getArrayFromSelect(conn, "mdl_assign", "id > " + lastAssignment + "  and duedate > UNIX_TIMESTAMP() order by id" , "id");
		}
	}

	@Override
	public ResultSet getNewAssignmentsOrderedAllFields(final long lastAssignment) {
//		return mso.getArrayFromSelect(conn, "mdl_assign", "id > " + lastAssignment + " and duedate > UNIX_TIMESTAMP() order by id" , "*");
		return mso.getArrayFromSelect(conn, "mdl_assign", "id > " + lastAssignment + " order by id" , "id, course, name, intro, duedate");
	}

	@Override
	public long getCourseIdFromAssignment(final long assignment_ID) {
		return mso.getLongFromSelect(conn, "mdl_assign", "id = " + assignment_ID, "course");
	}

	@Override
	public String getAssignmentName(final long assignment_ID) {
		return mso.getStringFromSelect(conn, "mdl_assign", "id = " + assignment_ID, "name");
	}

	@Override
	public long getAssignmentDeadline(final long assignment_ID) {
		return mso.getLongFromSelect(conn, "mdl_assign", "id = " + assignment_ID, "duedate");
	}

	@Override
	public long countSubmitted(final long assignment_ID) {
		// select count(1) from mdl_assignment_submissions where assignment = 388
		return mso.getLongFromSelect(conn, "mdl_assign_submissions", "assignment = " + assignment_ID, "count(1)");
	}

	@Override
	public boolean isAssignmentOffline(final long assignment_ID) {
		return mso.getBooleanFromSelect(conn, "mdl_assign", "id = " + assignment_ID, "(assignmenttype ='offline')");
	}

	@Override
	public long getLastAssignment() {
		String q = "select max(id) from mdl_assign";
		return mso.getLongFromSelect(conn, q);
	}

	@Override
	public long getLastProactiveAssignment() {
		String q = "select max(id) from mdl_proass";
		return mso.getLongFromSelect(conn, q);
	}

	@Override
	public boolean isNewProactiveAssignments (final long old_Assign_ID) {
		return mso.getBooleanFromSelect(conn, "mdl_proass", "count(1) > 0", "id > "+ old_Assign_ID);
	}

	/* **********************************************************************************
	 * Event TABLE
	 * **********************************************************************************
	 */
	@Override
	public boolean isNewEvent(final long lastEvent) {
		if (Global.EXPERIMENTS) {
			String q = "select IF(count(1)>0, TRUE, FALSE) as isNew ";
			q += "from mdl_event e, mdl_course c "; 
			q += "where e.visible = 1 and e.timestart > UNIX_TIMESTAMP() and e.courseid = c.id and (shortname = 'BPINFOR-34' or shortname = 'BPINFOR-12') and e.id >" + lastEvent;
			return mso.getBooleanFromSelect(conn, q);
		}
		else {
			return mso.getBooleanFromSelect(conn, "mdl_event", "visible = 1 and id > " + lastEvent, "count(1)>0")  ;
		}
	}

	@Override
	public ResultSet getNewEventsOrdered(final long lastEvent) {
		if (Global.EXPERIMENTS) {
			String q = "select e.id ";
			q += "from mdl_event e, mdl_course c "; 
			q += "where e.visible = 1 and e.timestart > UNIX_TIMESTAMP() and e.courseid = c.id and (shortname = 'BPINFOR-34' or shortname = 'BPINFOR-12') and e.id >" + lastEvent;
			q += " order by e.id ";
			return mso.getArrayFromSelect(conn, q);
		}
		else {
			return mso.getArrayFromSelect(conn, "mdl_event", "visible = 1 and id > " + lastEvent + " order by e.id" , "id");
		}
	}

	@Override
	public String getEventName(final long event_ID) {
		return mso.getStringFromSelect(conn, "mdl_event", "id = " + event_ID, "name");
	}

	@Override
	public long getEventDate(final long event_ID) {
		return mso.getLongFromSelect(conn, "mdl_event", "id = " + event_ID, "timestart");
	}

	@Override
	public String getEventModule(final long event_ID) {
		return mso.getStringFromSelect(conn, "mdl_event", "id = " + event_ID, "modulename");
	}

	@Override
	public String getEventType(final long event_ID) {
		return mso.getStringFromSelect(conn, "mdl_event", "id = " + event_ID, "eventtype");
	}

	@Override
	public long getCourseIdFromEvent(final long event_ID) {
		return mso.getLongFromSelect(conn, "mdl_event", "id = " + event_ID, "courseid");
	}

	private String nextWeeksEventsLine1;
	private String nextWeeksEventsLine2;
	private String nextWeeksEventsLine3;
	private String nextWeeksEventsLine4;
	private String nextWeeksEventsLineExperiment;

	private void buildEventQueryString() {
		nextWeeksEventsLine1 = "from mdl_event e, mdl_course c, mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r ";
		nextWeeksEventsLine2 = "where x.instanceid = e.courseid and c.visible = 1 and e.courseid = c.id and x.id = ra.contextid ";
		nextWeeksEventsLine3 = "and u.id = ra.userid and ra.roleid = r.id and r.name = '"+ roleNameStudent + "' and e.visible = 1 ";
		nextWeeksEventsLine4 = "and e.timestart > UNIX_TIMESTAMP() and e.timestart < UNIX_TIMESTAMP()+" + lu.uni.fstc.proactivity.utils.Time._1WeekInSecs;
		nextWeeksEventsLineExperiment = "and (c.shortname = 'BPINFOR-34' or c.shortname = 'BPINFOR-12') ";
	}

	@Override
	public boolean isEventNextWeek() {
 		String q = "select IF(count(1)>0, TRUE, FALSE) as isNew ";
		q += nextWeeksEventsLine1;
		q += nextWeeksEventsLine2;
		q += nextWeeksEventsLine3;
		if (Global.EXPERIMENTS)
			q += nextWeeksEventsLineExperiment;
		q += nextWeeksEventsLine4;
//		Global.logger.info(q);
		return mso.getBooleanFromSelect(conn, q);
	}

	@Override
	public ResultSet getStudentsForNextWeekEvents() {
 		String q = "select distinct(u.id) ";
		q += nextWeeksEventsLine1;
		q += nextWeeksEventsLine2;
		q += nextWeeksEventsLine3;
		if (Global.EXPERIMENTS)
			q += nextWeeksEventsLineExperiment;
		q += nextWeeksEventsLine4;
		return mso.getArrayFromSelect(conn, q);
	}

	@Override
	public ResultSet getNextWeekEventsFromStudent(final long student_ID) {
 		String q = "select modulename, eventtype, courseid, fullname CourseName, timestart, timeduration, e.name EventName ";
 		q += nextWeeksEventsLine1;
		q += nextWeeksEventsLine2;
		q += nextWeeksEventsLine3;
		if (Global.EXPERIMENTS)
			q += nextWeeksEventsLineExperiment;
		q += nextWeeksEventsLine4;
		q += " and u.id = " + student_ID;
		q += " order by modulename, eventtype, CourseName, EventName";
		Global.logger.finest("query: " + q );
		return mso.getArrayFromSelect(conn, q);
	}

	/* **********************************************************************************
	 * Log TABLE
	 * **********************************************************************************
	 */
	@Override
	public long getAssignmentCreator(final long course_ID, final long assignment_ID) {
		/* 
	 		select userid from mdl_log
			where course = 1416 and module = 'assignment' and action = 'add' and info = '389'
		 */
		return mso.getLongFromSelect(conn, "mdl_log", "course = " + course_ID + " and module = 'assignment' and action = 'add' and info = '" + assignment_ID + "'", "userid");
	}

	@Override
	public boolean hasFirstAccess (final long assignment_ID, final long course_ID, final long previousAccess, final long thisAccess) {
		/*
		 *** version 1
		  select * from mdl_log
		  where module = 'assignment' and action = 'view' and info = '388'
		  and userid in 
			(select userid from mdl_log
			where module = 'assignment' and action = 'view' and info = '388'
			and time > 0 and time < 1326968377 group by userid)
		  and userid in 
			(select u.id
			from mdl_assignment a, mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r
			where a.id = 388 and a.course = x.instanceid and x.id = ra.contextid and
			u.id = ra.userid and ra.roleid = r.id and r.name = 'Student')
		  group by userid having count(1) = 1;
		 *** version 2 
		  select (UNIX_TIMESTAMP()-time), mdl_log.* from mdl_log 
		  where course = 468 and module = 'assignment' and action = 'view'
		  and info = '209' and userid in
		      (select userid from mdl_log where course = 468 and module = 'assignment' and action = 'view' 
		       and info = '209' and time > UNIX_TIMESTAMP()-6000
		       group by userid) 
		  and userid in
			  (select ra.userid from mdl_assignment a, mdl_context x, mdl_role_assignments ra, mdl_role r 
			  where a.id = 209 and a.course = x.instanceid and x.id = ra.contextid and 
			  ra.roleid = r.id and r.name = 'Student proactiv')
		  group by userid having count(1) = 1; 
		 */
 		String q = "select userid from mdl_log ";
		q += "where course = " + course_ID + " and module = 'assignment' and action = 'view' and info = '" + assignment_ID + "' ";
		q += "and userid in ";
		q += "  (select userid  from mdl_log where course = " + course_ID + " and module = 'assignment' and action = 'view' "; 
		q += "   and info = '" + assignment_ID + "' and time > " + previousAccess + " and time <= " + thisAccess + " group by userid) ";
		q += "and userid in ";
		q += "  (select ra.userid from mdl_assignment a, mdl_context x, mdl_role_assignments ra, mdl_role r "; 
		q += "   where a.id = " + assignment_ID + " and a.course = x.instanceid and x.id = ra.contextid and "; 
		q += "   ra.roleid = r.id and r.name = '"+ roleNameStudent + "') ";
		q += "group by userid having count(1) = 1";
		return mso.getBooleanFromSelect(conn, q);
	}

	@Override
	public ResultSet getStudentsWithFirstAccess (final long assignment_ID, final long course_ID, final long previousAccess, final long thisAccess) {
		/*
		 *** version 1
		  select * from mdl_log
		  where module = 'assignment' and action = 'view' and info = '388'countAllEnrolledStudents
		  and userid in (
			select userid from mdl_log
			where module = 'assignment' and action = 'view' and info = '388'
			and time > 0 and time < 1326968377 group by userid)
		  and userid in (
			select u.id
			from mdl_assignment a, mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r
			where a.id = 388 and a.course = x.instanceid and x.id = ra.contextid and
			u.id = ra.userid and ra.roleid = r.id and r.name = 'Student')
		  group by userid having count(1) = 1;
		 *** version 2 
		  select (UNIX_TIMESTAMP()-time), mdl_log.* from mdl_log 
		  where course = 468 and module = 'assignment' and action = 'view'
		  and info = '209' and userid in
		      (select userid from mdl_log where course = 468 and module = 'assignment' and action = 'view' 
		       and info = '209' and time > UNIX_TIMESTAMP()-6000
		       group by userid) 
		  and userid in
			  (select ra.userid from mdl_assignment a, mdl_context x, mdl_role_assignments ra, mdl_role r 
			  where a.id = 209 and a.course = x.instanceid and x.id = ra.contextid and 
			  ra.roleid = r.id and r.name = 'Student proactiv')
		  group by userid having count(1) = 1; 
		 */
 		String q = "select userid from mdl_log ";
		q += "where course = " + course_ID + " and module = 'assignment' and action = 'view' and info = '" + assignment_ID + "' ";
		q += "and userid in ";
		q += "  (select userid  from mdl_log where course = " + course_ID + " and module = 'assignment' and action = 'view' "; 
		q += "   and info = '" + assignment_ID + "' and time > " + previousAccess + " and time <= " + thisAccess + " group by userid) ";
		q += "and userid in ";
		q += "  (select ra.userid from mdl_assignment a, mdl_context x, mdl_role_assignments ra, mdl_role r "; 
		q += "   where a.id = " + assignment_ID + " and a.course = x.instanceid and x.id = ra.contextid and "; 
		q += "   ra.roleid = r.id and r.name = '"+ roleNameStudent + "') ";
		q += "group by userid having count(1) = 1";
		
		return mso.getArrayFromSelect(conn, q);
	}

	/* **********************************************************************************
	 * **********************************************************************************
	 * **********************************************************************************
	 * TEST Methods and fields, come here
	 * **********************************************************************************
	 * **********************************************************************************
	 * **********************************************************************************
	 */

	@Override
	public String testComputing (final int n) {
		double res = 0.0;
		for (int i=0; i<n*100000; i++) {
			res += Math.PI*Math.E; // just to implement a long algorithm
		}
		// also insert in DB
		testInsertNumTable (n, res);
		return String.valueOf(res);
	}

	@Override
	public void testConnectionInsert (final String value) {
		final String q = "INSERT INTO `PAM`.`UsersOnline` (`userid`, `online`, `time`) VALUES (1, 0, 9999);";
		mso.executeUpdate(conn, q);
	}

	@Override
	public void testConnectionRead () {
		try {
			final ResultSet rs =  mso.getArrayFromSelect(conn, "Select * from mdl_user");
			while (rs.next()) {
				final String nome = rs.getString("username"); // column name
				Global.logger.info(nome);
			}
		} catch (final Exception e) {
			Global.logger.severe("Exception Msg: "+ e.getMessage());
		}		
	}	

	/**
	 * 
	 */
	public void testConnectionRead2 () {
		try {
			final ResultSet rs =  mso.getArrayFromSelect(conn, "Select * from mdl_user where  id < 50");
			while (rs.next()) {
				final String nome = rs.getString("username"); // column name
				Global.logger.info(nome);
			}
			rs.close();
		} catch (final Exception e) {
			Global.logger.severe("Exception Msg: "+ e.getMessage());
		}		
	}	

	/**
	 * 
	 */
	public void testConnectionRead3 () {
		try {
			final ResultSet rs =  mso.getArrayFromSelect(conn, "Select * from PAM.SystemParameters");
			while (rs.next()) {
				final long nome = rs.getLong("F"); // column name
				Global.logger.info("Param F = "+ nome);
			}
			rs.close();
		} catch (final Exception e) {
			Global.logger.severe("Exception Msg: "+ e.getMessage());
		}		
	}	

	/**
	 * @param course_ID
	 * @return array
	 */
	public ResultSet testgetStudentsFromCourse(final long course_ID) {
		String q = "select u.id, u.firstname, u.lastname, r.name ";
		q += "from mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r "; 
		q += "where x.instanceid = " + course_ID + " and x.id = ra.contextid and ";
		q += "u.id = ra.userid and ra.roleid = r.id and (r.name = 'student' or r.name = 'Student proactiv')";
		return mso.getArrayFromSelect(conn, q);
	}
		@Override
	public void testDeleteNumTable () {
		final String q = "Delete from testcalc";
		mso.executeUpdate(conn, q);
	}

	@Override
	public void testGetAllNumTable(){
		try {
			Global.logger.info("testcalc table content START:");
			final ResultSet rs =  mso.getArrayFromSelect(conn, "Select * from testcalc");
			while (rs.next()) {
				final int id = rs.getInt("id");
				final int num = rs.getInt("num"); // column name
				final double res = rs.getDouble("result"); // column name
				Global.logger.info("[" + id + "] " + num + " = " + res );
			}
			Global.logger.info("testcalc table content END");
			rs.close();
		} catch (final Exception e) {
			Global.logger.severe("Exception Msg: "+ e.getMessage());
		}
	}

	/**
	 * Test method to insert one record into the testcalc table 
	 * @param n
	 * @param value 
	 */
	public void testInsertNumTable (final int n, final double value) {
		String q = "";
		q = "Insert into testcalc(num, result)";
		q+= "values(" + n + ", " + value + ")";
		mso.executeUpdate(conn, q);
	}
	
	/* **********************************************************************************
	 * Added by ReMuS
	 * **********************************************************************************
	 * common methods
	 * **********************************************************************************
	 */
	
	@Override
	public void insertIntoContextTable(final String type, final long ID){
		String q = "";
		if(type.equals("category"))
			q += "INSERT INTO mdl_context (contextlevel,instanceid,depth) VALUES('40','"+ ID +"','2')";
		else if(type.equals("course"))
			q += "INSERT INTO mdl_context (contextlevel,instanceid,depth) VALUES('50','"+ ID +"','3')";
		else if(type.equals("blockinstance"))
			q += "INSERT INTO mdl_context (contextlevel,instanceid,depth) VALUES('80','"+ ID +"','4')";
		else if(type.equals("forum") || type.equals("chat") || type.equals("pdf") || type.equals("folder"))
			q += "INSERT INTO mdl_context (contextlevel,instanceid,depth) VALUES('70','"+ ID +"','4')";

		mso.executeUpdate(conn, q);
	}
	
	@Override
	public long getContextID(final long instanceid, final int depth){
		final String q = "SELECT id FROM mdl_context WHERE instanceid = '"+instanceid+"' AND depth = '"+depth+"' ";
		return mso.getLongFromSelect(conn, q);
	}
	
	@Override
	public void updateContext(final String path, final long context_ID){
		final String q = "UPDATE mdl_context SET path = '"+path+"' WHERE id = '"+context_ID+"'";

		mso.executeUpdate(conn, q);
	}
	
	@Override
	public void insertCacheFlags(final String name){
		final String q = "INSERT INTO mdl_cache_flags (flagtype,name,value,expiry,timemodified) VALUES('accesslib/dirtycontexts','"+name+"','1',UNIX_TIMESTAMP()+3600,UNIX_TIMESTAMP())";

		mso.executeUpdate(conn, q);
	}
	
	@Override
	public long getIDFromGroupingName(final String groupingName){
		final String q = "SELECT * FROM mdl_groupings WHERE name = '"+groupingName+"'";
		return mso.getLongFromSelect(conn, q);
	}
	
	@Override
	public long getLastId(){
		final String q = "SELECT LAST_INSERT_ID()";
		return mso.getLongFromSelect(conn, q);
	}
	
	@Override
	public String getContextPath(final long context_ID, final int depth){
		final String q = "SELECT path FROM mdl_context WHERE instanceid = '"+context_ID+"' AND depth = " + depth;
		return mso.getStringFromSelect(conn, q);
	}
	
	@Override
	public void insertIntoCourseModules(final long type, final long course_ID, final long grouping_ID){
		
		final String q = "INSERT INTO mdl_course_modules " +
				"(course,module,instance,visible,groupmode,groupingid,groupmembersonly,added) " +
				"VALUES("+course_ID+" ,"+ type +",'0','1','0',"+grouping_ID+",'1', UNIX_TIMESTAMP())";				
		mso.executeUpdate(conn, q);
	}
	
	/*
	 * **********************************************************************************
	 * S001 methods
	 * **********************************************************************************
	 * Category Creation
	 * **********************************************************************************
	 */
	
	@Override
	public boolean courseExist(final String courseShortName, final String courseName){
		final String q = "SELECT * FROM mdl_course WHERE shortname = '"+courseShortName+"' AND fullname = '"+courseName+"'";
		return mso.getBooleanFromSelect(conn, q);
	}
	
	@Override
	public void createCourseCategory(final String categoryName, final long sortorder){
		String q = "";
		q = "INSERT INTO mdl_course_categories (name,parent,description,sortorder) VALUES('"+ categoryName +"','0','','"+ sortorder +"')";
		mso.executeUpdate(conn, q);
	}
		
	@Override
	public long getCategoryIDFromCategoryName(final String categoryName){
		final String q = "SELECT id FROM mdl_course_categories WHERE name = '"+categoryName+"'";
		return mso.getLongFromSelect(conn, q);
	}
	
	@Override
	public void updateCourseCategory(final long category_ID){
		final String q = "UPDATE mdl_course_categories SET path = '/"+category_ID+"' WHERE id = '"+category_ID+"'";
		mso.executeUpdate(conn, q);
	}

	/*
	 * **********************************************************************************
	 * Course Creation
	 * **********************************************************************************
	 */
	
	@Override
	public void createCourse(final String courseFullName, final String courseShortName, final long categorySortorder, final long categoryID, final long startdate){
		String q = "INSERT INTO mdl_course (category,fullname,shortname,idnumber,format,numsections,startdate,hiddensections,newsitems,showgrades," +
				"showreports,maxbytes,groupmode,groupmodeforce,defaultgroupingid,visible,lang,enablecompletion,completionstartonenrol," +
				"restrictmodules,timecreated,timemodified,sortorder,summary,visibleold)";
		q += "VALUES('"+categoryID+"','"+courseFullName+"','"+courseShortName+"','','topics','3','"+startdate+"','0','0','1',";
		q += "'0','2097152','1','1','0','1','','0','0',";
		q += "'0',UNIX_TIMESTAMP(),UNIX_TIMESTAMP(),'"+categorySortorder+"','','1')";
		mso.executeUpdate(conn, q);
	}
	
	@Override
	public long getCourseIdFromName(final String courseFullName){
		final String q = "SELECT id FROM mdl_course WHERE fullname = '"+courseFullName+"'";
		return mso.getLongFromSelect(conn, q);
	}
	
	@Override
	public void insertBlockInstance(final String blockname, final long parentcontext_ID, final int defaultweight){
		final String q = "INSERT INTO mdl_block_instances (blockname,parentcontextid,showinsubcontexts,pagetypepattern,subpagepattern,defaultregion,defaultweight,configdata) " +
											"VALUES ( '"+blockname+"', "+parentcontext_ID+", 0, 'course-view-*',NULL,'side-post', "+defaultweight+", '')";
		mso.executeUpdate(conn, q);
	}
	
	@Override
	public long getBlockInstanceID(final String blockname, final long parentcontext_ID){
		final String q = "SELECT id FROM mdl_block_instances WHERE blockname = '"+blockname+"' AND parentcontextid = '"+parentcontext_ID+"'";
		return mso.getLongFromSelect(conn, q);
	}
	
	@Override
	public void createCourseSection(final long course_ID, final String courseDescription, final String sectionStudyGroupDescription, final String sectionCountryDescription, final String sectionCityDescription){
		String q = "";
		q += "INSERT INTO mdl_course_sections (course, section, summary, summaryformat, sequence) VALUES ("+course_ID + ", 0, '"+courseDescription+"', 1 , 0)";
		mso.executeUpdate(conn, q);
		
		q = "INSERT INTO mdl_course_sections (course, section, summary, summaryformat, sequence) VALUES ("+course_ID + ", 1, '"+ sectionStudyGroupDescription +"', 1, 0 )";
		mso.executeUpdate(conn, q);
		
		q = "INSERT INTO mdl_course_sections (course, section, summary, summaryformat, sequence) VALUES ("+course_ID + ", 2, '"+ sectionCountryDescription +"', 1, 0 )";
		mso.executeUpdate(conn, q);
		
		q = "INSERT INTO mdl_course_sections (course, section, summary, summaryformat, sequence) VALUES ("+course_ID + ", 3, '"+ sectionCityDescription +"', 1, 0 )";
		mso.executeUpdate(conn, q);
	}
	
	@Override
	public void insertEnrolMethods(final long course_ID){
		String q = "";
		q += "INSERT INTO mdl_enrol (enrol,status,courseid,enrolstartdate,enrolenddate,timemodified,timecreated,sortorder,enrolperiod,roleid) VALUES('manual','0','"+course_ID+"','0','0',UNIX_TIMESTAMP(),UNIX_TIMESTAMP(),'0','0','5')";
		mso.executeUpdate(conn, q);	
		
		q= "INSERT INTO mdl_enrol (enrol,status,courseid,enrolstartdate,enrolenddate,timemodified,timecreated,sortorder) VALUES('guest','1','"+course_ID+"','0','0',UNIX_TIMESTAMP(),UNIX_TIMESTAMP(),'1')";
		mso.executeUpdate(conn, q);
		
		q= "INSERT INTO mdl_enrol (enrol,status,courseid,enrolstartdate,enrolenddate,timemodified,timecreated,sortorder,customint1,customint2,customint3,customint4,enrolperiod,roleid) VALUES('self','1','"+course_ID+"','0','0',UNIX_TIMESTAMP(),UNIX_TIMESTAMP(),'2','0','15552000','0','1','0','5')";
		mso.executeUpdate(conn, q);
	}
	
	@Override
	public void updateFirstModInfo(final long course_ID){
		String q = "";
		q += "UPDATE mdl_course SET modinfo = 'a:0:{}' WHERE id = " + course_ID;
		mso.executeUpdate(conn, q);
	}
	
	@Override
	public String getGroupName(final long group_ID){		
		final String q = "SELECT name FROM mdl_groups WHERE id = " + group_ID ;
		return mso.getStringFromSelect(conn, q);
	}
	
	/*
	 * **********************************************************************************
	 * S002 methods
	 * **********************************************************************************
	 */

	@Override
	public void createEnrolmentMethodForCourse(final long course_ID){
		final String q = "INSERT INTO mdl_enrol " +
				"VALUES ('manual',1,"+course_ID+",0,NULL,0,0,0,0,0,0,NULL,NULL,NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,UNIX_TIMESTAMP(),UNIX_TIMESTAMP())";
		mso.executeUpdate(conn, q);
	}

	@Override
	public long getEnrolmentIDFromCourseID(final long course_ID){
		final String q = "SELECT id FROM mdl_enrol WHERE courseid = '"+course_ID+"' AND enrol = 'manual'";
		return mso.getLongFromSelect(conn, q);
	}

	@Override
	public void userEnrolments(final long enrol_ID){
		final String q = "INSERT INTO mdl_user_enrolments (enrolid,status,userid,timestart,timeend,timecreated,timemodified)"+ 
						"SELECT DISTINCT "+ enrol_ID +",'0', u.id, '1352674800','0', UNIX_TIMESTAMP(), UNIX_TIMESTAMP() from `mdl_user` u, mdl_role_assignments ra " +
						"where u.id not in ( " +
							"select distinct u.id from mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r " +
							"where x.id = ra.contextid and u.id = ra.userid and ra.roleid = r.id and (r.name <> 'Student proactiv' and r.name <> 'Student')" +
						") and u.id <> 1 and u.id = userid and u.lastaccess >= 1347685200 ";
		mso.executeUpdate(conn, q);
	}

	@Override
	public void roleAssignments(final long context_ID){
		final String q = "INSERT INTO mdl_role_assignments (roleid, contextid, userid, timemodified, modifierid) " +
						"SELECT DISTINCT 5, "+ context_ID +", u.id, UNIX_TIMESTAMP(), 2 " +
						"FROM `mdl_user` u, mdl_role_assignments ra " +
						"WHERE u.id not in (" +
							"SELECT DISTINCT u.id FROM mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r " +
							"WHERE x.id = ra.contextid AND u.id = ra.userid AND ra.roleid = r.id AND (r.name <> 'Student proactiv' AND r.name <> 'Student')" +
						") " +
						"AND u.id <> 1 AND u.id = userid AND u.lastaccess >= 1347685200;";
		mso.executeUpdate(conn, q);
	}

	/*
	 * **********************************************************************************
	 * S101 methods
	 * **********************************************************************************
	 */
	
	@Override
	public ResultSet getAllCategories(final int parent){
		final String q = "SELECT * FROM mdl_course_categories WHERE `name` not like '%Master en Management%' AND (`name` like '%BASV%' OR `name` like '%BASI%' OR `name` like '%BING%' OR `name` like '%Bachelor en%' OR `name` like '%Master %')";
		// String q = "SELECT * FROM mdl_course_categories WHERE parent = "+ parent + " AND `name` not like '%Thesis%' AND `name` like '%BASV%' OR `name` like '%BASI%' OR `name` like '%BING%' OR `name` like '%Bachelor en%' OR `name` like '%Master %'";
		// String q = "SELECT * FROM mdl_course_categories WHERE parent = "+ parent + " AND `name` not like '%Thesis%' AND `name` like '%Bachelor%' OR `name` like '%Master%'"; 
		return mso.getArrayFromSelect(conn, q);
	}
	
	/*
	 * **********************************************************************************
	 * S102 methods
	 * **********************************************************************************
	 */
	
	@Override
	public boolean groupExist(final long course_ID, final String groupName){
		final String q = "SELECT * FROM mdl_groups WHERE name = '"+ groupName + "' AND courseid = " + course_ID;
		return mso.getBooleanFromSelect(conn, q);
	}
	
	@Override
	public boolean groupingExist(final long course_ID, final String groupingName){
		final String q = "SELECT * FROM mdl_groupings WHERE name = '"+ groupingName + "' AND courseid = " + course_ID;
		return mso.getBooleanFromSelect(conn, q);
	}
	
	@Override
	public boolean groupIsInGrouping(final long group_ID, final long grouping_ID){
		final String q = "SELECT * FROM mdl_groupings_groups WHERE groupingid = "+ grouping_ID +" AND groupid = " + group_ID;
		return mso.getBooleanFromSelect(conn, q);
	}
	
	@Override
	public void createGroup(final long course_ID, final String groupName){
		final String q = "INSERT INTO mdl_groups (name,enrolmentkey,hidepicture,courseid,timecreated,timemodified,description,descriptionformat) " +
				"VALUES('"+groupName+"','','0','"+course_ID+"',UNIX_TIMESTAMP(), UNIX_TIMESTAMP()," +
				"'<p>This is the group of people inscribed in the "+groupName+".</p>','1')";
		mso.executeUpdate(conn, q);
	}

	@Override
	public void createGrouping(final long course_ID, final String groupingName){
		final String q = "INSERT INTO mdl_groupings (name,courseid,timecreated,timemodified,description,descriptionformat) " +
				"VALUES('"+groupingName+"',"+course_ID+",UNIX_TIMESTAMP(), UNIX_TIMESTAMP()," +
				"'<p>This is the group of people inscribed in the "+groupingName+".</p>','1')";
		mso.executeUpdate(conn, q);
	}

	@Override
	public void insertGroupIntoGrouping(final long group_ID, final long grouping_ID){
		final String q = "INSERT INTO mdl_groupings_groups (groupingid,groupid,timeadded) VALUES("+grouping_ID+", "+group_ID+",UNIX_TIMESTAMP())";
		mso.executeUpdate(conn, q);
	}

	@Override
	public long getGroupID(final String groupName){
		final String q = "SELECT id FROM mdl_groups WHERE name = '"+groupName+"'";
		return mso.getLongFromSelect(conn, q);
	}	

	/*
	 * **********************************************************************************
	 * S103 methods
	 * **********************************************************************************
	 */

	@Override
	public void inscribeUsersIntoGroup(final long course_ID, final long group_ID, final long category_ID){
		final String[] set = new String[2];
		if (!mso.startTransaction(conn))
			return;

		// TEST this properly: this code was changed, but not really tested, since it was the previous version that was executed! 
		String q = "INSERT INTO PAM.Log (user_id, instance_id, action, time) " + 
			"SELECT DISTINCT u.id, " + group_ID + ", 'enroll in group', UNIX_TIMESTAMP() " +
			"FROM mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r, mdl_course c, mdl_course_categories cc WHERE u.id in ( " +
			"SELECT ra.userid FROM mdl_context x, mdl_role_assignments ra " +
			"WHERE x.instanceid = "+ course_ID +" AND x.id = ra.contextid ) AND x.instanceid = c.id AND x.id = ra.contextid AND u.id = ra.userid " +
			"AND ra.roleid = r.id AND (r.name = 'Student' OR r.name = 'Student proactiv') AND c.category = cc.id AND cc.parent = " + category_ID;
		set[0]=q;

		q = "INSERT INTO mdl_groups_members (groupid, userid, timeadded) SELECT DISTINCT " + group_ID + ", u.id, UNIX_TIMESTAMP() " +
			"FROM mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r, mdl_course c, mdl_course_categories cc WHERE u.id in ( " +
			"SELECT ra.userid FROM mdl_context x, mdl_role_assignments ra " +
			"WHERE x.instanceid = "+ course_ID +" AND x.id = ra.contextid ) AND x.instanceid = c.id AND x.id = ra.contextid AND u.id = ra.userid " +
			"AND ra.roleid = r.id AND (r.name = 'Student' OR r.name = 'Student proactiv') AND c.category = cc.id AND cc.parent = " + category_ID;
//		mso.executeUpdate(conn, q);
		set[1]=q;

		// commit and rollback are managed in this call
		mso.executeBatch(conn, set);
	}

	@Override
	public void inscribeUserIntoGroup_2(final long group_ID, final long user_ID){
		final String[] set = new String[2];
		if (!mso.startTransaction(conn))
			return;
		
		// TEST this properly: this code was changed, but not really tested, since it was the previous version that was executed! 
		String q = "INSERT INTO PAM.Log (user_id, instance_id, action, time) VALUES ("+ user_ID +", "+ group_ID +", 'enroll in group', UNIX_TIMESTAMP())";
		set[0]=q;

		q = "INSERT INTO mdl_groups_members (groupid, userid, timeadded) VALUES ("+ group_ID +","+ user_ID +", UNIX_TIMESTAMP())";
//		mso.executeUpdate(conn, q);
		set[1]=q;

		// commit and rollback are managed in this call
		mso.executeBatch(conn, set);
	}
	
	/*
	 * **********************************************************************************
	 * S104 methods
	 * **********************************************************************************
	 */
	
	@Override
	public void insertIntoForum(final String name, final String type, final long course_ID, final String forum_description){
		final String q = "INSERT INTO mdl_forum (name,type,forcesubscribe,trackingtype,maxbytes,maxattachments,rsstype,rssarticles,blockperiod," +
				"blockafter,warnafter,assessed,assesstimestart,assesstimefinish,course,completiondiscussions,completionreplies,completionposts," +
				"intro,introformat,timemodified) " +
				"VALUES('"+ name +"','" + type + "','0','1','2097152','9','0','0','0','0','0','0','0','0', " +course_ID+ ",'0','0','0'," +
				"'"+ forum_description +"','1',UNIX_TIMESTAMP())";

		mso.executeUpdate(conn, q);
	}
	
	@Override
	public long getCourseSectionID(final long course_ID, final int section){
		final String q = "SELECT id FROM mdl_course_sections WHERE course = "+course_ID+" AND section = " + section;
		return mso.getLongFromSelect(conn, q);
	}
		
	@Override
	public String getCourseModinfo(final long course_ID){
		final String q = "SELECT modinfo FROM mdl_course WHERE id = " + course_ID;
		return mso.getStringFromSelect(conn, q);
	}
	
	@Override
	public void updateCourseModinfo(final String modinfo, final long course_ID){
		Global.logger.finest("[method not needed in cache - updateCourseModinfo] attributes: " + modinfo);
		final String q = "UPDATE mdl_course SET modinfo = '" + modinfo + "' WHERE id = " + course_ID;

		mso.executeUpdate(conn, q);
	}
	
	@Override
	public void updateCourseModule(final long id, final long course_section_ID, final long course_modules_ID){
		final String q = "UPDATE mdl_course_modules SET instance = " + id + ", section = " + course_section_ID + " WHERE id = " + course_modules_ID;

		mso.executeUpdate(conn, q);
	}
	
	@Override
	public void updateCourseSection(final String sequence, final long course_section_ID){
		final String q = "UPDATE mdl_course_sections SET sequence = CONCAT (sequence, '" + sequence + "') WHERE id = " + course_section_ID;

		mso.executeUpdate(conn, q);
	}
	
	@Override
	public void insertIntoChat(final String name, final long course_ID, final String chat_description){		
		final String q = "INSERT INTO mdl_chat (name,chattime,schedule,keepdays,studentlogs,course,intro,introformat,timemodified) " +
				"VALUES('"+ name +"', UNIX_TIMESTAMP(),'0','0','1', " +course_ID+ ","+"'"+ chat_description +"','1','1354534346')"; 
		mso.executeUpdate(conn, q);
	}
	
	@Override
	public void insertIntoEvent(final long chat_ID, final String chatName, final long course_ID, final String chat_description){
		final String q = "INSERT INTO mdl_event (eventtype,userid,description,format,name,courseid,groupid,modulename,instance,timestart,timeduration,timemodified) " +
				"VALUES('chattime','0','"+chat_description+"','1','"+chatName+"',"+course_ID+",'0','chat',"+chat_ID +", UNIX_TIMESTAMP(),'0',UNIX_TIMESTAMP())"; 
		mso.executeUpdate(conn, q);
	}
		
	@Override
	public void insertIntoFolder(final String name, final long course_ID, final String folder_description){
		final String q = "INSERT INTO mdl_folder (name,course,revision,intro,introformat,timemodified)  " +
				"VALUES('"+ name +"'," +course_ID+ ", 1,'"+ folder_description +"','1', UNIX_TIMESTAMP())"; 
		mso.executeUpdate(conn, q);
	}
		
	@Override
	public void addForumPost(final long admin_ID, final String subject, final String message){
		final String q = "INSERT INTO mdl_forum_posts (discussion,parent,userid,created,modified,mailed,subject,message,messageformat,messagetrust,mailnow) " +
				"VALUES('0','0',"+admin_ID+",UNIX_TIMESTAMP(), UNIX_TIMESTAMP(),'0','"+subject+"', '"+message+"','1','0','0')"; 
		mso.executeUpdate(conn, q);
	}
	
	@Override
	public void addForumDiscussion(final long course_ID, final long forum_ID, final long group_ID, final String subject, final long forum_post_ID, final long admin_ID){
		final String q = "INSERT INTO mdl_forum_discussions (timestart,timeend,course,forum,userid,groupid,name,firstpost,timemodified,usermodified) " +
				"VALUES('0','0',"+course_ID+","+forum_ID+","+admin_ID+","+ group_ID +",'"+subject+"',"+forum_post_ID+", UNIX_TIMESTAMP(),"+admin_ID+")"; 
		mso.executeUpdate(conn, q);
	}
	
	@Override
	public void updateForumPost(final long forum_post_ID, final long forum_discussion_ID){
		final String q = "UPDATE mdl_forum_posts SET discussion = "+forum_discussion_ID+" WHERE id = " + forum_post_ID; 
		mso.executeUpdate(conn, q);
	}

	@Override
	public void insertIntoLog(final long user_ID, final long course_ID, final long cmid, final String info) {
		String q2;
		final String q = "INSERT INTO mdl_log (time, userid, course, ip, module, cmid, action, url, info) ";

		q2 = q + "VALUES (UNIX_TIMESTAMP(), "+user_ID+","+course_ID+", ' ','course','0','add mod','../mod/chat/view.php?id="+cmid+"','"+info+"')";
		//System.out.println(q2);
		mso.executeUpdate(conn, q2);

		q2 = q + "VALUES (UNIX_TIMESTAMP(), "+user_ID+","+course_ID+", ' ','"+info+"',"+cmid+",'add','view.php?id="+cmid+"','"+info+"')";
		//System.out.println(q2);
		mso.executeUpdate(conn, q2);

		q2 = q + "VALUES (UNIX_TIMESTAMP(), "+user_ID+","+course_ID+", ' ','"+info+"',"+cmid+",'view "+info+"','view.php?id="+cmid+"','"+info+"')";
		//System.out.println(q2);
		mso.executeUpdate(conn, q2);
	}
	
	@Override
	public long getForumID(){
		final String q = "select id from mdl_modules where name = 'forum'";
		return mso.getLongFromSelect(conn, q);
	}
	
	@Override
	public long getChatID(){
		final String q = "select id from mdl_modules where name = 'chat'";
		return mso.getLongFromSelect(conn, q);
	}
	
	@Override
	public long getFolderID(){
		final String q = "select id from mdl_modules where name = 'folder'";
		return mso.getLongFromSelect(conn, q);
	}
	
	/*
	 * **********************************************************************************
	 * S105 methods
	 * **********************************************************************************
	 */
	
	@Override
	public ResultSet getStudentsFromGroup(final long group_ID){
		final String q = "SELECT userid FROM mdl_groups_members WHERE groupid = "+ group_ID;
		return mso.getArrayFromSelect(conn, q);
	}

	/*
	 * **********************************************************************************
	 * S301 methods
	 * **********************************************************************************
	 */
	
	@Override
	public boolean isUserInGroup(final long user_ID, final long group_ID){
		final String q = "SELECT id FROM mdl_groups_members WHERE groupid = "+ group_ID+" AND userid = " + user_ID;
		return mso.getBooleanFromSelect(conn, q);
	}
	
	/*
	 * **********************************************************************************
	 * M301 methods
	 * **********************************************************************************
	 */
	
	@Override
	public String getCity(final long user_ID){
		final String q = "SELECT city FROM mdl_user WHERE userid = "+ user_ID;
		return mso.getStringFromSelect(conn, q);
	}
	
	/*
	 * **********************************************************************************
	 * S201 methods
	 * **********************************************************************************
	 */
	
	@Override
	public void insertIntoResources(final String fileName, final long course_ID, final String fileDescription, final String displayOptions){
		// INSERT INTO mdl_resource (name,display,filterfiles,course,revision,intro,introformat,timemodified,displayoptions) 
		// VALUES('SecondPdf','0','0','1539','1','<p>Bravo !</p>','1',UNIX_TIMESTAMP(),'a:2:{s:12:\"printheading\";i:0;s:10:\"printintro\";i:1;}');
		final String q = "INSERT INTO mdl_resource (name,display,filterfiles,course,revision,intro,introformat,timemodified,displayoptions) " +
				"VALUES('"+ fileName +"','0','0',"+ course_ID +",'1','"+ fileDescription +"','1',UNIX_TIMESTAMP(),'"+ displayOptions +"')"; 
		mso.executeUpdate(conn, q);
	}
	
	@Override
	public long getAdminUserID() {
		final String q = "SELECT id FROM mdl_user WHERE username = 'admin'";
		return mso.getLongFromSelect(conn, q);
	}
	
	@Override
	public long getActivityLevel(final String groupName, final long begin_time, final long end_time){
		final String q = "select count(1) from mdl_log where course = (select id from mdl_course where shortname = 'cops') and time < "+end_time+" and time > "+begin_time+" and " +
				"cmid in (select id from mdl_course_modules where course = (select id from mdl_course where shortname = 'cops') " +
				"and groupingid = (select id from mdl_groupings where name = '"+groupName+"'))";
		return mso.getLongFromSelect(conn, q);
	}
	
	@Override
	public long getNewMembers(final long group_ID, final long begin_time, final long end_time){
		final String q = "select count(1) from PAM.Question_User where answer_time < "+end_time+" and answer_time > "+begin_time+"  and joined = 1 and user_id in (select userid from mdl_groups_members where groupid = "+group_ID+")";
		return mso.getLongFromSelect(conn, q);
	}

	@Override
	public void insertIntoFiles(final String type, final String contenthash, final String pathnamehash, final long pdf_context_ID, final String pdfFileName, final long pdfFileSize){
		String q = "INSERT INTO mdl_files (contenthash,pathnamehash,contextid,component,filearea,itemid,filepath,filename,userid,filesize,mimetype,status,source,author,license,timecreated,timemodified,sortorder) ";
		//String q="INSERT INTO mdl_files (contextid,component,filearea,itemid,filepath,filename,timecreated,timemodified,mimetype,userid,source,author,license,sortorder,filesize,contenthash,pathnamehash)";
		//String q="INSERT INTO mdl_files (contextid,component,filearea,itemid,filepath,filename,contenthash,filesize,timecreated,timemodified,mimetype,userid,pathnamehash)";
		
		//String q = "INSERT INTO mdl_files (contenthash,pathnamehash,contextid,component,filearea,itemid,filepath,filename,userid,filesize,mimetype,status,source,author,license,timecreated,timemodified,sortorder) ";
		
		
		//String q = "INSERT INTO mdl_files (contenthash,pathnamehash,contextid,component,filearea,itemid,filepath,filename,userid,filesize,mimetype,status,source,author,license,timecreated,timemodified,sortorder) ";
		//							VALUES  (  ....     , ....       ,    3    , 'user'  , 'draft',' .... ', '/','Thionville.pdf', '2', '31017', 'application/pdf', '0',NULL,'Admin User','public',UNIX_TIMESTAMP(),UNIX_TIMESTAMP(),'0'  
		
		/*
		 * 
		INSERT INTO mdl_files (contextid,component,filearea,itemid,filepath,filename,timecreated,timemodified,mimetype,userid,source,author,license,sortorder,filesize,contenthash,pathnamehash) 
						VALUES('3','user','draft','187579095','/','Thionville.pdf','1365420887','1365420887','application/pdf','2',NULL,'Admin User','allrightsreserved','0','31017','98aca81c47a203b5980f8ad25d96379127cc4641','ddf3456fe58f8162b219424da21681a923bea3cf')
		
		INSERT INTO mdl_files (contextid,component,filearea,itemid,filepath,filename,contenthash,filesize,timecreated,timemodified,mimetype,userid,pathnamehash) 
						VALUES('3','user','draft','187579095','/','.','da39a3ee5e6b4b0d3255bfef95601890afd80709','0','1365420888','1365420888',NULL,'2','56d733d5eb850398ed806fae6e1102554936e6c3')
		 */
		
		if (type.equals("pdf")) 
			q += "VALUES('"+ contenthash +"','"+ pathnamehash +"',"+ pdf_context_ID +",'mod_resource','content','0','/','"+ pdfFileName +"','2',"+ pdfFileSize +",'application/pdf','0',NULL,'Admin User','public',UNIX_TIMESTAMP(),UNIX_TIMESTAMP(),'0')";
		else if(type.equals("empty"))
			q += "VALUES('"+ contenthash +"','"+ pathnamehash +"',"+ pdf_context_ID +",'mod_resource','content','0','/','"+ pdfFileName +"','2',"+ pdfFileSize +",NULL,'0',NULL,NULL,NULL,UNIX_TIMESTAMP(),UNIX_TIMESTAMP(),'0')";
		/*
		else if (type.equals("draft"))
			q += "VALUES('"+ contenthash +"','"+ pathnamehash +"', "+ pdf_context_ID +" ,'user','draft',"+item_ID+",'/','"+ pdfFileName +"','2',"+ pdfFileSize +",'application/pdf','0',NULL,'Admin User','allrightsreserved',UNIX_TIMESTAMP(),UNIX_TIMESTAMP(),'0')";
		else if (type.equals("draftEmpty"))
			q += "VALUES('"+ contenthash +"','"+ pathnamehash +"', "+ pdf_context_ID +" ,'user','draft',"+item_ID+",'/','"+ pdfFileName +"','2',"+ pdfFileSize +",NULL,'0',NULL,NULL,NULL,UNIX_TIMESTAMP(),UNIX_TIMESTAMP(),'0')";
		*/

		mso.executeUpdate(conn, q);
	}

	/* **********************************************************************************
	 * Groups TABLEs
	 * Queries that execute commands from the user interface
	 * **********************************************************************************
	 */
	@Override
	public ResultSet getGroupsFromUser(final long user_ID) {
		final String q = "select mg.name group_name , mg.id group_id, pg.activity from mdl_groups mg, PAM.groups pg where mg.courseid = (select id from mdl_course where shortname = 'cops')  and mg.id in (select groupid from mdl_groups_members where userid = "+user_ID+") and mg.name = pg.group_name";
		return mso.getArrayFromSelect(conn, q);
	}

	/**
	 * Removes a user from a certain group
	 */
	@Override
	public void leaveGroup(final long user_ID, final long group_ID) {
		final String q = "delete from mdl_groups_members where userid = "+user_ID+" and groupid = "+group_ID;
		mso.executeUpdate(conn, q);
	}

	/*
	 * **********************************************************************************
	 * M202 methods
	 * **********************************************************************************
	 */
	
	@Override
	public ResultSet getAllActiveGroups(final long time) {
		final String q ="select mg.name, count(1) actions from mdl_groupings mg, mdl_log ml, mdl_course_modules cm where mg.id = cm.groupingid and ml.course = (select id from mdl_course where shortname = 'cops') and ml.time > (UNIX_TIMESTAMP() - "+time+") and ml.cmid = cm.id group by mg.name";
		//String q = "select mg.name from mdl_groupings mg where id in (select cm.groupingid from mdl_log ml, mdl_course_modules cm where ml.course = (select id from mdl_course where shortname = 'cops') and ml.time > (UNIX_TIMESTAMP() - "+time+") and ml.cmid = cm.id )";
		return mso.getArrayFromSelect(conn, q);
	}

	/*
	 * Moodle 2.5 methods
	 * 
	 */

	@Override
	public String getAssignmentName25(final long assignment_ID) {
		return mso.getStringFromSelect(conn, "mdl_proass", "id = " + assignment_ID, "name");
	}

	@Override
	public long getCourseIdFromAssignment25(final long assignment_ID) {
		return mso.getLongFromSelect(conn, "mdl_proass", "id = " + assignment_ID, "course");
	}

	@Override
	public long getAssignmentDeadline25(final long assignment_ID) {
		return mso.getLongFromSelect(conn, "mdl_proass", "id = " + assignment_ID, "duedate");
	}

	@Override
	public ResultSet getAllStudentsNotSubmitted25(final long course_ID,final long assignment_ID) {
		String q = "select u.id ";
		q += "from mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r "; 
		q += "where x.instanceid = " + course_ID + " and x.id = ra.contextid and ";
		q += "u.id = ra.userid and ra.roleid = r.id and r.shortname = 'student'";
		q += "and u.id not in (select userid from mdl_proass_submission where proassment = " + assignment_ID + ")";
		return mso.getArrayFromSelect(conn, q);
	}

	@Override
	public ResultSet getStudentsNotSubmitted25(final long course_ID,final long assignment_ID) {
		String q = "select u.id ";
		q += "from mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r "; 
		q += "where x.instanceid = " + course_ID + " and x.id = ra.contextid and ";
		q += "u.id = ra.userid and ra.roleid = r.id and r.shortname = '"+ roleNameStudent + "'";
		q += "and u.id not in (select userid from mdl_proass_submission where proassment = " + assignment_ID + ")";
		return mso.getArrayFromSelect(conn, q);
	}

	@Override
	public boolean assignmentExists25(final long assignment_ID) {
		return mso.getBooleanFromSelect(conn, "mdl_proass", "id = " + assignment_ID, "id");
	}

	@Override
	public long countSubmitted25(final long assignment_ID) {
		return mso.getLongFromSelect(conn, "mdl_proass_submission", "proassment = " + assignment_ID, "count(1)");
	}

	@Override
	public String getStringScenarioConfigParam(final long assignment_id, final String scenarioname, final String param_name) {
		final String q = "SELECT value FROM mdl_proass_plugin_config WHERE proassment = "+assignment_id+" AND plugin= '"+scenarioname+"' AND name='"+param_name+"'";
		return mso.getStringFromSelect(conn, q);
	}

	@Override
	public GregorianCalendar getDateScenarioConfigParam(final long assignment_id, final String scenarioname, final String param_name) {
		final String q = "SELECT value FROM mdl_proass_plugin_config WHERE proassment = "+assignment_id+" AND plugin= '"+scenarioname+"' AND name='"+param_name+"'";
		final String res= mso.getStringFromSelect(conn, q);
		final GregorianCalendar res2=(GregorianCalendar) GregorianCalendar.getInstance();
		res2.setTimeInMillis(Long.parseLong(res)*1000);
		return res2;
	}

	@Override
	public boolean getBooleanScenarioConfigParam(final long assignment_id, final String scenarioname, final String param_name) {
		final String q = "SELECT value FROM mdl_proass_plugin_config WHERE proassment = "+assignment_id+" AND plugin= '"+scenarioname+"' AND name='"+param_name+"'";
		final String res= mso.getStringFromSelect(conn, q);
		return res.equals("1");
	}

	@Override
	public int getIntegerScenarioConfigParam(final long assignment_id, final String scenarioname, final String param_name) {
		final String q = "SELECT value FROM mdl_proass_plugin_config WHERE proassment = "+assignment_id+" AND plugin= '"+scenarioname+"' AND name='"+param_name+"'";
		final String res= mso.getStringFromSelect(conn, q);
		return Integer.parseInt(res);
	}

	@Override
	public boolean isScenarioEnabled(final long assignment_id, final String scenarioname) {
		final String q = "SELECT value FROM mdl_proass_plugin_config WHERE proassment = "+assignment_id+" AND plugin= '"+scenarioname+"' AND name='enabled'";
		final String res= mso.getStringFromSelect(conn, q);
		return res.equals("1");
	}

	@Override
	public long getLongScenarioConfigParam(final long assignment_id, final String scenarioname, final String param_name) {
		final String q = "SELECT value FROM mdl_proass_plugin_config WHERE proassment = "+assignment_id+" AND plugin= '"+scenarioname+"' AND name='"+param_name+"'";
		final String res= mso.getStringFromSelect(conn, q);
		return Long.parseLong(res);
	}

	@Override
	public boolean hasHintsToSend(final long assignment_id, final int year, final int month, final int day) {
		final int hintnumber = getIntegerScenarioConfigParam(assignment_id, "hints", "hintnumber");
		boolean hasHintsTosend = false;
		for(int i=1; i<=hintnumber; i++){
			final GregorianCalendar gregcal = getDateScenarioConfigParam(assignment_id,"hints", "proasspaascenario_hint_datetosend"+i);
			if   (((gregcal.get(GregorianCalendar.YEAR)==year) && (gregcal.get(GregorianCalendar.MONTH)+1==month) 
				&& (gregcal.get(GregorianCalendar.DAY_OF_MONTH)==day)) && (!getBooleanScenarioConfigParam(assignment_id,"hints", "proasspaascenario_hint_sent"+i))) {
				hasHintsTosend=true;
				System.out.println("Hint found to send");
			}
		}
		return hasHintsTosend;
	}

	@Override
	public ArrayList<Long> getHintsToSend(final long assignment_id, final int year, final int month, final int day) {
		final int hintnumber = getIntegerScenarioConfigParam(assignment_id, "hints", "hintnumber");
		final ArrayList<Long> al = new ArrayList<Long>();
		for(int i=1; i<=hintnumber; i++){
			final GregorianCalendar gregcal = getDateScenarioConfigParam(assignment_id,"hints", "proasspaascenario_hint_datetosend"+i);
			if   (((gregcal.get(GregorianCalendar.YEAR)==year) && (gregcal.get(GregorianCalendar.MONTH)+1==month) 
				&& (gregcal.get(GregorianCalendar.DAY_OF_MONTH)==day)) && (!getBooleanScenarioConfigParam(assignment_id,"hints", "proasspaascenario_hint_sent"+i))) {
				al.add(Long.valueOf(i));
			}
		}
		return al;
	}

	@Override
	public long setSentToHint(final long assignment_id, final long hint_id) {
		final String q = "UPDATE mdl_proass_plugin_config SET value = 1 WHERE proassment = "+assignment_id+" AND name = 'proasspaascenario_hint_sent" +hint_id+"'";
		return mso.executeUpdate(conn, q);
	}
	
	@Override
	public long setSentToReminder(final long assignment_id) {
		final String q = "UPDATE mdl_proass_plugin_config SET value = 1 WHERE proassment = "+assignment_id+" AND name = 'remindersent'";
		return mso.executeUpdate(conn, q);
	}

	@Override
	public long countAllEnrolledStudents25(final long course_ID) {
		String q;
		if (Global.EXPERIMENTS)
			q = "select count(1)-3 "; // we have 3 extra students for testing
		else
			q = "select count(1) ";
				
		q += "from mdl_context x, mdl_user u, mdl_role_assignments ra, mdl_role r "; 
		q += "where x.instanceid = " + course_ID + " and x.id = ra.contextid and ";
		q += "u.id = ra.userid and ra.roleid = r.id and r.shortname = 'student'";
		return mso.getLongFromSelect(conn, q);
	}

	@Override
	public ResultSet getNewProactiveAssignmentsOrdered(final long lastAssignment) {
		return mso.getArrayFromSelect(conn, "mdl_proass", "id > " + lastAssignment + " order by id" , "id");
	}

	@Override
	public ResultSet getNewProactiveAssignmentsOrderedAllFields(final long lastAssignment) {
		return mso.getArrayFromSelect(conn, "mdl_proass", "id > " + lastAssignment + " order by id" , "id, course, name, intro, duedate");
	}

	@Override
	public long getAssignmentCreator25(final long course_ID, final long assignment_ID) {
		return mso.getLongFromSelect(conn, "mdl_log", "course = " + course_ID + " and module = 'proass' and action = 'add' and info = '" + assignment_ID + "'", "userid");
	}

	@Override
	public boolean isNewAssignment25(final long lastAssignment) {
		return mso.getBooleanFromSelect(conn, "mdl_proass", "id > " + lastAssignment, "IF(count(1)>0, TRUE, FALSE) as isNew ")  ;
	}
}