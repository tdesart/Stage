package lu.uni.fstc.tests;

import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.db.MoodleDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 *
 */
public class TestdbConnection1 {
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
//		DbConnectionParameters params = new DbConnectionParameters("moodleREAL.cfg.xml");

		MoodleDbWrapper conn1 = MoodleDbWrapper.getInstance("moodleREAL2.cfg.xml");
		Global.logger.info("Moodle server - " + AbstractMoodleDbWrapper.connParams.toString());

		//String lineToInsert = "sandro 10";
		//conn1.testConnectionInsert(lineToInsert);
		if (conn1 != null) 
			conn1.testConnectionRead2();
	}
}