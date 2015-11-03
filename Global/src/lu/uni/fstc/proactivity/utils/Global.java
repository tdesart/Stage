package lu.uni.fstc.proactivity.utils;

import java.util.logging.Logger;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2012 
 * 
 */
public class Global {

	/**
	 * GLOBAL Constant that indicates if this program is meant for the EXPERIMENTS or not<br>
	 * true = the program only applies to a selected list of courses
	 * false = for the whole list of courses, not only for the EXPERIMENTS
	 * Changed this into false, once it went into Production (Feb, 2013)
	 */
//	public static final boolean EXPERIMENTS = true;
	public static final boolean EXPERIMENTS = false;

	/**
	 * private Constant to hold the name for the <b>PAM</b> application's LOG.<p>
	 * <b>USAGE in SLF4J:</b><br>
	 * <code>import lu.uni.fstc.utils.Global;<br>
	 * import org.slf4j.Logger;<br>
	 * import org.slf4j.LoggerFactory;<br>
	 * <br>
	 * public class MyClass {<br>
	 *   final (static) Logger logger = LoggerFactory.getLogger(Global.PAM_LOGGER_NAME);<br>
	 *     ... etc<br>
	 * }</code><p>
	 * <b>USAGE in 1.4 Logger API:</b><br>
	 * <code>Logger logger = Logger.getLogger("com.example.MyServer");<br>
	 * Handler handler = new FileHandler("test.log");<br>
	 * LogRecord record = new LogRecord(Level.FINEST, "Value of 'myvar' is "+myvar);<br>
	 * logger.setHandler(handler);<br>
	 * logger.log(record);</code><p>
	 */
	@Deprecated
	private static final String PAM_LOGGER_NAME = "PAM_LOG";
	/**
	 * Private Constant to hold the name for the <b>WebSocketServer</b> application's LOG.<br>
	 * Could be different then the previous one.
	 */
	@Deprecated
	private static final String WSS_LOGGER_NAME = "WS_LOG";

	/**
//	 * <b><i>Deprecated</i></b> as of Dec 2014. To give way for a generic logger in the Engine<br>
//	 * The logger has now to be defined at the implementation of that generic logger.<p>
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
//	@Deprecated
	public static final Logger logger = Logger.getLogger(PAM_LOGGER_NAME);

	/**
	 * <b><i>Deprecated</i></b> as of Dec 2014. To give way for a generic logger in the Engine<br>
	 * The logger has now to be defined at the implementation of that generic logger.<p>
	 * Global Constant to hold the object that actually logs information for WebSocketServer on file/console<p>
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
//	@Deprecated
	public static final Logger wssLogger = Logger.getLogger(WSS_LOGGER_NAME);
}