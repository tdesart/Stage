package lu.uni.fstc.tests;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2012 
 *
 */
public class LoggerSLF4J_1 {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SecurityException 
	 */
	public static void main(String[] args) throws SecurityException, IOException {
	    Logger logger = LoggerFactory.getLogger(LoggerSLF4J_1.class);
	    logger.info("Hello World");
	
	}
}
