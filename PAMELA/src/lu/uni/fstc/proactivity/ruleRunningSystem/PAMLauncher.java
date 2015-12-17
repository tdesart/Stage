package lu.uni.fstc.proactivity.ruleRunningSystem;

import lu.uni.fstc.proactivity.db.ActiveDirWrapper;
import lu.uni.fstc.proactivity.db.MoodleCacheDbWrapper;
import lu.uni.fstc.proactivity.db.PAMDbWrapper;
import lu.uni.fstc.proactivity.messaging.PamSockekServer;
import lu.uni.fstc.proactivity.queue.Queue;
import lu.uni.fstc.proactivity.rules.AbstractRule;
import lu.uni.fstc.proactivity.rules.activeDir.AD_G;
import lu.uni.fstc.proactivity.rules.activeDir.mdp.AD_M;
import lu.uni.fstc.proactivity.rules.activeDir.suggestion.AD_S;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.Time;

/**
 * <b>Rule Running System</b>, MAIN engine that starts PAMELA proactivity
 * 
 * @author Sandro Reis
 * @version 3.x - Sandro Reis © 2011-2013
 * 
 */
public class PAMLauncher {

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
		final ProactiveEngine qm;
		//MoodleCacheDbWrapper dataNativeSystem = null;
		ActiveDirWrapper dataNativeSystem = null;
		PAMDbWrapper dataEngine = null;

		Global.logger.info("**************************************************************");
	    Global.logger.info("******************* Starting PAM Launcher! *******************");

		String command = "";
		if (args.length > 0) {
			// Get the first argument from the command line
			command = args[0];
		}

		Global.logger.fine("The Engine is NOT in stand alone mode!");
		
		// "PROXY design pattern": only at creation the programmer has to know the exact type of the database wrapper to instantiate it properly
		// (in this case, a proxy with cache to a MySQL connection to Moodle(TM))
		// from now on every call should be generic to the AbstractMoodleDbWrapper abstract class
		dataNativeSystem = ActiveDirWrapper.getInstance("activedir-data.cfg.xml");
		if (!dataNativeSystem.isConnected()) {
			Global.logger.severe("Unable to connect to the Moodle database. Exiting the application!");
			return;
		}
		Global.logger.finer("MoodleCacheDbWrapper was created as " + dataNativeSystem.toString() + ".");
		
		dataEngine = PAMDbWrapper.getInstance("activedir-engine.cfg.xml");

		if (!dataEngine.isConnected()) {
			Global.logger.severe("Unable to connect to the PAM database. Exiting the application!");
			return;
		}
		Global.logger.finer("PAMDbWrapper was created as " + dataEngine.toString() + ".");

		// starts the engine without the InitRuleList (3rd Parameter)
		qm = new ProactiveEngine (dataNativeSystem, dataEngine);

		
		// Create the socket server, set up the security connection values, and launch it now!
		/*boolean ok = false;
		if ((command.compareToIgnoreCase("stop") != 0) && (command.compareToIgnoreCase("reset") != 0)) {
			initRules(qm, dataNativeSystem, dataEngine);

			if (socketServerSetUp(qm, dataNativeSystem, dataEngine))
				ok = sockectServerLaunch(qm, dataNativeSystem, dataEngine);
			
			if (!ok) {
				Global.logger.severe("Unable to setup and launch the socket server. Exiting the application!");
				return;
			}
		}*/
		
		initRules(qm, dataNativeSystem, dataEngine);
		

		if (command.compareToIgnoreCase("stop") == 0) {
			stop(qm);
		}
		else if (command.compareToIgnoreCase("start") == 0) {
			start(qm);
		}
		else if (command.compareToIgnoreCase("restart") == 0) {
			reset(qm);
			start(qm);
		}
		else if (command.compareToIgnoreCase("reset") == 0) {
			reset(qm);
		}
		else {
			Global.logger.severe("Wrong or no parameters! Valid first parameter: 'start', 'restart', 'stop' or 'reset'.");
		}

		Global.logger.finest("PAMDbWrapper = " + PAMDbWrapper.connParams.toString());
		Global.logger.finest("MoodleCacheDbWrapper   = " + ActiveDirWrapper.connParams.toString());

		Global.logger.info("******************* Exiting PAM Launcher! ********************");
	    Global.logger.info("**************************************************************");
	}

	private static void stop(final ProactiveEngine qm) {
		Global.logger.info("Sending stop to Engine!");
		qm.forceStop();
	}

	private static void start(final ProactiveEngine qm) {
		final Thread qmThread = new Thread(qm);
		Global.logger.info("Starting the Engine (Thread)!");
		qmThread.start();
	}

	private static void reset(final ProactiveEngine qm) {
		Global.logger.info("Reseting the Engine!");
		qm.forceStop(); // to make sure any possible running engine will detect it and stop
		Time.sleepNMilliseconds(5500); //pause half a second more then one iteration
		qm.clearRunning(); // clear running flag to allow for a new engine instance to start
	}
	
	private static void initRules (final ProactiveEngine qm, final ActiveDirWrapper dataNativeSystem, final PAMDbWrapper dataEngine){

		Global.logger.info("Creating initial rules!");
		qm.currentQueue = new Queue<AbstractRule>();
		
		// Démarre suggestion
		AbstractRule rule = new AD_S();
		rule.setEngine(qm);
		rule.setDataEngine(dataEngine);
		rule.setDataNativeSystem(dataNativeSystem);
		qm.currentQueue.enqueue(rule);
		
		//Démarre mot de passe
		rule = new AD_M();
		rule.setEngine(qm);
		rule.setDataEngine(dataEngine);
		rule.setDataNativeSystem(dataNativeSystem);
		qm.currentQueue.enqueue(rule);
		
		//Démarre profil de groupe et detection groupe inutile
		rule = new AD_G();
		rule.setEngine(qm);
		rule.setDataEngine(dataEngine);
		rule.setDataNativeSystem(dataNativeSystem);
		qm.currentQueue.enqueue(rule);
//		
	}
	
	/**
	 * @param qm
	 * @param dataNativeSystem
	 * @param dataEngine
	 */
	private static boolean socketServerSetUp(final ProactiveEngine qm, final MoodleCacheDbWrapper dataNativeSystem, final PAMDbWrapper dataEngine) {
		try {
			final PamSockekServer socketServer = new PamSockekServer (PamSockekServer.DEFAUL_PORT, dataNativeSystem, dataEngine, "privateKeyStore.cfg.xml");
			qm.socketServer = socketServer;
		} catch (final Exception e) {
			Global.logger.severe("Unable to CREATE a secure connection to the Socket Server. Exiting the application!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Launching the Socket Server Thread
	 * @return a boolean
	 * @throws Exception
	 */
	private static boolean sockectServerLaunch(final ProactiveEngine qm, final MoodleCacheDbWrapper dataNativeSystem, final PAMDbWrapper dataEngine) {
		try {
			// If socket server was not created in the main, create it now.
			if (qm.socketServer == null)
				socketServerSetUp (qm , dataNativeSystem, dataEngine);

			// Launch the Thread
			Global.wssLogger.fine("Starting the WSS thread!");
			qm.socketServer.start();
		} catch (final Exception e) {
			Global.logger.severe("Unable to START the thread with a secure connection to the Socket Server. Exiting the application!");
			e.printStackTrace();
			return false;
		}
		return true;
	}
}