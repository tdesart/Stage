package org.java_websocket.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.java_websocket.SocketChannelIOHelper;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.WebSocketFactory;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.WrappedByteChannel;
import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.Handshakedata;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * <tt>WebSocketServer</tt> is an abstract class that only takes care of the
 * HTTP handshake portion of WebSockets. <br>
 * It's up to a subclass to add functionality/purpose to the server.<br><br>
 * Original version from TooTallNate
 * 
 * @author TooTallNate - Original
 * 
 */
public abstract class WebSocketServer extends WebSocketAdapter implements Runnable {

	/**
	 * 
	 */
	public static final int DECODERS = Runtime.getRuntime().availableProcessors();

	/**
	 * Holds the list of active WebSocket connections. <br>
	 * "Active" means WebSocket handshake is complete and socket can be written to, or read from.
	 */
	private final Set<WebSocket> connections = new HashSet<WebSocket>();

	/**
	 * The port number that this WebSocket server should listen on.<br>
	 * Default is WebSocket.DEFAULT_PORT.
	 */
	private InetSocketAddress address;

	/**
	 * The socket channel for this WebSocket server.
	 */
	private ServerSocketChannel server;

	/**
	 * The 'Selector' used to get event keys from the underlying socket.
	 */
	private Selector selector;

	/**
	 * The Draft of the WebSocket protocol the Server is adhering to.
	 */
	private List<Draft> drafts;

	private Thread selectorthread;

	private List<WebSocketWorker> decoders;

	private BlockingQueue<WebSocketImpl> oqueue;
	private List<WebSocketImpl> iqueue;
	private BlockingQueue<ByteBuffer> buffers;
	private int queueinvokes = 0;
	private AtomicInteger queuesize = new AtomicInteger( 0 );

	private WebSocketServerFactory wsf = new WebSocketServerFactory() {

		@Override
		public WebSocketImpl createWebSocket( WebSocketAdapter a, Draft d, Socket s ) {
			return new WebSocketImpl( a, d, s );
		}

		@Override
		public WebSocketImpl createWebSocket( WebSocketAdapter a, List<Draft> d, Socket s ) {
			return new WebSocketImpl( a, d, s );
		}

		@Override
		public SocketChannel wrapChannel( SelectionKey c ) {
			return (SocketChannel) c.channel();
		}
	};

	// CONSTRUCTORS ////////////////////////////////////////////////////////////
	/**
	 * Nullary constructor. Creates a WebSocketServer that will attempt to
	 * listen on port <var>WebSocket.DEFAULT_PORT</var>.
	 * @throws UnknownHostException 
	 */
	public WebSocketServer() throws UnknownHostException {
		this( new InetSocketAddress( WebSocket.DEFAULT_PORT ), DECODERS, null );
	}

	/**
	 * Creates a WebSocketServer that will attempt to bind/listen on the given <var>address</var>.
	 * 
	 * @param address
	 *            The address (host:port) this server should listen on.
	 */
	public WebSocketServer( InetSocketAddress address ) {
		this( address, DECODERS, null );
	}

	/**
	 * @param address
	 * @param decoders
	 */
	public WebSocketServer( final InetSocketAddress address , final int decoders ) {
		this( address, decoders, null );
	}

	/**
	 * Creates a WebSocketServer that will attempt to bind/listen on the given <var>address</var>,
	 * and comply with <tt>Draft</tt> version <var>draft</var>.
	 * 
	 * @param address
	 *            The address (host:port) this server should listen on.
	 * @param drafts 
	 */
	public WebSocketServer( InetSocketAddress address , final List<Draft> drafts ) {
		this( address, DECODERS, drafts );
	}

	/**
	 * @param address
	 * @param decodercount
	 * @param drafts
	 */
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "SC_START_IN_CTOR", justification = "All extensions of this abstract class call only this SUPER class with the appropriate parameters")
	public WebSocketServer( final InetSocketAddress address , final int decodercount , final List<Draft> drafts ) {
		if( drafts == null )
			this.drafts = Collections.emptyList();
		else
			this.drafts = drafts;

		setAddress( address );

		oqueue = new LinkedBlockingQueue<WebSocketImpl>();
		iqueue = new LinkedList<WebSocketImpl>();

		decoders = new ArrayList<WebSocketWorker>( decodercount );
		buffers = new LinkedBlockingQueue<ByteBuffer>();
Global.wssLogger.config("Web Socket Server set up in address: " + address +", decodercount(threads) = " + decodercount +", drafts="+drafts);
		for( int i = 0 ; i < decodercount ; i++ ) {
			WebSocketWorker ex = new WebSocketWorker();
			decoders.add( ex );
			ex.start();
		}
	}

	/**
	 * Starts the server selectorthread that binds to the currently set port number and
	 * listeners for WebSocket connection requests. Creates a fixed thread pool with the size {@link WebSocketServer#DECODERS}<br>
	 * May only be called once.
	 * 
	 * Alternatively you can call {@link WebSocketServer#run()} directly.
	 * 
	 * @throws IllegalStateException
	 */
	public void start() {
		if( selectorthread != null )
			throw new IllegalStateException( "Already started" );
		new Thread( this ).start();
	}

	/**
	 * Closes all connected clients sockets, then closes the underlying
	 * ServerSocketChannel, effectively killing the server socket selectorthread and
	 * freeing the port the server was bound to.
	 * 
	 * @throws IOException
	 *             When socket related I/O errors occur.
	 */
	public void stop() throws IOException {
		synchronized ( connections ) {
			for( WebSocket ws : connections ) {
				ws.close( CloseFrame.NORMAL );
			}
		}

		selectorthread.interrupt();
		this.server.close();
	}

    @Override
	protected void finalize() throws Throwable {
        // Called by the garbage collector
		Global.wssLogger.info("Finalizing WebSocketServer");
        try {
        	stop();
        	super.finalize();
        } catch (Exception ignore) {
        }
    }

	/**
	 * Returns a WebSocket[] of currently connected clients.
	 * Its iterators will be failfast and its not judicious
	 * to modify it.
	 * 
	 * @return The currently connected clients.
	 */
	public Set<WebSocket> connections() {
		return this.connections;
	}

	/**
	 * Sets the address (host:port) that this WebSocketServer should listen on.
	 * 
	 * @param address
	 *            The address (host:port) to listen on.
	 */
	public void setAddress( InetSocketAddress address ) {
		this.address = address;
	}

	/**
	 * @return InetSocketAddress
	 */
	public InetSocketAddress getAddress() {
		return this.address;
	}

	/**
	 * Gets the port number that this server listens on.
	 * 
	 * @return The port number.
	 */
	public int getPort() {
		int port = getAddress().getPort();
		if( port == 0 && server != null ) {
			port = server.socket().getLocalPort();
		}
		return port;
	}

	/**
	 * @return List<Draft>
	 */
	public List<Draft> getDraft() {
		return Collections.unmodifiableList( drafts );
	}

	/**
	 * Runnable IMPLEMENTATION 
	 */
	@Override
	public void run() {
		if( selectorthread != null )
			throw new IllegalStateException( "This instance of " + getClass().getSimpleName() + " can only be started once!" );
		selectorthread = Thread.currentThread();
		selectorthread.setName( "WebsocketSelector" + selectorthread.getId() );
		try {
			server = ServerSocketChannel.open();
			server.configureBlocking( false );
			final ServerSocket socket = server.socket();
			socket.setReceiveBufferSize( WebSocket.RCVBUF );
			socket.bind( address );
			selector = Selector.open();
			server.register( selector, server.validOps() );
		} catch ( final IOException ex ) {
			try {
				stop();
				handleIOException( null, ex );
			} catch (final IOException e) {
				e.printStackTrace();
			}
			return;
		}
		try {
			while ( !selectorthread.isInterrupted() ) {
				SelectionKey key = null;
				WebSocketImpl conn = null;
				try {
					selector.select();
					registerWrite();

					final Set<SelectionKey> keys = selector.selectedKeys();
					final Iterator<SelectionKey> i = keys.iterator();

					while ( i.hasNext() ) {
						key = i.next();

						if( !key.isValid() ) {
							// Object o = key.attachment();
							continue;
						}

						if( key.isAcceptable() ) {
							final SocketChannel channel = server.accept();
							channel.configureBlocking( false );
							final WebSocketImpl w = wsf.createWebSocket( this, drafts, channel.socket() );
							w.key = channel.register( selector, SelectionKey.OP_READ, w );
							w.channel = wsf.wrapChannel( w.key );
							i.remove();
//							allocateBuffers( w );
							allocateBuffers( );
							continue;
						}

						if( key.isReadable() ) {
							conn = (WebSocketImpl) key.attachment();
							final ByteBuffer buf = takeBuffer();
							try {
								if( SocketChannelIOHelper.read( buf, conn, conn.channel ) ) {
									conn.inQueue.put( buf );
									queue( conn );
									i.remove();
									if( conn.channel instanceof WrappedByteChannel ) {
										if( ( (WrappedByteChannel) conn.channel ).isNeedRead() ) {
											iqueue.add( conn );
										}
									}
								} else {
									pushBuffer( buf );
								}
							} catch ( final IOException e ) {
								pushBuffer( buf );
								throw e;
							} catch ( final RuntimeException e ) {
								pushBuffer( buf );
								throw e;
							}
						}
						if( key.isWritable() ) {
							conn = (WebSocketImpl) key.attachment();
							if( SocketChannelIOHelper.batch( conn, conn.channel ) ) {
								if( key.isValid() )
									key.interestOps( SelectionKey.OP_READ );
							}
						}
					}
					while ( !iqueue.isEmpty() ) {
						conn = iqueue.remove( 0 );
						final WrappedByteChannel c = ( (WrappedByteChannel) conn.channel );
						final ByteBuffer buf = takeBuffer();
						if(SocketChannelIOHelper.readMore( buf, conn, c ))
							iqueue.add( conn );
						conn.inQueue.put( buf );
						queue( conn );
					}
				} catch ( final CancelledKeyException e ) {
					// an other thread may cancel the key
				} catch ( final IOException ex ) {
					if( key != null )
						key.cancel();
					handleIOException( conn, ex );
				} catch ( final InterruptedException e ) {
					// FIXME controlled shutdown
					// TODO test to see if this fixes the controlled shutdown
					try {
						stop();
					} catch (IOException e1) {
						handleIOException(conn, e1);
						e1.printStackTrace();
					}
					handleFatal(conn, null);
					return;
				}
			}
		} catch ( final RuntimeException e ) {
			// should hopefully never occur
			handleFatal( null, e );
		}
	}

	protected void allocateBuffers( /*WebSocket c*/ ) throws InterruptedException {
		if( queuesize.get() >= 2 * decoders.size() + 1 ) {
			return;
		}
		queuesize.incrementAndGet();
		buffers.put( createBuffer() );
	}

	protected void releaseBuffers( /*WebSocket c*/ ) {
		// queuesize.decrementAndGet();
		// takeBuffer();
	}

	/**
	 * @return ByteBuffer
	 */
	public ByteBuffer createBuffer() {
		return ByteBuffer.allocate( WebSocket.RCVBUF );
	}

	private void queue( WebSocketImpl ws ) throws InterruptedException {
		if( ws.workerThread == null ) {
			ws.workerThread = decoders.get( queueinvokes % decoders.size() );
			queueinvokes++;
		}
		ws.workerThread.put( ws );
	}

	private ByteBuffer takeBuffer() throws InterruptedException {
		return buffers.take();
	}

	private void pushBuffer( ByteBuffer buf ) throws InterruptedException {
		if( buffers.size() > queuesize.intValue() )
			return;
		buffers.put( buf );
	}

	private void registerWrite() throws CancelledKeyException {
		int size = oqueue.size();
		for( int i = 0 ; i < size ; i++ ) {
			WebSocketImpl conn = oqueue.remove();
			conn.key.interestOps( SelectionKey.OP_READ | SelectionKey.OP_WRITE );
		}
	}

	private void handleIOException( WebSocket conn, IOException ex ) {
		onWebsocketError( conn, ex );// conn may be null here
		if( conn != null ) {
			conn.close( CloseFrame.ABNORMAL_CLOSE );
		}
	}

	private void handleFatal( WebSocket conn, RuntimeException e ) {
		onError( conn, e );
		try {
			selector.close();
		} catch ( IOException e1 ) {
			onError( null, e1 );
		}
		for( WebSocketWorker w : decoders ) {
			w.interrupt();
		}
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
	protected String getFlashSecurityPolicy() {
		return "<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"" + getPort() + "\" /></cross-domain-policy>";
	}

	@Override
	public final void onWebsocketMessage( WebSocket conn, String message ) {
		onMessage( conn, message );
	}

	@Override
	public final void onWebsocketMessage( WebSocket conn, ByteBuffer blob ) {
		onMessage( conn, blob );
	}

	@Override
	public final void onWebsocketOpen( WebSocket conn, Handshakedata handshake ) {
		synchronized ( connections ) {
			if( this.connections.add( conn ) ) {
				onOpen( conn, (ClientHandshake) handshake );
			}
		}
	}

	@Override
	public final void onWebsocketClose( WebSocket conn, int code, String reason, boolean remote ) {
		oqueue.add( (WebSocketImpl) conn );// because the ostream will close the channel
		selector.wakeup();
		try {
			synchronized ( connections ) {
				if( this.connections.remove( conn ) ) {
					onClose( conn, code, reason, remote );
				}
			}
		} finally {
//			releaseBuffers( conn );
			releaseBuffers();
		}
	}

	/**
	 * @param conn
	 *            may be null if the error does not belong to a single connection
	 */
	@Override
	public final void onWebsocketError( WebSocket conn, Exception ex ) {
		onError( conn, ex );
	}

	@Override
	public final void onWriteDemand( WebSocket w ) {
		WebSocketImpl conn = (WebSocketImpl) w;
		oqueue.add( conn );
		selector.wakeup();
	}

	/**
	 * @param wsf
	 */
	public final void setWebSocketFactory( final WebSocketServerFactory wsf ) {
		this.wsf = wsf;
	}

	/**
	 * @return WebSocketFactory
	 */
	public final WebSocketFactory getWebSocketFactory() {
		return wsf;
	}

	// ABTRACT METHODS /////////////////////////////////////////////////////////
	/**
	 * @param conn
	 * @param handshake
	 */
	public abstract void onOpen( WebSocket conn, ClientHandshake handshake );

	/**
	 * @param conn
	 * @param code
	 * @param reason
	 * @param remote
	 */
	public abstract void onClose( WebSocket conn, int code, String reason, boolean remote );

	/**
	 * @param conn
	 * @param message
	 */
	public abstract void onMessage( WebSocket conn, String message );

	/**
	 * @param conn
	 * @param ex
	 */
	public abstract void onError( WebSocket conn, Exception ex );

	/**
	 * @param conn
	 * @param message
	 */
	public void onMessage( final WebSocket conn, final ByteBuffer message ) {
	}

	/**
	 * @author TooTallNate - Original
	 */
	public class WebSocketWorker extends Thread {

		private final BlockingQueue<WebSocketImpl> iqueue;

		/**
		 * 
		 */
		public WebSocketWorker() {
			iqueue = new LinkedBlockingQueue<WebSocketImpl>();
			setName( "WebSocketWorker-" + getId() );
			setUncaughtExceptionHandler( new UncaughtExceptionHandler() {
				@Override
				public void uncaughtException( final Thread t, final Throwable e ) {
					getDefaultUncaughtExceptionHandler().uncaughtException( t, e );
				}
			} );
		}

		/**
		 * @param ws
		 * @throws InterruptedException
		 */
		public void put( final WebSocketImpl ws ) throws InterruptedException {
			iqueue.put( ws );
		}

		@Override
		public void run() {
			WebSocketImpl ws = null;
			try {
				while ( true ) {
					ByteBuffer buf = null;
					ws = iqueue.take();
					buf = ws.inQueue.poll();
					assert ( buf != null );
					try {
						ws.decode( buf );
						// ws.flush();
					} catch ( final IOException e ) {
						handleIOException( ws, e );
					} finally {
						pushBuffer( buf );
					}
				}
			} catch ( final RuntimeException e ) {
				handleFatal( ws, e );
			} catch ( final InterruptedException e ) {
			} catch ( final Throwable e ) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @author TooTallNate - Original
	 */
	public interface WebSocketServerFactory extends WebSocketFactory {
		@Override
		public WebSocketImpl createWebSocket( WebSocketAdapter a, Draft d, Socket s );

		@Override
		public WebSocketImpl createWebSocket( WebSocketAdapter a, List<Draft> drafts, Socket s );

		/**
		 * Allows to wrap the Socketchannel( key.channel() ) to insert a protocol layer( like ssl or proxy authentication) beyond the ws layer.
		 * 
		 * @param key
		 *            a SelectionKey of an open SocketChannel.
		 * @return The channel on which the read and write operations will be performed.<br>
		 * @throws IOException 
		 */
		public ByteChannel wrapChannel( SelectionKey key ) throws IOException;
	}
}