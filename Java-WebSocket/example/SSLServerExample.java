import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.java_websocket.WebSocket;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;


import lu.uni.fstc.proactivity.utils.Global;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public class SSLServerExample {

	/**
	 * Keystore with certificate created like so (in JKS format):
	 *
	 * keytool -genkey -validity 3650 -keystore "keystore.jks" -storepass "storepassword" -keypass "keypassword" -alias "default" -dname "CN=127.0.0.1, OU=MyOrgUnit, O=MyOrg, L=MyCity, S=MyRegion, C=MyCountry"
	 * 
	 * keytool -genkey -keyalg RSA -validity 3650 -keystore "keystore-test.jks" -storepass "store123" -keypass "key123" -alias "moodle-test.uni.lux" -dname "CN = moodle-test.uni.lux, OU = Service Informatique, O = University of Luxembourg, L = Luxembourg, ST = Luxembourg, C = LU"
	 * keytool -certreq -alias "moodle-test.uni.lux" -keystore "keystore-test.jks" -file "moodle-test.csr"
	 * 
	 * keytool -genkey -validity 3650 -keystore "keystorePAM.jks" -storepass "storE123$" -keypass "keY123$" -alias "default" -dname "CN = moodle.fstc.uni.lu, OU = Service Informatique, O = University of Luxembourg, L = Luxembourg, ST = Luxembourg, C = LU"
	 * keytool -alias "default" -import -file C:\Users\sandro.reis\workspace\PAMELA\moodle.fstc.uni.lu.crt -keystore keystorePAM.jks

	 * @param args
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		WebSocket.DEBUG = true;

		final ChatServer chatserver = new ChatServer( 8887 );
//		final ChatServer chatserver = new ChatServer( new java.net.InetSocketAddress( "localhost", 8887) );

		// load up the key store
		final String STORETYPE = "JKS";
/*/
//		final String KEYSTORE = "C:\\Users\\sandro.reis\\workspace\\PAMELA\\keystore.jks";
		final String KEYSTORE = "keystore.jks";
		final String STOREPASSWORD = "storepassword";
		final String KEYPASSWORD = "keypassword";
/**//*/
		final String KEYSTORE = "moodle.jks";
		final String STOREPASSWORD = "storE123$";
		final String KEYPASSWORD = "keY123$";
/**//**/
		final String KEYSTORE = "moodle-test.jks";
		final String STOREPASSWORD = "store123";
		final String KEYPASSWORD = "key123";
/**/
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

		chatserver.setWebSocketFactory( new DefaultSSLWebSocketServerFactory( sslContext ) );
		chatserver.start();

Global.wssLogger.info("SSL server keystore:"+KEYSTORE+", launched at address: " + chatserver.getAddress());
	}
}