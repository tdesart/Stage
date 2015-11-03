package lu.uni.fstc.tests;

import lu.uni.fstc.proactivity.db.*;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 *
 */
public class TestProxyWithCache1 {
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		long  ini, end;
/*/		
		String computer= "moodle-test.uni.lux";
		String port= "3306";
		String schema = "PAS";
		String user = "root";
		String password  ="moodle"; 
/**/
/**/
		String connParams = "moodle.cfg.xml";
		MoodleDbWrapper conn1 = MoodleDbWrapper.getInstance(connParams);
		ini = System.currentTimeMillis();
		conn1.testDeleteNumTable();
		Global.logger.info("  " + conn1.testComputing(11));
		Global.logger.info("  " + conn1.testComputing(12));
		Global.logger.info("  " + conn1.testComputing(13));
		Global.logger.info("  " + conn1.testComputing(11));
		Global.logger.info("  " + conn1.testComputing(11));
		Global.logger.info("  " + conn1.testComputing(20));
		Global.logger.info("  " + conn1.testComputing(3));
		Global.logger.info("  " + conn1.testComputing(3));
		Global.logger.info("  " + conn1.testComputing(3));
		Global.logger.info("  " + conn1.testComputing(4));
		Global.logger.info("  " + conn1.testComputing(50));
		Global.logger.info("  " + conn1.testComputing(50));
		Global.logger.info("  " + conn1.testComputing(50));		
		conn1.testGetAllNumTable();
		end = System.currentTimeMillis();
		Global.logger.info("Without Cache: " + (end-ini) + "msecs");
/**/ /**/
		Global.logger.info("*************************************");
//		AbstractMoodleDbWrapper conn2 = (MoodleCacheDbWrapper) MoodleCacheDbWrapper.getInstance(conn1);
		MoodleCacheDbWrapper conn2 = MoodleCacheDbWrapper.getInstance(connParams);
		ini = System.currentTimeMillis();
		conn2.testDeleteNumTable();
		conn2.testGetAllNumTable();
		Global.logger.info("  " + conn2.testComputing(11));
		Global.logger.info("  " + conn2.testComputing(12));
		Global.logger.info("  " + conn2.testComputing(13));
		Global.logger.info("  " + conn2.testComputing(11));
		Global.logger.info("  " + conn2.testComputing(11));
		Global.logger.info("  " + conn2.testComputing(20));
		Global.logger.info("  " + conn2.testComputing(3));
		Global.logger.info("  " + conn2.testComputing(3));
		Global.logger.info("  " + conn2.testComputing(3));
		Global.logger.info("  " + conn2.testComputing(4));
		Global.logger.info("  " + conn2.testComputing(50));
		Global.logger.info("  " + conn2.testComputing(50));
		Global.logger.info("  " + conn2.testComputing(50));		
		conn2.testGetAllNumTable();
		end = System.currentTimeMillis();
		Global.logger.info("With Cache: " + (end-ini) + "msecs");
/**/ /**/
  		Global.logger.info("*************************************");
		Global.logger.info("Another call to the MoodleCacheDbWrapper instance ");
		MoodleCacheDbWrapper conn3 = MoodleCacheDbWrapper.getInstance(connParams);
		Global.logger.info("  " + conn3.testComputing(50));
		Global.logger.info("  " + conn3.testComputing(32));
		conn3.testGetAllNumTable();
/**/
	}
}