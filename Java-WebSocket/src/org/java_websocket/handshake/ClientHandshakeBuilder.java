package org.java_websocket.handshake;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 *
 */
public interface ClientHandshakeBuilder extends HandshakeBuilder, ClientHandshake {

	/**
	 * @param resourcedescriptor
	 */
	public void setResourceDescriptor( String resourcedescriptor );
}