package org.java_websocket.client;
import java.io.IOException;
import java.net.Socket;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import org.java_websocket.SSLSocketChannel2;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient.WebSocketClientFactory;
import org.java_websocket.drafts.Draft;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public class DefaultSSLWebSocketClientFactory implements WebSocketClientFactory {
	protected SSLContext sslcontext;
	protected ExecutorService exec;

	/**
	 * @param sslContext
	 */
	public DefaultSSLWebSocketClientFactory( final SSLContext sslContext ) {
		this( sslContext, Executors.newSingleThreadScheduledExecutor() );
	}

	/**
	 * @param sslContext
	 * @param exec
	 */
	public DefaultSSLWebSocketClientFactory( final SSLContext sslContext , final ExecutorService exec ) {
		if( sslContext == null || exec == null )
			throw new IllegalArgumentException();
		this.sslcontext = sslContext;
		this.exec = exec;
	}

	@Override
	public ByteChannel wrapChannel( final SelectionKey c, final String host, final int port ) throws IOException {
		final SSLEngine e = sslcontext.createSSLEngine( host, port );
		e.setUseClientMode( true );
		return new SSLSocketChannel2( c, e, exec );
	}

	@Override
	public WebSocketImpl createWebSocket( final WebSocketAdapter a, final Draft d, final Socket c ) {
		return new WebSocketImpl( a, d, c );
	}

	@Override
	public WebSocketImpl createWebSocket( final WebSocketAdapter a, final List<Draft> d, final Socket s ) {
		return new WebSocketImpl( a, d, s );
	}
}