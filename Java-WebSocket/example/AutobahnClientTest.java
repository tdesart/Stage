import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public class AutobahnClientTest extends WebSocketClient {

	/**
	 * @param d
	 * @param uri
	 */
	public AutobahnClientTest( final Draft d , final URI uri ) {
		super( uri, d );
	}
	/**
	 * @param args
	 */
	public static void main( final String[] args ) {
		System.out.println( "Testutility to profile/test this implementation using the Autobahn suit.\n" );
		System.out.println( "Type 'r <casenumber>' to run a testcase. Example: r 1" );
		System.out.println( "Type 'r <first casenumber> <last casenumber>' to run a testcase. Example: r 1 295" );
		System.out.println( "Type 'u' to update the test results." );
		System.out.println( "Type 'ex' to terminate the program." );
		System.out.println( "During sequences of cases the debugoutput will be turned of." );

		System.out.println( "You can now enter in your commands:" );

		try {
			final BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );

			/*First of the thinks a programmer might want to change*/
			Draft d = new Draft_17();
			final String clientname = "tootallnate/websocket";

			final String protocol = "ws";
			final String host = "localhost";
			final int port = 9001;

			final String serverlocation = protocol + "://" + host + ":" + port;
			String line = "";
			AutobahnClientTest e;
			URI uri = null;
			final String perviousline = "";
			String nextline = null;
			Integer start = null;
			Integer end = null;

			while ( line!= null && !line.contains( "ex" ) ) {
				try {
					if( nextline != null ) {
						line = nextline;
						nextline = null;
						WebSocket.DEBUG = false;
					} else {
						System.out.print( ">" );
						line = sysin.readLine();
						WebSocket.DEBUG = true;
					}
					if (line == null)
						break;
					
					if( line.equals( "l" ) ) {
						line = perviousline;
					}
					final String[] spl = line.split( " " );
					if( line.startsWith( "r" ) ) {
						if( spl.length == 3 ) {
							start = new Integer( spl[ 1 ] );
							end = new Integer( spl[ 2 ] );
						}
						if( start != null && end != null ) {
							if( start.compareTo(end)>0 ) {
								start = null;
								end = null;
							} else {
								nextline = "r " + start;
								start = Integer.valueOf(start.intValue()+1);
								if( spl.length == 3 )
									continue;
							}
						}
						uri = URI.create( serverlocation + "/runCase?case=" + spl[ 1 ] + "&agent=" + clientname );

					} else if( line.startsWith( "u" ) ) {
						WebSocket.DEBUG = false;
						uri = URI.create( serverlocation + "/updateReports?agent=" + clientname );
					} else if( line.startsWith( "d" ) ) {
						try {
							d = (Draft) Class.forName( "Draft_" + spl[ 1 ] ).getConstructor().newInstance();
						} catch ( final Exception ex ) {
							System.out.println( "Could not change draft" + ex );
						}
					}
					if( uri == null ) {
						System.out.println( "Do not understand the input." );
						continue;
					}
					System.out.println( "//////////////////////Exec: " + uri.getQuery() );
					e = new AutobahnClientTest( d, uri );
					final Thread t = new Thread( e );
					t.start();
					try {
						t.join();

					} catch ( final InterruptedException e1 ) {
						e1.printStackTrace();
					} finally {
						e.close();
					}
				} catch ( final ArrayIndexOutOfBoundsException e1 ) {
					System.out.println( "Bad Input r 1, u 1, d 10, ex" );
				} catch ( final IllegalArgumentException e2 ) {
					e2.printStackTrace();
				}

			}
		} catch ( final ArrayIndexOutOfBoundsException e ) {
			System.out.println( "Missing server uri" );
		} catch ( final IllegalArgumentException e ) {
			e.printStackTrace();
			System.out.println( "URI should look like ws://localhost:8887 or wss://echo.websocket.org" );
		} catch ( final IOException e ) {
			e.printStackTrace();
		}
		System.exit( 0 );
	}

	@Override
	public void onMessage( final String message ) {
		send( message );
	}

	@Override
	public void onMessage( final ByteBuffer blob ) {
		try {
			getConnection().send( blob );
		} catch ( final InterruptedException e ) {
			e.printStackTrace();
		}
	}

	@Override
	public void onError( final Exception ex ) {
		System.out.println( "Error: " );
		ex.printStackTrace();
	}

	@Override
	public void onOpen( final ServerHandshake handshake ) {
	}

	@Override
	public void onClose( final int code, final String reason, final boolean remote ) {
		System.out.println( "Closed: " + code + " " + reason );
	}

}
