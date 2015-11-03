package lu.uni.fstc.tests;

import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.db.MoodleCacheDbWrapper;
import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.db.PAMDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.Time;

import lu.uni.fstc.proactivity.ruleRunningSystem.ProactiveEngine;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 *
 */
public class TestQManager1 {

	private static ProactiveEngine qm;

/*/	private static String computer   = "moodle-test.uni.lux";
	private static String dbPort	 = "3306";
	private static String dbSchema   = "PAS";
	private static String dbUser	 = "root";
	private static String dbPassword = "moodle"; 
/**/
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// only at creation the programmer has to know the exact type of the database wrapper 
		// (in this case, a proxy with cache to a MySQL connection)
		// from know on every call should be generic to the AbstractMoodleDbWrapper abstract class
		AbstractMoodleDbWrapper db = MoodleCacheDbWrapper.getInstance("moodle.cfg.xml");
/**/		if (!db.isConnected()) { 
			Global.logger.info("Unable to connect to the database. Exiting the application!");
			return;
		}
		Global.logger.info("AbstractMoodleDbWrapper was created as " + db.toString() + ".");

		final AbstractPAM2MoodleDbWrapper dbPam = PAMDbWrapper.getInstance("pam.cfg.xml");
		if (!dbPam.isConnected()) {
			Global.logger.info("Unable to connect to the PAM database. Exiting the application!");
			return;
		}
		Global.logger.info("AbstractPAMDbWrapper was created as " + dbPam.toString() + ".");
/**/
		// starts the engine with the InitRuleList
/*/		InitRuleList irl = new InitRuleList();
		Vector<Object> v = irl.getInitRulesList();
		qm = new ProactiveEngine (lu.uni.fstc.db, v);
/**/
		// starts the engine without the InitRuleList
		qm = new ProactiveEngine (db, dbPam);
		Thread qmThread = new Thread(qm);
		ProactiveEngine qm2 = new ProactiveEngine (db, dbPam);
		Thread qmThread2 = new Thread(qm2);
		ProactiveEngine qm4 = new ProactiveEngine (db, dbPam);
		Thread qmThread4 = new Thread(qm4);
	  	//new Thread(qm).start();
		Global.logger.info("starting the engine 1!");
		qmThread.start();

		// Testing if it starts a second ProactiveEngine while the first is still running
		Time.sleepOneSecond();
		Global.logger.info("starting the engine 2!");
		qmThread2.start();

		// Testing if it starts a second ProactiveEngine after the first has already finished
		Time.sleepNSeconds(20); // this parameter has to be greater then the one inside the ProactiveEngine.run()
		Global.logger.info("sending stop to the engine 1!");
		qm.forceStop();
		Time.sleepNSeconds(6); // this parameter should be consistent with the one inside the ProactiveEngine.run()
		Global.logger.info("starting the engine 4!");
 		qmThread4.start();
		Time.sleepNSeconds(1);
		Global.logger.info("sending stop to the engine 4!");
		qm.forceStop();
/**/

/*/		//start Socket server
	 	new Thread(server).start();

		//wait for the socket server to start
		while(server.getServer_status() == false)
		{}
/**/
 		Global.logger.info("System is running! Leaving PAM_Start Main ...");
	}

}
