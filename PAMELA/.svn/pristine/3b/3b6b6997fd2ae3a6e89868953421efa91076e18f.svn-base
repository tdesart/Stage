package lu.uni.fstc.proactivity.rules.moodleCoPs;


import lu.uni.fstc.proactivity.db.AbstractEngineDataAccessWrapper;
import lu.uni.fstc.proactivity.db.AbstractHostDataAccessWrapper;
import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.db.MoodleCacheDbWrapper;
import lu.uni.fstc.proactivity.db.PAMDbWrapper;
import lu.uni.fstc.proactivity.ruleRunningSystem.ProactiveEngine;
import lu.uni.fstc.proactivity.rules.AbstractRule;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * We need this intermediate structure to re-define the dbPam as AbstractPAMDbWrapper, <br>
 * so that each rule that derives from this, has access to the methods to manipulate data on CoP's database. 
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2015
 *
 */
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "MF_CLASS_MASKS_FIELD", justification = "I need to define a field with the same name as a visible instance field in a superclass, because I need to redefine it to a more specific type!")
public abstract class AbstractRuleCops extends AbstractRule {

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
	 * @return the db
	 */
	@Override
	public final AbstractMoodleDbWrapper getDataNativeSystem() {
		return this.dataNativeSystem;
	}

	/**
	 * @param dataNativeSystem the dataNativeSystem to set
	 */
	@Override
	public final void setDataNativeSystem(final AbstractHostDataAccessWrapper dataNativeSystem) {
		Global.logger.finer("AbstractMySQLDbWrapper in db =" + dataNativeSystem);
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
	 * @param dataEngine the dbPam to set
	 */
	@Override
	public final void setDataEngine(final AbstractEngineDataAccessWrapper dataEngine) {
		Global.logger.finer("AbstractLocalDbWrapper in dataEngine =" + dataEngine);
		this.dataEngine = (PAMDbWrapper) dataEngine;
		Global.logger.finer("dataEngine =" + this.dataEngine);
	}
}