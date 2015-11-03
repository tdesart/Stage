package org.java_websocket.exceptions;

import org.java_websocket.framing.CloseFrame;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public class InvalidHandshakeException extends InvalidDataException {

	/**
	 * Serializable
	 */
	private static final long serialVersionUID = -1426533877490484964L;

	/**
	 * 
	 */
	public InvalidHandshakeException() {
		super( CloseFrame.PROTOCOL_ERROR );
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public InvalidHandshakeException( final String arg0 , final Throwable arg1 ) {
		super( CloseFrame.PROTOCOL_ERROR, arg0, arg1 );
	}

	/**
	 * @param arg0
	 */
	public InvalidHandshakeException( final String arg0 ) {
		super( CloseFrame.PROTOCOL_ERROR, arg0 );
	}

	/**
	 * @param arg0
	 */
	public InvalidHandshakeException( final Throwable arg0 ) {
		super( CloseFrame.PROTOCOL_ERROR, arg0 );
	}
}