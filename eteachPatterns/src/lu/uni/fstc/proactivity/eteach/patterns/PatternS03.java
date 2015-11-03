package lu.uni.fstc.proactivity.eteach.patterns;

import java.sql.ResultSet;


import lu.uni.fstc.proactivity.eteach.events.UserEvent;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014 
 *
 */
public class PatternS03 extends AbstractPatternSatisfaction {

	// possible STATES until pattern is found
	private final int STATE_INITIAL = 0;
	private final int STATE_EOL_READ = 1;
	private final int STATE_MHC_READ = 2;
//	private final int STATE_WL_READ = 3; // last one = pattern found!

	// Current STATE 
	private int state;

	/**
	 * @see lu.uni.fstc.proactivity.eteach.patterns.AbstractPattern#checkPattern(lu.uni.fstc.proactivity.eteach.events.UserEvent, ResultSet)
	 */
	@Override
	public boolean checkPattern(UserEvent e, ResultSet rs) {
		if ((state == STATE_EOL_READ) && (e == UserEvent.WL)) {
			// this event cancels the past, thus reseting the pattern
			resetPatternInitialState();
			state = STATE_INITIAL;
			Global.logger.fine("One WL FOUND (after EOL). reseting state=" + state);
		}
		else if ((state == STATE_MHC_READ) && (e == UserEvent.EOL)) {
			// this event cancels the past, thus reseting the pattern
			state = STATE_EOL_READ;
			Global.logger.fine("One EOL FOUND. next state=" + state);
		}
		else if ((state == STATE_INITIAL) && (e == UserEvent.EOL)) {
			state = STATE_EOL_READ;
			Global.logger.fine("One EOL FOUND. next state=" + state);
		}
		else if ((state == STATE_EOL_READ) && (e == UserEvent.MHC)) {
			state = STATE_MHC_READ;
			Global.logger.fine("One mhC FOUND (after EOL). next state=" + state);
		}
		else if ((state == STATE_MHC_READ) && (e == UserEvent.WL)) {
//			state = STATE_WL_READ;
			Global.logger.fine("One WL FOUND (after mhC). Pattern complete");
			return true;
		}
		return false;
	}

	@Override
	public void resetPatternInitialState() {
		state = STATE_INITIAL;
	}
}