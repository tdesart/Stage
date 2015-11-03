package org.java_websocket.exceptions;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public class InvalidDataException extends Exception {
	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 3731842424390998726L;
	
	private int closecode;
	
	/**
	 * @param closecode
	 */
	public InvalidDataException( final int closecode ) {
		this.closecode = closecode;
	}

	/**
	 * @param closecode
	 * @param s
	 */
	public InvalidDataException( final int closecode , final String s ) {
		super( s );
		this.closecode = closecode;
	}

	/**
	 * @param closecode
	 * @param t
	 */
	public InvalidDataException( final int closecode , final Throwable t ) {
		super( t );
		if( t instanceof InvalidDataException ) {
			this.closecode = ( (InvalidDataException) t ).getCloseCode();
		}
	}

	/**
	 * @param closecode
	 * @param s
	 * @param t
	 */
	public InvalidDataException( final int closecode , final String s , final Throwable t ) {
		super( s, t );
		if( t instanceof InvalidDataException ) {
			this.closecode = ( (InvalidDataException) t ).getCloseCode();
		}
	}

	/**
	 * @return int
	 */
	public int getCloseCode() {
		return closecode;
	}
}