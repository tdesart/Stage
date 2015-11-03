package org.java_websocket.handshake;

import java.util.Iterator;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public interface Handshakedata {

	/**
	 * @return Iterator<String>
	 */
	public Iterator<String> iterateHttpFields();

	/**
	 * @param name
	 * @return String
	 */
	public String getFieldValue( String name );

	/**
	 * @param name
	 * @return boolean
	 */
	public boolean hasFieldValue( String name );

	/**
	 * @return byte[]
	 */
	public byte[] getContent();
}