package lu.uni.fstc.tests;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2012 
 *
 */
public class Logger14API_3 {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SecurityException 
	 */
	public static void main(String[] args) throws SecurityException, IOException {
	    String myvar = "12";
		Logger logger = Logger.getLogger("com.example.MyServer");

		// OR in a even simpler way ...
		logger.severe("");
		logger.warning("This is a 2nd potential problem !!");
		logger.fine("This is a 2nd FINE message.");
		logger.info("This is a 2nd informational message. '" + Logger.GLOBAL_LOGGER_NAME +"'");
		logger.finest("Value of  2nd 'myvar' is " + myvar);
	
	}
}
