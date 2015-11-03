package lu.uni.fstc.tests;

import lu.uni.fstc.proactivity.parameters.DbConnectionParameters;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 *
 */
public class TestCFGFile1 {
	/**
	 * @throws Exception
	 */
/*/	public TestCFGFile1() throws Exception{
		final Document doc = Utils.openConfigFile("lu.uni.fstc.tests/pam.cfg.xml");
//		final DbConnectionParameters connParams = readSettings(doc);
		final DbConnectionParameters connParams = new DbConnectionParameters();
		connParams.readSettings(doc);
		String errorMsg = connParams.validateConfiguration();
		if (!errorMsg.isEmpty()) throw new ConfigurationException(errorMsg);
		Global.logger.info("" + connParams.toString());
	}
/**/
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(final String[] args) throws Exception {
		DbConnectionParameters connParams = new DbConnectionParameters("pam.cfg.xml");
		Global.logger.info("PAM server - " + connParams.toString());
		connParams = new DbConnectionParameters("moodle.cfg.xml");
		Global.logger.info("Moodle server - " + connParams.toString());
	}
}