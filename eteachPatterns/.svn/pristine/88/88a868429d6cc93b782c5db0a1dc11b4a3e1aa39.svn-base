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
public class PatternD06 extends AbstractPatternDissatisfaction {

	// Maximum number of TWIN TAGS until pattern is found
	private final int MAX_TWINTAG_COUNT = 5;
	
	// Maximum number of the same TAGS until tag is counted as twin tag (normally 2)
	private final int COUNT_TO_BE_TWINTAG = 2;

	// TwinTags counter 
	private int numTwinTags;

	private HashMap<String, Long> twinTags = new HashMap <String, Long>();

	/**
	 * @see lu.uni.fstc.proactivity.eteach.patterns.AbstractPattern#checkPattern(lu.uni.fstc.proactivity.eteach.events.UserEvent, ResultSet)
	 */
	@Override
	public boolean checkPattern(UserEvent e, ResultSet rs) {
		if (e == UserEvent.EOL) {
			String tag = Operations.getThemeFromTag(rs);
			long tagCount = Operations.checkTagCount(twinTags, tag);
			
			// Only increment the counter if its a new twin tag (found for the MAXTAGS(2nd) time)
			// if value is < MAXTAGS (1), then we haven't found the pattern yet
			// if value is > MAXTAGS (3+), then we're finding the same value over and over again, we don't want to do anything with it!
			if (tagCount == COUNT_TO_BE_TWINTAG)
				numTwinTags++;

			Global.logger.fine("One more EOL FOUND. this TAG count = " + tagCount + ", numTwinTags=" + numTwinTags);

			if (numTwinTags == MAX_TWINTAG_COUNT)
				return true;
		}
		return false;
	}

	@Override
	public void resetPatternInitialState() {
		twinTags.clear();
		numTwinTags = 0;
	}
}