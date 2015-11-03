package org.java_websocket.exceptions;

import org.java_websocket.framing.CloseFrame;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public class LimitExedeedException extends InvalidDataException {

	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 6908339749836826785L;

	/**
	 * 
	 */
	public LimitExedeedException() {
		super( CloseFrame.TOOBIG );
	}

	/**
	 * @param s
	 */
	public LimitExedeedException( final String s ) {
		super( CloseFrame.TOOBIG, s );
	}
}
