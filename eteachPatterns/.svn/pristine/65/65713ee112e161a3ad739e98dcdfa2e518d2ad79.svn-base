package lu.uni.fstc.proactivity.rules.eteach;

import lu.uni.fstc.proactivity.db.AbstractETeachDbWrapper;
import lu.uni.fstc.proactivity.db.AbstractEngineDataAccessWrapper;
import lu.uni.fstc.proactivity.db.AbstractHostDataAccessWrapper;
import lu.uni.fstc.proactivity.db.ETeachCacheDbWrapper;
import lu.uni.fstc.proactivity.db.EteachEngineDbWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;
import lu.uni.fstc.proactivity.utils.Global;


/**
 * We need this intermediate structure to re-define the db as AbstractMoodleDbWrapper, <br>
 * so that each rule that derives from this, has access to the methods to manipulate data on moodle's database. 
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2015
 *
 */
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "MF_CLASS_MASKS_FIELD", justification = "I need to define a field with the same name as a visible instance field in a superclass, because I need to redefine it to a more specific type!")
public abstract class AbstractRuleEteach extends AbstractRule {

	/**
	 * this 're-cast' is necessary to allow to access specific 'eTeach' methods 
	 */
	protected AbstractETeachDbWrapper dataNativeSystem;

	/**
	 * this 're-cast' is necessary to allow to access specific 'eTeach' methods 
	 */
	protected EteachEngineDbWrapper dataEngine;

	/**
	 * @return the db
	 */
	@Override
	public AbstractETeachDbWrapper getDataNativeSystem() {
		Global.logger.finest("db: "+ this.dataNativeSystem.toString());
		return this.dataNativeSystem;
	}

	/**
	 * This is the method called, because the caller only knows 'db' as AbstractMySQLDbWrapper, not as AbstractETeachDbWrapper 
	 * @param dataNativeSystem the db to set
	 */
	@Override
	public final void setDataNativeSystem(final AbstractHostDataAccessWrapper dataNativeSystem) {
		Global.logger.finest("value IN AbstractMySQLDbWrapper in dataNativeSystem =" + dataNativeSystem);
		this.dataNativeSystem = (ETeachCacheDbWrapper) dataNativeSystem;
		Global.logger.finest("value OUT (ETeachCacheDbWrapper) this.dataNativeSystem =" + this.dataNativeSystem);
	}	

	/**
	 * @return the dataEngine
	 */
	@Override
	public EteachEngineDbWrapper getDataEngine() {
		Global.logger.finest("dataEngine: "+ this.dataEngine);
		return this.dataEngine;
	}

	/**
	 * This is the method called, because the caller only knows 'dataEngine' as AbstractEngineDbWrapper, not as EteachEngineDbWrapper 
	 * @param dataEngine
	 */
	@Override
	public final void setDataEngine(final AbstractEngineDataAccessWrapper dataEngine) {
		Global.logger.finest("value IN AbstractEngineDbWrapper in dataEngine =" + dataEngine);
		this.dataEngine = (EteachEngineDbWrapper) dataEngine;
		Global.logger.finest("value OUT (EteachEngineDbWrapper) dataEngine =" + this.dataEngine);
	}	
}