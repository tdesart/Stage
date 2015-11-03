package lu.uni.fstc.tests_Remus_rules;

import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.db.MoodleCacheDbWrapper;
import lu.uni.fstc.proactivity.db.PAMDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.ruleRunningSystem.ProactiveEngine;
import lu.uni.fstc.proactivity.rules.moodleCoPs.M203;
import lu.uni.fstc.proactivity.rules.moodleCoPs.S105;

/**
 * @author remus.dobrican
 *
 */
public class TestM203_S105Transactions {

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
		
		final M203 rule = new M203(0);
		rule.setEngine(new ProactiveEngine (db, dbPam));

		final boolean ret = rule.execute();
		Global.logger.info("M201.execute() returned: " + ret);
		
		
		two = System.currentTimeMillis();
		Global.logger.info("exec time: "  + (two-one));
		
		long three, four;
		three = System.currentTimeMillis();
		
						
		final S105 rule1 = new S105("Bachelor en Informatique (professionnel)", "Group Enrolment", "Welcome to your new group!", 50);
		rule1.setEngine(new ProactiveEngine (db, dbPam));

		final boolean ret1 = rule1.execute();
		Global.logger.info("S105.execute() returned: " + ret1);
		
		
		four = System.currentTimeMillis();
		Global.logger.info("exec time: "  + (four-three));
	}

}
