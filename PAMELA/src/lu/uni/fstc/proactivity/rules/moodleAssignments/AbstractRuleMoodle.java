package lu.uni.fstc.proactivity.rules.moodleAssignments;


import lu.uni.fstc.proactivity.db.AbstractEngineDataAccessWrapper;
import lu.uni.fstc.proactivity.db.AbstractHostDataAccessWrapper;
import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.db.MoodleCacheDbWrapper;
import lu.uni.fstc.proactivity.db.PAMDbWrapper;
import lu.uni.fstc.proactivity.ruleRunningSystem.ProactiveEngine;
import lu.uni.fstc.proactivity.rules.AbstractRule;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.Time;

/**
 * We need this intermediate structure to re-define the db as AbstractMoodleDbWrapper, <br>
 * so that each rule that derives from this, has access to the methods to manipulate data on moodle's database. 
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2015
 *
 */
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "MF_CLASS_MASKS_FIELD", justification = "I need to define a field with the same name as a visible instance field in a superclass, because I need to redefine it to a more specific type!")
public abstract class AbstractRuleMoodle extends AbstractRule {

	protected ProactiveEngine engine;

	/**
	 * this 're-cast' is necessary to allow to access specific 'Moodle' methods 
	 */
	protected AbstractMoodleDbWrapper dataNativeSystem;

	/**
	 * this 're-cast' is necessary to allow to access specific 'PAM' methods 
	 */
	protected AbstractPAM2MoodleDbWrapper dataEngine;
	/**
	 * @return the dataNativeSystem
	 */
	@Override
	public final AbstractMoodleDbWrapper getDataNativeSystem() {
		return this.dataNativeSystem;
	}

	/**
	 * @param dataNativeSystem the db to set
	 */
	@Override
	public final void setDataNativeSystem(final AbstractHostDataAccessWrapper dataNativeSystem) {
		Global.logger.finer("AbstractMySQLDbWrapper in dataNativeSystem =" + dataNativeSystem);
		this.dataNativeSystem = (MoodleCacheDbWrapper) dataNativeSystem;
	}

	/**
	 * @return the dbPam
	 */
	@Override
	public final AbstractPAM2MoodleDbWrapper getDataEngine() {
		Global.logger.finer(this.dataEngine.toString());
		return this.dataEngine;
	}

	/**
	 * @param dataEngine the dataEngine to set
	 */
	@Override
	public final void setDataEngine(final AbstractEngineDataAccessWrapper dataEngine) {
		Global.logger.finer("AbstractEngineDbWrapper in dataEngine =" + dataEngine);
		this.dataEngine = (PAMDbWrapper) dataEngine;
		Global.logger.finer("dataEngine =" + this.dataEngine);
	}

	/**
	 * if deadline is not set<br>
	 * • assignment is valid if we're 'inside' a semester (start of course + one Semester)<br>
	 * else<br>
	 * • assignment is valid if deadline is not yet reached<br> 
	 * @return true if assignment is still valid in terms of dates
	 * @see Time#oneSemesterInSecs
	 */
	protected final boolean isAssignmentValid(final long assignment_id, final long course_id) {
		// if assignment was deleted, then it is no longer valid, and there's no use in verifying deadline or do any math...
		if (!dataNativeSystem.assignmentExists(assignment_id))
			return false;
		
		long deadline = dataNativeSystem.getAssignmentDeadline(assignment_id);
		long now = System.currentTimeMillis()/1000;

		if (deadline == 0) {
			long startdate = dataNativeSystem.getCourseStartDate(course_id);
			return (now < (startdate + lu.uni.fstc.proactivity.utils.Time.oneSemesterInSecs));
		}
		else
			return (now < deadline);
	}
}