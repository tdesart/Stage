package lu.uni.fstc.proactivity.eteach.patterns;

import java.sql.ResultSet;


import lu.uni.fstc.proactivity.eteach.events.UserEvent;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014 
 *
 */
public class PatternS01 extends AbstractPatternSatisfaction {

	// possible STATES until pattern is found
	private final int STATE_INITIAL = 0;
	private final int STATE_READ = 1;

	// Current STATE 
	private int state;

	// Maximum counter of STATES until pattern is found
	private final int MAX_EOL_COUNT = 5;

	// Current (counter of) STATE(S) 
	private int numEOLS;

	/**
	 * @see lu.uni.fstc.proactivity.eteach.patterns.AbstractPattern#checkPattern(lu.uni.fstc.proactivity.eteach.events.UserEvent, ResultSet)
	 */
	@Override
	public boolean checkPattern(UserEvent e, ResultSet rs) {
		if (e == UserEvent.AR || e == UserEvent.SR ) {
			state = STATE_READ;
			Global.logger.fine("One AR/SR FOUND. state=" + state);

			if (numEOLS == MAX_EOL_COUNT)
				return true;

			numEOLS = 0;
		}
		else if ((state == STATE_READ) && (e == UserEvent.EOL)) {
			// One more step of the pattern
			numEOLS++;
			Global.logger.fine("One more EOL FOUND. numEOLS=" + numEOLS);
		}
		return false;
	}

	@Override
	public void resetPatternInitialState() {
		state = STATE_INITIAL;
		numEOLS = 0;
	}
}