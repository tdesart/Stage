package org.java_websocket.drafts;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.java_websocket.WebSocket.Role;
import org.java_websocket.exceptions.IncompleteHandshakeException;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidHandshakeException;
import org.java_websocket.exceptions.LimitExedeedException;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ClientHandshakeBuilder;
import org.java_websocket.handshake.HandshakeBuilder;
import org.java_websocket.handshake.HandshakeImpl1Client;
import org.java_websocket.handshake.HandshakeImpl1Server;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.util.Charsetfunctions;

/**
 * Base class for everything of a websocket specification which is not common such as the way the handshake is read or frames are transfered.
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public abstract class Draft {

	/**
	 * @author Sandro Reis
	 */
	public enum HandshakeState {
		/** 
		 * Handshake matched this Draft successfully 
		 */
		MATCHED,
		/** 
		 * Handshake is does not match this Draft 
		 */
		NOT_MATCHED
	}
	/**
	 * @author Sandro Reis
	 */
	public enum CloseHandshakeType {
		/**
		 * 
		 */
		NONE, 
		/**
		 * 
		 */
		ONEWAY, 
		/**
		 * 
		 */
		TWOWAY
	}

	/**
	 * 
	 */
	public static int MAX_FAME_SIZE = 1000 * 1;
	
	/**
	 * 
	 */
	public static final int INITIAL_FAMESIZE = 64;

	/**
	 * 
	 */
	public static final byte[] FLASH_POLICY_REQUEST = Charsetfunctions.utf8Bytes( "<policy-file-request/>\0" );

	/** In some cases the handshake will be parsed different depending on whether */
	protected Role role = null;

	/**
	 * @param buf
	 * @return ByteBuffer
	 */
	public static ByteBuffer readLine( final ByteBuffer buf ) {
		ByteBuffer sbuf = ByteBuffer.allocate( buf.remaining() );
		byte prev = '0';
		byte cur = '0';
		while ( buf.hasRemaining() ) {
			prev = cur;
			cur = buf.get();
			sbuf.put( cur );
			if( prev == (byte) '\r' && cur == (byte) '\n' ) {
				sbuf.limit( sbuf.position() - 2 );
				sbuf.position( 0 );
				return sbuf;

			}
		}
		// ensure that there wont be any bytes skipped
		buf.position( buf.position() - sbuf.position() );
		return null;
	}

	/**
	 * @param buf
	 * @return String
	 */
	public static String readStringLine( final ByteBuffer buf ) {
		ByteBuffer b = readLine( buf );
		return b == null ? null : Charsetfunctions.stringAscii( b.array(), 0, b.limit() );
	}

	/**
	 * @param buf
	 * @param role
	 * @return HandshakeBuilder
	 * @throws InvalidHandshakeException
	 * @throws IncompleteHandshakeException
	 */
	public static HandshakeBuilder translateHandshakeHttp( final ByteBuffer buf, final Role role ) throws InvalidHandshakeException , IncompleteHandshakeException {
		HandshakeBuilder handshake;

		String line = readStringLine( buf );
		if( line == null )
			throw new IncompleteHandshakeException( buf.capacity() + 128 );

		String[] firstLineTokens = line.split( " ", 3 );// eg. HTTP/1.1 101 Switching the Protocols
		if( firstLineTokens.length != 3 ) {
			throw new InvalidHandshakeException();
		}

		if( role == Role.CLIENT ) {
		    // translating/parsing the response from the SERVER
			handshake = new HandshakeImpl1Server();
			ServerHandshakeBuilder serverhandshake = (ServerHandshakeBuilder) handshake;
			serverhandshake.setHttpStatus( Short.parseShort( firstLineTokens[ 1 ] ) );
			serverhandshake.setHttpStatusMessage( firstLineTokens[ 2 ] );
		} else {
		    // translating/parsing the request from the CLIENT
			ClientHandshakeBuilder clienthandshake = new HandshakeImpl1Client();
			clienthandshake.setResourceDescriptor( firstLineTokens[ 1 ] );
			handshake = clienthandshake;
		}

		line = readStringLine( buf );
		while ( line != null && line.length() > 0 ) {
			String[] pair = line.split( ":", 2 );
			if( pair.length != 2 )
				throw new InvalidHandshakeException( "not an http header" );
			handshake.put( pair[ 0 ], pair[ 1 ].replaceFirst( "^ +", "" ) );
			line = readStringLine( buf );
		}
		if( line == null )
			throw new IncompleteHandshakeException();
		return handshake;
	}

	/**
	 * @param request
	 * @param response
	 * @return HandshakeState
	 * @throws InvalidHandshakeException
	 */
	public abstract HandshakeState acceptHandshakeAsClient( ClientHandshake request, ServerHandshake response ) throws InvalidHandshakeException;

	/**
	 * @param handshakedata
	 * @return HandshakeState
	 * @throws InvalidHandshakeException
	 */
	public abstract HandshakeState acceptHandshakeAsServer( ClientHandshake handshakedata ) throws InvalidHandshakeException;

	protected boolean basicAccept( Handshakedata handshakedata ) {
		return handshakedata.getFieldValue( "Upgrade" ).equalsIgnoreCase( "websocket" ) && handshakedata.getFieldValue( "Connection" ).toLowerCase( Locale.ENGLISH ).contains( "upgrade" );
	}

	/**
	 * @param framedata
	 * @return ByteBuffer
	 */
	public abstract ByteBuffer createBinaryFrame( Framedata framedata ); // TODO Allow to send data on the base of an Iterator or InputStream

	/**
	 * @param binary
	 * @param mask
	 * @return List<Framedata>
	 */
	public abstract List<Framedata> createFrames( ByteBuffer binary, boolean mask );

	/**
	 * @param text
	 * @param mask
	 * @return List<Framedata>
	 */
	public abstract List<Framedata> createFrames( String text, boolean mask );

	/**
	 * 
	 */
	public abstract void reset();

	/**
	 * @param handshakedata
	 * @param ownrole
	 * @return List<ByteBuffer>
	 */
	public List<ByteBuffer> createHandshake( final Handshakedata handshakedata, final Role ownrole ) {
		return createHandshake( handshakedata, ownrole, true );
	}

	/**
	 * @param handshakedata
	 * @param ownrole
	 * @param withcontent
	 * @return List<ByteBuffer>
	 */
	public List<ByteBuffer> createHandshake( final Handshakedata handshakedata, final Role ownrole, final boolean withcontent ) {
		StringBuilder bui = new StringBuilder( 100 );
		if( handshakedata instanceof ClientHandshake ) {
			bui.append( "GET " );
			bui.append( ( (ClientHandshake) handshakedata ).getResourceDescriptor() );
			bui.append( " HTTP/1.1" );
		} else if( handshakedata instanceof ServerHandshake ) {
			bui.append( "HTTP/1.1 101 " + ( (ServerHandshake) handshakedata ).getHttpStatusMessage() );
		} else {
			throw new RuntimeException( "unknow role" );
		}
		bui.append( "\r\n" );
		Iterator<String> it = handshakedata.iterateHttpFields();
		while ( it.hasNext() ) {
			String fieldname = it.next();
			String fieldvalue = handshakedata.getFieldValue( fieldname );
			bui.append( fieldname );
			bui.append( ": " );
			bui.append( fieldvalue );
			bui.append( "\r\n" );
		}
		bui.append( "\r\n" );
		byte[] httpheader = Charsetfunctions.asciiBytes( bui.toString() );

		byte[] content = withcontent ? handshakedata.getContent() : null;
		ByteBuffer bytebuffer = ByteBuffer.allocate( ( content == null ? 0 : content.length ) + httpheader.length );
		bytebuffer.put( httpheader );
		if( content != null )
			bytebuffer.put( content );
		bytebuffer.flip();
		return Collections.singletonList( bytebuffer );
	}

	/**
	 * @param request
	 * @return ClientHandshakeBuilder
	 * @throws InvalidHandshakeException
	 */
	public abstract ClientHandshakeBuilder postProcessHandshakeRequestAsClient( ClientHandshakeBuilder request ) throws InvalidHandshakeException;

	/**
	 * @param request
	 * @param response
	 * @return HandshakeBuilder
	 * @throws InvalidHandshakeException
	 */
	public abstract HandshakeBuilder postProcessHandshakeResponseAsServer( ClientHandshake request, ServerHandshakeBuilder response ) throws InvalidHandshakeException;

	/**
	 * @param buffer
	 * @return a Framedata List
	 * @throws InvalidDataException
	 */
	public abstract List<Framedata> translateFrame( ByteBuffer buffer ) throws InvalidDataException;

	/**
	 * @return CloseHandshakeType
	 */
	public abstract CloseHandshakeType getCloseHandshakeType();

	/**
	 * @param buf
	 * @return Handshakedata
	 * @throws InvalidHandshakeException
	 */
	public Handshakedata translateHandshake( final ByteBuffer buf ) throws InvalidHandshakeException {
		return translateHandshakeHttp( buf, role );
	}

	/**
	 * @param bytecount
	 * @return int
	 * @throws LimitExedeedException
	 * @throws InvalidDataException
	 */
	public int checkAlloc( final int bytecount ) throws LimitExedeedException , InvalidDataException {
		if( bytecount < 0 )
			throw new InvalidDataException( CloseFrame.PROTOCOL_ERROR, "Negative count" );
		return bytecount;
	}

	/**
	 * @param role
	 */
	public void setParseMode( final Role role ) {
		this.role = role;
	}
}