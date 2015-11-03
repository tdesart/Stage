package org.java_websocket.exceptions;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public class NotSendableException extends RuntimeException {

	/**
	 * Serializable
	 */
	private static final long serialVersionUID = -6468967874576651628L;

	/**
	 * 
	 */
	public NotSendableException() {
	}

	/**
	 * @param message
	 */
	public NotSendableException( final String message ) {
		super( message );
	}

	/**
	 * @param cause
	 */
	public NotSendableException( final Throwable cause ) {
		super( cause );
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NotSendableException( final String message , final Throwable cause ) {
		super( message, cause );
	}
}