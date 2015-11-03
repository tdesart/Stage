package lu.uni.fstc.tests;

import lu.uni.fstc.proactivity.ruleRunningSystem.ProactiveEngine;

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
public class TestQManager3 {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// only at creation the programmer has to know the exact type of the database wrapper instance
		// (in this case, a proxy with cache to a MySQL connection)
		// from know on every call should be generic to the AbstractMoodleDbWrapper abstract class

		AbstractMoodleDbWrapper db = MoodleCacheDbWrapper.getInstance("moodle.cfg.xml");
		if (!db.isConnected()) {
			Global.logger.info("Unable to connect to the database. Exiting the application!");
			return;
		}

		final AbstractPAM2MoodleDbWrapper dbPam = PAMDbWrapper.getInstance("pam.cfg.xml");
		if (!dbPam.isConnected()) {
			Global.logger.info("Unable to connect to the PAM database. Exiting the application!");
			return;
		}

		// starts the engine without a InitRuleList
		ProactiveEngine qm = new ProactiveEngine (db, dbPam);
		Thread qmThread = new Thread(qm);
		Global.logger.info("starting the engine at " + System.currentTimeMillis() + ".");
		qmThread.start();

		Time.sleepNSeconds(13);
		Global.logger.info("sending stop to the engine 1!");
//		qm.forceStop();
	}
}
