package lu.uni.fstc.proactivity.db;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * Second level Abstract class that holds the definition of the basic attributes and methods needed to operate the local engine, regardless of the type of data(base) system. 
 * This level is used to abstract the specifics of the local engines needs from those of the HOST systems of the specific projects.
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2015
 *
 */
 abstract public class AbstractEngineDataAccessWrapper extends AbstractDataAccessWrapper {

	/**
	 * AbstractLocalDbWrapper Constructor<br>
	 * <b>Private</b> (protected, so that it's accessed from 'children') to implement the "Singleton" Object 
	 */
	protected AbstractEngineDataAccessWrapper() {
	}

	/* **********************************************************************************
	 * Engine Signals Management (Definition)
	 * **********************************************************************************
	 */
	/**
	 * @return boolean
	 */
	abstract public boolean isScheduleToStop();

	/**
	 * @param stopSignal 
	 * @return the long that results from the operation
	 */
	abstract public long setScheduleToStop (final boolean stopSignal);

	/**
	 * @return a boolean with the value of the stop signal
	 */
	abstract public boolean isRunning(); 

	/**
	 * @param runSignal
	 * @return the long that results from the operation
	 */
	abstract public long setRunning (final boolean runSignal);

	@Override
	public AbstractEngineDataAccessWrapper getThis() {
		Global.logger.finer("this:" + this);
		return this; 
	}
}