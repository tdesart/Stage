import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public class ExampleClient extends WebSocketClient {

	/**
	 * @param serverUri
	 * @param draft
	 */
	public ExampleClient( final URI serverUri , final Draft draft ) {
		super( serverUri, draft );
	}

	/**
	 * @param serverURI
	 */
	public ExampleClient( final URI serverURI ) {
		super( serverURI );
	}

	@Override
	public void onOpen( final ServerHandshake handshakedata ) {
	}

	@Override
	public void onMessage( final String message ) {
	}

	@Override
	public void onClose( final int code, final String reason, final boolean remote ) {
	}

	@Override
	public void onError( final Exception ex ) {
	}

	/**
	 * @param args
	 * @throws URISyntaxException
	 */
	public static void main( final String[] args ) throws URISyntaxException {
		final ExampleClient c = new ExampleClient( new URI( "ws://localhost:8887" ), new Draft_10() );
		c.connect();
	}
}