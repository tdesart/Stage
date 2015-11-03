package lu.uni.fstc.proactivity.rules.moodleCoPs;

import lu.uni.fstc.proactivity.db.MySQLOperations;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Remus Dobrican
 * @version 1.0 - Remus Dobrican © 2013 
 *
 *	First Scenarios which triggers the whole Proactive Cycle 
 * It is used only once, then it dies;
 */
public class S001 extends AbstractRuleCops {

	private long start_time, currentTime, category_ID, category_context_ID,
	course_ID, course_context_ID, blockInstance_ID,
	blockInstance_context_ID;	
	private String path = "";
	private String blockNames[] = { "search_forums", "news_items",
			"calendar_upcoming", "recent_activity" };
	private final String courseShortName = "cops",
			categoryName = "Social Groups",
			courseName = "Communities of Practice",
			courseDescription = "<p>Welcome to <span style=\"font-size: medium;\">Social Groups on Moodle</span> !</p>  "
					+ "<p>This is a place where <span style=\"font-size: small;\"><strong>STUDENTS </strong></span>can :</p>  "
					+ "<ul>  <li>share information about courses (previous lecture notes, practical labs, etc.); </li>  "
					+ "<li>meet people from your country of origin / city of residence that are studying in Campus Kirchberg; </li> "
					+ "<li>ask students from all of the semesters of your study programs about courses, projects and exams; </li> "
					+ "<li>find solutions to common problems (like transportation, housing, etc.) within the community of students living in the same city (e.g. students living in Esch-sur-Alzette); </li> "
					+ " </ul> <p>Please use these groups to improve Online Social Life of Students!</p>",
			sectionStudyGroupDescription = "<p>This section is dedicated for social groups formed from the different <strong>Study Programs</strong>.</p><p>The group''s resources are available only for students <span style=\"text-decoration: underline;\">inscribed in all the semesters</span> in the same study program.</p>",
			sectionCountryDescription = "<p>This section is dedicated for social groups based on the <strong>Country of Origin</strong> of the students.</p><p>The group''s resources are available only for students <span style=\"text-decoration: underline;\">coming from the same country</span>.</p>",
			sectionCityDescription = "<p>This section is dedicated for social groups based on the <strong>City of Residence</strong> where students currently live.</p><p>The group''s resources are available only for students <span style=\"text-decoration: underline;\">living in the same city</span>.</p>";
	private final long courseStartDate = 1351724400, categorySortorder = 5000;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private S001() {
		setType(RuleType.META);
	}

	/**
	 * @param start_time 
	 */
	public S001(final long start_time) {
		this();
		this.start_time = start_time;
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {			
		currentTime = System.currentTimeMillis();
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		// we only need to compare the currentTime with our date = 1351944000
		// (03.11.2012 - 12:00:00)
		// and if it is bigger then it will be execute (only once)
		return (currentTime > start_time);
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#conditions()
	 */
	@Override
	protected boolean conditions() {
		return !dataNativeSystem.courseExist(courseShortName, courseName);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#actions()
	 */
	@Override
	protected void actions() {
		MySQLOperations mso = MySQLOperations.getInstance();
		
		try {
			if (!mso.startTransaction(dataNativeSystem.getConn()))
				return;

			// INSERT INTO mdl_course_categories
			dataNativeSystem.createCourseCategory(categoryName, categorySortorder);

			// UPDATE mdl_course_categories
			category_ID = dataNativeSystem.getCategoryIDFromCategoryName(categoryName);
			dataNativeSystem.updateCourseCategory(category_ID);

			// INSERT INTO mdl_context
			dataNativeSystem.insertIntoContextTable("category", category_ID);

			// UPDATE mdl_context
			category_context_ID = dataNativeSystem.getLastId();
			path = "/1/" + category_context_ID;
			dataNativeSystem.updateContext(path, category_context_ID);

			// INSERT INTO mdl_cache_flags ('/1/33071')
			dataNativeSystem.insertCacheFlags("/1/" + category_context_ID);
			
			
			
			// INSERT INTO mdl_course
			dataNativeSystem.createCourse(courseName, courseShortName,
					categorySortorder + 1, category_ID, courseStartDate);

			// INSERT INTO mdl_context
			course_ID = dataNativeSystem.getCourseIdFromName(courseName);
			dataNativeSystem.insertIntoContextTable("course", course_ID);

			// UPDATE mdl_context
			//course_context_ID = engine.db.getContextID(course_ID, 3);
			course_context_ID = dataNativeSystem.getLastId();
			path = "/1/" + category_context_ID + "/" + course_context_ID;
			dataNativeSystem.updateContext(path, course_context_ID);

			// INSERT INTO mdl_block_instances
			for (int i = 0; i < 4; i++) {
				dataNativeSystem.insertBlockInstance(blockNames[i], course_context_ID, i);
				blockInstance_ID = dataNativeSystem.getBlockInstanceID(blockNames[i],
						course_context_ID);
				dataNativeSystem.insertIntoContextTable("blockinstance", blockInstance_ID);
				blockInstance_context_ID = dataNativeSystem.getContextID(blockInstance_ID,
						4);
				path = "/1/" + category_context_ID + "/" + course_context_ID + "/"
						+ blockInstance_context_ID;
				dataNativeSystem.updateContext(path, blockInstance_context_ID);
			}

			// INSERT INTO mdl_course_sections (course,section,summaryformat)
			// VALUES('1431','0','1')
			dataNativeSystem.createCourseSection(course_ID, courseDescription,
					sectionStudyGroupDescription, sectionCountryDescription, sectionCityDescription);

			// INSERT INTO mdl_cache_flags ('/1/33071')
			dataNativeSystem.insertCacheFlags("/1/" + category_ID + "/" + course_ID);

			// INSERT INTO mdl_enrol ...
			// ('manual','0','1431','0','0','1352993565','1352993565','0','0','5')
			// INSERT INTO mdl_enrol ...
			// ('guest','1','1431','0','0','1352993565','1352993565','1')
			// INSERT INTO mdl_enrol ...
			// ('self','1','1431','0','0','1352993565','1352993565','2','0','15552000','0','1','0','5')
			dataNativeSystem.insertEnrolMethods(course_ID);

			// UPDATE mdl_course SET modinfo = 'a:0:{}' WHERE id = '1479'
			dataNativeSystem.updateFirstModInfo(course_ID);

			mso.commit(dataNativeSystem.getConn());
		}
		catch (final Exception e){
			e.printStackTrace();
			mso.rollback(engine.dataNativeSystem.getConn());
			Global.logger.severe("Error in the transaction: rollback executed!");
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		
		if (getActivated()) {			
			createRule(new S002(course_ID));
		} else
			createRule(this);
		return true;
	}

	@Override
	public String toString() {
		return "S001 - Initial Scenario: Creates Course " + courseName
				+ " and then generates rule S002";
	}

	/**
	 * @return a long
	 */
	public long getStart_time() {
		return start_time;
	}

	/**
	 * @param start_time
	 */
	public void setStart_time(final long start_time) {
		this.start_time = start_time;
	}
}