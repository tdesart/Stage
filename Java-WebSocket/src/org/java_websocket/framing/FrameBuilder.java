package org.java_websocket.framing;

import java.nio.ByteBuffer;

import org.java_websocket.exceptions.InvalidDataException;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public interface FrameBuilder extends Framedata {

	/**
	 * @param fin
	 */
	public abstract void setFin( boolean fin );

	/**
	 * @param optcode
	 */
	public abstract void setOptcode( Opcode optcode );

	/**
	 * @param payload
	 * @throws InvalidDataException
	 */
	public abstract void setPayload( ByteBuffer payload ) throws InvalidDataException;

	/**
	 * @param transferemasked
	 */
	public abstract void setTransferemasked( boolean transferemasked );
}