package lu.uni.fstc.proactivity.ruleRunningSystem;


import lu.uni.fstc.proactivity.db.AbstractEngineDataAccessWrapper;
import lu.uni.fstc.proactivity.db.AbstractHostDataAccessWrapper;
import lu.uni.fstc.proactivity.queue.Queue;
import lu.uni.fstc.proactivity.rules.AbstractRule;
import lu.uni.fstc.proactivity.utils.Time;

/**
 * <b>Class that manages the Thread Proactive Engine</b>
 * This is a generic ProactiveEngine, that only does the basic control flow of the engine.<p>
 * You have to extend this class, to add functionalities, like logging or persistence for the rules, for example.<p>
 * This class is abstract because you have to set up the initial rule list, by overriding the method <i>initializeRuleList</i>, according to your system and your needs.
 *  
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2015
 *
 */
public class GenericProactiveEngine implements Runnable {

	/**
	 * Maximum Iterations Allowed - <br>
	 * should be used with a different value (than Long.MAX_VALUE) for test purposes only 
	 */
	public long Max_iterationCount = Long.MAX_VALUE;

	protected long iterationCount;

	protected long executionTime;

	protected AbstractRule currentRule;

	/**
	 * The queue of rules running in the current iteration
	 */
	public Queue<AbstractRule> currentQueue;
	/**
	 * The queue of rules to be ran on the next iteration
	 */
	public Queue<AbstractRule> nextQueue;
	/**
	 * The Moodle database wrapper to run the queries in
	 */
	public AbstractHostDataAccessWrapper dataNativeSystem;
	/**
	 * The PAM database wrapper to run the queries in
	 */
	public AbstractEngineDataAccessWrapper dataEngine;
	
	/**
	 * System Parameters to be read by Hibernate to this object at initialisation
	 */
	public SystemParameters params;
	
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
	public GenericProactiveEngine(final AbstractHostDataAccessWrapper db, final AbstractEngineDataAccessWrapper dbPam) {
		this (db, dbPam, null);
	}

	/**
	 * @param db the AbstractMoodleDbWrapper 
	 * @param dbPam the AbstractPAMDbWrapper
	 * @param initRulesList the initial Queue
	 */
	public GenericProactiveEngine(final AbstractHostDataAccessWrapper db, final AbstractEngineDataAccessWrapper dbPam, final Queue<AbstractRule> initRulesList) {
		this.dataNativeSystem = db;
		this.dataEngine = dbPam;
		this.currentQueue = initRulesList;
	}

	/**
	 ******************************************
	 ******************************************
	 *******   Managing the execution   *******
	 ******************************************
	 ******************************************
	 */
	
	/**
	 * Method to centralise the reading of ALL system parameters from where ever they are<br>
	 * If not overridden, then the default values kick in...
	 */
	protected void getSystemParameters(){
		params = new SystemParameters();
		params.setDefaulValues();
	}

	/**
	 * Stop the engine from running the rules at the end of the current iteration
	 */
	public void forceStop() {
		setScheduleToStop(true);
	}

	/**
	 * @return the scheduleToStop
	 */
	public boolean isScheduleToStop() {
		return dataEngine.isScheduleToStop();
	}

	/**
	 * @param scheduleToStop the scheduleToStop to set
	 */
	protected void setScheduleToStop(final boolean scheduleToStop) {
		dataEngine.setScheduleToStop(scheduleToStop);
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return dataEngine.isRunning();
	}

	/**
	 * Clear the running flag
	 * NOTE: don't call this unless you're ABSOLUTELY sure the engine ended abnormally
	 */
	public void clearRunning() {
		setRunning(false);
	}

	/**
	 * @param running the running to set
	 */
	protected long setRunning(final boolean running) {
		return dataEngine.setRunning(running);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		terminateIteration();
	}

	/**
	 ******************************************
	 ******************************************
	 ************   Core Methods   ************
	 ******************************************
	 ******************************************
	 */

	/**
	 * Initializes the Current Rule List<p>
	 * If left empty, the engine will not have anything to execute, thus, dying immediately.<p>
	 * So, be sure to <b>extend it</b> with the right code to initialize the list<br>
	 * or <b>initialize</b> that same list, a priori, for example <b>in the launcher</b>.<p>
	 * If the list is not initialized before, or this method is not overridden, <b>this method is executed with an empty queue</b>, and a Runtime Exception is thrown, to signal the programmer that something is badly designed!
	 */
	protected void initializeRuleList() throws RuntimeException {
		/*  // example of how to add one rule (MTA001m) to the queue 
		 	final AbstractRule rule = new rules.moodleAssignments.MTA001m(0);
			rule.setEngine(this);
			rule.setDataNativeSystem(dataNativeSystem.getThis());
			rule.setDataEngine(dataEngine.getThis());
			currentQueue.enqueue(rule);
		 /**/
		
		if (this.currentQueue == null)
			throw new sun.reflect.generics.reflectiveObjects.NotImplementedException();
	}

	/**
	 * Operations needed at the start of the engine come here<p>
	 */
	protected void initializeEngine() {
		// if engine was initialised without a queue, initialize it 
		if (this.currentQueue == null)
			initializeRuleList();

		// Read the system parameters from the database
		getSystemParameters();
	
		// Create and Initialise Queue
		this.nextQueue = new Queue<AbstractRule>();
	}

	/**
	 * Operations needed at the start of each iteration come here<p>
	 * by default, nothing is necessary;
	 */
	protected void initializeIteration(){
	}

	protected AbstractRule executeNextRule() {
		// dequeue (get and remove) rule from current queue
		final AbstractRule currentRule = this.currentQueue.dequeue();

//		Global.logger.info("Rule:" + currentRule.toString());
//		Global.logger.info("Rule type: " + currentRule.getType());
		
		// Execute the Rule
		currentRule.execute();
		
		return currentRule;
	}

	/**
	 * Operations needed at the end of an Iteration come here
	 */
	protected void terminateIteration() {
		// moves the [elements of] next queue to the current queue
		this.currentQueue.enqueue(nextQueue);

		// empties the next queue
		this.nextQueue.initializeQueue();

		// executes the actions needed at the end of an iteration, like clearing the database cache, for example
		dataNativeSystem.terminateOperation();
	}

	/**
	 * Operations needed at the end of an Execution come here
	 */
	protected void terminateEngine() {
		// CLEAR FLAGS: Set to false as soon as the loop has finished to allow for other threads to enter
		setRunning(false); 
		setScheduleToStop(false);
	}

	/**
	 * method that controls the execution of the thread
	 */
	@Override
	public void run() {

		iterationCount = 0;
		long ruleCount = 0;

		setScheduleToStop(false);
		
		// refuse to start if already running any instance (or set to run)
		if (setRunning(true) == 0)
			return;
		
		initializeEngine();

		/* 
		 * Conditions to stop one execution of rules:
		 * 1 - the current queue is now empty
		 * 2 - The ENGINE has received a STOP signal (ex: from the GUI when the systems parameters have changed)
		 * 
		 * Doesn't even start if the currentQueue is empty after initialisation
		 */
//		while (!isScheduleToStop() && !this.currentQueue.isEmpty()) {
		while (!isScheduleToStop() && !this.currentQueue.isEmpty() && iterationCount < Max_iterationCount) {

			iterationCount++;
			ruleCount = 0;
			initializeIteration();
			/* 
			 * Each iteration represents one cycle of running all rules in the current rule list
			 * Conditions to stop one iteration of rules execution:
			 * 1 - the current queue is now empty
			 * 2 - the number of rules executed in this iteration has reached the limit as defined by parameter N
			 * 3 - the time this iteration took has reached the limit as defined by parameter F  
			 */
			while ( ( !this.currentQueue.isEmpty() ) &&
					( ruleCount < params.N ) &&
					( executionTime  <= params.F ) ) {
				ruleCount++;
				executeNextRule();
			}
			terminateIteration();
			pauseIfNecessary(executionTime);
		}

		// clean-up
		terminateEngine();
	}

	/**
	 * Pauses the execution of the rules, to allow time for the host system to run
	 */
	protected void pauseIfNecessary(final long executionTime) {
		/* This first condition is only to avoid pausing if
		 *   the Engine is about to stop OR
		 *   the Current queue is empty,
		 * --> meaning there will be no more iterations anyway 
		 */
		if (isScheduleToStop() || this.currentQueue.isEmpty()) 
			return;

		final long timeLeft = params.F - executionTime;
		
		// Pause the amount of time that is bigger:
		//	(params.P) Execution time was too long: time left less than P.;
		//  (timeLeft) Execution time was OK: time left greater than P.;
		Time.sleepNMilliseconds(Math.max(params.P, timeLeft));
	}

	/**
	 * Adds a rule to the queue
	 * @param rt the rule to add
	 * @param queue the Queue to add the rule to
	 */
	public void addRule2Queue(final AbstractRule rt, final Queue<AbstractRule> queue) {
		if (rt != null && queue != null)
			queue.enqueue(rt);
	}
}