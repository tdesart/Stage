package org.java_websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

/**
 * Original version from TooTallNate, changed to meet our needs, by Sandro Reis
 * @author TooTallNate - Original
 *
 */
public class SocketChannelIOHelper {

	/**
	 * @param buf
	 * @param ws
	 * @param channel
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean read( final ByteBuffer buf, final WebSocketImpl ws, final ByteChannel channel ) throws IOException {
		buf.clear();
		final int read = channel.read( buf );
		buf.flip();

		if( read == -1 ) {
			ws.eot( null );
			return false;
		}
		return read != 0;
	}

	/**
	 * @param buf
	 * @param ws
	 * @param channel
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean readMore( final ByteBuffer buf, final WebSocketImpl ws, final WrappedByteChannel channel ) throws IOException {
		buf.clear();
		final int read = channel.readMore( buf );
		buf.flip();

		if( read == -1 ) {
			ws.eot( null );
			return false;
		}
		return channel.isNeedRead();
	}

	/** Returns whether the whole outQueue has been flushed 
	 * @param ws 
	 * @param sockchannel 
	 * @return boolean
	 * @throws IOException */
	public static boolean batch( final WebSocketImpl ws, final ByteChannel sockchannel ) throws IOException {
		ByteBuffer buffer = ws.outQueue.peek();

		if( buffer == null ) {
			if( sockchannel instanceof WrappedByteChannel ) {
				final WrappedByteChannel c = (WrappedByteChannel) sockchannel;
				if( c.isNeedWrite() ) {
					c.writeMore();
					return !c.isNeedWrite();
				}
				return true;
			}
		} else {
			do {// FIXME writing as much as possible is unfair!!
				/*int written = */sockchannel.write( buffer );
				if( buffer.remaining() > 0 ) {
					return false;
				} else {
					ws.outQueue.poll(); // Buffer finished. Remove it.
					buffer = ws.outQueue.peek();
				}
			} while ( buffer != null );
		}

		if( ws.isClosed() ) {
			synchronized ( ws ) {
				sockchannel.close();
			}
		}
		return sockchannel instanceof WrappedByteChannel == true ? !( (WrappedByteChannel) sockchannel ).isNeedWrite() : true;
	}
}