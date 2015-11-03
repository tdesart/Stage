package org.java_websocket;

import java.nio.ByteBuffer;

import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.Framedata.Opcode;
import org.java_websocket.framing.FramedataImpl1;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.HandshakeImpl1Server;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public abstract class WebSocketAdapter implements WebSocketListener {

	/**
	 * This default implementation does not do anything. Go ahead and overwrite it.
	 * 
	 * @see org.java_websocket.WebSocketListener#onWebsocketHandshakeReceivedAsServer(WebSocket, Draft, ClientHandshake)
	 */
	@Override
	public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer( final WebSocket conn, final Draft draft, final ClientHandshake request ) throws InvalidDataException {
		return new HandshakeImpl1Server();
	}

	/**
	 * This default implementation does not do anything which will cause connections to be accepted. Go ahead and overwrite it.
	 * 
	 * @see org.java_websocket.WebSocketListener#onWebsocketHandshakeReceivedAsClient(WebSocket, ClientHandshake, ServerHandshake)
	 */
	@Override
	public void onWebsocketHandshakeReceivedAsClient( final WebSocket conn, final ClientHandshake request, final ServerHandshake response ) throws InvalidDataException {
	}

	/**
	 * This default implementation does not do anything which will cause the connections to always progress.
	 * 
	 * @see org.java_websocket.WebSocketListener#onWebsocketHandshakeSentAsClient(WebSocket, ClientHandshake)
	 */
	@Override
	public void onWebsocketHandshakeSentAsClient( final WebSocket conn, final ClientHandshake request ) throws InvalidDataException {
	}

	/**
	 * This default implementation does not do anything. Go ahead and overwrite it.
	 * 
	 * @see org.java_websocket.WebSocketListener#onWebsocketMessage(WebSocket, String)
	 */
	@Override
	public void onWebsocketMessage( final WebSocket conn, final String message ) {
	}

	/**
	 * This default implementation does not do anything. Go ahead and overwrite it.
	 * 
	 * @see org.java_websocket.WebSocketListener#onWebsocketOpen(WebSocket, Handshakedata)
	 */
	@Override
	public void onWebsocketOpen( final WebSocket conn, final Handshakedata handshake ) {
	}

	/**
	 * This default implementation does not do anything. Go ahead and overwrite it.
	 * 
	 * @see org.java_websocket.WebSocketListener#onWebsocketClose(WebSocket, int, String, boolean)
	 */
	@Override
	public void onWebsocketClose( final WebSocket conn, final int code, final String reason, final boolean remote ) {
	}

	/**
	 * This default implementation does not do anything. Go ahead and overwrite it.
	 * 
	 * @see org.java_websocket.WebSocketListener#onWebsocketMessage(WebSocket, ByteBuffer)
	 */
	@Override
	public void onWebsocketMessage( final WebSocket conn, final ByteBuffer blob ) {
	}

	/**
	 * This default implementation will send a pong in response to the received ping.
	 * The pong frame will have the same payload as the ping frame.
	 * 
	 * @see org.java_websocket.WebSocketListener#onWebsocketPing(WebSocket, Framedata)
	 */
	@Override
	public void onWebsocketPing( final WebSocket conn, final Framedata f ) {
		final FramedataImpl1 resp = new FramedataImpl1( f );
		resp.setOptcode( Opcode.PONG );
		conn.sendFrame( resp );
	}

	/**
	 * This default implementation does not do anything. Go ahead and overwrite it.
	 * 
	 * @see org.java_websocket.WebSocketListener#onWebsocketPong(WebSocket, Framedata)
	 */
	@Override
	public void onWebsocketPong( final WebSocket conn, final Framedata f ) {
	}

	/**
	 * Gets the XML string that should be returned if a client requests a Flash
	 * security policy.
	 * 
	 * The default implementation allows access from all remote domains, but
	 * only on the port that this WebSocketServer is listening on.
	 * 
	 * This is specifically implemented for gitime's WebSocket client for Flash:
	 * http://github.com/gimite/web-socket-js
	 * 
	 * @return An XML String that comforms to Flash's security policy. You MUST
	 *         not include the null char at the end, it is appended automatically.
	 */
	@Override
	public String getFlashPolicy( final WebSocket conn ) {
		return "<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"" + conn.getLocalSocketAddress().getPort() + "\" /></cross-domain-policy>\0";
	}

	/**
	 * This default implementation does not do anything. Go ahead and overwrite it.
	 * 
	 * @see org.java_websocket.WebSocketListener#onWebsocketError(WebSocket, Exception)
	 */
	@Override
	public void onWebsocketError( final WebSocket conn, final Exception ex ) {
	}
}