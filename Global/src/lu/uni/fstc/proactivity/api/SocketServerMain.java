package lu.uni.fstc.proactivity.api;

import java.io.IOException;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sergio Marques Dias
 * @version 3.0 - Sandro Reis © 2011/2012
 * @version 2.0 - Sergio Marques Dias <br> 
 * @version 1.0 - Yann Milin
 *
 */
@Deprecated
@SuppressWarnings("deprecation")
public class SocketServerMain {

	final static int port = 3333;

	static SocketServerThread ss;	

	/**
	 * @param args
	 * @throws IOException 
	 */
//	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {


		ss = new SocketServerThread (port);
		String firstArg = "";
		if (args.length > 0)
			firstArg = args[0];

		if (firstArg.compareToIgnoreCase("stop") == 0) {
			// NOTE: doesn't work
			Global.logger.info("Sending stop to Socket Server!");
			ss.stopServer();
		}
		else if (firstArg.compareToIgnoreCase("start") == 0) {
			final Thread serverThread = new Thread(ss);
			Global.logger.info("Starting the Socket Server!");
			serverThread.start();
		}
	}
}
