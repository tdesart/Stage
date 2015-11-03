package lu.uni.fstc.proactivity.ruleRunningSystem;

import java.util.Iterator;
import java.util.List;

import lu.uni.fstc.proactivity.db.AbstractEngineDataAccessWrapper;
import lu.uni.fstc.proactivity.db.AbstractHostDataAccessWrapper;
import lu.uni.fstc.proactivity.db.Persistence;
import lu.uni.fstc.proactivity.messaging.PamSockekServer;
import lu.uni.fstc.proactivity.queue.Queue;
import lu.uni.fstc.proactivity.rules.AbstractRule;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.StringUtils;

/**
 * <b>Class that manages the Thread Proactive Engine</b>
 * this is an extension of the generic AbstractProactiveEngine, that adds Persistence and Logging to the generic one 
 * 
 * @author Sandro Reis 
 * @version 3.x - Sandro Reis © 2011-2013 
 * @version 2.0 - Sergio Marques Dias <br> 
 * @version 1.0 - Yann Milin
 * 
 */
public class ProactiveEngine extends GenericProactiveEngine{

	/**
	 * boolean that signals if we want to save the Q on the database
	 */
	public boolean save_queue = true;

	/**
	 * Constant that represents the simple class name of AbstractRule
	 */	
	final String RuleClassName = StringUtils.stringAfter(AbstractRule.class.toString(), ".");

	/**
	 * Constant that represents the simple class name of SystemParameters
	 */	
	private final String SysParamClassName = StringUtils.stringAfter(SystemParameters.class.toString(), ".");

	/**
	 * The Socket Server that we use to communicate with the clients
	 */
	public PamSockekServer socketServer;

	Statistics iterationStats;
	StatisticsOnExecution executionStats;

	/**
	 ******************************************
	 ******************************************
	 ************   Constructors   ************
	 ******************************************
	 ******************************************
	 */

	/**
	 * @param db the AbstractMoodleDbWrapper 
	 * @param dbPam the AbstractPAMDbWrapper
	 */
	public ProactiveEngine(final AbstractHostDataAccessWrapper db, final AbstractEngineDataAccessWrapper dbPam) {
		super (db, dbPam);
	}

	/**
	 * @param db the AbstractMoodleDbWrapper 
	 * @param dbPam the AbstractPAMDbWrapper
	 * @param initRulesList the initial Queue
	 */
	public ProactiveEngine(final AbstractHostDataAccessWrapper db, final AbstractEngineDataAccessWrapper dbPam, final Queue<AbstractRule> initRulesList) {
		super(db, dbPam, initRulesList);
	}

	/**
	 ******************************************
	 ******************************************
	 *******   Managing the execution   *******
	 ******************************************
	 ******************************************
	 */

	/**
	 * Method to centralise the reading of ALL system parameters from the database<br>
	 * It is assumed that all parameters are in one register of the table mapping the parameters object
	 */
	@Override
	public void getSystemParameters() {
		List<?> li = Persistence.getClassData(SysParamClassName);
		Iterator<?> iterator = li.iterator();

		// only one line in the table, so read the first...
		if (iterator.hasNext())
			params = (SystemParameters) iterator.next(); 
	}

	@Override
	public void forceStop() {
		super.forceStop();

		// code to force the socket server to stop and clear up every connection
		// IT DOESN'T WORK properly
		/*/
		try {
			socketServer = new PamSockekServer(PamSockekServer.DEFAUL_PORT);
			socketServer.stop();
			Global.logger.info(socketServer.toString());
		} catch (IOException e) {
			Global.logger.severe("Exception Msg: "+ e.getMessage());
			e.printStackTrace();
		}
		/**/
	}

	@Override
	protected void finalize() {
		try {
			super.finalize();
			Global.logger.warning("Finalize method called 'terminateQueueIteration()'");
		} catch (final Throwable e) {
			Global.logger.severe("Finalize method threw an exception:" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 ******************************************
	 ******************************************
	 ************   Core Methods   ************
	 ******************************************
	 ******************************************
	 */

	/**
	 * Initialise the Current Rule List from the database 
	 */
	@Override
	protected void initializeRuleList() {
		Global.logger.config("Initialing Rule list from within the Engine...!\n");
		int nRules = 0;
		this.currentQueue = new Queue<AbstractRule>();

		final List<?> li = Persistence.getClassData(RuleClassName);
		final Iterator<?> iterator = li.iterator();

		while (iterator.hasNext()) {
			nRules++;
			final AbstractRule rule = (AbstractRule) iterator.next();
			rule.setEngine(this);
			rule.setDataNativeSystem(this.dataNativeSystem.getThis());
			rule.setDataEngine(this.dataEngine.getThis());

			this.currentQueue.enqueue(rule);
			Global.logger.finer("rule [" + nRules + "]= " +rule.toString()+", " + rule.getClass());
			Global.logger.finer("QM's   dbs: this.db =" + this.dataNativeSystem + ", this.dbPam =" + this.dataEngine);
			Global.logger.finer("rule's dbs: db =" + rule.getDataNativeSystem() + ", dbPam =" + rule.getDataEngine());
		}
	}

	@Override
	protected void initializeEngine() {
		if (this.currentQueue == null)
			Global.logger.finer("Queue not initiliazed! going for the db to read...");
		else
			Global.logger.finer("Queue already initialized! ");

		super.initializeEngine();
		
		// Initialise Statistics from one Execution (with the system parameters)
		executionStats = new StatisticsOnExecution(params);

		// save the executionStats on Hibernate so that its ID is created and thus, available
		// to save in each iteration as a parent_id
		Persistence.beginTransaction();
		Persistence.save(executionStats, true);

		// after saving the first time, I now have the 'id' to store as (its own) parent, and save again
		executionStats.setExecution_id(executionStats.getId());
		Persistence.save(executionStats, false);
		Persistence.commitTransaction(false);
	}

	@Override
	protected void initializeIteration() {
		// Initialise Statistics from one Iteration
		iterationStats = new Statistics(executionStats.getId());
		executionStats.incnIterations();
		executionTime = 0;

		Global.logger.fine("Initializing Queue Iteration n." + executionStats.getnIterations());
	}

	@Override
	protected AbstractRule executeNextRule() {
		Global.logger.finer("Rule n." + iterationStats.getnRulesExecuted());
		
		final AbstractRule currentRule = super.executeNextRule();
//		Global.logger.info("Rule:" + currentRule.toString());
//		Global.logger.info("Rule type: " + currentRule.getType());
		
		
		// Treat Statistics on the execution of this ONE rule
		currentRule.getType().incExecutedCount(); // for statistics purposes

		// fake processing time for testing purposes
		//Time.seconds(0.245999*(iterationStats.getnRulesExecuted()+1)*executionStats.getnIterations());	// good for 4 rules (N=5)
		//Time.seconds(0.16*(iterationStats.getnRulesExecuted())*executionStats.getnIterations());		// good for 6 rules (N=5)
		executionTime = System.currentTimeMillis() - iterationStats.getStartDate();

		return currentRule;
	}

	@Override
	protected void terminateIteration() {
		Global.logger.finer("Method enter");
		executionTime = System.currentTimeMillis() - iterationStats.getStartDate();

		super.terminateIteration();
		
		// updates Statistics (Iteration + Execution)
		iterationStats.addExecutionTime(executionTime);
		iterationStats.setCounts();
		executionStats.addExecutionTime(executionTime);
		executionStats.setCounts();
		iterationStats.finalise(this.currentQueue.length());
		Statistics.resetCounts();
	
		Persistence.beginTransaction();
	
		if (save_queue) {
			// replaces queue on database with data from current queue
			Persistence.delClassData(RuleClassName);
			this.currentQueue.saveQueue();
		}
	
		// saves Statistics (Iteration)
		Persistence.save(iterationStats, true);
	
		// updates Statistics (Execution)- although not finished with the execution yet
		Persistence.save(executionStats, false);
	
		Persistence.commitTransaction(false);
		Global.logger.finer("Method exit");
	}

	@Override
	protected void terminateEngine() {
		Global.logger.finer("Method enter");
		// FIXME this doesn't stop all client threads. WHY ??
		// see WebSocketServer.java	@ "/Java-WebSocket/src/org/java_websocket/server" line 374

/*/
		try {
			// socketServer might be null here, maybe in the case the QM is unable to start (2nd instance?)
			socketServer.stop();
//			socketServer.finalize();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
/**/
		super.terminateEngine();

		// save Statistics from one Execution
		Persistence.beginTransaction();
		executionStats.finalise(this.currentQueue.length());
		Persistence.save(executionStats, false);
		Persistence.commitTransaction(true);
		Global.logger.finer("Method exit");
	}

	@Override
	public void run() {

		// refuse to start, AND LOG IT, if already running any instance (or set to run)
		if (isRunning()) {
		    Global.logger.warning("Unable to run a second instance! Exiting...");
			return;
		}

//		if (!initializeSockectServer()) {
//			return;
//		}

		super.run();

		// Logging the reason for exiting the execution
//		if (isScheduleToStop())
		if (this.currentQueue.isEmpty())
			Global.logger.info("Current Queue is EMPTY!");
		else if (iterationCount >= Max_iterationCount)
			Global.logger.info("iterationCount has reached its limit =" + Max_iterationCount);
		else
			Global.logger.info("Received a STOP message!");

		Global.logger.info("******************** Engine STOPped! *********************");
	}

	@Override
	public void addRule2Queue(final AbstractRule rt, final Queue<AbstractRule> queue) {
		if (rt == null)
			Global.logger.warning("Trying to add null RULE to a queue!");
		else
			super.addRule2Queue(rt, queue);
	}
}