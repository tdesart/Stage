package lu.uni.fstc.proactivity.rules.moodleCoPs;


import lu.uni.fstc.proactivity.db.MySQLOperations;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.rules.moodleAssignments.MTA_PushQuestion;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Remus Dobrican
 * @version 1.0 - Remus Dobrican © 2013 
 *
 *	Inscribes users into the special Course created by rule S001
 */
public class S002 extends AbstractRuleCops {
	
	private long course_ID, enroll_ID, context_ID;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private S002() {
		setType(RuleType.SCENARIO);	
	}

	/**
	 * @param course_ID 
	 */
	public S002(final long course_ID) {
		this();
		this.course_ID = course_ID;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		return true; 
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
		MySQLOperations mso = MySQLOperations.getInstance();
		try {
			if (!mso.startTransaction(engine.dataNativeSystem.getConn()))
				return;
			
			enroll_ID = dataNativeSystem.getEnrolmentIDFromCourseID(course_ID);
		
			//	INSERT INTO mdl_user_enrolments
			dataNativeSystem.userEnrolments(enroll_ID);
			
			context_ID = dataNativeSystem.getContextID(course_ID, 3);
			//	INSERT INTO mdl_role_assignments
			dataNativeSystem.roleAssignments(context_ID);
	
			mso.commit(engine.dataNativeSystem.getConn());
		}
		catch (final Exception e){
			e.printStackTrace();
			mso.rollback(engine.dataNativeSystem.getConn());
			Global.logger.severe("Error in the transaction: rollback executed!");
		}
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		
		/*
			if this rules is activated 
				then we create the first rule of the Proactive Cycle - S101
				the parameter "CITY" is used for the creation of the first groups
		*/
		if (getActivated()) {
			Global.logger.fine("Creating new S101  [" + course_ID + "] ");
			createRule(new S101(course_ID));
			
			Global.logger.fine("Creating new S301  [" + course_ID + "] ");
			createRule(new S301(course_ID));
			
			Global.logger.fine("Creating new M201  [" + course_ID + ", " + 0 + "] ");
			createRule(new M201(course_ID, 0, 1));
			
			Global.logger.fine("Creating new M202  [ 0 ] ");
			createRule(new M202(0));
			
			Global.logger.fine("Creating new M203  [ 0 ] ");
			createRule(new M203(0));
			
			Global.logger.fine("Creating new M301  [" + course_ID + "] ");
			createRule(new M301(course_ID));	
			
			Global.logger.fine("Creating new MTA_PushQuestion()");
			createRule(new MTA_PushQuestion());
		} else 
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "S002 - Scenario: Triggers the proactive cycle, generating rule S101 ";
	}
	
	/**
	 * @return a long
	 */
	public long getCourse_ID() {
		return course_ID;
	}

	/**
	 * @param course_ID
	 */
	public void setCourse_ID(final long course_ID) {
		this.course_ID = course_ID;
	}
}