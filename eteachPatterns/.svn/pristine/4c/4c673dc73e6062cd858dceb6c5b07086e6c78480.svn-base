package lu.uni.fstc.proactivity.eteach.patterns;

import java.sql.ResultSet;

import lu.uni.fstc.proactivity.eteach.events.UserEvent;
import lu.uni.fstc.proactivity.utils.Global;


/**
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014 
 *
 */
public class PatternS09 extends AbstractPatternSatisfaction {

	// Maximum counter of STATES until pattern is found
	private final int MAX_EOL_COUNT = 10;

	// Maximum counter of STATES until pattern is found
	private final int MAX_WL_COUNT = 3;

	// Current (counter of) STATE(S) 
	private int numEOLS;
	private int numWLS;

	/**
	 * @see lu.uni.fstc.proactivity.eteach.patterns.AbstractPattern#checkPattern(lu.uni.fstc.proactivity.eteach.events.UserEvent, ResultSet)
	 */
	@Override
	public boolean checkPattern(UserEvent e, ResultSet rs) {
		if (e == UserEvent.EOL) {
			// One more step of the pattern
			numEOLS++;
			Global.logger.fine("One more EOL FOUND. numEOLS=" + numEOLS);

			// If MAXEOLS is reached, then reset the pattern
			if (numEOLS == MAX_EOL_COUNT)
				resetPatternInitialState();
		}
		else if (e == UserEvent.WL && numEOLS > 0) {
			// One more step of the pattern, only after first EOL
			numWLS++;
			Global.logger.fine("One more mhC FOUND. numMHCS=" + numWLS);

			// If Pattern found !!
			if (numWLS == MAX_WL_COUNT)
				return true;
		}
		return false;
	}

	@Override
	public void resetPatternInitialState() {
		numEOLS = 0;
		numWLS = 0;
	}
}