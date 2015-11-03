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
public class DesktopJavaLoggerBasedOnJavaUtil extends AbstractLoggerBasedOnJavaUtil {

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
	 * This method delegates to java.util.logging.Logger the logging function 
	 * The field 'tag' is ignored by this implementation of the logger 
	 * @see lu.uni.fstc.proactivity.log.AbstractLoggerBasedOnJavaUtil#log(java.util.logging.Level, java.lang.String, java.lang.String)
	 */
	@Override
	public void log (final java.util.logging.Level level, final String tag, final String message) {
		logger.log(level, message);
	}
}
