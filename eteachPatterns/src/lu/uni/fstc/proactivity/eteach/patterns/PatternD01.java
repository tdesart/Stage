package lu.uni.fstc.proactivity.eteach.patterns;

import java.sql.ResultSet;

import lu.uni.fstc.proactivity.eteach.events.UserEvent;


//import lu.uni.fstc.utils.Global;

/**
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014 
 *
 */
public class PatternD01 extends AbstractPatternDissatisfaction {

	// Maximum counter of STATES until pattern is found
//	private final int MAXEOLS = 10;

	// Current (counter of) STATE(S) 
//	private int numEOLS;

	/**
	 * @see lu.uni.fstc.proactivity.eteach.patterns.AbstractPattern#checkPattern(lu.uni.fstc.proactivity.eteach.events.UserEvent, ResultSet)
	 */
	@Override
	public boolean checkPattern(UserEvent e, ResultSet rs) {
		// NOT what Denis S. wanted!
		// He wanted the difference between line numbers in the table in the interface
		// I don't have that info, since it's not saved on db with the click information!!
/*		if (e == UserEvent.EOL) {
			// One more step of the pattern
			numEOLS++;
			Global.logger.fine("One more EOL FOUND. numEOLS=" + numEOLS);

			// If Pattern found !!
			if (numEOLS == MAXEOLS)
				return true;
		}
*/
		return false;
	}

	@Override
	public void resetPatternInitialState() {
//		numEOLS = 0;
	}
}