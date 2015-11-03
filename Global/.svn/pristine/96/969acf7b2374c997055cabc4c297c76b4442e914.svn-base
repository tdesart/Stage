package lu.uni.fstc.proactivity.db;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * A "Singleton" Object to wrap the MySql connection to Moodle<br>
 * this wrapper uses the <b>Proxy Design Pattern</b> to add Cache to the initial MoodleDbWrapper object<p>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011-2013
 *
 */
public class MoodleCacheDbWrapper extends AbstractMoodleDbWrapper {

	private static MoodleCacheDbWrapper theInstance = null;
	private final MoodleDbWrapper theMySQLInstance;

	/**
	 * The permanent cache
	 */
	private final HashMap<String, Object> stableCache;

	/**
	 * The temporary cache
	 */
	private final HashMap<String, Object> cache;

	/**
	 * MoodleCacheDbWrapper Constructor<br>
	 * <b>Private</b> to implement the "Singleton" Object 
	 * @param configFilePath the path to the config file
	 * @throws Exception 
	 */
	private MoodleCacheDbWrapper (final String configFilePath) throws Exception {
		super();
		this.theMySQLInstance = MoodleDbWrapper.getInstance(configFilePath);
		this.setConnected (this.theMySQLInstance.isConnected()); // propagate boolean connected

//		this.connParams = this.theMySQLInstance.connParams;
		stableCache = new HashMap <String, Object>();
		cache       = new HashMap <String, Object>();
		testCache   = new HashMap <Integer, String>();
	}

	/**
	 * Creates ONCE a "Singleton" Object
	 * @param configFilePath the path to the config file
	 * @throws Exception 
	 */
	public static synchronized MoodleCacheDbWrapper getInstance (final String configFilePath) throws Exception {
		if (theInstance == null) {
			theInstance = new MoodleCacheDbWrapper(configFilePath);
		}
		return theInstance;
	}

	// Clears the temporary cache
	@Override
	public void terminateOperation() {
		cache.clear();
	}

/*/	/**
	 * Every method making use of DB data, should have a copy here, with this generic structure,<br> 
	 * to serve as a PROXY that tries to use the cache first and <br>
	 * only if it does not exist in the cache it should call its corresponding method in the MoodleDbWrapper to really execute it 
	 * @param o input object 
	 * @return value from cache or execution 
	 */
/*/	public synchronized String anyMethodTemplate (Object o) {
		String s = o.toString();
		String F = (String) cache.get(s);
		if (F != null) {
			return "[in cache] " + F;
		} else {
			String f = msc.anyMethodTemplate(o);
			cache.put(s, new String(f));
			return "[not in cache] " + f;
		}
	}
/**/

	/* **********************************************************************************
	 * User TABLE
	 * **********************************************************************************
	 */
	@Override
	public boolean userExists(final long user_ID) {
		final String key = "userExists " + user_ID;
		final Boolean res = (Boolean) (cache.get(key));
		boolean res2;
		
		if (res != null) {
			res2 = res.booleanValue();
			Global.logger.finest("[in cache] key: " + key + ", value:" + res2);
		} else {
			res2 = theMySQLInstance.userExists(user_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Boolean.valueOf(res2));
		}
		return res2;
	}

	@Override
	public String getUserFullName(final long user_ID) {
		final String key = "getUserFullName " + user_ID;
		String res = (String) stableCache.get(key);
		if (res != null) {
			Global.logger.finest("[in stableCache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getUserFullName(user_ID);
			Global.logger.finest("[not in stableCache] key: " + key + ", value:" + res);
			stableCache.put(key, res);
		}
		return res;
	}

	@Override
	public long getUserIDFromEmail(final String email) {
		final String key = "getPAMAdminUserID " + email;
		final Long res = (Long) stableCache.get(key);
		long res2;
		
		if (res != null) {
			res2 = res.longValue();
			Global.logger.finest("[in cache] key: " + key + ", value:" + res2);
		} else {
			res2 = theMySQLInstance.getUserIDFromEmail(email);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			stableCache.put(key, Long.valueOf(res2));
		}
		return res2;
	}
	
	@Override
	public ResultSet getStudentsFromCourse(final long course_ID) {
		final String key = "getStudentsFromCourse " + course_ID;
		ResultSet res = (ResultSet) cache.get(key);
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getStudentsFromCourse(course_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res);
			cache.put(key, res);
		}
		return res;
	}

	@Override
	public long countAllEnrolledStudents(final long course_ID) {
		final String key = "countAllEnrolledStudents " + course_ID;
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			res2 = res.longValue();
			Global.logger.finest("[in cache] key: " + key + ", value:" + res2);
		} else {
			res2 = theMySQLInstance.countAllEnrolledStudents(course_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	// In the experiment, count all students anyway, not just the proactive students
	// Decision made in agreement with Denis S.
	@Override
	public long countEnrolledStudents(final long course_ID) {
		final String key = "countEnrolledStudents " + course_ID;
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			res2 = res.longValue();
			Global.logger.finest("[in cache] key: " + key + ", value:" + res2);
		} else {
			res2 = theMySQLInstance.countEnrolledStudents(course_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	@Override
	public ResultSet getTeachersFromCourse(final long course_ID) {
		final String key = "getTeachersFromCourse " + course_ID;
		ResultSet res = (ResultSet) stableCache.get(key);
		if (res != null) {
			Global.logger.finest("[in stableCache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getTeachersFromCourse(course_ID);
			Global.logger.finest("[not in stableCache] key: " + key + ", value:" + res);
			stableCache.put(key, res);
		}
		return res;
	}

	@Override
	public ResultSet getAllStudentsNotSubmitted(final long course_ID, final long assignment_ID) {
		final String key = "getStudentsNotSubmitted " + course_ID + " " + assignment_ID;
		ResultSet res = (ResultSet) cache.get(key);
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getAllStudentsNotSubmitted(course_ID, assignment_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res);
			cache.put(key, res);
		}
		return res;
	}

	@Override
	public ResultSet getStudentsNotSubmitted(final long course_ID, final long assignment_ID) {
		final String key = "getStudentsProactiveNotSubmitted " + course_ID + " " + assignment_ID;
		ResultSet res = (ResultSet) cache.get(key);
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getStudentsNotSubmitted(course_ID, assignment_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res);
			cache.put(key, res);
		}
		return res;
	}
	/* **********************************************************************************
	 * Course TABLE
	 * **********************************************************************************
	 */
	@Override
	public String getCourseName(final long course_ID) {
		final String key = "getCourseName " + course_ID;
		String res = (String) stableCache.get(key);
		if (res != null) {
			Global.logger.finest("[in stableCache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getCourseName(course_ID);
			Global.logger.finest("[not in stableCache] key: " + key + ", value:" + res);
			stableCache.put(key, res);
		}
		return res;
	}

	@Override
	public long getCourseStartDate(final long course_ID) {
		final String key = "getCourseStartDate " + course_ID;
		final Long res = (Long) stableCache.get(key);
		long res2;
		
		if (res != null) {
			res2 = res.longValue();
			Global.logger.finest("[in cache] key: " + key + ", value:" + res2);
		} else {
			res2 = theMySQLInstance.getCourseStartDate(course_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	@Override
	public long getCopsCourseId() {
		final String key = "getCopsCourseId";
		final Long res = (Long) stableCache.get(key);
		long res2;
		
		if (res != null) {
			res2 = res.longValue();
			Global.logger.finest("[in cache] key: " + key + ", value:" + res2);
		} else {
			res2 = theMySQLInstance.getCopsCourseId();
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	/* **********************************************************************************
	 * Forum TABLE
	 * **********************************************************************************
	 */
	@Override
	public ResultSet getForumsFromCourse(final long course_ID) {
		final String key = "getForumsFromCourse " + course_ID;
		ResultSet res = (ResultSet) cache.get(key);
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getForumsFromCourse(course_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res);
			cache.put(key, res);
		}
		return res;
	}

	/* **********************************************************************************
	 * Assignment TABLE
	 * **********************************************************************************
	 */
	@Override
	public boolean isNewAssignment(final long lastAssignment) {
		final String key = "checkDbForNewAssignment " + lastAssignment;
		final Boolean res = (Boolean) cache.get(key);
		boolean res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.booleanValue();
		} else {
			res2 = theMySQLInstance.isNewAssignment(lastAssignment);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Boolean.valueOf(res2));
		}
		return res2;
	}

	@Override
	public boolean assignmentExists(final long assignment_ID) {
		return theMySQLInstance.assignmentExists(assignment_ID);
	}
	
	@Override
	public ResultSet getNewAssignmentsOrdered(final long lastAssignment) {
		final String key = "getNewAssignmentsOrdered " + lastAssignment;
		ResultSet res = (ResultSet) cache.get(key);
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getNewAssignmentsOrdered(lastAssignment);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res);
			cache.put(key, res);
		}
		return res;
	}

	@Override
	public ResultSet getNewAssignmentsOrderedAllFields(final long lastAssignment) {
		final String key = "getNewAssignmentsOrderedAllFields " + lastAssignment;
		ResultSet res = (ResultSet) cache.get(key);
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getNewAssignmentsOrderedAllFields(lastAssignment);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res);
			cache.put(key, res);
		}
		return res;
	}

	@Override
	public long getCourseIdFromAssignment(final long assignment_ID) {
		final String key = "readCourse " + assignment_ID;
		final Long res = (Long) stableCache.get(key);
		long res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.getCourseIdFromAssignment(assignment_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	@Override
	public String getAssignmentName(final long assignment_ID) {
		final String key = "getAssignmentName " + assignment_ID;
		String res = (String) stableCache.get(key);
		if (res != null) {
			Global.logger.finest("[in stableCache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getAssignmentName(assignment_ID);
			Global.logger.finest("[not in stableCache] key: " + key + ", value:" + res);
			stableCache.put(key, res);
		}
		return res;
	}

	@Override
	public long getAssignmentDeadline(final long assignment_ID) {
		final String key = "getAssignmentDeadline " + assignment_ID;
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.getAssignmentDeadline(assignment_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	@Override
	public long countSubmitted(final long assignment_ID) {
		final String key = "countSubmitted " + assignment_ID;
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.countSubmitted(assignment_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	@Override
	public boolean isAssignmentOffline(final long assignment_ID) {
		final String key = "isAssignmentOffline " + assignment_ID;
		final Boolean res = (Boolean) stableCache.get(key);
		boolean res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.booleanValue();
		} else {
			res2 = theMySQLInstance.isAssignmentOffline(assignment_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Boolean.valueOf(res2));
		}
		return res2;
	}

	@Override
	public long getLastAssignment() {
		final long res = theMySQLInstance.getLastAssignment();
		Global.logger.finest("[do not check in cache], value:" + res);
		return res;
	}

	@Override
	public long getLastProactiveAssignment() {
		final long res = theMySQLInstance.getLastProactiveAssignment();
		Global.logger.finest("[do not check in cache], value:" + res);
		return res;
	}

	@Override
	public boolean isNewProactiveAssignments(final long old_Assign_ID) {
		final boolean res = theMySQLInstance.isNewProactiveAssignments(old_Assign_ID);
		Global.logger.finest("[do not check in cache], value:" + res);
		return res;
	}

	/* **********************************************************************************
	 * Event TABLE
	 * **********************************************************************************
	 */
	/**
	 * @param lastEvent
	 * @return true if there are new assignments 
	 */
	@Override
	public boolean isNewEvent(final long lastEvent) {
		final String key = "isNewEvent " + lastEvent;
		final Boolean res = (Boolean) cache.get(key);
		boolean res2;
		
		if (res != null) {
			res2 = res.booleanValue();
			Global.logger.finest("[in cache] key: " + key + ", value:" + res2);
		} else {
			res2 = theMySQLInstance.isNewEvent(lastEvent);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Boolean.valueOf(res2));
		}
		return res2;
	}

	/**
	 * @param lastEvent
	 * @return the array of students Id's 
	 */
	@Override
	public ResultSet getNewEventsOrdered(final long lastEvent) {
		final String key = "getNewEvents " + lastEvent;
		ResultSet res = (ResultSet) cache.get(key);
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getNewEventsOrdered(lastEvent);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res);
			cache.put(key, res);
		}
		return res;
	}

	@Override
	public String getEventName(final long event_ID) {
		final String key = "getEventName " + event_ID;
		String res = (String) stableCache.get(key);
		if (res != null) {
			Global.logger.finest("[in stableCache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getEventName(event_ID);
			Global.logger.finest("[not in stableCache] key: " + key + ", value:" + res);
			stableCache.put(key, res);
		}
		return res;
	}

	@Override
	public long getEventDate(final long event_ID) {
		final String key = "getEventDate " + event_ID;
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			res2 = res.longValue();
			Global.logger.finest("[in cache] key: " + key + ", value:" + res2);
		} else {
			res2 = theMySQLInstance.getEventDate(event_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	@Override
	public String getEventModule(final long event_ID) {
		final String key = "getEventModule " + event_ID;
		String res = (String) stableCache.get(key);
		if (res != null) {
			Global.logger.finest("[in stableCache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getEventModule(event_ID);
			Global.logger.finest("[not in stableCache] key: " + key + ", value:" + res);
			stableCache.put(key, res);
		}
		return res;
	}

	@Override
	public String getEventType(final long event_ID) {
		final String key = "getEventType " + event_ID;
		String res = (String) stableCache.get(key);
		if (res != null) {
			Global.logger.finest("[in stableCache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getEventType(event_ID);
			Global.logger.finest("[not in stableCache] key: " + key + ", value:" + res);
			stableCache.put(key, res);
		}
		return res;
	}

	@Override
	public long getCourseIdFromEvent(final long event_ID) {
		final String key = "getCourseIdFromEvent " + event_ID;
		final Long res = (Long) stableCache.get(key);
		long res2;
		
		if (res != null) {
			res2 = res.longValue();
			Global.logger.finest("[in cache] key: " + key + ", value:" + res2);
		} else {
			res2 = theMySQLInstance.getCourseIdFromEvent(event_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	@Override
	public boolean isEventNextWeek() {
		final boolean res = theMySQLInstance.isEventNextWeek();
		Global.logger.finest("[do not check in cache], value:" + res);
		return res;
	}

	@Override
	public ResultSet getStudentsForNextWeekEvents() {
		final ResultSet res = theMySQLInstance.getStudentsForNextWeekEvents();
		Global.logger.finest("[do not check in cache], value:" + res);
		return res;
	}

	@Override
	public ResultSet getNextWeekEventsFromStudent(final long student_ID) {
		final ResultSet res = theMySQLInstance.getNextWeekEventsFromStudent(student_ID);
		Global.logger.finest("[do not check in cache], value:" + res);
		return res;
	}

	/* **********************************************************************************
	 * Log TABLE
	 * **********************************************************************************
	 */
	@Override
	public long getAssignmentCreator(final long course_ID, final long assignment_ID) {
		final String key = "getAssignmentCreator " + course_ID + " " + assignment_ID;
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			res2 = res.longValue();
			Global.logger.finest("[in cache] key: " + key + ", value:" + res2);
		} else {
			res2 = theMySQLInstance.getAssignmentCreator(course_ID, assignment_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	@Override
	public boolean hasFirstAccess (final long assignment_ID, final long course_ID, final long previousAccess, final long thisAccess) {
		final String key = "hasFirstAccess " + assignment_ID + " " + course_ID + " " + previousAccess + " " + thisAccess;
		final Boolean res = (Boolean) cache.get(key);
		boolean res2;
		
		if (res != null) {
			res2 = res.booleanValue();
			Global.logger.finest("[in cache] key: " + key + ", value:" + res2);
		} else {
			res2 = theMySQLInstance.hasFirstAccess (assignment_ID, course_ID, previousAccess, thisAccess);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Boolean.valueOf(res2));
		}
		return res2;
	}

	@Override
	public ResultSet getStudentsWithFirstAccess (final long assignment_ID, final long course_ID, final long previousAccess, final long thisAccess) {
		final String key = "getStudentsWithFirstAccess " + assignment_ID + " " + course_ID + " " + previousAccess + " " + thisAccess;
		ResultSet res = (ResultSet) cache.get(key);
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getStudentsWithFirstAccess(assignment_ID, course_ID, previousAccess, thisAccess);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res);
			cache.put(key, res);
		}
		return res;
	}

	/* **********************************************************************************
	 * Moodle scripts
	 * **********************************************************************************
	 */
/*/	@Override
	public boolean sendEmail(final long user_id, final String subject, final String body) {
		Boolean res = theMySQLInstance.sendEmail(user_id, subject, body);
//		Global.logger.finest("[do not check in cache], value:" + res);
		return res;
	}
/**/
	/* **********************************************************************************
	 * **********************************************************************************
	 * **********************************************************************************
	 * TEST Methods and fields, come here
	 * **********************************************************************************
	 * **********************************************************************************
	 * **********************************************************************************
	 */

	private final HashMap<Integer, String> testCache;

	@Override
	public synchronized String testComputing (final int n) {
		final Integer N = Integer.valueOf(n);
		final String F = testCache.get(N);
		if (F != null)
			if (!F.isEmpty())
				return "[in testCache] " + F;
//		} else {
			final String f = theMySQLInstance.testComputing(n);
			testCache.put(N, f);
			return "[not in testCache] " + f;
//		}
	}

	@Override
	public void testConnectionInsert (final String value) {
		theMySQLInstance.testConnectionInsert(value);
	}
	
	@Override
	public void testConnectionRead (){
		theMySQLInstance.testConnectionRead();
	}

	@Override
	public void testDeleteNumTable () {
		theMySQLInstance.testDeleteNumTable();
	}

	@Override
	public void testGetAllNumTable(){
		theMySQLInstance.testGetAllNumTable();
	}
	
	/* **********************************************************************************
	 * Added by ReMuS
	 * **********************************************************************************
	 * common methods
	 * **********************************************************************************
	 */
	
	@Override
	public void insertIntoContextTable(final String type, final long ID){
		theMySQLInstance.insertIntoContextTable(type, ID);
		Global.logger.finest("[method not needed in cache] attributes: " + type + " " + ID);
	}
	
	@Override
	public long getContextID(final long instanceid, final int depth){
		Global.logger.finest("[method not needed in cache] attributes: " + instanceid + " " + depth);
		return theMySQLInstance.getContextID(instanceid, depth);
	}
	
	@Override
	public void updateContext(final String path, final long context_ID){
		theMySQLInstance.updateContext(path, context_ID);
		Global.logger.finest("[method not needed in cache] attributes: " + context_ID + " " + path);
	}
	
	@Override
	public void insertCacheFlags(final String name){
		theMySQLInstance.insertCacheFlags(name);
		Global.logger.finest("[method not needed in cache] attributes: " + name);
	}
	
	@Override
	public long getLastId(){
		Global.logger.finest("[method not needed in cache - getLastId()] attributes: ");		
		return theMySQLInstance.getLastId();
	}
	
	@Override
	public String getContextPath(final long context_ID, final int depth){
		final String key = "getContextPath " + context_ID + depth;
		String res = (String) stableCache.get(key);
		if (res != null) {
			Global.logger.finest("[in stableCache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getContextPath(context_ID, depth);
			Global.logger.finest("[not in stableCache] key: " + key + ", value:" + res);
			stableCache.put(key, res);
		}
		return res;
	}

	@Override
	public void insertIntoCourseModules(final long type, final long course_ID, final long grouping_ID){
		theMySQLInstance.insertIntoCourseModules(type, course_ID, grouping_ID);
		Global.logger.finest("[method not needed in cache - insertIntoCourseModules] attributes: " + type + " " + course_ID + " " + grouping_ID);
	}
	
	@Override
	public long getGroupID(final String groupName){
		final String key = "getGroupID " + groupName;
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.getGroupID(groupName);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}
	
	@Override
	public long getAdminUserID() {
		final String key = "getAdminUserID";
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.getAdminUserID();
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}
	
	/*
	 * **********************************************************************************
	 * S001 methods
	 * **********************************************************************************
	 * Category Creation
	 * **********************************************************************************
	 */
	
	@Override
	public boolean courseExist(final String courseShortName, final String courseName) {
		final String key = "courseExists " + courseShortName + courseName;
		final Boolean res = (Boolean) stableCache.get(key);
		boolean res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.booleanValue();
		} else {
			res2 = theMySQLInstance.courseExist(courseShortName, courseName);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Boolean.valueOf(res2));
		}
		return res2;
	}
	
	@Override
	public void createCourseCategory(final String categoryName, final long sortorder){
		theMySQLInstance.createCourseCategory(categoryName, sortorder);
		Global.logger.finest("[method not needed in cache] attributes: " + categoryName + " " + sortorder);
	}
	
	@Override
	public long getCategoryIDFromCategoryName(final String categoryName){
		final String key = "getCategoryIDFromCategoryName " + categoryName;
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.getCategoryIDFromCategoryName(categoryName);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}
	
	@Override
	public void updateCourseCategory(final long category_ID){
		theMySQLInstance.updateCourseCategory(category_ID);
		Global.logger.finest("[method not needed in cache] attributes: " + category_ID);
	}

	/*
	 * **********************************************************************************
	 * Course Creation
	 * **********************************************************************************
	 */	

	@Override
	public void createCourse(final String courseFullName, final String courseShortName, final long categorySortorder, final long categoryID, final long startdate){
		theMySQLInstance.createCourse(courseFullName, courseShortName, categorySortorder, categoryID, startdate);
		Global.logger.finest("[method not needed in cache] attributes: " + courseFullName + " " + courseShortName + " " + categoryID + " "+ startdate + " " + categorySortorder);
	}

	@Override
	public long getCourseIdFromName(final String courseFullName){
		final String key = "getCourseIdFromName " + courseFullName;
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.getCourseIdFromName(courseFullName);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	@Override
	public void insertBlockInstance(final String blockname, final long parentcontext_ID, final int defaultweight){
		theMySQLInstance.insertBlockInstance(blockname, parentcontext_ID, defaultweight);
		Global.logger.finest("[method not needed in cache] attributes: " + blockname + " " + parentcontext_ID + " " + defaultweight);
	}
		
	@Override
	public long getBlockInstanceID(final String blockname, final long parentcontext_ID){
		Global.logger.finest("[method not needed in cache] attributes: " + blockname + " " + parentcontext_ID);
		return theMySQLInstance.getBlockInstanceID(blockname, parentcontext_ID);
	}
		
	@Override
	public void createCourseSection(final long course_ID, final String courseDescription, final String sectionStudyGroupDescription, final String sectionCountryDescription, final String sectionCityDescription){
		theMySQLInstance.createCourseSection(course_ID, courseDescription, sectionStudyGroupDescription, sectionCountryDescription, sectionCityDescription );
		Global.logger.finest("[method not needed in cache - createCourseSection] attributes: " + course_ID );
	}
	
	@Override
	public void insertEnrolMethods(final long course_ID){
		theMySQLInstance.insertEnrolMethods(course_ID);
		Global.logger.finest("[method not needed in cache - insertEnrolMethods] attributes: " + course_ID);
	}
	 
	@Override
	public void updateFirstModInfo(final long course_ID){
		theMySQLInstance.updateFirstModInfo(course_ID);
		Global.logger.finest("[method not needed in cache - updateFirstModInfo] attributes: " + course_ID);
	}
		 
	@Override
	public String getGroupName(final long group_ID){		
		Global.logger.finest("[method not needed in cache - getGroupName] attributes: " + group_ID);
		return theMySQLInstance.getGroupName(group_ID);
	}
	
	/*
	 * **********************************************************************************
	 * S002 methods
	 * **********************************************************************************
	 */
	
	@Override
	public void createEnrolmentMethodForCourse(final long course_ID){
		theMySQLInstance.createEnrolmentMethodForCourse(course_ID);
		Global.logger.finest("[method not needed in cache - createEnrolmentMethodForCourse] attributes: " + course_ID);
	}
	
	@Override
	public long getEnrolmentIDFromCourseID(final long course_ID){
		final String key = "getEnrolmentIDFromCourseID " + course_ID;
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.getEnrolmentIDFromCourseID(course_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}
	
	@Override
	public void userEnrolments(final long enrol_ID){
		theMySQLInstance.userEnrolments(enrol_ID);
		Global.logger.finest("[method not needed in cache - userEnrolments] attributes: " + enrol_ID);
	}
	
	@Override
	public void roleAssignments(final long context_ID){
		theMySQLInstance.roleAssignments(context_ID);
		Global.logger.finest("[method not needed in cache - roleAssignments] attributes: " + context_ID);
	}

	/*
	 * **********************************************************************************
	 * S101 methods
	 * **********************************************************************************
	 */
	
	@Override
	public ResultSet getAllCategories(final int parent){
		Global.logger.finest("[method not needed in cache - roleAssignments] attributes: " + parent);
		return theMySQLInstance.getAllCategories(parent);
	}
	
	/*
	 * **********************************************************************************
	 * S102 methods
	 * **********************************************************************************
	 */
	@Override
	public boolean groupExist(final long course_ID, final String groupName){
		final String key = "groupExist " + course_ID + groupName;
		final Boolean res = (Boolean) cache.get(key);
		boolean res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.booleanValue();
		} else {
			res2 = theMySQLInstance.groupExist(course_ID, groupName);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Boolean.valueOf(res2));
		}
		return res2;
	}

	@Override
	public boolean groupingExist(final long course_ID, final String groupingName){
		final String key = "groupingExist " + course_ID + groupingName;
		final Boolean res = (Boolean) cache.get(key);
		boolean res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.booleanValue();
		} else {
			res2 = theMySQLInstance.groupingExist(course_ID, groupingName);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Boolean.valueOf(res2));
		}
		return res2;
	}
	
	@Override
	public boolean groupIsInGrouping(final long groupName, final long groupingName){
		final String key = "groupIsInGrouping " + groupName + groupingName;
		final Boolean res = (Boolean) cache.get(key);
		boolean res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.booleanValue();
		} else {
			res2 = theMySQLInstance.groupIsInGrouping(groupName, groupingName);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Boolean.valueOf(res2));
		}
		return res2;
	}
	
	@Override
	public void createGroup(final long course_ID, final String groupName){
		theMySQLInstance.createGroup(course_ID, groupName);
		Global.logger.finest("[method not needed in cache - createGroup] attributes: " + course_ID + " " + groupName);
	}
	
	@Override
	public void createGrouping(final long course_ID, final String groupingName){
		theMySQLInstance.createGrouping(course_ID, groupingName);
		Global.logger.finest("[method not needed in cache - createGrouping] attributes: " + course_ID + " " + groupingName);
	}
	
	@Override
	public void insertGroupIntoGrouping(final long group_ID, final long grouping_ID){
		theMySQLInstance.insertGroupIntoGrouping(group_ID, grouping_ID);
		Global.logger.finest("[method not needed in cache - insertGroupIntoGrouping] attributes: " + group_ID + " " + grouping_ID);
	}
		
	@Override
	public long getIDFromGroupingName(final String groupingName){
		final String key = "getIDFromGroupingName " + groupingName;
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.getIDFromGroupingName(groupingName);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}
	
	/*
	 * **********************************************************************************
	 * S103 methods
	 * **********************************************************************************
	 */
	
	@Override
	public void inscribeUsersIntoGroup(final long course_ID, final long group_ID, final long category_ID){
		theMySQLInstance.inscribeUsersIntoGroup(course_ID, group_ID, category_ID);
		Global.logger.finest("[method not needed in cache - inscribeUsersIntoGroup] attributes: " + course_ID + " " + group_ID + " " + category_ID);
	}
	
	@Override
	public void inscribeUserIntoGroup_2(final long groupID, final long user_ID){
		Global.logger.finest("[method not needed in cache - inscribeUserIntoGroup_2] attributes: " + groupID + user_ID );
		theMySQLInstance.inscribeUserIntoGroup_2( groupID, user_ID);
	}
	
	/*
	 * **********************************************************************************
	 * S104 methods
	 * **********************************************************************************
	 */
		
	@Override
	public void insertIntoForum(final String name, final String type, final long course_ID, final String forum_description){
		theMySQLInstance.insertIntoForum(name, type, course_ID, forum_description);
		Global.logger.finest("[method not needed in cache - insertIntoForum] attributes: " + name + " " + type + " " + course_ID + " " + forum_description);
	}
	
	@Override
	public long getCourseSectionID(final long course_ID, final int section){
		final String key = "getCourseSectionID " + course_ID + section;
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.getCourseSectionID(course_ID, section);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}
		
	@Override
	public String getCourseModinfo(final long course_ID){
		Global.logger.finest("[method not needed in cache - getModinfo] attributes: " + course_ID);
		return theMySQLInstance.getCourseModinfo(course_ID);
	}
	
	@Override
	public void updateCourseModinfo(final String modinfo, final long course_ID){
		Global.logger.finest("[method not needed in cache - updateCourseModinfo] attributes: " + modinfo + " " + course_ID);
		theMySQLInstance.updateCourseModinfo(modinfo, course_ID);
	}
	
	@Override
	public void updateCourseModule(final long id, final long course_section_ID, final long course_modules_ID){
		theMySQLInstance.updateCourseModule(id, course_section_ID, course_modules_ID);
		Global.logger.finest("[method not needed in cache - updateCourseModule] attributes: " + course_modules_ID + " " + id + " " + course_section_ID);
	}
	
	@Override
	public void updateCourseSection(final String sequence, final long course_section_ID){
		theMySQLInstance.updateCourseSection(sequence, course_section_ID);
		Global.logger.finest("[method not needed in cache - updateCourseSection] attributes: " + sequence + " " + course_section_ID);
	}
	
	@Override
	public void insertIntoChat(final String name, final long course_ID, final String chat_description){
		theMySQLInstance.insertIntoChat(name, course_ID, chat_description);
		Global.logger.finest("[method not needed in cache - insertIntoChat] attributes: " + name + " " + course_ID + " " + chat_description);
	}
	
	@Override
	public void insertIntoEvent(final long chat_ID, final String chatName, final long course_ID, final String chat_description){
		theMySQLInstance.insertIntoEvent(chat_ID, chatName, course_ID, chat_description);
		Global.logger.finest("[method not needed in cache - insertIntoEvent] attributes: " + chat_ID + " " + chatName + " " + course_ID + " " + chat_description);
	}
	
	@Override
	public void insertIntoFolder(final String name, final long course_ID, final String folder_description){
		theMySQLInstance.insertIntoFolder(name, course_ID, folder_description);
		Global.logger.finest("[method not needed in cache - insertIntoFolder] attributes: " + name + " " + course_ID + " " + folder_description);
	}
	
	@Override
	public void addForumPost(final long admin_ID, final String subject,final String message){
		theMySQLInstance.addForumPost(admin_ID, subject, message);
		Global.logger.finest("[method not needed in cache - addForumPost] attributes: " + subject + " " + message);
	}
	
	@Override
	public void addForumDiscussion(final long course_ID, final long forum_ID, final long group_ID, final String subject,final long forum_post_ID, final long admin_ID){
		theMySQLInstance.addForumDiscussion(course_ID, forum_ID, group_ID, subject,forum_post_ID, admin_ID);
		Global.logger.finest("[method not needed in cache - addForumDiscussion] attributes: " + forum_ID + " " + group_ID + " " + forum_post_ID);
	}
	
	@Override
	public void updateForumPost(final long forum_post_ID, final long forum_discussion_ID){
		theMySQLInstance.updateForumPost(forum_post_ID, forum_discussion_ID);
		Global.logger.finest("[method not needed in cache - updateForumPost] attributes: " + forum_post_ID + " " + forum_discussion_ID);
	}
	
	@Override
	public void insertIntoLog(final long user_ID, final long course_ID, final long cmid, final String info ){
		theMySQLInstance.insertIntoLog(user_ID, course_ID, cmid, info);
		Global.logger.finest("[method not needed in cache - insertIntoLog] attributes: " + course_ID + " " + info);
	}
	
	@Override
	public long getForumID(){
		final String key = "getForumID";
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.getForumID();
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}
	
	@Override
	public long getChatID(){
		final String key = "getChatID";
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.getChatID();
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}
	
	@Override
	public long getFolderID(){
		final String key = "getFolderID";
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.getFolderID();
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}
	
	/*
	 * **********************************************************************************
	 * S105 methods
	 * **********************************************************************************
	 */
	
	@Override
	public ResultSet getStudentsFromGroup(final long group_ID){
		Global.logger.finest("[method not needed in cache - getStudentsFromGroup] attributes: " + group_ID);
		return theMySQLInstance.getStudentsFromGroup(group_ID);
	}
	
	/*
	 * **********************************************************************************
	 * S301 methods
	 * **********************************************************************************
	 */
	
	@Override
	public boolean isUserInGroup(final long user_ID, final long group_ID){
		Global.logger.finest("[method not needed in cache - isUserInGroup] attributes: " + user_ID + group_ID);
		return theMySQLInstance.isUserInGroup(user_ID, group_ID);
	}
	
	/*
	 * **********************************************************************************
	 * M301 methods
	 * **********************************************************************************
	 */
	
	@Override
	public String getCity(final long user_ID){
		Global.logger.finest("[method not needed in cache - getCity] attributes: " + user_ID );
		return theMySQLInstance.getCity(user_ID);
	}	
	
	/*
	 * **********************************************************************************
	 * M201 methods
	 * **********************************************************************************
	 */
	
	@Override
	public void insertIntoResources(final String fileName, final long course_ID, final String fileDescription, final String displayOptions){
		Global.logger.finest("[method not needed in cache - insertIntoResources] attributes: " + fileName + course_ID + fileDescription );
		theMySQLInstance.insertIntoResources( fileName, course_ID, fileDescription, displayOptions);
	}
	
	@Override
	public long getActivityLevel(final String groupName, final long begin_time, final long end_time){
		Global.logger.finest("[method not needed in cache - getActivityLevel] attributes: " + groupName);
		return theMySQLInstance.getActivityLevel(groupName, begin_time, end_time);
	}
	
	@Override
	public long getNewMembers(final long group_ID, final long begin_time, final long end_time){
		Global.logger.finest("[method not needed in cache - getNewMembers] attributes: " + group_ID);
		return theMySQLInstance.getNewMembers(group_ID, begin_time, end_time);
	}
		
	@Override
	public void insertIntoFiles(final String type,final String contenthash, final String pathnamehash, final long pdf_context_ID, final String pdfFileName, final long pdfFileSize){
		Global.logger.finest("[method not needed in cache - insertIntoFiles] attributes: " + pdfFileName + contenthash + pathnamehash );
		theMySQLInstance.insertIntoFiles( type, contenthash, pathnamehash, pdf_context_ID, pdfFileName, pdfFileSize);
	}

	/* **********************************************************************************
	 * Groups TABLEs
	 * Queries that execute commands from the user interface
	 * **********************************************************************************
	 */
	@Override
	public ResultSet getGroupsFromUser(final long user_ID) {
		Global.logger.finest("[method not needed in cache - leaveGroup] attributes: " + user_ID );
		return theMySQLInstance.getGroupsFromUser (user_ID);
	}
	
	@Override
	public void leaveGroup(final long user_ID, final long group_ID){
		Global.logger.finest("[method not needed in cache - leaveGroup] attributes: " + user_ID + group_ID );
		theMySQLInstance.leaveGroup (user_ID, group_ID);
	}
	
	/*
	 * **********************************************************************************
	 * M202 methods
	 * **********************************************************************************
	 */

	@Override
	public ResultSet getAllActiveGroups(final long time){
		Global.logger.finest("[method not needed in cache - getAllActiveGroups] attributes: " + time );
		return theMySQLInstance.getAllActiveGroups (time);
	}

	/*
	 * Moodle 2.5 methods
	 * 
	 */

	@Override
	public String getAssignmentName25(final long assignment_ID) {
		final String key = "getAssignmentName25 " + assignment_ID;
		String res = (String) stableCache.get(key);
		if (res != null) {
			Global.logger.finest("[in stableCache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getAssignmentName25(assignment_ID);
			Global.logger.finest("[not in stableCache] key: " + key + ", value:" + res);
			stableCache.put(key, res);
		}
		return res;
	}

	@Override
	public long getCourseIdFromAssignment25(final long assignment_ID) {
		final String key = "readCourse25 " + assignment_ID;
		final Long res = (Long) stableCache.get(key);
		long res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.getCourseIdFromAssignment25(assignment_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			stableCache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	@Override
	public long getAssignmentDeadline25(final long assignment_ID) {
		final String key = "getAssignmentDeadline25 " + assignment_ID;
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.getAssignmentDeadline25(assignment_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	@Override
	public ResultSet getAllStudentsNotSubmitted25(final long course_ID,final long assignment_ID) {
		final String key = "getStudentsNotSubmitted25 " + course_ID + " " + assignment_ID;
		ResultSet res = (ResultSet) cache.get(key);
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getAllStudentsNotSubmitted25(course_ID, assignment_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res);
			cache.put(key, res);
		}
		return res;
	}

	@Override
	public ResultSet getStudentsNotSubmitted25(final long course_ID,
			final long assignment_ID) {
		return null;
	}

	@Override
	public boolean assignmentExists25(final long assignment_ID) {
		return theMySQLInstance.assignmentExists25(assignment_ID);
	}

	@Override
	public long countSubmitted25(final long assignment_ID) {
		final String key = "countSubmitted25 " + assignment_ID;
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.countSubmitted25(assignment_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	@Override
	public String getStringScenarioConfigParam(final long assignment_id,final String scenarioname, final String param_name) {
		final String key = "getStringScenarioConfigParam id=" + assignment_id+" name="+scenarioname+" paramname="+param_name;
		String res = (String) cache.get(key);

		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getStringScenarioConfigParam(assignment_id, scenarioname, param_name);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res);
			cache.put(key, res);
		}
		return res;
	}

	@Override
	public GregorianCalendar getDateScenarioConfigParam(final long assignment_id,final String scenarioname, final String param_name) {
		final String key = "getDateScenarioConfigParam id=" + assignment_id+" name="+scenarioname+" paramname="+param_name;
		GregorianCalendar res = (GregorianCalendar) cache.get(key);

		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getDateScenarioConfigParam(assignment_id, scenarioname, param_name);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res);
			cache.put(key, res);
		}
		return res;
	}

	@Override
	public boolean getBooleanScenarioConfigParam(final long assignment_id,final String scenarioname, final String param_name) {
		Global.logger.finest("[method not needed in cache - getBooleanScenarioConfigParam] ");
		return theMySQLInstance.getBooleanScenarioConfigParam(assignment_id, scenarioname, param_name);
	}

	@Override
	public int getIntegerScenarioConfigParam(final long assignment_id,final String scenarioname, final String param_name) {
		final String key = "getIntegerScenarioConfigParam id=" + assignment_id+" name="+scenarioname+" paramname="+param_name;
		Integer res = (Integer) cache.get(key);
		int res2;

		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.intValue();
		} else {
			res2 = theMySQLInstance.getIntegerScenarioConfigParam(assignment_id, scenarioname, param_name);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Integer.valueOf(res2));
		}
		return res2;
	}

	@Override
	public boolean isScenarioEnabled(final long assignment_id, final String scenarioname) {
		Global.logger.finest("[method not needed in cache - isScenarioEnabled] ");
		return theMySQLInstance.isScenarioEnabled(assignment_id, scenarioname);
	}

	@Override
	public long getLongScenarioConfigParam(final long assignment_id,final String scenarioname, final String param_name) {
		final String key = "getIntegerScenarioConfigParam id=" + assignment_id+" name="+scenarioname+" paramname="+param_name;
		Long res = (Long) cache.get(key);
		long res2;

		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.getLongScenarioConfigParam(assignment_id, scenarioname, param_name);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	@Override
	public boolean hasHintsToSend(final long assignment_id, final int year, final int month,final int day) {
		Global.logger.finest("[method not needed in cache - hasHintsToSend] ");
		return theMySQLInstance.hasHintsToSend(assignment_id,year, month,day);
	}

	@Override
	public ArrayList<Long> getHintsToSend(final long assignment_id, final int year, final int month,final int day) {
		Global.logger.finest("[method not needed in cache - getHintsToSend] ");
		return theMySQLInstance.getHintsToSend(assignment_id,year, month,day);
	}

	@Override
	public long setSentToHint(final long assignment_id, final long hint_id) {
		Global.logger.finest("[method not needed in cache - setSentToHint] ");
		return theMySQLInstance.setSentToHint(assignment_id,hint_id);
	}
	
	@Override
	public long setSentToReminder(final long assignment_id) {
		Global.logger.finest("[method not needed in cache - setSentToHint] ");
		return theMySQLInstance.setSentToReminder(assignment_id);
	}

	@Override
	public long countAllEnrolledStudents25(final long course_ID) {
		final String key = "countAllEnrolledStudents25 " + course_ID;
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.countAllEnrolledStudents25(course_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	@Override
	public ResultSet getNewProactiveAssignmentsOrdered(final long lastAssignment) {
		final String key = "getNewProactiveAssignmentsOrdered " + lastAssignment;
		ResultSet res = (ResultSet) cache.get(key);
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getNewProactiveAssignmentsOrdered(lastAssignment);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res);
			cache.put(key, res);
		}
		return res;
	}

	
	@Override
	public ResultSet getNewProactiveAssignmentsOrderedAllFields(final long lastAssignment) {
		final String key = "getNewProactiveAssignmentsOrderedAllFields " + lastAssignment;
		ResultSet res = (ResultSet) cache.get(key);
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
		} else {
			res = theMySQLInstance.getNewProactiveAssignmentsOrderedAllFields(lastAssignment);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res);
			cache.put(key, res);
		}
		return res;
	}
	
	@Override
	public long getAssignmentCreator25(final long course_ID, final long assignment_ID) {
		final String key = "getAssignmentCreator25 " + course_ID + " " + assignment_ID;
		final Long res = (Long) cache.get(key);
		long res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.longValue();
		} else {
			res2 = theMySQLInstance.getAssignmentCreator25(course_ID, assignment_ID);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Long.valueOf(res2));
		}
		return res2;
	}

	@Override
	public boolean isNewAssignment25(final long lastAssignment) {
		final String key = "checkDbForNewAssignment25 " + lastAssignment;
		final Boolean res = (Boolean) cache.get(key);
		boolean res2;
		
		if (res != null) {
			Global.logger.finest("[in cache] key: " + key + ", value:" + res);
			res2 = res.booleanValue();
		} else {
			res2 = theMySQLInstance.isNewAssignment25(lastAssignment);
			Global.logger.finest("[not in cache] key: " + key + ", value:" + res2);
			cache.put(key, Boolean.valueOf(res2));
		}
		return res2;
	}
}