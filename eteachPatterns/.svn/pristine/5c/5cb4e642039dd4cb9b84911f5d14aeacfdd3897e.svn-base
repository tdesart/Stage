package lu.uni.fstc.proactivity.eteach.patterns;

import java.sql.ResultSet;

import lu.uni.fstc.proactivity.eteach.events.UserEvent;
import lu.uni.fstc.proactivity.utils.Global;


/**
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014 
 *
 */
public class PatternD08 extends AbstractPatternDissatisfaction {

	// possible STATES until pattern is found
	private final int STATE_INITIAL = 0;
	private final int STATE_EOL_READ = 1;

	// Current STATE 
	private int state;

	// Maximum counter of STATES until pattern is found
	private final int MAX_COMBINATION_COUNT = 3;

	// Current (counter of) STATE(S) 
	private int numCombinations;

	/**
	 * @see lu.uni.fstc.proactivity.eteach.patterns.AbstractPattern#checkPattern(lu.uni.fstc.proactivity.eteach.events.UserEvent, ResultSet)
	 */
	@Override
	public boolean checkPattern(UserEvent e, ResultSet rs) {
		if (((state == STATE_EOL_READ) || (state == STATE_INITIAL)) && (e == UserEvent.WL)) {
			resetPatternInitialState();
			Global.logger.fine("One WL FOUND (after EOL). reseting state=" + state);
		}
//		else if ((state == STATE_EOL_READ) && (e == UserEvent.EOL)) {
//			resetPatternInitialState();
//			Global.logger.fine("One EOL FOUND (after EOL). reseting state=" + state);
//		}
//		else if ((state == STATE_INITIAL) && (e == UserEvent.EOL)) {
		else if (e == UserEvent.EOL) {
			state = STATE_EOL_READ;
			Global.logger.fine("One EOL FOUND. next state=" + state);
		}
		else if ((state == STATE_EOL_READ) && ((e == UserEvent.AR) || (e == UserEvent.SR))) {
			state = STATE_INITIAL;
			numCombinations++;
			Global.logger.fine("One more AR/SR FOUND. numCombinations=" + numCombinations);

			// If Pattern found !!
			if (numCombinations == MAX_COMBINATION_COUNT)
				return true;
		}
		return false;
	}

	@Override
	public void resetPatternInitialState() {
		state = STATE_INITIAL;
		numCombinations = 0;
	}
}