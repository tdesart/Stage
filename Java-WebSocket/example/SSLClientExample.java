import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.java_websocket.WebSocket;
import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
class WebSocketChatClient extends WebSocketClient {

	public WebSocketChatClient( URI serverUri ) {
		super( serverUri );
	}

	@Override
	public void onOpen( ServerHandshake handshakedata ) {
		Global.wssLogger.info( "Connected" );
	}

	@Override
	public void onMessage( String message ) {
		Global.wssLogger.info( "got: " + message );
	}

	@Override
	public void onClose( int code, String reason, boolean remote ) {
		Global.wssLogger.info( "Disconnected" );
		// Sandro Reis :this next line generated a warinig in FindBugs, saying that this would kill the entire JVM.
		// Probably not what the programmer intended! I assumed the same, thus replaced it to kill only the current thread
// 		System.exit( 0 );
		Thread.currentThread().interrupt();
	}

	@Override
	public void onError( Exception ex ) {
		ex.printStackTrace();
	}

}

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public class SSLClientExample {

	/**
	 * Keystore with certificate created like so (in JKS format):
	 *
	 * keytool -genkey -validity 3650 -keystore "keystore.jks" -storepass "storepassword" -keypass "keypassword" -alias "default" -dname "CN=127.0.0.1, OU=MyOrgUnit, O=MyOrg, L=MyCity, S=MyRegion, C=MyCountry"
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		WebSocket.DEBUG = true;

		final WebSocketChatClient chatclient = new WebSocketChatClient( new URI( "wss://localhost:8887" ) );
//		final WebSocketChatClient chatclient = new WebSocketChatClient( new URI( "wss://moodle-test.uni.lux:8887" ) );

		// load up the key store
		final String STORETYPE = "JKS";
		final String KEYSTORE = "C:\\Users\\sandro.reis\\workspace\\PAMELA\\keystore.jks";
		final String STOREPASSWORD = "storepassword";
		final String KEYPASSWORD = "keypassword";

		final KeyStore ks = KeyStore.getInstance( STORETYPE );
		final File kf = new File( KEYSTORE );
		ks.load( new FileInputStream( kf ), STOREPASSWORD.toCharArray() );

		final KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
		kmf.init( ks, KEYPASSWORD.toCharArray() );
		final TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
		tmf.init( ks );

		SSLContext sslContext = null;
		sslContext = SSLContext.getInstance( "TLS" );
		sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null );

		chatclient.setWebSocketFactory( new DefaultSSLWebSocketClientFactory( sslContext ) );

		chatclient.connect();
		Global.wssLogger.info("client CONNECTED to SSL server addr: " + chatclient.getURI());

		final BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
		while ( true ) {
			chatclient.send( reader.readLine() );
		}
	}
}