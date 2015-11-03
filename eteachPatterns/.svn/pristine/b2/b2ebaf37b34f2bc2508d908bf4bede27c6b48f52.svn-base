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
public class PatternS07 extends AbstractPatternSatisfaction {

	// Maximum number of the same TAGS until pattern is found
	private final int MAX_TAG_COUNT = 5;

	private HashMap<String, Long> tagList = new HashMap <String, Long>();

	/**
	 * @see lu.uni.fstc.proactivity.eteach.patterns.AbstractPattern#checkPattern(lu.uni.fstc.proactivity.eteach.events.UserEvent, ResultSet)
	 */
	@Override
	public boolean checkPattern(UserEvent e, ResultSet rs) {
		if (e == UserEvent.EOL) {
			String tag = Operations.getThemeFromTag(rs);
			long tagCount = Operations.checkTagCount(tagList, tag);

			Global.logger.fine("One more EOL FOUND. this tag " + tag + ", tagCount=" + tagCount);

			if (tagCount == MAX_TAG_COUNT)
				return true;
		}
		return false;
	}

	@Override
	public void resetPatternInitialState() {
		// tagList is not supposed to e cleared once the pattern is detected! 
//		tagList.clear();
	}
}