package lu.uni.fstc.proactivity.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * <b>Socket Server Threads for the connection with the users</b>
 *  
/ * @deprecated <br>
 * This class has been depreciated due to the fact that the communication is done using the database<br>
 * Sockets don't work as we needed in PHP, because the server script would stop waiting for connection, and we need it to be running.
 *  
 * @author Sergio Marques Dias
 * @version 3.0 - Sandro Reis © 2011/2012
 * @version 2.0 - Sergio Marques Dias <br> 
 * @version 1.0 - Yann Milin
 * 
 */
@Deprecated
@SuppressWarnings("deprecation")
public class SocketClientThread implements Runnable  {
	
	private String clientId = "";
	private BufferedReader is;
	private PrintStream os;
	private Socket clientSocket;
	private int id;
	private SocketServerThread server;

	/**
	 * @param clientSocket
	 * @param id
	 * @param server
	 */
	public SocketClientThread(Socket clientSocket, int id, SocketServerThread server) {
		this.clientSocket = clientSocket;
		this.id = id;
		this.server = server;
		
		Global.logger.info("Connection " + id + " established with: " + clientSocket );
		try {
			is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			os = new PrintStream(clientSocket.getOutputStream());
		} 
		catch (IOException e) {
			Global.logger.severe(e.getMessage());
		}
	}

	/**
	 * @return clientId
	 */
	public String getClientId() {
		return clientId;
	}
	
	/**
	 * Creates Socket Server Threads for each client socket
	 */
	@Override
	public void run() {
		String clientid;
		InetAddress sAddress = clientSocket.getInetAddress();
		Global.logger.info("socket channel of "+id+" : " + sAddress);
		try {
			boolean serverStop = false;
	
			while (true) {
				clientid = is.readLine();
				Global.logger.info("Received " + clientid + " from Connection " + id + "." );

				setClientId(clientid);
				
				server.addToSocketList(this);
				server.printSocketList();
				break;
			}

		if (serverStop)
			server.stopServer();
		} 
		catch (IOException e) {
			Global.logger.severe(e.getMessage());
		}
	}
	
	/**
	 * @param msg
	 */
	public void sendMessage(String msg) {
		os.print(msg);
		os.flush();
		Global.logger.info("message sent :" + msg);
	}
	
	/**
	 * @param id
	 */
	private void setClientId(String id) {
		clientId = id;
	}
	
	/**
	 * @return a string 
	 */
	@Override
	public String toString() {
		String str = "Sockect Client Thread id = " + this.clientId;
		return str;
	}
}