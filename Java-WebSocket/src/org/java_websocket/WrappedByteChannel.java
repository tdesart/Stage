package org.java_websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

import javax.net.ssl.SSLException;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public interface WrappedByteChannel extends ByteChannel {

	/**
	 * @return boolean
	 */
	public boolean isNeedWrite();

	/**
	 * @throws IOException
	 */
	public void writeMore() throws IOException;

	/**
	 * @return boolean
	 */
	public boolean isNeedRead();

	/**
	 * @param dst
	 * @return int
	 * @throws SSLException
	 */
	public int readMore( ByteBuffer dst ) throws SSLException;
}