package lu.uni.fstc.tests;

import lu.uni.fstc.proactivity.utils.Global;

import lu.uni.fstc.proactivity.ruleRunningSystem.ProactiveEngine;
import lu.uni.fstc.proactivity.rules.AbstractRule;
import lu.uni.fstc.proactivity.rules.RuleType;

/**
 * <b>Rule that only prints the input string on the system output, for test purposes</b>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 * 
 */
public class Rule_PrintOnScreen extends AbstractRule {

	private String txt;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 * 
	 */
	public Rule_PrintOnScreen() {
		setType(RuleType.MESSAGE);
	}

	/**
	 * @param txt 
	 * 
	 */
	public Rule_PrintOnScreen(String txt) {
		this();
		setTxt(txt);
	}

	/**
	 * @param engine the engine
	 * @param txt 
	 * 
	 */
	public Rule_PrintOnScreen(ProactiveEngine engine, String txt) {
		this(txt);
		setEngine(engine);
	}

	/**
	 * @see rules.AbstractRule#actions()
	 */
	@Override
	protected void actions() {
		Global.logger.info(getTxt());
	}

	/**
	 * @see rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		if (txt.compareTo("Rule n. 2") != 0 && txt.compareTo("Rule n. 4") != 0)
			return true;
		else 
			return false;
	}

	/**
	 * @see rules.AbstractRule#conditions()
	 */
	@Override
	protected boolean conditions() {
		return true;
	}

	/**
	 * @see rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
	}

	/**
	 * @return the text
	 */
	public String getTxt() {
		return txt;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.info(this.toString());
		if (getActivated())
			createRule(this);
		else 
			createRule(new Rule_PrintOnScreen(txt+" [Added]"));
		
		return true;
	}

	/**
	 * @param txt the text to set
	 */
	public void setTxt(String txt) {
		this.txt = txt;
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return txt;
	}
}