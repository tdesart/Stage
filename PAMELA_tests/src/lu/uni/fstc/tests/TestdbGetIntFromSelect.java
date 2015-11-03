package lu.uni.fstc.tests;

import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.db.PAMDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 *
 */
public class TestdbGetIntFromSelect {
	/**
	 * @param args 
	 * @throws Exception 
	 */
	public static void main(final String[] args) throws Exception {
	    Global.logger.info("**************************************************************");

		// only at creation the programmer has to know the exact type of the database wrapper to instantiate it properly
		// (in this case, a proxy with cache to a MySQL connection to Moodle(TM))
		// from know on every call should be generic to the AbstractMoodleDbWrapper abstract class

		final AbstractPAM2MoodleDbWrapper dbPam = PAMDbWrapper.getInstance("pam.cfg.xml");
		if (!dbPam.isConnected()) {
			Global.logger.severe("Unable to connect to the PAM database. Exiting the application!");
			return;
		}
		Global.logger.finer("AbstractPAMDbWrapper was created as " + dbPam.toString() + ".");

		dbPam.testdbGetIntFromSelect();
	    Global.logger.info("**************************************************************");
	}
}