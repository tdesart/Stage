package lu.uni.fstc.test_Remus;

import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.db.MoodleCacheDbWrapper;
import lu.uni.fstc.proactivity.db.PAMDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author REMUS
 *
 */
public class TestLogQueries {
	
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

		Global.logger.info("MTA_PushQuestion starting !");

		//QueueManager qm = new QueueManager (db, dbPam);
		//sqm.initializeSockectServerforTestPurpose();

		Global.logger.info("Waiting started!");
		//lu.uni.fstc.utils.Time.sleepNSeconds(5);

		long one, two;
		one = System.currentTimeMillis();
		
		//for (long i = 0; i < 2000000; i++)
			//dbPam.insertIntoLog();
		
		two = System.currentTimeMillis();
		Global.logger.info("MTA_PushQuestion.execute() returned:  (execution time: "  + (two-one) + " ms)");

//		if (qm.sockectServer != null) 
//			qm.sockectServer.stop();
	}

	
}
