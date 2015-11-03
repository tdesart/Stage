package lu.uni.fstc.proactivity.eteach.patterns;

import java.sql.ResultSet;
import java.util.HashMap;


import lu.uni.fstc.proactivity.eteach.events.Operations;
import lu.uni.fstc.proactivity.eteach.events.UserEvent;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014 
 *
 */
public class PatternS10 extends AbstractPatternSatisfaction {

	// Maximum counter of STATES until pattern is found
	private final int MAX_EOL_COUNT = 10;

	// Current (counter of) STATE(S) 
	private int numEOLS;
	private boolean atLeastOnce;

	private HashMap<String, Boolean> twinTags = new HashMap <String, Boolean>();

	/**
	 * @see lu.uni.fstc.proactivity.eteach.patterns.AbstractPattern#checkPattern(lu.uni.fstc.proactivity.eteach.events.UserEvent, ResultSet)
	 */
	@Override
	public boolean checkPattern(UserEvent e, ResultSet rs) {
		if (e == UserEvent.EOL) {
			// One more step of the pattern
			numEOLS++;
			Global.logger.fine("One more EOL FOUND. numEOLS=" + numEOLS);

			String tag = Operations.getThemeFromTag(rs);
			boolean exists = Operations.checkTagExiting(twinTags, tag);

			// this theme (TWIN TAG) has already been found, mark it so Pattern is found after MAXEOLS! 
			if (exists && !atLeastOnce)
				atLeastOnce = true;

			// MAXEOLS has been reached, so analyse reset it
			if (numEOLS == MAX_EOL_COUNT) {
				if (atLeastOnce)
					return true;
				else
					resetPatternInitialState();
			}
		}
		return false;
	}

	@Override
	public void resetPatternInitialState() {
		twinTags.clear();
		numEOLS = 0;
		atLeastOnce = false;
	}
}