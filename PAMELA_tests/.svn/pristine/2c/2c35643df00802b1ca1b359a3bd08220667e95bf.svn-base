package lu.uni.fstc.tests;

import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.Time;

import lu.uni.fstc.proactivity.rules.AbstractRule;

/**
 * <b>Rule_Welcome rule</b>
 *  
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 * @version 2.0 - Sergio Marques Dias <br> 
 * @version 1.0 - Yann Milin
 * 
 */
public class Rule_TestEngineParams extends AbstractRule {

	private int rule_key;
	private int time_sleep;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	public Rule_TestEngineParams() {
	}

	/**
	 * When creating a Rule_Welcome Rule, the user name and course id are needed
	 * @param time_sleep
	 * @param rule_key
	 */
	public Rule_TestEngineParams(final int time_sleep, int rule_key) {
		setTime_sleep(time_sleep);
		setRule_key(rule_key);
		Global.logger.info("Rule_TestEngineParams rule created (object) with rule_key " + getRule_key() + ".");
	}

	@Override
	protected void actions() {
//		Global.logger.info(toString());
		Time.sleepNMilliseconds(getTime_sleep());
	}

	@Override
	protected boolean activationGuards() {
		createRule(this);
		return true;
	}

	@Override
	protected boolean conditions() {
		return true;
	}

	@Override
	protected void dataAcquisition() {
	}

	@Override
	protected boolean rulesGeneration() {
		return false;
	}

	/**
	 * @return the rule_key
	 */
	public int getRule_key() {
		return rule_key;
	}
	
	/**
	 * @param rule_key 
	 */
	public void setRule_key(int rule_key) {
		this.rule_key = rule_key;
	}
	
	/**
	 * @return the time_sleep
	 */
	public final int getTime_sleep() {
		return this.time_sleep;
	}

	/**
	 * @param time_sleep the time_sleep to set
	 */
	public final void setTime_sleep(int time_sleep) {
		this.time_sleep = time_sleep;
	}

	@Override
	public String toString() {
		return "Rule_TestEngineParams rule. Rule Key=" + this.rule_key + ". Time Sleep=" + this.time_sleep + ".";
	}
}