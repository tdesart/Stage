package lu.uni.fstc.proactivity.log;

/**
 *  An attempt to generalise the use of logging in the GenericEngine<p>
 *  <b>NOT: not finished yet, thus not used in the code.</b>
 *  
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2014 
 * 
 */
public abstract class AbstractLogger {

	// NOTE: all this can be removed, if we agree to use the LEVELS provided by
	// 	java.util.logging.Level
	//void log(java.util.logging.Level level, String tag, String message) {
	//logger.log(level, message);

	/** 
	 * SEVERE - for error logging
	 */
	public final static int SEVERE  = 100;
	/**
	 * WARNING - self-explanatory
	 */
	public final static int WARNING = 80;
	/**
	 * INFO - normal information level
	 */
	public final static int INFO    = 70;
	/**
	 * CONFIG  - for configuration settings
	 */
	public final static int CONFIG  = 50;
	/**
	 * FINE (3nd lowest value) - for debugging
	 */
	public final static int FINE    = 30;
	/**
	 * FINER (2nd lowest value) - for debugging
	 */
	public final static int FINER   = 20;
	/**
	 * <li>FINEST (lowest value) - for debugging
	 */
	public final static int FINEST  = 10;
	
	/**
	 * Available levels
	 * <ul><li>SEVERE (highest value)
	 * <li>WARNING
	 * <li>INFO
	 * <li>CONFIG
	 * <li>FINE
	 * <li>FINER
	 * <li>FINEST (lowest value)</ul>
	 *  NOTE: <b>Use the constants, instead of the integer values</b><p>
	 * @param level the logging level as provided by the constants
	 * @param tag associated tag (optional)
	 * @param message the string message to log
	 *  
	 */
	abstract public void log(int level, String tag, String message);
}
