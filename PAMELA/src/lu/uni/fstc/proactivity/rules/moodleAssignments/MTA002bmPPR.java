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
 */
public class MTA002bmPPR extends AbstractRuleMoodle {

	private long assignment_id;
	private long user_id;
	private int lastlevel;
	private long lastdate;
	private boolean sent;
	private boolean enabled,exists;
	
	private long course_id;
	private double submissionLevel;
	
	private int year,year2,month,month2,day,day2;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	public MTA002bmPPR() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * 
	 * @param assignment_id
	 * @param user_id
	 */
	public MTA002bmPPR(final long assignment_id,final long user_id) {
		this();
		setAssignment_id(assignment_id);
		setUser_id(user_id);
		setSent(false);
		setLastlevel(150);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		this.exists = dataNativeSystem.assignmentExists25(this.assignment_id);
		if (exists) {
			this.enabled=dataNativeSystem.isScenarioEnabled(this.assignment_id,"potentialproblem");
			if (enabled) {
				final GregorianCalendar tcal = new GregorianCalendar();
				year = tcal.get(GregorianCalendar.YEAR);
				month = tcal.get(GregorianCalendar.MONTH);
				day = tcal.get(GregorianCalendar.DAY_OF_MONTH);
				final GregorianCalendar ppdate=dataNativeSystem.getDateScenarioConfigParam(this.assignment_id, "potentialproblem", "probdetdatetodetect");
				final long date=dataNativeSystem.getLongScenarioConfigParam(this.assignment_id, "potentialproblem", "probdetdatetodetect");
				if (date!=lastdate) {
					sent=false;
					lastdate=date;
				}

				year2 = ppdate.get(GregorianCalendar.YEAR);
				month2 = ppdate.get(GregorianCalendar.MONTH);
				day2 = ppdate.get(GregorianCalendar.DAY_OF_MONTH);
				final int notiflevel=dataNativeSystem.getIntegerScenarioConfigParam(this.assignment_id, "potentialproblem", "probdetsubmissionlevel");
				if (10*notiflevel!=lastlevel) {
					sent=false;
					lastlevel=10*notiflevel;
				}
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
		return (exists && enabled && !sent && (this.submissionLevel < lastlevel) && (year==year2) && (month==month2) && (day==day2));
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
		Global.logger.info("Creating PPR001m for the new assignment [" + assignment_id + "]");
		createRule(new PPR001m(assignment_id, user_id, lastlevel));
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
		return "MTA002bmPPR - Meta-scenario: Monitoring the accomplishment of an assignment at date : "+Time.longDateSecondsToString(lastdate) + " Assignment ID = " + this.assignment_id;
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

	/**
	 * @return a int
	 */
	public int getLastlevel() {
		return lastlevel;
	}

	/**
	 * @param lastlevel
	 */
	public void setLastlevel(final int lastlevel) {
		this.lastlevel = lastlevel;
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
