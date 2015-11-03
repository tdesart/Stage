package org.java_websocket.drafts;

import org.java_websocket.exceptions.InvalidHandshakeException;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ClientHandshakeBuilder;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public class Draft_17 extends Draft_10 {
	@Override
	public HandshakeState acceptHandshakeAsServer( final ClientHandshake handshakedata ) throws InvalidHandshakeException {
		final int v = readVersion( handshakedata );
		if( v == 13 )
			return HandshakeState.MATCHED;
		return HandshakeState.NOT_MATCHED;
	}

	@Override
	public ClientHandshakeBuilder postProcessHandshakeRequestAsClient( final ClientHandshakeBuilder request ) {
		super.postProcessHandshakeRequestAsClient( request );
		request.put( "Sec-WebSocket-Version", "13" );// overwriting the previous
		return request;
	}
}