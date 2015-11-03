package org.java_websocket.framing;

import java.nio.ByteBuffer;

import org.java_websocket.exceptions.InvalidFrameException;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public interface Framedata {

	/**
	 * @author sandro.reis
	 *
	 */
	public enum Opcode {
		/**
		 * 
		 */
		CONTINUOUS,
		/**
		 * 
		 */
		TEXT, 
		/**
		 * 
		 */
		BINARY, 
		/**
		 * 
		 */
		PING, 
		/**
		 * 
		 */
		PONG, 
		/**
		 * 
		 */
		CLOSING
		// more to come
	}

	/**
	 * @return boolean
	 */
	public boolean isFin();

	/**
	 * @return boolean
	 */
	public boolean getTransfereMasked();

	/**
	 * @return Opcode
	 */
	public Opcode getOpcode();

	/**
	 * @return ByteBuffer
	 */
	public ByteBuffer getPayloadData();// TODO the separation of the application data and the extension data is yet to be done

	/**
	 * @param nextframe
	 * @throws InvalidFrameException
	 */
	public abstract void append( Framedata nextframe ) throws InvalidFrameException;
}