package org.java_websocket;

import java.net.Socket;
import java.util.List;

import org.java_websocket.drafts.Draft;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public interface WebSocketFactory {
	
	/**
	 * @param a
	 * @param d
	 * @param s
	 * @return WebSocket
	 */
	public WebSocket createWebSocket( WebSocketAdapter a, Draft d, Socket s );
	
	/**
	 * @param a
	 * @param drafts
	 * @param s
	 * @return WebSocket
	 */
	public WebSocket createWebSocket( WebSocketAdapter a, List<Draft> drafts, Socket s );
}