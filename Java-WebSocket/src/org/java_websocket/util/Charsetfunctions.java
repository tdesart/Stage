package org.java_websocket.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.CloseFrame;

/**
 * Original version from TooTallNate, changed to meet our needs, by Sandro Reis
 * @author TooTallNate - Original
 *
 */
public class Charsetfunctions {

	/**
	 * 
	 */
	public static final CodingErrorAction codingErrorAction = CodingErrorAction.REPORT;

	/**
	 * @param s
	 * @return UTF-8 encoding in bytes
	 */
	public static byte[] utf8Bytes( final String s ) {
		try {
			return s.getBytes( "UTF8" );
		} catch ( final UnsupportedEncodingException e ) {
			throw new RuntimeException( e );
		}
	}

	/**
	 * @param s
	 * @return ASCII encoding in bytes
	 */
	public static byte[] asciiBytes( final String s ) {
		try {
			return s.getBytes( "ASCII" );
		} catch ( final UnsupportedEncodingException e ) {
			throw new RuntimeException( e );
		}
	}

	/**
	 * @param bytes
	 * @return a string
	 */
	public static String stringAscii( final byte[] bytes ) {
		return stringAscii( bytes, 0, bytes.length );
	}
	
	/**
	 * @param bytes
	 * @param offset
	 * @param length
	 * @return a string
	 */
	public static String stringAscii( final byte[] bytes, final int offset, final int length ){
		try {
			return new String( bytes, offset, length, "ASCII" );
		} catch ( final UnsupportedEncodingException e ) {
			throw new RuntimeException( e );
		}
	}

	/**
	 * @param bytes
	 * @return a string
	 * @throws InvalidDataException
	 */
	public static String stringUtf8( final byte[] bytes ) throws InvalidDataException {
		return stringUtf8( ByteBuffer.wrap( bytes ) );
	}

	/*public static String stringUtf8( byte[] bytes, int off, int length ) throws InvalidDataException {
		CharsetDecoder decode = Charset.forName( "UTF8" ).newDecoder();
		decode.onMalformedInput( codingErrorAction );
		decode.onUnmappableCharacter( codingErrorAction );
		//decode.replaceWith( "X" );
		String s;
		try {
			s = decode.decode( ByteBuffer.wrap( bytes, off, length ) ).toString();
		} catch ( CharacterCodingException e ) {
			throw new InvalidDataException( CloseFrame.NO_UTF8, e );
		}
		return s;
	}*/

	/**
	 * @param bytes
	 * @return a string
	 * @throws InvalidDataException
	 */
	public static String stringUtf8( final ByteBuffer bytes ) throws InvalidDataException {
		final CharsetDecoder decode = Charset.forName( "UTF8" ).newDecoder();
		decode.onMalformedInput( codingErrorAction );
		decode.onUnmappableCharacter( codingErrorAction );
		// decode.replaceWith( "X" );
		String s;
		try {
			bytes.mark();
			s = decode.decode( bytes ).toString();
			bytes.reset();
		} catch ( final CharacterCodingException e ) {
			throw new InvalidDataException( CloseFrame.NO_UTF8, e );
		}
		return s;
	}

	/**
	 * @param args
	 * @throws InvalidDataException
	 */
	public static void main( final String[] args ) throws InvalidDataException {
		stringUtf8( utf8Bytes( "\0" ) );
		stringAscii( asciiBytes( "\0" ) );
	}
}