package lu.uni.fstc.proactivity.ruleRunningSystem;

import lu.uni.fstc.proactivity.db.MoodleCacheDbWrapper;
import lu.uni.fstc.proactivity.db.PAMDbWrapper;
import lu.uni.fstc.proactivity.messaging.PamSockekServer;
import lu.uni.fstc.proactivity.utils.Global;


/**
 * <b>Rule Running System</b>, MAIN engine that starts PAMELA proactivity
 * 
 * @author Sandro Reis
 * @version 3.x - Sandro Reis © 2011-2013
 * 
 */
public class WSSLauncher {

	/**
	 * <b>accepted first parameter:</b><p>
	 * <ul><li><i>start</i>: launch queue manager=proactive engine
	 * <li><i>restart</i>: reset and launch queue manager
	 * <li><i>stop</i>: stop queue manager
	 * <li><i>reset</i>: reset queue manager flags
	 * </ul>
	 * <b>accepted second parameter (optional):</b><p>
	 * <ul><li><i>local</i>: starts the engine without the any db reference
	 * <li><i>local</i>: starts the engine without the any db reference
	 * </ul>
	 * @param args <br>
	 * @throws Exception 
	 */
	public static void main(final String[] args) throws Exception {
		MoodleCacheDbWrapper dataNativeSystem = null;
		PAMDbWrapper dataEngine = null;

		Global.wssLogger.info("**************************************************************");
	    Global.wssLogger.info("********************* Starting WSS Main! *********************");


		// "PROXY design pattern": only at creation the programmer has to know the exact type of the database wrapper to instantiate it properly
		// (in this case, a proxy with cache to a MySQL connection to Moodle(TM))
		// from now on every call should be generic to the AbstractMoodleDbWrapper abstract class
		dataNativeSystem = MoodleCacheDbWrapper.getInstance("moodle.cfg.xml");
		if (!dataNativeSystem.isConnected()) {
			Global.wssLogger.severe("Unable to connect to the Moodle database. Exiting the application!");
			return;
		}
		Global.wssLogger.finer("MoodleCacheDbWrapper was created as " + dataNativeSystem.toString() + ".");

		dataEngine = PAMDbWrapper.getInstance("pam.cfg.xml");

		if (!dataEngine.isConnected()) {
			Global.wssLogger.severe("Unable to connect to the PAM database. Exiting the application!");
			return;
		}
		Global.wssLogger.finer("PAMDbWrapper was created as " + dataEngine.toString() + ".");

		// Create the socket server, set up the security connection values, but don't launch it now!
		try {
			final PamSockekServer socketServer = new PamSockekServer (PamSockekServer.DEFAUL_PORT, dataNativeSystem, dataEngine, "privateKeyStore.cfg.xml");

			final Thread qmThread = new Thread(socketServer);
			Global.wssLogger.info("Starting the WSS!");
			qmThread.start();
			
		} catch (final Exception e) {
			Global.wssLogger.severe("Unable to set up a secure connection to the Socket Server. Exiting the application!");
			e.printStackTrace();
			return;
		}

		Global.wssLogger.info("********************* Exiting WSS Main! **********************");
	    Global.wssLogger.info("**************************************************************");
	}
}