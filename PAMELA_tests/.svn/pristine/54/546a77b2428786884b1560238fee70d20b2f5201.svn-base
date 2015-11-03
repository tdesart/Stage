package lu.uni.fstc.test_Remus;

import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.db.MoodleCacheDbWrapper;
import lu.uni.fstc.proactivity.db.PAMDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.ruleRunningSystem.ProactiveEngine;
import lu.uni.fstc.proactivity.rules.moodleCoPs.S102;
import lu.uni.fstc.proactivity.rules.moodleCoPs.S103;
import lu.uni.fstc.proactivity.rules.moodleCoPs.S104;


/**
 * @author REMUS
 *
 */
public class TestS102 {

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
		
						
		final S102 rule = new S102(1544, "Belgium", 1);
		rule.setEngine(new ProactiveEngine (db, dbPam));

		final boolean ret = rule.execute();
		Global.logger.info("S102.execute() returned: " + ret);
		
		final S104 rule1 = new S104(1544, "Belgium", 1);
		rule1.setEngine(new ProactiveEngine (db, dbPam));

		final boolean ret1 = rule1.execute();
		Global.logger.info("S104.execute() returned: " + ret1);
		
		final S103 rule2 = new S103(1544, "Belgium", "country");
		rule2.setEngine(new ProactiveEngine (db, dbPam));

		final boolean ret2 = rule2.execute();
		Global.logger.info("S103.execute() returned: " + ret2);
		
		two = System.currentTimeMillis();
		Global.logger.info("exec time: "  + (two-one));
	}

}
