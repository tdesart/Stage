package lu.uni.fstc.tests;

import java.io.BufferedReader;
import java.io.InputStreamReader;

//import org.json.JSONObject;

import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.db.MoodleCacheDbWrapper;
import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.db.PAMDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.StringUtils;

//import messaging.SMServerNewGroup;

import lu.uni.fstc.proactivity.ruleRunningSystem.ProactiveEngine;
import lu.uni.fstc.proactivity.rules.RuleMessageMoodle;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 */
public class TestMSG001 {
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
/*/
		boolean production = true;
/*/		boolean production = false;
/**/
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

		long init=0, id;
		if (production)
			id = 1065;
		else
			id = 2310;

		final ProactiveEngine engine = new ProactiveEngine (db, dbPam);
		engine.getSystemParameters();
		final Thread qmThread = new Thread(engine);
		Global.logger.info("Starting the Engine!");
		qmThread.start();

		BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );

		lu.uni.fstc.proactivity.utils.Time.sleepNSeconds(3);
/**/	// send a new_message json test message until exit is entered 
		boolean more = true;
		boolean ret = false;

		while (more) {
			Global.logger.warning("input something NOW");
			String in = sysin.readLine();
			if (!StringUtils.isEmptyOrNullString(in) && "exit".equalsIgnoreCase(in))
				more = false;
			Long uid;
			try {
				uid = Long.valueOf(in);
			} catch (Exception e) {
				uid = Long.valueOf(id);
			}
			if (uid == null)
				uid = Long.valueOf(id);
			else if (uid.longValue() == 0)
				uid = Long.valueOf(id);

			Global.logger.info("Creating a message now for user:" + uid);
			init = System.currentTimeMillis();
			RuleMessageMoodle rule1 = new RuleMessageMoodle(uid.longValue(), "Test Mail Title", "Test mail n.2<br>Created from code on a TEST JAVA Class<br>Hello Sergio, This is Sandro from inside a JAVA program, calling the Messenger RULE!");
			rule1.setEngine(engine);
	 		ret = rule1.execute();
		}
/**/
/*/	// send a new_group json test message 
		Global.logger.warning("input something NOW");
		sysin.readLine();
		
		Global.logger.info("SENDING JSON with a fake group for user:" + id);
		SMServerNewGroup grpSrv = new SMServerNewGroup(engine.dbPam, id, "groupName", 8888888, 0); // the last parameters are the group Id and activity
		JSONObject json2 = grpSrv.buildJSON();
		engine.sockectServer.sendToId(id, json2.toString());
/**/		
		Global.logger.info("MSG001.execute() returned: " + ret + ", total time : "  + (System.currentTimeMillis()-init));
/*/
		MSG001 rule2 = new MSG001(id, "Test Mail Title", "Test mail n.2<br>Created from code on a php script<br>Hello Denis, This is Sandro from inside a JAVA program, calling the Messenger RULE!");
		rule2.setEngine(engine);
 		ret = rule2.execute();
		Global.logger.info("MSG001.execute() returned: " + ret + ", total time : "  + (System.currentTimeMillis()-init));

		MSG001 rule3 = new MSG001(id, "Test Mail Title", "Test mail n.2<br>Created from code on a php script<br>Hello Denis, This is Sandro from inside a JAVA program, calling the Messenger RULE!");
		rule3.setEngine(engine);
 		ret = rule3.execute();
		Global.logger.info("MSG001.execute() returned: " + ret + ", total time : "  + (System.currentTimeMillis()-init));
/**/
	}
}
