package lu.uni.fstc.proactivity.api;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * <b>Server for the sockets, which manages the list of sockets for the user connections</b>
 *  
 *@deprecated <br>
 * This class has been depreciated due to the fact that the communication is done using the database<br>
 * These Sockets don't work as we needed in PHP, because the server script would stop waiting for a connection, and we need it to be running.
 *  
 * @author Sergio Marques Dias
 * @version 3.0 - Sandro Reis © 2011/2012
 * @version 2.0 - Sergio Marques Dias <br> 
 * @version 1.0 - Yann Milin
 * 
 */
@Deprecated
@SuppressWarnings("deprecation")
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "CD_CIRCULAR_DEPENDENCY ", justification = "This class was created for a prototype! I wish to ignore this error message. Sandro Reis.")
public class SocketServerThread implements Runnable {
	
	private boolean serverOn = false;
	private int numConnections = 0;
	private int port;
	
	private ServerSocket echoServer = null;
	private List<SocketClientThread> clientList = null;

	/**
	 * Default Constructor
	 */
/*/	public SocketServerThread() {
	}
/*/

	/**
	 * @param port
	 */
	public SocketServerThread (final int port) {
//		this();
		setPort(port);
		try {
			echoServer = new ServerSocket(getPort());
			clientList = new ArrayList<SocketClientThread>();
		}
		catch (final IOException e) {
			Global.logger.severe(e.getMessage());
		}
	}

	/**
	 * @param s 
	 * <p>Adds an entry to the Socket List
	 */
	synchronized public void addToSocketList(final SocketClientThread s) {
		if(!clientList.contains(s)){
			clientList.add(s);
			incNumConnections();
		}
	}
	
	/**
	 * @return clientList
	 */
	public List<SocketClientThread> getSocketList() {
		return clientList;
	}

	/**
	 * Prints to the output the Socket client List
	 */
	public void printSocketList() {
		final Iterator<SocketClientThread> i = clientList.iterator();
		 while (i.hasNext()) {
			 SocketClientThread client;
			 client = i.next();
			 Global.logger.info(client.toString());
		 }
	}

	/**
	 * @param s a SocketClientThread
	 * <p>Removes an entry from the Socket client List
	 */
	synchronized public void removeFromSocketList(final SocketClientThread s) {
		if(clientList.remove(s))
			decNumConnections();
	}

	@Override
	public void run() {
		// Try to open a server socket on the given port
		// Note that we can't choose a port less than 1024 if we are not
		// privileged users (root)

		if(echoServer.isBound()) {
			setServerOn(true);
			Global.logger.info("Server has started and is waiting for connections.");	
		}

		// Whenever a connection is received, start a new thread to process the connection
		// and wait for the next connection.

		while (isServerOn()) {
			try {
				final Socket clientSocket = echoServer.accept();
				final SocketClientThread newClientConnection = new SocketClientThread(clientSocket, getNumConnections(), this);
				new Thread(newClientConnection).start();

				addToSocketList(newClientConnection);
			}
			catch (final IOException e) {
				Global.logger.severe(e.getMessage());
			}
		}
		Global.logger.info("Server is leaving the main cycle.");
	}

	/**
	 * Stops the server
	 * @throws IOException 
	 */
	public void stopServer() throws IOException {
		Global.logger.info("Server cleaning up.");
		echoServer.close();
		setServerOn(false);

		//System.exit(0);
		Global.logger.info("Server out.");
	}

	/**
	 * @return the serverOn
	 */
	public boolean isServerOn() {
		return this.serverOn;
	}

	/**
	 * @param serverOn the serverOn to set
	 */
	public void setServerOn(final boolean serverOn) {
		this.serverOn = serverOn;
	}

	/**
	 * @return the numConnections
	 */
	public int getNumConnections() {
		return this.numConnections;
	}

	/**
	 * 
	 */
	public void incNumConnections() {
		this.numConnections++;
	}

	/**
	 * 
	 */
	public void decNumConnections() {
		this.numConnections--;
	}

	/**
	 * @param numConnections the numConnections to set
	 */
	public void setNumConnections(final int numConnections) {
		this.numConnections = numConnections;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(final int port) {
		this.port = port;
	}
}