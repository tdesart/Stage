package org.java_websocket.drafts;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.java_websocket.WebSocket.Role;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidFrameException;
import org.java_websocket.exceptions.InvalidHandshakeException;
import org.java_websocket.exceptions.LimitExedeedException;
import org.java_websocket.exceptions.NotSendableException;
import org.java_websocket.framing.CloseFrameBuilder;
import org.java_websocket.framing.FrameBuilder;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.Framedata.Opcode;
import org.java_websocket.framing.FramedataImpl1;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ClientHandshakeBuilder;
import org.java_websocket.handshake.HandshakeBuilder;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.util.Base64;
import org.java_websocket.util.Charsetfunctions;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public class Draft_10 extends Draft {

//	private static class IncompleteException extends Throwable {
	private static class IncompleteException extends Exception {
		
		/**
		 * It's Serializable.
		 */
		private static final long serialVersionUID = 7330519489840500997L;
		
		
		private final int preferedsize;
		public IncompleteException( final int preferedsize ) {
			this.preferedsize = preferedsize;
		}
		public int getPreferedSize() {
			return preferedsize;
		}
	}

	/**
	 * @param handshakedata
	 * @return the version
	 */
	public static int readVersion( final Handshakedata handshakedata ) {
		final String vers = handshakedata.getFieldValue( "Sec-WebSocket-Version" );
		if( vers.length() > 0 ) {
			int v;
			try {
				v = Integer.parseInt(vers.trim());
				return v;
			} catch ( final NumberFormatException e ) {
				return -1;
			}
		}
		return -1;
	}

	private ByteBuffer incompleteframe;
	
//	Sandro Reis: Unused, so commented
//	private final Framedata fragmentedframe = null;
	
	private final Random reuseableRandom = new Random();

	@Override
	public HandshakeState acceptHandshakeAsClient( final ClientHandshake request, final ServerHandshake response ) throws InvalidHandshakeException {
		if( !request.hasFieldValue( "Sec-WebSocket-Key" ) || !response.hasFieldValue( "Sec-WebSocket-Accept" ) )
			return HandshakeState.NOT_MATCHED;

		final String seckey_answere = response.getFieldValue( "Sec-WebSocket-Accept" );
		String seckey_challenge = request.getFieldValue( "Sec-WebSocket-Key" );
		seckey_challenge = generateFinalKey( seckey_challenge );

		if( seckey_challenge.equals( seckey_answere ) )
			return HandshakeState.MATCHED;
		return HandshakeState.NOT_MATCHED;
	}

	@Override
	public HandshakeState acceptHandshakeAsServer( final ClientHandshake handshakedata ) throws InvalidHandshakeException {
		// Sec-WebSocket-Origin is only required for browser clients
		final int v = readVersion( handshakedata );
		if( v == 7 || v == 8 )// g
			return basicAccept( handshakedata ) ? HandshakeState.MATCHED : HandshakeState.NOT_MATCHED;
		return HandshakeState.NOT_MATCHED;
	}

	@Override
	public ByteBuffer createBinaryFrame( final Framedata framedata ) {
		final ByteBuffer mes = framedata.getPayloadData();
		final boolean mask = role == Role.CLIENT; // framedata.getTransfereMasked();
		final int sizebytes = mes.remaining() <= 125 ? 1 : mes.remaining() <= 65535 ? 2 : 8;
		final ByteBuffer buf = ByteBuffer.allocate( 1 + ( sizebytes > 1 ? sizebytes + 1 : sizebytes ) + ( mask ? 4 : 0 ) + mes.remaining() );
		final byte optcode = fromOpcode( framedata.getOpcode() );
		byte one = (byte) ( framedata.isFin() ? -128 : 0 );
		one |= optcode;
		buf.put( one );
		final byte[] payloadlengthbytes = toByteArray( mes.remaining(), sizebytes );
		assert ( payloadlengthbytes.length == sizebytes );

		if( sizebytes == 1 ) {
			buf.put( (byte) ( payloadlengthbytes[ 0 ] | ( mask ? (byte) -128 : 0 ) ) );
		} else if( sizebytes == 2 ) {
			buf.put( (byte) ( (byte) 126 | ( mask ? (byte) -128 : 0 ) ) );
			buf.put( payloadlengthbytes );
		} else if( sizebytes == 8 ) {
			buf.put( (byte) ( (byte) 127 | ( mask ? (byte) -128 : 0 ) ) );
			buf.put( payloadlengthbytes );
		} else
			throw new RuntimeException( "Size representation not supported/specified" );

		if( mask ) {
			final ByteBuffer maskkey = ByteBuffer.allocate( 4 );
			maskkey.putInt( reuseableRandom.nextInt() );
			buf.put( maskkey.array() );
			for( int i = 0 ; i < mes.limit() ; i++ ) {
				buf.put( (byte) ( mes.get() ^ maskkey.get( i % 4 ) ) );
			}
		} else
			buf.put( mes );
		// translateFrame ( buf.array () , buf.array ().length );
		assert ( buf.remaining() == 0 ) : buf.remaining();
		buf.flip();

		return buf;
	}

	@Override
	public List<Framedata> createFrames( final ByteBuffer binary, final boolean mask ) {
		final FrameBuilder curframe = new FramedataImpl1();
		try {
			curframe.setPayload( binary );
		} catch ( final InvalidDataException e ) {
			throw new NotSendableException( e );
		}
		curframe.setFin( true );
		curframe.setOptcode( Opcode.BINARY );
		curframe.setTransferemasked( mask );
		return Collections.singletonList( (Framedata) curframe );
	}

	@Override
	public List<Framedata> createFrames( final String text, final boolean mask ) {
		final FrameBuilder curframe = new FramedataImpl1();
		try {
			curframe.setPayload( ByteBuffer.wrap( Charsetfunctions.utf8Bytes( text ) ) );
		} catch ( final InvalidDataException e ) {
			throw new NotSendableException( e );
		}
		curframe.setFin( true );
		curframe.setOptcode( Opcode.TEXT );
		curframe.setTransferemasked( mask );
		return Collections.singletonList( (Framedata) curframe );
	}

	private byte fromOpcode( final Opcode opcode ) {
		if( opcode == Opcode.CONTINUOUS )
			return 0;
		else if( opcode == Opcode.TEXT )
			return 1;
		else if( opcode == Opcode.BINARY )
			return 2;
		else if( opcode == Opcode.CLOSING )
			return 8;
		else if( opcode == Opcode.PING )
			return 9;
		else if( opcode == Opcode.PONG )
			return 10;
		throw new RuntimeException( "Don't know how to handle " + opcode.toString() );
	}

	private String generateFinalKey( final String in ) {
		final String seckey = in.trim();
		final String acc = seckey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
		MessageDigest sh1;
		try {
			sh1 = MessageDigest.getInstance( "SHA1" );
		} catch ( final NoSuchAlgorithmException e ) {
			throw new RuntimeException( e );
		}
		return Base64.encodeBytes( sh1.digest( acc.getBytes() ) );
	}

	@Override
	public ClientHandshakeBuilder postProcessHandshakeRequestAsClient( final ClientHandshakeBuilder request ) {
		request.put( "Upgrade", "websocket" );
		request.put( "Connection", "Upgrade" ); // to respond to a Connection keep alives
		request.put( "Sec-WebSocket-Version", "8" );

		final byte[] random = new byte[ 16 ];
		reuseableRandom.nextBytes( random );
		request.put( "Sec-WebSocket-Key", Base64.encodeBytes( random ) );

		return request;
	}

	@Override
	public HandshakeBuilder postProcessHandshakeResponseAsServer( final ClientHandshake request, final ServerHandshakeBuilder response ) throws InvalidHandshakeException {
		response.put( "Upgrade", "websocket" );
		response.put( "Connection", request.getFieldValue( "Connection" ) ); // to respond to a Connection keep alives
		response.setHttpStatusMessage( "Switching Protocols" );
		final String seckey = request.getFieldValue( "Sec-WebSocket-Key" );
		if( seckey == null )
			throw new InvalidHandshakeException( "missing Sec-WebSocket-Key" );
		response.put( "Sec-WebSocket-Accept", generateFinalKey( seckey ) );
		return response;
	}

	private byte[] toByteArray( final long val, final int bytecount ) {
		final byte[] buffer = new byte[ bytecount ];
		final int highest = 8 * bytecount - 8;
		for( int i = 0 ; i < bytecount ; i++ ) {
			buffer[ i ] = (byte) ( val >>> ( highest - 8 * i ) );
		}
		return buffer;
	}

	private Opcode toOpcode( final byte opcode ) throws InvalidFrameException {
		switch ( opcode ) {
			case 0:
				return Opcode.CONTINUOUS;
			case 1:
				return Opcode.TEXT;
			case 2:
				return Opcode.BINARY;
				// 3-7 are not yet defined
			case 8:
				return Opcode.CLOSING;
			case 9:
				return Opcode.PING;
			case 10:
				return Opcode.PONG;
				// 11-15 are not yet defined
			default :
				throw new InvalidFrameException( "unknow optcode " + (short) opcode );
		}
	}

	@Override
	public List<Framedata> translateFrame( final ByteBuffer buffer ) throws LimitExedeedException , InvalidDataException {
		final List<Framedata> frames = new LinkedList<Framedata>();
		Framedata cur;

		if( incompleteframe != null ) {
			// complete an incomplete frame
			while ( true ) {
				try {
					buffer.mark();
					final int available_next_byte_count = buffer.remaining();// The number of bytes received
					final int expected_next_byte_count = incompleteframe.remaining();// The number of bytes to complete the incomplete frame

					if( expected_next_byte_count > available_next_byte_count ) {
						// did not receive enough bytes to complete the frame
						incompleteframe.put( buffer.array(), buffer.position(), available_next_byte_count );
						buffer.position( buffer.position() + available_next_byte_count );
						return Collections.emptyList();
					}
					incompleteframe.put( buffer.array(), buffer.position(), expected_next_byte_count );
					buffer.position( buffer.position() + expected_next_byte_count );

					cur = translateSingleFrame( (ByteBuffer) incompleteframe.duplicate().position( 0 ) );
					frames.add( cur );
					incompleteframe = null;
					break; // go on with the normal frame receival
				} catch ( final IncompleteException e ) {
					// extending as much as suggested
					// int oldsize = 
						incompleteframe.limit();
					final ByteBuffer extendedframe = ByteBuffer.allocate( checkAlloc( e.getPreferedSize() ) );
					assert ( extendedframe.limit() > incompleteframe.limit() );
					incompleteframe.rewind();
					extendedframe.put( incompleteframe );
					incompleteframe = extendedframe;
					
					return translateFrame( buffer );
				}
			}
		}

		while ( buffer.hasRemaining() ) {// Read as much as possible full frames
			buffer.mark();
			try {
				cur = translateSingleFrame( buffer );
				frames.add( cur );
			} catch ( final IncompleteException e ) {
				// remember the incomplete data
				buffer.reset();
				final int pref = e.getPreferedSize();
				incompleteframe = ByteBuffer.allocate( checkAlloc( pref ) );
				incompleteframe.put( buffer );
				break;
			}
		}
		return frames;
	}

	/**
	 * @param buffer
	 * @return the frame
	 * @throws IncompleteException
	 * @throws InvalidDataException
	 */
	public Framedata translateSingleFrame( final ByteBuffer buffer ) throws IncompleteException , InvalidDataException {
		final int maxpacketsize = buffer.remaining();
		int realpacketsize = 2;
		if( maxpacketsize < realpacketsize )
			throw new IncompleteException( realpacketsize );
		final byte b1 = buffer.get( /*0*/);
		final boolean FIN = b1 >> 8 != 0;
		final byte rsv = (byte) ( ( b1 & ~(byte) 128 ) >> 4 );
		if( rsv != 0 )
			throw new InvalidFrameException( "bad rsv " + rsv );
		final byte b2 = buffer.get( /*1*/);
		final boolean MASK = ( b2 & -128 ) != 0;
		int payloadlength = (byte) ( b2 & ~(byte) 128 );
		final Opcode optcode = toOpcode( (byte) ( b1 & 15 ) );

		if( !FIN ) {
			if( optcode == Opcode.PING || optcode == Opcode.PONG || optcode == Opcode.CLOSING ) {
				throw new InvalidFrameException( "control frames may no be fragmented" );
			}
		}

		if( payloadlength >= 0 && payloadlength <= 125 ) {
		} else {
			if( optcode == Opcode.PING || optcode == Opcode.PONG || optcode == Opcode.CLOSING ) {
				throw new InvalidFrameException( "more than 125 octets" );
			}
			if( payloadlength == 126 ) {
				realpacketsize += 2; // additional length bytes
				if( maxpacketsize < realpacketsize )
					throw new IncompleteException( realpacketsize );
				final byte[] sizebytes = new byte[ 3 ];
				sizebytes[ 1 ] = buffer.get( /*1 + 1*/);
				sizebytes[ 2 ] = buffer.get( /*1 + 2*/);
				payloadlength = new BigInteger( sizebytes ).intValue();
			} else {
				realpacketsize += 8; // additional length bytes
				if( maxpacketsize < realpacketsize )
					throw new IncompleteException( realpacketsize );
				final byte[] bytes = new byte[ 8 ];
				for( int i = 0 ; i < 8 ; i++ ) {
					bytes[ i ] = buffer.get( /*1 + i*/);
				}
				final long length = new BigInteger( bytes ).longValue();
				if( length > Integer.MAX_VALUE ) {
					throw new LimitExedeedException( "Payloadsize is to big..." );
				} else {
					payloadlength = (int) length;
				}
			}
		}

		// int maskskeystart = foff + realpacketsize;
		realpacketsize += ( MASK ? 4 : 0 );
		// int payloadstart = foff + realpacketsize;
		realpacketsize += payloadlength;

		if( maxpacketsize < realpacketsize )
			throw new IncompleteException( realpacketsize );

		final ByteBuffer payload = ByteBuffer.allocate( checkAlloc( payloadlength ) );
		if( MASK ) {
			final byte[] maskskey = new byte[ 4 ];
			buffer.get( maskskey );
			for( int i = 0 ; i < payloadlength ; i++ ) {
				payload.put( (byte) ( buffer.get( /*payloadstart + i*/) ^ maskskey[ i % 4 ] ) );
			}
		} else {
			payload.put( buffer.array(), buffer.position(), payload.limit() );
			buffer.position( buffer.position() + payload.limit() );
		}

		FrameBuilder frame;
		if( optcode == Opcode.CLOSING ) {
			frame = new CloseFrameBuilder();
		} else {
			frame = new FramedataImpl1();
			frame.setFin( FIN );
			frame.setOptcode( optcode );
		}
		payload.flip();
		frame.setPayload( payload );
		return frame;
	}

	@Override
	public void reset() {
		incompleteframe = null;
	}

	@Override
	public CloseHandshakeType getCloseHandshakeType() {
		return CloseHandshakeType.TWOWAY;
	}
}