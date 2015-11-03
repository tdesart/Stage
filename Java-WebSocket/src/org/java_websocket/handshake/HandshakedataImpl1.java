package org.java_websocket.handshake;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public class HandshakedataImpl1 implements HandshakeBuilder {
	private byte[] content;
	private final LinkedHashMap<String,String> map;

	/**
	 * 
	 */
	public HandshakedataImpl1() {
		map = new LinkedHashMap<String,String>();
	}

	/*public HandshakedataImpl1( Handshakedata h ) {
		httpstatusmessage = h.getHttpStatusMessage();
		resourcedescriptor = h.getResourceDescriptor();
		content = h.getContent();
		map = new LinkedHashMap<String,String>();
		Iterator<String> it = h.iterateHttpFields();
		while ( it.hasNext() ) {
			String key = (String) it.next();
			map.put( key, h.getFieldValue( key ) );
		}
	}*/

	@Override
	public Iterator<String> iterateHttpFields() {
		return Collections.unmodifiableSet( map.keySet() ).iterator();// Safety first
	}

	@Override
	public String getFieldValue( final String name ) {
		final String s = map.get( name.toLowerCase( Locale.ENGLISH ) );
		if( s == null ) {
			return "";
		}
		return s;
	}

	@Override
	public byte[] getContent() {
		return content;
	}

	@Override
	public void setContent( final byte[] content ) {
		this.content = content;
	}

	@Override
	public void put( final String name, final String value ) {
		map.put( name.toLowerCase( Locale.ENGLISH ), value );
	}

	@Override
	public boolean hasFieldValue( final String name ) {
		return map.containsKey( name.toLowerCase( Locale.ENGLISH ) );
	}
}