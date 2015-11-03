package lu.uni.fstc.proactivity.eteach.patterns;

import java.sql.ResultSet;

import lu.uni.fstc.proactivity.eteach.events.UserEvent;
import lu.uni.fstc.proactivity.utils.Global;


/**
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014 
 *
 */
public class PatternD03 extends AbstractPatternDissatisfaction {

	// Maximum counter of STATES until pattern is found
	private final int MAX_AR_COUNT = 10;

	// Current (counter of) STATE(S) 
	private int numARS;

	/**
	 * @see lu.uni.fstc.proactivity.eteach.patterns.AbstractPattern#checkPattern(lu.uni.fstc.proactivity.eteach.events.UserEvent, ResultSet)
	 */
	@Override
	public boolean checkPattern(UserEvent e, ResultSet rs) {
		if (e == UserEvent.EOL || e == UserEvent.WL || e == UserEvent.SR) {
			// this event cancels the past, thus reseting the pattern
			resetPatternInitialState();
			Global.logger.fine("One EOL/WL/SR FOUND (after AR). reseting state=" + numARS);
		}
		else if (e == UserEvent.AR) {
			// One more step of the pattern
			numARS++;
			Global.logger.fine("One more AR FOUND. numARS=" + numARS);

			// If Pattern found !!
			if (numARS == MAX_AR_COUNT)
				return true;
		}
		return false;
	}

	@Override
	public void resetPatternInitialState() {
		numARS = 0;
	}
}