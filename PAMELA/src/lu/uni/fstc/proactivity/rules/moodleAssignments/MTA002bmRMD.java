package lu.uni.fstc.proactivity.rules.moodleAssignments;

import java.util.GregorianCalendar;


import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.Time;

/**
 * @author Sandro Reis
 * @author Gilles Neyens
 * @version 3.0 - Sandro Reis © 2012 
 * @version 3.1 - Gilles Neyens © 2013 
 *
 */public class MTA002bmRMD extends AbstractRuleMoodle{

	private long assignment_id;
//	private long user_id;
	private long course_id;
	private long lastdate;
	private boolean sent;
	private boolean enabled,exists;
	
	private double submissionLevel;
	
	private int year,year2,month,month2,day,day2;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	public MTA002bmRMD() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * @param assignment_id
	 */
	public MTA002bmRMD(final long assignment_id) {
		this();
		setAssignment_id(assignment_id);
		setSent(false);
		setLastdate(0);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		this.exists = dataNativeSystem.assignmentExists25(this.assignment_id);
		if (exists) {
			this.enabled=dataNativeSystem.isScenarioEnabled(this.assignment_id,"reminder");
			if (enabled) {
				final GregorianCalendar tcal = new GregorianCalendar();
				year = tcal.get(GregorianCalendar.YEAR);
				month = tcal.get(GregorianCalendar.MONTH);
				day = tcal.get(GregorianCalendar.DAY_OF_MONTH);
				final GregorianCalendar ppdate=dataNativeSystem.getDateScenarioConfigParam(this.assignment_id, "reminder", "reminderdatetosend");
				final long date=dataNativeSystem.getLongScenarioConfigParam(this.assignment_id, "reminder", "reminderdatetosend");
				if (date!=lastdate) {
					sent=false;
					lastdate=date;
				}
				year2 = ppdate.get(GregorianCalendar.YEAR);
				month2 = ppdate.get(GregorianCalendar.MONTH);
				day2 = ppdate.get(GregorianCalendar.DAY_OF_MONTH);
				course_id = dataNativeSystem.getCourseIdFromAssignment25(this.assignment_id);
				final long totalEnrol = dataNativeSystem.countAllEnrolledStudents25(course_id);
				final long accomplishedAssign = dataNativeSystem.countSubmitted25(assignment_id);
				if (totalEnrol == 0)
					this.submissionLevel = 100;
				else
					this.submissionLevel = ((double) accomplishedAssign / totalEnrol)*100;
			}
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		return (exists && enabled && (year==year2) && (month==month2) && (day==day2) && !sent);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#conditions()
	 */
	@Override
	protected boolean conditions() {
		return true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#actions()
	 */
	@Override
	protected void actions() {
		Global.logger.info("Creating RMD001m for the new assignment [" + assignment_id + "]");
		createRule(new RMD001m(assignment_id));
		sent = true;
	}

	/**
	 * @return a long
	 */
	public long getLastdate() {
		return lastdate;
	}

	/**
	 * @param lastdate
	 */
	public void setLastdate(final long lastdate) {
		this.lastdate = lastdate;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		if (exists) {
			if (enabled) {
				if (this.submissionLevel<100) {
					createRule(this);
				}
			} else {
				createRule(this);
			}
		}
		return true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return "MTA002bmRMD - Meta-scenario: Monitoring students that didn't submit their assignment at date : " + Time.longDateSecondsToString(lastdate) + " Assignment ID = " + this.assignment_id;
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
	 * @return a boolean
	 */
	public boolean isSent() {
		return sent;
	}

	/**
	 * @param sent
	 */
	public void setSent(final boolean sent) {
		this.sent = sent;
	}
}