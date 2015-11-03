package lu.uni.fstc.proactivity.rules.moodleAssignments;


import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @author Gilles Neyens
 * @version 3.0 - Sandro Reis © 2012 
 * @version 3.1 - Gilles Neyens © 2013 
 *
 */
public class MTA002am extends AbstractRuleMoodle {

	private long assignment_id;
	private long user_id;
	private int lastlevel;
	private boolean sent;

	private boolean enabled,exists;
	
	private long course_id;
	private double submissionLevel;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	public MTA002am() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * 
	 * @param assignment_id
	 * @param user_id
	 */
	public MTA002am(final long assignment_id,final long user_id) {
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
		this.exists=dataNativeSystem.assignmentExists25(this.assignment_id);
		if(exists){
			this.enabled=dataNativeSystem.isScenarioEnabled(this.assignment_id,"notification");
			if(enabled){
				final int notiflevel=this.dataNativeSystem.getIntegerScenarioConfigParam(this.assignment_id, "notification", "submissionlevel");
				if(10*notiflevel!=lastlevel) {
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
		return (exists && enabled && !sent && (this.submissionLevel >= lastlevel));
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
		sent=true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		if(exists){
			if(enabled){
				if (getActivated()) {
					Global.logger.info("Creating NTF003m for the new assignment [" + assignment_id + "]");
					createRule(new NTF003m(assignment_id,user_id,lastlevel));
//					this.setActivated(false);			
				}
				// FIXME shouldn't it die in case of activation ? 
				if(this.submissionLevel<100){
					createRule(this);
				}
				return true;
				
			}else{
				createRule(this);
				return true;
			}
		}else{
			return true;
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return "MTA002am - Meta-scenario: Monitoring reaching "+this.lastlevel+" of an assignment submission. Assignment ID = " + this.assignment_id;
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
