package lu.uni.fstc.proactivity.db;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * Second level Abstract class that holds basic attributes and methods needed to operate with a MySQL database
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2012
 *
 */
public abstract class AbstractHostDataAccessWrapper extends AbstractDataAccessWrapper {

	/**
	 * AbstractMySQLDbWrapper Constructor<br>
	 * <b>Private</b> (protected, so that it's accessed from 'children') to implement the "Singleton" Object 
	 */
	protected AbstractHostDataAccessWrapper() {
	}

	@Override
	public AbstractHostDataAccessWrapper getThis() {
		Global.logger.finer("this:" + this);
		return this; 
	}
}