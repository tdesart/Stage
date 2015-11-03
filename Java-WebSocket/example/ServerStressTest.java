import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.java_websocket.client.WebSocketClient;

/**
 * Original version from TooTallNate
 * @author TooTallNate - Original
 */
public class ServerStressTest extends JFrame {
	private static final long serialVersionUID = 5602697971427422582L;
	private final JSlider clients;
	private final JSlider interval;
	private final JSlider joinrate;
	private final JButton start, stop, reset;
	private final JLabel joinratelabel = new JLabel();
	private final JLabel clientslabel = new JLabel();
	private final JLabel intervallabel = new JLabel();
	private final JTextField uriinput = new JTextField( "ws://localhost:8887" );
	private final JTextArea text = new JTextArea( "payload" );
	private Timer timer = new Timer( true );
	private Thread adjustthread;

	private int notyetconnected = 0;

	/**
	 * 
	 */
	public ServerStressTest() {
		setTitle( "ServerStressTest" );
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		start = new JButton( "Start" );
		start.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed( final ActionEvent e ) {
				start.setEnabled( false );
				stop.setEnabled( true );
				reset.setEnabled( false );
				interval.setEnabled( false );
				clients.setEnabled( false );

				stopAdjust();
				adjustthread = new Thread( new Runnable() {
					@Override
					public void run() {
						try {
							adjust();
						} catch ( final InterruptedException e ) {
							System.out.println( "adjust chanced" );
						}
					}
				} );
				adjustthread.start();

			}
		} );
		stop = new JButton( "Stop" );
		stop.setEnabled( false );
		stop.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed( final ActionEvent e ) {
				timer.cancel();
				stopAdjust();
				start.setEnabled( true );
				stop.setEnabled( false );
				reset.setEnabled( true );
				joinrate.setEnabled( true );
				interval.setEnabled( true );
				clients.setEnabled( true );
			}
		} );
		reset = new JButton( "reset" );
		reset.setEnabled( true );
		reset.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed( final ActionEvent e ) {
				while ( !websockets.isEmpty() )
					websockets.remove( 0 ).close();

			}
		} );
		joinrate = new JSlider( 0, 5000 );
		joinrate.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged( final ChangeEvent e ) {
				joinratelabel.setText( "Joinrate: " + joinrate.getValue() + " ms " );
			}
		} );
		clients = new JSlider( 0, 10000 );
		clients.addChangeListener( new ChangeListener() {

			@Override
			public void stateChanged( final ChangeEvent e ) {
				clientslabel.setText( "Clients: " + clients.getValue() );

			}
		} );
		interval = new JSlider( 0, 5000 );
		interval.addChangeListener( new ChangeListener() {

			@Override
			public void stateChanged( final ChangeEvent e ) {
				intervallabel.setText( "Interval: " + interval.getValue() + " ms " );

			}
		} );

		setSize( 300, 400 );
		setLayout( new GridLayout( 10, 1, 10, 10 ) );
		add( new JLabel( "URI" ) );
		add( uriinput );
		add( joinratelabel );
		add( joinrate );
		add( clientslabel );
		add( clients );
		add( intervallabel );
		add( interval );
		final JPanel south = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
		add( text );
		add( south );

		south.add( start );
		south.add( stop );
		south.add( reset );

		joinrate.setValue( 200 );
		interval.setValue( 1000 );
		clients.setValue( 1 );

	}

	List<WebSocketClient> websockets = Collections.synchronizedList( new LinkedList<WebSocketClient>() );
	URI uri;
	
	/**
	 * @throws InterruptedException
	 */
	public void adjust() throws InterruptedException {
		System.out.println( "Adjust" );
		try {
			uri = new URI( uriinput.getText() );
		} catch ( final URISyntaxException e ) {
			e.printStackTrace();
		}
		final int totalclients = clients.getValue();
		while ( websockets.size() < totalclients ) {
			final WebSocketClient cl = new ExampleClient( uri ) {
				@Override
				public void onClose( final int code, final String reason, final boolean remote ) {
					System.out.println( "Closed duo " + code + " " + reason );
					clients.setValue( websockets.size() );
					websockets.remove( this );
				}
			};

			cl.connect();
			clients.setValue( websockets.size() );
			websockets.add( cl );
			Thread.sleep( joinrate.getValue() );
		}
		while ( websockets.size() > clients.getValue() ) {
			websockets.remove( 0 ).close();
		}
		timer = new Timer( true );
		timer.scheduleAtFixedRate( new TimerTask() {
			@Override
			public void run() {
				send();
			}
		}, 0, interval.getValue() );

	}

	/**
	 * 
	 */
	public void stopAdjust() {
		if( adjustthread != null ) {
			adjustthread.interrupt();
			try {
				adjustthread.join();
			} catch ( final InterruptedException e ) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 */
	public void send() {
		notyetconnected = 0;
		final String payload = text.getText();
		final long time1 = System.currentTimeMillis();
		synchronized ( websockets ) {
			for( final WebSocketClient cl : websockets ) {
				try {
					cl.send( payload );
				} catch ( final NotYetConnectedException e ) {
					notyetconnected++;
				}
			}
		}
		System.out.println( websockets.size() + "/" + notyetconnected + " clients sent \"" + payload + "\"" + ( System.currentTimeMillis() - time1 ) );
	}
	
	/**
	 * @param args
	 */
	public static void main( final String[] args ) {
		new ServerStressTest().setVisible( true );
	}

}
