package lu.uni.fstc.test_Remus;


import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.db.MoodleCacheDbWrapper;
import lu.uni.fstc.proactivity.db.PAMDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.ruleRunningSystem.ProactiveEngine;

/**
 * @author REMUS
 *
 */
public class M201SimpleTest {

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
		
						
		final M201Simple rule = new M201Simple(1544, "Bachelor en Informatique (professionnel)", 0);
		rule.setEngine(new ProactiveEngine (db, dbPam));

		final boolean ret = rule.execute();
		Global.logger.info("S201.execute() returned: " + ret);
		
		
		
		two = System.currentTimeMillis();
		Global.logger.info("exec time: "  + (two-one));
	}

}
