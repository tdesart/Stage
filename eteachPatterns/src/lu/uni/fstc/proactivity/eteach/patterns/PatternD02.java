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
public class PatternD02 extends AbstractPatternDissatisfaction {

	// Maximum counter of STATES until pattern is found
	private final int MAX_EOL_COUNT = 5;

	// Maximum average time between EOLs to consider (in milliseconds)
	private final long MAX_TIME_AVERAGE = 3500;

	// Current (counter of) STATE(S) 
	private int numEOLS;
	private long timeInitial;

	/**
	 * @see lu.uni.fstc.proactivity.eteach.patterns.AbstractPattern#checkPattern(lu.uni.fstc.proactivity.eteach.events.UserEvent, ResultSet)
	 */
	@Override
	public boolean checkPattern(UserEvent e, ResultSet rs) {
		// One more step of the pattern
		if (e == UserEvent.EOL) {
			numEOLS++;
			Global.logger.fine("One more EOL FOUND. numEOLS=" + numEOLS);

			if (numEOLS==1) {
				timeInitial = Operations.getTimeFromRecord(rs);
			}
			else if (numEOLS == MAX_EOL_COUNT) {
				long timeFinal = Operations.getTimeFromRecord(rs);

				long average = (timeFinal-timeInitial)/(numEOLS-1);
				Global.logger.fine("Time average=" + average);
				if (average <= MAX_TIME_AVERAGE) {
					return true;
				}

				// In case pattern is not complete, due to time average being different, 
				// we need to reset it anyway 
				resetPatternInitialState();
			}
		}
		return false;
	}

	@Override
	public void resetPatternInitialState() {
		numEOLS=0;
	}
}