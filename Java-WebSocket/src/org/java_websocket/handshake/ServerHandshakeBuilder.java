package org.java_websocket.handshake;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public interface ServerHandshakeBuilder extends HandshakeBuilder, ServerHandshake {

	/**
	 * @param status
	 */
	public void setHttpStatus( short status );

	/**
	 * @param message
	 */
	public void setHttpStatusMessage( String message );
}