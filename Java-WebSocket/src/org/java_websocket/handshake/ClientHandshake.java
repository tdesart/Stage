package org.java_websocket.handshake;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public interface ClientHandshake extends Handshakedata {

	/**
	 * @return a string
	 */
	public String getResourceDescriptor();
}