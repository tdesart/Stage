package lu.uni.fstc.tests;

import lu.uni.fstc.proactivity.db.MoodleDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 *
 */
public class TestdbConnection2 {
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		MoodleDbWrapper conn1 = MoodleDbWrapper.getInstance("moodle.cfg.xml");
		Global.logger.info("Moodle server - " + MoodleDbWrapper.connParams.toString());

		//String lineToInsert = "sandro 10";
		//conn1.testConnectionInsert(lineToInsert);
		if (conn1 != null) 
			conn1.testConnectionRead2();
	}
}