package org.java_websocket.handshake;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public interface HandshakeBuilder extends Handshakedata {

	/**
	 * @param content
	 */
	public abstract void setContent( byte[] content );

	/**
	 * @param name
	 * @param value
	 */
	public abstract void put( String name, String value );
}