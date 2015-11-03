package org.java_websocket.exceptions;

import org.java_websocket.framing.CloseFrame;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public class InvalidFrameException extends InvalidDataException {

	/**
	 * Serializable
	 */
	private static final long serialVersionUID = -9016496369828887591L;

	/**
	 * 
	 */
	public InvalidFrameException() {
		super( CloseFrame.PROTOCOL_ERROR );
	}

	/**
	 * @param arg0
	 */
	public InvalidFrameException( final String arg0 ) {
		super( CloseFrame.PROTOCOL_ERROR, arg0 );
	}

	/**
	 * @param arg0
	 */
	public InvalidFrameException( final Throwable arg0 ) {
		super( CloseFrame.PROTOCOL_ERROR, arg0 );
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public InvalidFrameException( final String arg0 , final Throwable arg1 ) {
		super( CloseFrame.PROTOCOL_ERROR, arg0, arg1 );
	}
}