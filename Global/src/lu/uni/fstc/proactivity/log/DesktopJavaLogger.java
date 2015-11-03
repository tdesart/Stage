package lu.uni.fstc.proactivity.log;

import java.util.logging.Logger;

/**
 *  An attempt to generalise the use of logging in the GenericEngine<p>
 *  <b>NOT: not finished yet, thus not used in the code.</b>
 *  
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2014 
 * 
 */
public class DesktopJavaLogger extends AbstractLogger {

	private static final String PAM_LOGGER_NAME = "PAM_LOG";
	
	/**
	 * Global Constant to hold the object that actually logs information on file/console<p>
	 * Based on the Logger: <b>java.util.logging.Logger</b><br>
	 * The levels in descending order are:
	 * <ul><li>SEVERE (highest value)
	 * <li>WARNING
	 * <li>INFO
	 * <li>CONFIG
	 * <li>FINE
	 * <li>FINER
	 * <li>FINEST (lowest value)</ul>
	 * @see java.util.logging.Logger
	 */
	public static final Logger logger = Logger.getLogger(PAM_LOGGER_NAME);

	/**
	 * @see lu.uni.fstc.proactivity.log.AbstractLogger#log(int, java.lang.String, java.lang.String)
	 */
	@Override
	public void log(int ilevel, String tag, String message) {

		// NOTE: all this can be removed, if we agree to use the LEVELS provided by
		// 	java.util.logging.Level
		//void log(java.util.logging.Level level, String tag, String message) {
		//logger.log(level, message);

		java.util.logging.Level thisLevel;
		switch (ilevel) {
		case SEVERE:
		    thisLevel = java.util.logging.Level.SEVERE;
		    break;
		case WARNING:
		    thisLevel = java.util.logging.Level.WARNING;
		    break;
		case INFO:
		    thisLevel = java.util.logging.Level.INFO;
		    break;
		case CONFIG:
		    thisLevel = java.util.logging.Level.CONFIG;
		    break;
		case FINE:
		    thisLevel = java.util.logging.Level.FINE;
		    break;
		case FINER:
		    thisLevel = java.util.logging.Level.FINER;
		    break;
		case FINEST:
		    thisLevel = java.util.logging.Level.FINEST;
		    break;
		default: 
		    thisLevel = java.util.logging.Level.INFO;
		    break;
		}
		logger.log(thisLevel, message);
	}
}
