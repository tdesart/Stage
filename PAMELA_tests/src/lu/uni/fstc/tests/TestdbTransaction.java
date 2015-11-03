package lu.uni.fstc.tests;

import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.db.PAMDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * <b>Rule Running System, principal engine that starts PAMELA</b>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011/2012
 * 
 */
public class TestdbTransaction {
	/**
	 * @param args 
	 * @throws Exception 
	 */
	public static void main(final String[] args) throws Exception {
	    Global.logger.info("**************************************************************");

		final AbstractPAM2MoodleDbWrapper dbPam = PAMDbWrapper.getInstance("pam.cfg.xml");
		if (!dbPam.isConnected()) {
			Global.logger.severe("Unable to connect to the PAM database. Exiting the application!");
			return;
		}
		Global.logger.finer("AbstractPAMDbWrapper was created as " + dbPam.toString() + ".");

		dbPam.testTransactionExecution1();
//		dbPam.testBatchExecution();
//		dbPam.testBatchExecutionWithTransaction();
	    Global.logger.info("**************************************************************");
	}
}