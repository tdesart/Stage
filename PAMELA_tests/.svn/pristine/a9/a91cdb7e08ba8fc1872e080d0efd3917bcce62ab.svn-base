package lu.uni.fstc.test_Remus;
import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.db.MoodleCacheDbWrapper;
import lu.uni.fstc.proactivity.db.PAMDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.ruleRunningSystem.ProactiveEngine;
import lu.uni.fstc.proactivity.rules.moodleCoPs.M301;

/**
 * @author REMUS
 *
 */
public class TestM301 {

	/*/	private static String computer   = "moodle-test.uni.lux";
	private static String dbPort	 = "3306";
	private static String dbSchema   = "moodle_test";
	private static String dbUser	 = "root";
	private static String dbPassword = "moodle"; 
	/**/
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
		final AbstractMoodleDbWrapper db = MoodleCacheDbWrapper.getInstance("moodle.cfg.xml");
		if (!db.isConnected()) {
			Global.logger.info("Unable to connect to the Moodle database. Exiting the application!");
			return;
		}
		final AbstractPAM2MoodleDbWrapper dbPam = PAMDbWrapper.getInstance("pam.cfg.xml");
		if (!dbPam.isConnected()) {
			Global.logger.info("Unable to connect to the PAM database. Exiting the application!");
			return;
		}
//		Global.logger.info("AbstractMoodleDbWrapper was created as " + db.toString() + ".");
		// starts the engine with the InitRuleList

		long one, two;
		one = System.currentTimeMillis();
		
		/*
		final M203 rule = new M203(0);
		rule.setEngine(new QueueManager (db, dbPam));

		final boolean ret = rule.execute();
		Global.logger.info("M203.execute() returned: " + ret);
		*/
		
		 	final M301 rule = new M301(1547);
			rule.setEngine(new ProactiveEngine (db, dbPam));
	
			final boolean ret = rule.execute();
			Global.logger.info("M301.execute() returned: " + ret);
		 
		
		two = System.currentTimeMillis();
		Global.logger.info("exec time: "  + (two-one));
	}

}
