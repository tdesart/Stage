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
public class PatternS06 extends AbstractPatternSatisfaction {

	// possible STATES until pattern is found
	private final int STATE_INITIAL = 0;
	private final int STATE_EOL_READ = 1;

	// Current STATE 
	private int state;

	// Maximum number of the same SITE until pattern is found
	private final int MAX_THEME_COUNT = 2;
	static private String thisTheme;

	private HashMap<String, Long> themeList = new HashMap <String, Long>();

	/**
	 * @see lu.uni.fstc.proactivity.eteach.patterns.AbstractPattern#checkPattern(lu.uni.fstc.proactivity.eteach.events.UserEvent, ResultSet)
	 */
	@Override
	public boolean checkPattern(UserEvent e, ResultSet rs) {
		if (e == UserEvent.EOL) {
			state = STATE_EOL_READ;
			thisTheme = Operations.getThemeFromTag(rs);
			Global.logger.fine("One EOL FOUND. theme = '" + thisTheme + "'. next state=" + state);
		}
		else if ((state == STATE_EOL_READ) && (e == UserEvent.MHC)) {
			long themeCount = Operations.checkTagCount(themeList, thisTheme);

			Global.logger.fine("One mhC FOUND after EOL. this Theme " + thisTheme  + ", themeCount=" + themeCount);
			if (themeCount == MAX_THEME_COUNT)
				return true;
		}
		return false;

	}

	@Override
	public void resetPatternInitialState() {
		state = STATE_INITIAL;
		thisTheme = lu.uni.fstc.proactivity.utils.StringUtils.EMPTY_STRING;
		// themeList is not supposed to e cleared once the pattern is detected! 
//		themeList.clear();
	}
}