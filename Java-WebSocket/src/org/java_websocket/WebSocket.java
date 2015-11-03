package org.java_websocket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;

import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidHandshakeException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshakeBuilder;

/**
 * Original version from TooTallNate, changed to meet our needs, by Sandro Reis
 * @author TooTallNate - Original
 *
 */
public abstract class WebSocket {
	/**
	 *
	 */
	public enum Role {
		/**
		 * 
		 */
		CLIENT,
		/**
		 * 
		 */
		SERVER
	}

	/**
	 * a possible id for this socket 
	 * SR: added to store the user Id I need for PAM
	 */
	public long id;

	/**
	 * 
	 */
	public static final int RCVBUF = 16384;

	/**
	 * 
	 */
	public static/*final*/boolean DEBUG = false; // must be final in the future in order to take advantage of VM optimization

	/**
	 * 
	 */
	public static final int READY_STATE_CONNECTING = 0;
	/**
	 * 
	 */
	public static final int READY_STATE_OPEN = 1;
	/**
	 * 
	 */
	public static final int READY_STATE_CLOSING = 2;
	/**
	 * 
	 */
	public static final int READY_STATE_CLOSED = 3;
	/**
	 * The default port of WebSockets, as defined in the spec. If the nullary
	 * constructor is used, DEFAULT_PORT will be the port the WebSocketServer
	 * is binded to. Note that ports under 1024 usually require root permissions.
	 */
	public static final int DEFAULT_PORT = 80;

	/**
	 * 
	 */
	public static final int DEFAULT_WSS_PORT = 443;

	/**
	 * sends the closing handshake.
	 * may be send in response to an other handshake.
	 * @param code 
	 * @param message 
	 */
	public abstract void close( int code, String message );

	/**
	 * @param code
	 */
	public abstract void close( int code );

	protected abstract void close( InvalidDataException e );

	/**
	 * Send Text data to the other end.
	 * @param text 
	 * 
	 * @throws IllegalArgumentException
	 * @throws NotYetConnectedException
	 */
	public abstract void send( String text ) throws NotYetConnectedException;

	/**
	 * Send Binary data (plain bytes) to the other end.
	 * @param bytes 
	 * 
	 * @throws IllegalArgumentException
	 * @throws InterruptedException
	 * @throws NotYetConnectedException
	 */
	public abstract void send( ByteBuffer bytes ) throws IllegalArgumentException , NotYetConnectedException , InterruptedException;

	/**
	 * @param bytes
	 * @throws IllegalArgumentException
	 * @throws NotYetConnectedException
	 * @throws InterruptedException
	 */
	public abstract void send( byte[] bytes ) throws IllegalArgumentException , NotYetConnectedException , InterruptedException;

	/**
	 * @param framedata
	 */
	public abstract void sendFrame( Framedata framedata );

	/**
	 * @return a boolean
	 */
	public abstract boolean hasBufferedData();

	/**
	 * @param handshakedata
	 * @throws InvalidHandshakeException
	 * @throws InterruptedException
	 */
	public abstract void startHandshake( ClientHandshakeBuilder handshakedata ) throws InvalidHandshakeException , InterruptedException;

	/**
	 * @return InetSocketAddress
	 */
	public abstract InetSocketAddress getRemoteSocketAddress();

	/**
	 * @return InetSocketAddress
	 */
	public abstract InetSocketAddress getLocalSocketAddress();

	/**
	 * @return boolean
	 */
	public abstract boolean isConnecting();

	/**
	 * @return boolean
	 */
	public abstract boolean isOpen();

	/**
	 * @return boolean
	 */
	public abstract boolean isClosing();

	/**
	 * @return boolean
	 */
	public abstract boolean isClosed();
	
	/**
	 * @return a Draft
	 */
	public abstract Draft getDraft();

	/**
	 * Retrieve the WebSocket 'readyState'.
	 * This represents the state of the connection.
	 * It returns a numerical value, as per W3C WebSockets specs.
	 * 
	 * @return Returns '0 = CONNECTING', '1 = OPEN', '2 = CLOSING' or '3 = CLOSED'
	 */
	public abstract int getReadyState();
}