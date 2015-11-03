package lu.uni.fstc.proactivity.log;

/**
 *  An attempt to generalise the use of logging in the GenericEngine<p>
 *  <b>NOT: not finished yet, thus not used in the code.</b>
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2014 
 * 
 */
public abstract class AbstractLoggerBasedOnJavaUtil {

	/**
	 * Available levels
	 * <ul><li>SEVERE (highest value)
	 * <li>WARNING
	 * <li>INFO
	 * <li>CONFIG
	 * <li>FINE
	 * <li>FINER
	 * <li>FINEST (lowest value)</ul>
	 *  
	 * @param level the logging level as provided by java.util.logging.Level
	 * @param tag associated tag (optional)
	 * @param message the string message to log
	 *  
	 */
	 abstract public void log (final java.util.logging.Level level, String tag, String message);
}