package lu.uni.fstc.proactivity.rules.moodleAssignments;

import java.util.ArrayList;
import java.util.GregorianCalendar;


import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @author Gilles Neyens
 * @version 3.0 - Sandro Reis © 2012 
 * @version 3.1 - Gilles Neyens © 2013 
 *
 */
public class MTA008m extends AbstractRuleMoodle {

	private long assignment_id;
	private long user_id;
	
	private boolean enabled,exists;
	
	GregorianCalendar tcal = new GregorianCalendar(); 
	private int run_year = tcal.get(GregorianCalendar.YEAR);
	private int run_month = tcal.get(GregorianCalendar.MONTH)+1;
	private int run_day = tcal.get(GregorianCalendar.DAY_OF_MONTH);

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private MTA008m() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * @param assignment_id 
	 * @param user_id 
	 */
	public MTA008m(final long assignment_id, final long user_id) {
		this();
		setAssignment_id(assignment_id);
		setUser_id(user_id);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		this.exists = dataNativeSystem.assignmentExists25(this.assignment_id);
		if (exists) {
			this.enabled = dataNativeSystem.isScenarioEnabled(this.assignment_id,"hints");
			if (enabled) {
				tcal = new GregorianCalendar();
				run_year = tcal.get(GregorianCalendar.YEAR);
				run_month = tcal.get(GregorianCalendar.MONTH)+1;
				run_day = tcal.get(GregorianCalendar.DAY_OF_MONTH);
			}
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		return dataNativeSystem.hasHintsToSend(assignment_id, run_year, run_month, run_day);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#conditions()
	 */
	@Override
	protected boolean conditions() {
		return exists && enabled;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#actions()
	 */
	@Override
	protected void actions() {
		// no actions, only Rule Generation
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		if (exists) {
			if (enabled) {
				if (getActivated()) {
					final ArrayList<Long> hints = dataNativeSystem.getHintsToSend(assignment_id, run_year, run_month, run_day);	
					for (int i=0; i<hints.size(); i++) {
						Global.logger.info("Creating HNT004m for hint id [" + hints.get(i) + "]");
						createRule(new HNT004m(assignment_id, hints.get(i).longValue()));
					}
					//this.setActivated(false);
					createRule(this);
					return true;
				}
			}
			createRule(this);
			return true;
		}else{
			return true;
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return "MTA008m - Meta-scenario: New specific Hints Detection: "+run_year+"-"+run_month+"-"+run_day;
	}

	/**
	 * @param assignment_id the assignment_id to set
	 */
	public void setAssignment_id(final long assignment_id) {
		this.assignment_id = assignment_id;
	}

	/**
	 * @return the assignment_id
	 */
	public long getAssignment_id() {
		return this.assignment_id;
	}

	/**
	 * @return a long
	 */
	public long getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id
	 */
	public void setUser_id(final long user_id) {
		this.user_id = user_id;
	}
}