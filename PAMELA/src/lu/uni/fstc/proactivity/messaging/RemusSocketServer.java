package lu.uni.fstc.proactivity.messaging;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Set;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;

import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.StringUtils;

/**
 * A simple WebSocketServer implementation. Keeps track the userId in the socket<br><br>
 * Original version from TooTallNate.<br>
 * Updated to meet our needs: db access, id on the socket (trace back to user), security, treat messages by type, etc.
 * 
 * @author TooTallNate - Original
 * @author Sandro Reis © 2012/2013
 * 
 */
public class RemusSocketServer extends WebSocketServer {

//	private final String TYPE_ANSWER_COUNTRY	= "question_country";
//	private final String TYPE_ANSWER_CITY		= "question_city";
//	private final String TYPE_COMMAND			= "command";

	/**
	 * the flag to define if this is running in DEBUG mode or not
	 */
	private final static boolean DEBUGGING = false; 

	/**
	 * the default port for the Socket Server
	 */
	public final static int DEFAUL_PORT = 8887; // 843 flash policy port

	/**
	 * The PAM database wrapper to run the queries in
	 */
//	private AbstractPAMDbWrapper dbPam;

	/**
	 * The Moodle database wrapper to run the queries in
	 */
//	private AbstractMoodleDbWrapper db;

	/**
	 * @param port
	 * @throws UnknownHostException
	 */
	public RemusSocketServer (final int port) throws UnknownHostException {
		super (new InetSocketAddress(port));
		WebSocket.DEBUG = DEBUGGING; // push it forward to the WebSocket
	    Global.wssLogger.info("This Socket Server is now set up in port: " + getPort() +".");
	}

	/**
	 * @param port
	 * @param db the database wrapper to Moodle
	 * @param dbPam the database wrapper to PAM 
	 * @param configFilePath 
	 * @throws Exception 
	 */
//	public RemusSocketServer (final int port, final AbstractMoodleDbWrapper db, final AbstractPAMDbWrapper dbPam, final String configFilePath) throws Exception {
//		this(port);
//		this.setDbPam(dbPam);
//		this.setDb(db);
//		this.setSecureConnection(configFilePath);
//	}

	/**
	 * @param address
	 */
	public RemusSocketServer (final InetSocketAddress address) {
		super (address);
		WebSocket.DEBUG = DEBUGGING; // push it forward to the WebSocket
	    Global.wssLogger.info("This Socket Server is now set up in port: " + getPort() +".");
	}

	@Override
	public void onOpen (WebSocket conn, ClientHandshake handshake) {
		try {
			//SR: updated here to get the 'id' from the HTTP parameters
			String idStr = StringUtils.stringAfter(handshake.getResourceDescriptor(), "id=");
			long id = Long.parseLong(idStr);
			conn.id = id;

			if (id==0) {
				String msg = "Connection refused because connection Id is 0!";
			    Global.wssLogger.warning(msg);
				onWebsocketClose(conn, 0, msg, false);
				return;
			}
//			sendToId (id, "Welcome message from the server to connection with ID = "  + id);
		} catch (Exception e) {
		    Global.wssLogger.warning("Wasn't able to detect the id from the initial connection! Assuming broadcast...");
			conn.id = 0;
		}
		Global.wssLogger.info("The ID = " + conn.id + " entered the room (" + conn + ")");
	}

	@Override
	public void onClose (WebSocket conn, int code, String reason, boolean remote) {
	    Global.wssLogger.info("The ID = " + conn.id + " has left the room (" + conn + ")");
	}

	@Override
	public void onMessage (WebSocket conn, String message) {
	    Global.wssLogger.fine("Message from connection id = " +  conn.id + ": \n'" + message + "'");

		if (conn.id == 0) {
		    Global.wssLogger.warning("Connection ID is 0, Message ignored!");
			return;
		}

		// parse the message into JSON
	    JSONObject json; 
		try {
			json = new JSONObject(message);
		} catch (JSONException e) {
		    Global.wssLogger.severe("The message received is not a valid JSON object: " + message);
		    e.printStackTrace();
		    return;
		}

		// analyse the message type 
		String type = "";
		try {
			type = json.getString(SocketMessage.KEY_MSG_TYPE);
		} catch (JSONException e) {
		    Global.wssLogger.warning("'" + SocketMessage.KEY_MSG_TYPE + "' key not found in JSON object: " + json);
		    return;
		}
	    Global.wssLogger.info("JSON object: " + json + " is of type '" + type);
		
/*/
		// instantiate the right SMClient object depending on the message type
		if (type.equalsIgnoreCase(TYPE_COMMAND)) {
			Global.wssLogger.finer("type is command");
			SMClientCommand msgClient = new SMClientCommand(getDb(), getDbPam(), conn.id, json);
			// delegate to SMClient.execute to do its (specific) job and treat the result
			if (msgClient.execute()) {
				JSONObject result = msgClient.getResult();
				if (result != null) {
					sendToId(conn.id, result.toString());
					try {
						Global.wssLogger.fine("Sending JSON result to the client:\n" + result.toString(2));
					} catch (JSONException ignore) {
					}
				}
				else
					Global.wssLogger.warning("No JSON result to send to the client!");
			}
		} else if (type.equalsIgnoreCase(TYPE_ANSWER_COUNTRY)) {
			Global.wssLogger.finer("type is answer country");
			SMClient msgClient = new SMClientAnswerCountry(getDbPam(), conn.id, json);
			// delegate to SMClient.execute to do its (specific) job
			msgClient.execute();
		} else if (type.equalsIgnoreCase(TYPE_ANSWER_CITY)) {
			Global.wssLogger.finer("type is answer city");
			SMClient msgClient = new SMClientAnswerCity(getDbPam(), conn.id, json);
			// delegate to SMClient.execute to do its (specific) job
			msgClient.execute();
		} else {
			// if SMClient has the wrong type, report it and exit
			Global.wssLogger.warning("Unexpected type received (JSON object): '" + type + "'.");
			return;
		}
/**/
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main (final String[] args) throws InterruptedException , IOException {
	    Global.wssLogger.info("**************************************************************");
	    Global.wssLogger.info("****************** Starting Socket Server! *******************");

	    // this is how it's called in PAM.java
	    // final PamSockekServer socketServer = new PamSockekServer (PamSockekServer.DEFAUL_PORT, db, dbPam, "privateKeyStore.cfg.xml");

		int port = DEFAUL_PORT; 
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception ex) {
			//ignore error, assume default port and continue
		}

		PamSockekServer s = new PamSockekServer (port);
		try {
			s.setSecureConnection("privateKeyStore.cfg.xml");
//			s.setDb(db);
//			s.setDbPam(dbPam);

			s.start();
//	    Global.wssLogger.info("Port: " + s.getPort() +" is now being used by this Socket Server.");
		} catch (final Exception e) {
			Global.wssLogger.severe("Exception Msg: "+ e.getMessage());
			e.printStackTrace();
		}

/*/
	    BufferedReader sysin = new BufferedReader (new InputStreamReader (System.in));
//		while (!dbPam.isScheduleToStop()) {
		while (true) {
			String inId = sysin.readLine();
			long id;
			try {
				id = Long.parseLong (inId);
			} catch (NumberFormatException e) {
				id = 0;
			}

			String in = sysin.readLine();
			s.sendToId (id, in);
		}
//		stop();
/**/
	}

	@Override
	public void finalize () throws Throwable {
		Global.wssLogger.info("****************** Exiting Socket Server! ********************");
	    Global.wssLogger.info("**************************************************************");
	    super.finalize();
	}

	@Override
	public void onError( WebSocket conn, Exception ex ) {
		Global.wssLogger.severe("Error detected on socket server on '" + getAddress() + "': " + ex.getMessage());
		ex.printStackTrace();
	}

	/**
	 * Sends <var>text</var> to the connected WebSocket client identified with <var>id</var>.<br>
	 * If <var>id</var> is 0, the method assumes it is a <code>broadcast</code>, and calls <code>sendToAll</code> instead.<br> 
	 * If <var>id</var> is less than 0, <var>text</var> is null or empty, the method returns without any action. 
	 * @param id The Long that identifies the WebSocket
	 * @param text The String to send across the network.
	 */
	public void sendToId (final long id,  String text) {
		// Nothing to do, under these conditions
		if ((id < 0) || StringUtils.isEmptyOrNullString(text))
			return;

		// Broadcast message and leave
		if (id == 0) {
			sendToAll(text);
			return;
		}

		// Search for the WebSocket connection with the id provided as the method argument and the message through this web socket
		Set<WebSocket> con = connections();
		synchronized (con) {
		    Iterator<WebSocket> itr = con.iterator();
		    while (itr.hasNext()) {
		    	WebSocket c = itr.next();
				if (c.id == id) { // found it !
					Global.wssLogger.finer("found ["+ c.id + "]. Sending text: " + text);
					c.send (text);

					// To allow for several client connection from the same user,
					// NOW we don't break, but send to all connections with the same id...
//					break; // go away, no need to continue while cycle
				}
		    }
		}
	}

	/**
	 * Sends <var>text</var> to all currently connected WebSocket clients.<br>
	 * If <var>text</var> is null or empty, the method returns without any action.
	 * @param text The String to send across the network.
	 */
	public void sendToAll (String text) {
		// Nothing to do, under these conditions
		if (StringUtils.isEmptyOrNullString(text))
			return;

		// Go through the set of exiting WebSocket connections and send the message through them
		Set<WebSocket> con = connections();
		synchronized (con) {
		    Iterator<WebSocket> itr = con.iterator();
		    while (itr.hasNext()) {
		    	WebSocket c = itr.next();
				c.send (text);
			}
		}
	}

	/**
	 * Checks the currently open sockets to see if the user is online
	 * @param userId 
	 * @return true if the user is currently online
	 */
	public boolean isUserOnline (final long userId) {
		// Nothing to do, under these conditions
		if (userId < 0)
			return false;

		// Search for the WebSocket connection with the id provided as the method argument and the message through this web socket
		Set<WebSocket> con = connections();
		synchronized (con) {
		    Iterator<WebSocket> itr = con.iterator();
		    while (itr.hasNext()) {
		    	WebSocket c = itr.next();
				if (c.id == userId) { // found it !
					return true;
				}
		    }
		}
		return false;
	}

	/**
	 * Gets the id of all the active connections (socket) and creates a comma separated String with them
	 * @return a string with comma separated id's from the active socket connections
	 */
	public String getActiveIdsStr () {
		StringBuffer buf = new StringBuffer();
		boolean first = true;
		final Set<WebSocket> con = connections();
		synchronized (con) {
		    final Iterator<WebSocket> itr = con.iterator();
		    while (itr.hasNext()) {
		    	final WebSocket c = itr.next();
		    	if (first) {
		    		buf.append (c.id);
					first = false;
		    	}
		    	else
		    		buf.append (", " + c.id);
		    	
		    }
		}
		return buf.toString();
	}

	/**
	 * a public method to set up everything, so that the web server is embedded in a secure connection (WSS)
	 * @param configFilePath the path to the private key configuration file
	 * @throws Exception 
	 */
/*/	public void setSecureConnection(final String configFilePath) throws Exception {
		// set up the configuration parameters
		SockectServerConnectionParameters connParams = new SockectServerConnectionParameters(configFilePath);

		final String STORETYPE = "JKS";
		final String PROTOCOL = "TLS";
		final String ALGORITHM = "SunX509";
		
		// load up the key store
		final KeyStore ks = KeyStore.getInstance( STORETYPE );
		final File kf = new File(connParams.keyStoreFileName);
		ks.load( new FileInputStream( kf ), connParams.keyStorePassword.toCharArray() );

		final KeyManagerFactory kmf = KeyManagerFactory.getInstance(ALGORITHM);
		kmf.init(ks, connParams.keyPassword.toCharArray());
		final TrustManagerFactory tmf = TrustManagerFactory.getInstance(ALGORITHM);
		tmf.init(ks);

		SSLContext sslContext = null;
		sslContext = SSLContext.getInstance(PROTOCOL);
		sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		this.setWebSocketFactory( new DefaultSSLWebSocketServerFactory(sslContext) );
	}
/**/

	/**
	 * @return the dbPam
	 */
/*/	public final AbstractPAMDbWrapper getDbPam() {
		return this.dbPam;
	}
/**/
	/**
	 * @param dbPam the dbPam to set
	 */
/*/	public final void setDbPam(final AbstractPAMDbWrapper dbPam) {
		this.dbPam = dbPam;
	}
/**/
	/**
	 * @return the db
	 */
/*/	public final AbstractMoodleDbWrapper getDb() {
		return this.db;
	}
/**/
	/**
	 * @param db the db to set
	 */
/*/	public final void setDb(final AbstractMoodleDbWrapper db) {
		this.db = db;
	}
/**/
}