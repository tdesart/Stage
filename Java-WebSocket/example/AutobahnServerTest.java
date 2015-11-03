import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public class AutobahnServerTest extends WebSocketServer {
	private static int counter = 0;
	
	/**
	 * @param port
	 * @param d
	 * @throws UnknownHostException
	 */
	public AutobahnServerTest( final int port , final Draft d ) throws UnknownHostException {
		super( new InetSocketAddress( port ), Collections.singletonList( d ) );
	}
	
	/**
	 * @param address
	 * @param d
	 */
	public AutobahnServerTest( final InetSocketAddress address, final Draft d ) {
		super( address, Collections.singletonList( d ) );
	}

	@Override
	public void onOpen( final WebSocket conn, final ClientHandshake handshake ) {
		counter++;
		System.out.println( "///////////Opened connection number" + counter );
	}

	@Override
	public void onClose( final WebSocket conn, final int code, final String reason, final boolean remote ) {
		System.out.println( "closed" );
	}

	@Override
	public void onError( final WebSocket conn, final Exception ex ) {
		System.out.println( "Error:" );
		ex.printStackTrace();
	}

	@Override
	public void onMessage( final WebSocket conn, final String message ) {
		conn.send( message );
	}

	@Override
	public void onMessage( final WebSocket conn, final ByteBuffer blob ) {
		try {
			conn.send( blob );
		} catch ( final InterruptedException e ) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 * @throws UnknownHostException
	 */
	public static void main( final String[] args ) throws  UnknownHostException {
		WebSocket.DEBUG = false;
		int port;
		try {
			port = Integer.parseInt(args[0]);
		} catch ( final Exception e ) {
			System.out.println( "No port specified. Defaulting to 9003" );
			port = 9003;
		}
		new AutobahnServerTest( port, new Draft_17() ).start();
	}

}
