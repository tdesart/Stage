package lu.uni.fstc.tests;
import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.db.MoodleCacheDbWrapper;
import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.db.PAMDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;

import lu.uni.fstc.proactivity.messaging.PamSockekServer;

import lu.uni.fstc.proactivity.ruleRunningSystem.ProactiveEngine;
import lu.uni.fstc.proactivity.rules.moodleAssignments.MTA_PushQuestion;

/**
 * @author Sandro Reis
 *
 */
public class TestMTA_PushQuestion {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
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

		Global.logger.info("MTA_PushQuestion starting !");

		ProactiveEngine qm = new ProactiveEngine (db, dbPam);
		PamSockekServer socketServer;
		try {
			//   create it now on the Default Port, 
			//   set the database Wrappers, and  
			//   set up the secure connection to the socket server.
			socketServer = new PamSockekServer (PamSockekServer.DEFAUL_PORT, db, dbPam, "privateKeyStore.cfg.xml");
			qm.socketServer = socketServer;

			// Run the Thread
			socketServer.start();
		} catch (final Exception e) {
			Global.logger.severe("Unable to SET UP and START a secure connection to the Socket Server. Exiting the application!");
			e.printStackTrace();
		}

		Global.logger.info("Waiting started!");
		lu.uni.fstc.proactivity.utils.Time.sleepNSeconds(5);

		long one, two;
		one = System.currentTimeMillis();
		MTA_PushQuestion rule = new MTA_PushQuestion();
		rule.setEngine(qm);

		boolean ret = rule.execute();
		two = System.currentTimeMillis();
		Global.logger.info("MTA_PushQuestion.execute() returned: " + ret + ". (execution time: "  + (two-one) + " ms)");

//		if (qm.sockectServer != null) 
//			qm.sockectServer.stop();
	}
}