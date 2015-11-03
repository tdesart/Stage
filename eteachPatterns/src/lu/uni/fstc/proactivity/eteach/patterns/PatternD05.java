package lu.uni.fstc.proactivity.eteach.patterns;

import java.sql.ResultSet;

import lu.uni.fstc.proactivity.eteach.events.Operations;
import lu.uni.fstc.proactivity.eteach.events.UserEvent;
import lu.uni.fstc.proactivity.utils.Global;


/**
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014 
 *
 */
public class PatternD05 extends AbstractPatternDissatisfaction {

	// possible STATES until pattern is found
	private final int STATE_INITIAL = 0;
	private final int STATE_WL_READ = 1;

	// Current STATE 
	private int state;
 
	// Maximum interval time between clicks to consider (in milliseconds)
//	private final long MAX_TIME_INTERVAL = 2*60*1000; //  2 minutes
	private final long MAX_TIME_INTERVAL =   30*1000; // 30 seconds

	private long timeInitial;

	/**
	 * @see lu.uni.fstc.proactivity.eteach.patterns.AbstractPattern#checkPattern(lu.uni.fstc.proactivity.eteach.events.UserEvent, ResultSet)
	 */
	@Override
	public boolean checkPattern(UserEvent e, ResultSet rs) {

		if ((state == STATE_INITIAL) && (e == UserEvent.WL)) {
			state = STATE_WL_READ;
			timeInitial = Operations.getTimeFromRecord(rs);
			Global.logger.fine("One WL FOUND. next state=" + state);
		}
		else if ((state == STATE_WL_READ) && ((e == UserEvent.AR) || (e == UserEvent.SR) || (e == UserEvent.EOL))) {
			state = STATE_INITIAL;
			long timeFinal = Operations.getTimeFromRecord(rs);

			long interval = (timeFinal-timeInitial);
			Global.logger.fine("One EOL, AR or SR found, State=" +state+ ", Time interval=" + interval);
			if (interval <= MAX_TIME_INTERVAL) {
				return true;
			}

			// In case pattern is not complete, due to time interval being different, 
			// we need to reset it anyway 
			resetPatternInitialState();
		}
		return false;
	}

	@Override
	public void resetPatternInitialState() {
		state = STATE_INITIAL;
	}
}