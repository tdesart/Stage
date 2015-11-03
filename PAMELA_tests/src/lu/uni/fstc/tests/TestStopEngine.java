package lu.uni.fstc.tests;

import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.db.MoodleCacheDbWrapper;
import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.db.PAMDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.Time;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 *
 */
public class TestStopEngine {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(final String[] args) throws Exception {
		Global.logger.info("init!");
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

		// starts the engine without a InitRuleList
		Global.logger.info("writing STOP!");
		final TestEngine eng = new TestEngine(db, dbPam);
//		db.setScheduleToStop(true);
		eng.forceStop();
		
		Global.logger.info("pausing!");
		Time.sleepNSeconds(2);
		Global.logger.info("Leaving!");
	}
}
