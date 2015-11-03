package lu.uni.fstc.proactivity.messaging.client;

import org.json.JSONObject;

import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.messaging.SocketMessage;


/**
 * Abstract representation of any object created on the client side and sent to the server to be treated there with the abstract execute() method<p>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2013
 *
 */
public abstract class SMClient extends SocketMessage {

	protected SMClient(final AbstractPAM2MoodleDbWrapper dbPam, final long id, final JSONObject json) {
		super(dbPam, id, json);
		setRole(Role.CLIENT);
	}

	/**
	 * Treating the client message appropriately, depending on the object type
	 * @return <b>true</b> if the execution returns a Result (in case of SMClientCommand instances)<br>
	 *  	   <b>true</b> a correct (expected) answer was received, and was able to be treated without any errors (in case of SMClientAnswer instances)
	 */ 
	public abstract boolean execute();
}