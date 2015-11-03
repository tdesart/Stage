package lu.uni.fstc.proactivity.eteach.patterns;

import java.sql.ResultSet;

import lu.uni.fstc.proactivity.eteach.events.UserEvent;
import lu.uni.fstc.proactivity.utils.Global;


/**
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014 
 *
 */
public class PatternD09 extends AbstractPatternDissatisfaction {

	// Maximum counter of STATES until pattern is found
	private final int MAX_EOL_COUNT = 10;

	// Current (counter of) STATE(S) 
	private int numEOLS;

	/**
	 * @see lu.uni.fstc.proactivity.eteach.patterns.AbstractPattern#checkPattern(lu.uni.fstc.proactivity.eteach.events.UserEvent, ResultSet)
	 */
	@Override
	public boolean checkPattern(UserEvent e, ResultSet rs) {
		if (e == UserEvent.MHC) {
			// this event cancels the past, thus reseting the pattern
			resetPatternInitialState();
			Global.logger.fine("One MHC FOUND. reseting numEOLS=" + numEOLS);
		}
		else if (e == UserEvent.EOL) {
			// One more step of the pattern
			numEOLS++;
			Global.logger.fine("One more EOL FOUND. numEOLS=" + numEOLS);

			// If Pattern found !!
			if (numEOLS == MAX_EOL_COUNT)
				return true;
		}
		return false;
	}

	@Override
	public void resetPatternInitialState() {
		numEOLS = 0;
	}
}