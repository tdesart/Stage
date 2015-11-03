package org.java_websocket.exceptions;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public class IncompleteHandshakeException extends RuntimeException {

	private static final long serialVersionUID = 7906596804233893092L;
	private final int newsize;

	/**
	 * @param newsize
	 */
	public IncompleteHandshakeException( final int newsize ) {
		this.newsize = newsize;
	}

	/**
	 * 
	 */
	public IncompleteHandshakeException() {
		this.newsize = 0;
	}

	/**
	 * @return int
	 */
	public int getPreferedSize() {
		return newsize;
	}
}