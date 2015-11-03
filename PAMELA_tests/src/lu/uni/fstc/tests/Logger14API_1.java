package lu.uni.fstc.tests;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis й 2012 
 *
 */
public class Logger14API_1 {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SecurityException 
	 */
	public static void main(String[] args) throws SecurityException, IOException {
		String myvar = "12";
		
		Logger logger = Logger.getLogger("com.example.MyServer");
		LogRecord record ;

		Handler handlers[];
//		Handler handler0 = handlers[0];
//		logger.removeHandler(handler0);

		/* I prefer to do it on the logging.properties File
		 *   java.util.logging.FileHandler.pattern
		 * If I use this code to add, it will write on both files.
		 * Without this, it will only write on one, defined by the CFG file. 
		 */
//		Handler handler = new FileHandler("test.log"); 

		/* This also can be achieved on the logging.properties File
		 *   java.util.logging.FileHandler.formatter
		 */
//		Formatter formatter = new SingleLineFormatter();
//		handler.setFormatter(formatter);
//		logger.addHandler(handler);

		record = new LogRecord(Level.SEVERE, "");
		logger.log(record);

		record = new LogRecord(Level.WARNING, "This is a  potential problem !!");
		logger.log(record); 	

		record = new LogRecord(Level.INFO, "This is an informational message. сур'" + logger.getName() +"'");
		logger.log(record); 		

		handlers = logger.getHandlers();
		int len2 = handlers.length;
		record = new LogRecord(Level.FINE, "This is a  FINE message. Handlers[].len = " + len2);
		logger.log(record); 

		// OR in a simple(r) way ...
		logger.log(Level.FINEST, "Value of   'myvar' is " + myvar);

		// OR in a even simpler way ...
		logger.severe("");
		logger.warning("This is a 2nd potential problem !!");
		logger.fine("This is a 2nd FINE message. Handlers[].len = " + len2);
		logger.info("This is a 2nd informational message. '" + logger.getName() +"'");
		logger.finest("Value of  2nd 'myvar' is " + myvar);
	}
}
