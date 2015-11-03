package lu.uni.fstc.tests;

import lu.uni.fstc.proactivity.rules.AbstractRule;
import lu.uni.fstc.proactivity.rules.Importance;
import lu.uni.fstc.proactivity.rules.RuleMessageMoodle;
import lu.uni.fstc.proactivity.rules.RuleType;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2013
 *
 */
public class TestMSG001comSocketServer extends AbstractRule {

	private long last_ind;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private TestMSG001comSocketServer() {
		setType(RuleType.META);
	}

	/**
	 * @param index 
	 */
	public TestMSG001comSocketServer(long index) {
		this();
		setLast_ind(index);
	}

	/**
	 * @see rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
	}

	/**
	 * @see rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		Global.logger.info("This index = [" + getLast_ind() + "] ");
		return (getLast_ind() == 4);
	}

	/**
	 * @see rules.AbstractRule#conditions()
	 */
	@Override
	protected boolean conditions() {
		return true;
	}

	/**
	 * @see rules.AbstractRule#actions()
	 */
	@Override
	protected void actions() {
		// no actions, only Rule Generation
	}

	/**
	 * @see rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		if (getActivated()) {
			String subject = "Notification: 'FAKE'";
			String text  = "This notification is related to your new assignment 'NONE' for the course 'NOPE'. \n";
				   text += "Please take into consideration the deadline: 'FOREVER'!";
			createRule(new RuleMessageMoodle(2, subject, text, Importance.NORMAL));
		}
		else
			createRule(new TestMSG001comSocketServer(getLast_ind()+1));
		return true;
	}

	/**
	 * @return the last_ind
	 */
	public final long getLast_ind() {
		return this.last_ind;
	}

	/**
	 * @param last_index the last_ind to set
	 */
	public final void setLast_ind(long last_index) {
		this.last_ind = last_index;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return "MTA TEST ss - Meta-scenario: index = " + this.getLast_ind();
	}
}