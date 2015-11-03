package org.java_websocket.handshake;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public interface ServerHandshake extends Handshakedata {

	/**
	 * @return short
	 */
	public short getHttpStatus();

	/**
	 * @return String
	 */
	public String getHttpStatusMessage();
}