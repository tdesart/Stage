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
public class TestQManager2 {

	private static ProactiveEngine qm;
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(final String[] args) throws Exception {
		// only at creation the programmer has to know the exact type of the database wrapper instance
		// (in this case, a proxy with cache to a MySQL connection)
		// from know on every call should be generic to the AbstractMoodleDbWrapper abstract class

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
		
/*/		// Queue for test purposes only
		lu.uni.fstc.queue.Queue<rules.AbstractRule> currentQueue = new lu.uni.fstc.queue.Queue<rules.AbstractRule>();

		// starts the engine with the InitRuleList
		qm = new ProactiveEngine (db, currentQueue);

		// Rules for test purposes only
		long last_assig_id = 0;
		rules.MTA001_old r = new rules.MTA001_old(last_assig_id);
		r.setEngine(qm);
		currentQueue.enqueue(r);
		rules.MTA005a r2 = new rules.MTA005a(0);
		r2.setEngine(qm);
		currentQueue.enqueue(r2);
		rules.MTA005b r3 = new rules.MTA005b(0);
		r3.setEngine(qm);
		currentQueue.enqueue(r3);
/*/
		// starts the engine without a InitRuleList
		qm = new ProactiveEngine (db, dbPam);
/**/
		final Thread qmThread = new Thread(qm);
		Global.logger.info("starting the engine at " + System.currentTimeMillis() + ".");
		qmThread.start();
		Time.sleepNSeconds(3);
//		Time.seconds(13);
		Global.logger.info("sending stop to the engine 1!");
		qm.forceStop();
// 		Global.logger.info("System is running! Leaving PAM_Start Main ...");
	}
}
