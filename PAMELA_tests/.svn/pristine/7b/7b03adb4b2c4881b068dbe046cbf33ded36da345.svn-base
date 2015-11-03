package lu.uni.fstc.test_nRules;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lu.uni.fstc.proactivity.ruleRunningSystem.ProactiveEngine;
import lu.uni.fstc.proactivity.rules.AbstractRule;

import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.db.MoodleCacheDbWrapper;
import lu.uni.fstc.proactivity.db.PAMDbWrapper;
import lu.uni.fstc.proactivity.queue.Queue;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.Time;

/**
 * <b>Rule Running System</b>, principal engine that starts the queue manager
 * 
 * @author Sandro Reis
 * @version 2014
 * 
 */
public class Launch_Test_nRules {

	static final long NRULES = 100;
	static final long MAX_ITERATIONCOUNT = 20;
	static final boolean DO_SAVE = true;

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
		AbstractMoodleDbWrapper db = null;
		AbstractPAM2MoodleDbWrapper dbPam = null;

		Global.logger.info("**************************************************************");
	    Global.logger.info("*********************** Starting Main! ***********************");

		String command = "";
		if (args.length > 0) {
			// Get the first argument from the command line
			command = args[0];
		}

		Global.logger.fine("The Engine is NOT in stand alone mode!");
		
		// "PROXY design pattern": only at creation the programmer has to know the exact type of the database wrapper to instantiate it properly
		// (in this case, a proxy with cache to a MySQL connection to Moodle(TM))
		// from now on every call should be generic to the AbstractMoodleDbWrapper abstract class
		db = MoodleCacheDbWrapper.getInstance("moodle.cfg.xml");
		if (!db.isConnected()) {
			Global.logger.severe("Unable to connect to the Moodle database. Exiting the application!");
			return;
		}
		Global.logger.finer("AbstractMoodleDbWrapper was created as " + db.toString() + ".");

		dbPam = PAMDbWrapper.getInstance("pam.cfg.xml");

		if (!dbPam.isConnected()) {
			Global.logger.severe("Unable to connect to the PAM database. Exiting the application!");
			return;
		}
		Global.logger.finer("AbstractPAMDbWrapper was created as " + dbPam.toString() + ".");

		// starts the engine without the InitRuleList (3rd Parameter)
		qm = new ProactiveEngine (db, dbPam);
		qm.save_queue = DO_SAVE;
		qm.Max_iterationCount = MAX_ITERATIONCOUNT;
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

		Global.logger.finest("AbstractPAMDbWrapper    = " + AbstractPAM2MoodleDbWrapper.connParams.toString());
		Global.logger.finest("AbstractMoodleDbWrapper = " + AbstractMoodleDbWrapper.connParams.toString());

		Global.logger.info("*********************** Exiting Main! ************************");
	    Global.logger.info("**************************************************************");
	}

	private static void stop(final ProactiveEngine qm) {
		Global.logger.info("Sending stop to Engine!");
		qm.forceStop();
	}

	private static void start(final ProactiveEngine qm) {
		final Thread qmThread = new Thread(qm);
		Global.logger.info("Creating the initial Rule List!");
		initializeQueue(qm);
		
		Global.logger.info("Starting the Engine!");
		qmThread.start();
	}

	private static void reset(final ProactiveEngine qm) {
		Global.logger.info("Reseting the Engine!");
		qm.forceStop(); // to make sure any possible running engine will detect it and stop
		Time.sleepNMilliseconds(5500); //pause half a second more then one iteration
		qm.clearRunning(); // clear running flag to allow for a new engine instance to start
	}

	private static void initializeQueue(final ProactiveEngine qm){
		Global.logger.config("Initialing Rule list from Launch, not QM ...!\n");

		final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		Queue<AbstractRule> initRulesList;
		qm.currentQueue = new Queue<AbstractRule>();
		
		for (int i =0 ; i<NRULES; i++) {
			final Date date = new Date();

			AbstractRule rule = new TestRule("Remus " + dateFormat.format(date));
			rule.setEngine(qm);
			rule.setDataNativeSystem(qm.dataNativeSystem.getThis());
			rule.setDataEngine(qm.dataEngine.getThis());

			qm.currentQueue.enqueue(rule);
		}
	}
}