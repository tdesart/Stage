package lu.uni.fstc.proactivity.eteach;

import lu.uni.fstc.proactivity.db.ETeachCacheDbWrapper;
import lu.uni.fstc.proactivity.db.EteachEngineDbWrapper;
import lu.uni.fstc.proactivity.eteach.events.Operations;
import lu.uni.fstc.proactivity.ruleRunningSystem.ProactiveEngine;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.Time;

/**
 * <b>Rule Running System</b>, MAIN engine that starts E-TEACH proactivity
 * 
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014
 * 
 */
public class EteachLauncher {
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
		ETeachCacheDbWrapper dataNativeSystem = null;
		EteachEngineDbWrapper dataEngine = null;

		Global.logger.info("");
//		Global.logger.info("**************************************************************");
	    Global.logger.info("******************* Starting ETeach Main! ********************");
//	    Global.logger.info("**************************************************************\n");

		String command = "";
		if (args.length > 0) {
			// Get the first argument from the command line
			command = args[0];
		}

		// "PROXY design pattern": only at creation the programmer has to know the exact type of the database wrapper to instantiate it properly
		// (in this case, a proxy with cache to a MySQL connection to Moodle(TM))
		// from now on every call should be generic to the AbstractMoodleDbWrapper abstract class
		dataNativeSystem = ETeachCacheDbWrapper.getInstance("eteach.cfg.xml");
		if (!dataNativeSystem.isConnected()) {
			Global.logger.severe("Unable to connect to the ETeach database. Exiting the application!");
			return;
		}
		Global.logger.finer("ETeachCacheDbWrapper was created as " + dataNativeSystem.toString() + ".");

		dataEngine = EteachEngineDbWrapper.getInstance("eteach.cfg.xml");
		if (!dataEngine.isConnected()) {
			Global.logger.severe("Unable to connect to the ETeach database. Exiting the application!");
			return;
		}
		Global.logger.finer("EteachEngineDbWrapper was created as " + dataEngine.toString() + ".");

		// starts the engine without the InitRuleList (3rd Parameter)
		qm = new ProactiveEngine (dataNativeSystem, dataEngine);

		if (command.compareToIgnoreCase("stop") == 0) {
			stop(qm);
		}
		else if (command.compareToIgnoreCase("start") == 0) {
			Operations.setDb(dataNativeSystem);
			
			start(qm);
		}
		else if (command.compareToIgnoreCase("restart") == 0) {
			reset(qm);
			Operations.setDb(dataNativeSystem);
			start(qm);
		}
		else if (command.compareToIgnoreCase("reset") == 0) {
			reset(qm);
		}
		else {
			Global.logger.severe("Wrong or no parameters! Valid first parameter: 'start', 'restart', 'stop' or 'reset'.");
		}

		Global.logger.config("EteachEngineDbWrapper = " + EteachEngineDbWrapper.connParams.toString());
		Global.logger.config("ETeachCacheDbWrapper  = " + ETeachCacheDbWrapper.connParams.toString());

//	    Global.logger.info("**************************************************************");
//		Global.logger.info("******************* Exiting ETeach Main! *********************");
//	    Global.logger.info("**************************************************************");
	}

	private static void stop(final ProactiveEngine qm) {
		Global.logger.info("Sending stop to Engine!\n");
		qm.forceStop();
	}

	private static void start(final ProactiveEngine qm) {
		Global.logger.fine("Creating the Engine!");
		final Thread qmThread = new Thread(qm);
		
//		Global.logger.info("Initialising the Rule List!\n");
//		initRuleList(qm);
		Global.logger.fine("Starting the Engine!\n");
		qmThread.start();
	}

	private static void reset(final ProactiveEngine qm) {
		Global.logger.info("Reseting the Engine!\n");
		qm.forceStop(); // to make sure any possible running engine will detect it and stop
		Time.sleepNMilliseconds(5500); //pause half a second more then one iteration 
		qm.clearRunning(); // clear running flag to allow for a new engine instance to start
	}
}