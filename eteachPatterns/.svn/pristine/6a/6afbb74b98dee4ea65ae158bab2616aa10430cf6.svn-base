package lu.uni.fstc.proactivity.eteach.patterns;

import java.sql.ResultSet;


import lu.uni.fstc.proactivity.eteach.events.UserEvent;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014 
 *
 */
public class PatternS02 extends AbstractPatternSatisfaction {

	// possible STATES until pattern is found
	private final int STATE_INITIAL = 0;
	private final int STATE_EOL_READ = 1;

	// Current STATE 
	private int state;

	/**
	 * @see lu.uni.fstc.proactivity.eteach.patterns.AbstractPattern#checkPattern(lu.uni.fstc.proactivity.eteach.events.UserEvent, ResultSet)
	 */
	@Override
	public boolean checkPattern(UserEvent e, ResultSet rs) {
		if ((state == STATE_EOL_READ) && (e == UserEvent.MHC)) {
			// this event cancels the past, thus reseting the pattern
			resetPatternInitialState();
			Global.logger.fine("One mhC FOUND (after EOL). reseting state=" + state);
		}
		else if ((state == STATE_INITIAL) && (e == UserEvent.EOL)) {
			state = STATE_EOL_READ;
			Global.logger.fine("One EOL FOUND. next state=" + state);
		}
		else if ((state == STATE_EOL_READ) && (e == UserEvent.WL)) {
			Global.logger.fine("One WL FOUND (after EOL). Pattern complete!");
			return true;
		}
		return false;
	}

	@Override
	public void resetPatternInitialState() {
		state = STATE_INITIAL;
	}
}