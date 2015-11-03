package lu.uni.fstc.proactivity.rules;


import lu.uni.fstc.proactivity.db.AbstractEngineDataAccessWrapper;
import lu.uni.fstc.proactivity.db.AbstractHostDataAccessWrapper;
import lu.uni.fstc.proactivity.ruleRunningSystem.GenericProactiveEngine;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * <b>(Abstract) Definition of a rule: rules are specifications of this class.</b><br>
 * based on the RuleTemplate class defined on version 1.<br>
 * It was later <b>renamed</b> and <b>extended</b>.<br>
 * 
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2015 
 * @version 3.0 - Sandro Reis © 2011 
 * @version 2.0 - Sergio Marques Dias <br> 
 * @version 1.0 - Yann Milin
 * 
 */
public abstract class AbstractRule {
	private long id;
	private boolean activated;
	protected GenericProactiveEngine engine;
	protected AbstractHostDataAccessWrapper dataNativeSystem;
	protected AbstractEngineDataAccessWrapper dataEngine;

	// default value, to be changed on the constructor if needed
	private RuleType type = RuleType.SCENARIO; 

	/**
	 * default constructor, mandatory for Hibernate to build object<br>
	 * without these instructions, fields would be empty if rule is created by Hibernate
	 */
	public AbstractRule() {
/*/		// set the new rule's engine to this, if not yet set
		// so that, afterwards, in the new rules execution, this.engine is not null
		if (this.engine == null) 
			this.setEngine(this.engine);

		// these two assignments are necessary because the rules db and dbPam were re-casted to more specific types to provide access to database methods<br>
		// otherwise, trough the engine we don't have access to them
		this.setDb(this.engine.db);
		this.setDbPam(this.engine.dbPam);
/**/	}

	/* **********************************************************************************
	 * **********************************************************************************
	 * **********************************************************************************
	 * Abstract methods come here
	 * **********************************************************************************
	 * **********************************************************************************
	 * **********************************************************************************
	 */

	/**
	 * Get information from the LMS (Moodle DB) in order to use these data in its other parts
	 */
	protected abstract void dataAcquisition();

	/**
	 * Performed after the data acquisition part and is made of a set of 
	 * AND-connected tests on local variables that, once evaluated, determines 
	 * if the conditions and actions parts will be performed afterwards
	 * <br>
	 * If all the activation guards are evaluated positively, then the 
	 * conditions and actions parts are performed.
	 * <br>
	 * Boolean variable called “activated” set accordingly to the result 
	 * of the activation guards evaluation
	 * 
	 * @return result of the evaluation
	 */
	protected abstract boolean activationGuards();

	/**
	 * Made of a set of AND-connected tests on local variables that, once 
	 * evaluated, determines if the actions part will be performed afterwards.
	 * <br>
	 * Conditions tests syntax and semantics are equivalent to activation 
	 * guards tests.
	 * @return result of the condition's evaluation
	 */
	protected abstract boolean conditions();

	/**
	 * Made of a list of instructions that will be performed in sequence if 
	 * all the test of the conditions part are evaluated positively.
	 */
	protected abstract void actions();

	/**
	 * Performed at the end. It allows the rule to generate other(s) rule(s) 
	 * that will be performed afterwards.
	 */
	protected abstract boolean rulesGeneration();

	/**
	 * overrides Object.toString()
	 * implementations of AbstractRule are obliged to implement a specific toString Method!
	 * 
	 */
	@Override
	public abstract String toString();
	
	/* **********************************************************************************
	 * **********************************************************************************
	 * **********************************************************************************
	 * FINAL methods come here
	 * **********************************************************************************
	 * **********************************************************************************
	 * **********************************************************************************
	 */

	/**
	 * @param rule the instance of the rule to add to the queue to run in the next iteration 
	 */
	public final void createRule(final AbstractRule rule) {
		// if parameter is empty, do nothing
		if (rule == null)
			return;

		// set the new rule's engine to this, if not yet set
		// so that, afterwards, in the new rules execution, this.engine is not null
		if (rule.engine == null) 
			rule.setEngine(this.engine);

		// delegate on the right engine method to add the rule in the right place
		if (this.engine != null) {
			// these two assignments are necessary because the rules db and dbPam were re-casted to more specific types to provide access to database methods<br>
			// otherwise, trough the engine we don't have access to them
			rule.setDataNativeSystem(this.engine.dataNativeSystem);
			rule.setDataEngine(this.engine.dataEngine);

			this.engine.addRule2Queue(rule, this.engine.nextQueue);
		}
		else
			Global.logger.warning("Engine is null. Not able to add rule '" + rule + "' to rule's engine queue");
	}

	/**
	 * @return RulesGeneration return value
	 */
	public final boolean execute() {
		Global.logger.finer("Rule starting execution: " + this.toString());
//		String str = "End of Rule Execution. (" + this.getType() + ") "; // for logging purposes only
//		String str = "(" + this.getType() + ") "; // for logging purposes only
		String str = ""; 

		dataAcquisition();
		if (activationGuards()) {
			str += "Ativated: ";			// for logging purposes only
			this.activated = true;
			this.type.incActivatedCount();	// for statistics purposes

			if (conditions()) {
				this.type.incActedCount();	// for statistics purposes
				actions();
			}
		}
		else {
			str += "Not activated: ";		// for logging purposes only
			this.activated = false;
		}

		boolean ret = rulesGeneration();
		str += this.toString();
		if (this.activated) // differentiate logging
			Global.logger.fine(str);
		else
			Global.logger.finer(str);
		return ret;
	}

	/**
	 * @return Id
	 */
	protected final long getId() {
		return this.id;
	}

	/**
	 * @param i to set the Id
	 */
	protected final void setId(long i) {
		this.id = i;
	}

	/**
	 * @return activated
	 */
	protected final boolean getActivated() {
		return this.activated;
	}

	/**
	 * @param act to set  activated
	 */
	protected final void setActivated(boolean act) {
		this.activated = act;
	}

	/**
	 * @return the engine
	 */
	protected GenericProactiveEngine getEngine() {
		return this.engine;
	}

	/**
	 * @param engine the engine to set
	 */
	public final void setEngine(GenericProactiveEngine engine) {
		this.engine = engine;
	}

	/**
	 * @return the db
	 */
//	abstract public AbstractHostDataAccessWrapper getDataNativeSystem();
	public AbstractHostDataAccessWrapper getDataNativeSystem() {
		Global.logger.finer(this.dataNativeSystem.toString());
		return this.dataNativeSystem;
	}

	/**
	 * @param dataNativeSystem the db to set
	 */
//	abstract public void setDataNativeSystem(AbstractHostDataAccessWrapper dataNativeSystem);
	public void setDataNativeSystem(AbstractHostDataAccessWrapper dataNativeSystem) {
		Global.logger.finer("value IN AbstractMySQLDbWrapper in dataNativeSystem =" + dataNativeSystem);
		this.dataNativeSystem = dataNativeSystem;
		Global.logger.finer("value OUT this.dataNativeSystem =" + this.dataNativeSystem);
	}

	/**
	 * @return the dbPam
	 */
//	abstract public AbstractEngineDataAccessWrapper getDataEngine();
	public AbstractEngineDataAccessWrapper getDataEngine() {
		Global.logger.finer(this.dataEngine.toString());
		return this.dataEngine;
	}

	/**
	 * @param dataEngine the dbEngine to set
	 */
//	abstract public void setDataEngine(AbstractEngineDataAccessWrapper dataEngine);
	public void setDataEngine(AbstractEngineDataAccessWrapper dataEngine) {
		Global.logger.finer("value IN AbstractLocalDbWrapper in dbPam =" + dataEngine);
		this.dataEngine = dataEngine;
		Global.logger.finer("value OUT this.dbPam =" + this.dataEngine);
	}

	/**
	 * @return the ruleType
	 */
	public final RuleType getType() {
		return this.type;
	}

	/**
	 * @param type the ruleType to set
	 */
	public final void setType(RuleType type) {
		this.type = type;
	}
}