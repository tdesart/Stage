package lu.uni.fstc.tests;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import lu.uni.fstc.proactivity.log.LoggerOutputStream;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2012 
 *
 */
public class Logger14API_2 {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SecurityException 
	 */
	public static void main(String[] args) throws SecurityException, IOException {
//		PrintWriter pw = new PrintWriter(new LoggerOutputStream((Logger) Logger.getLogger(Logger14API_2.class)));
/**/
		PrintStream ps = new PrintStream(new LoggerOutputStream(Logger.getLogger("this one"), Level.CONFIG));

	    System.setOut(ps);
	    System.out.println("1\nThis is a System.Out debug statement.");
/**/
/*/
		PrintStream ps2 = new PrintStream(new LoggerOutputStream(Logger.getLogger("another one"), Level.INFO));
	    System.setErr(ps2);

	    System.err.println("This is an error statement.");
/**/	
	}
}
