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
public class PatternS05 extends AbstractPatternSatisfaction {

	// possible STATES until pattern is found
	private final int STATE_INITIAL = 0;
	private final int STATE_EOL_READ = 1;

	// Current STATE 
	private int state;

	// Maximum number of the same SITE until pattern is found
	private final int MAX_SITE_COUNT = 3;
	static private String thisSite;

	private HashMap<String, Long> siteList = new HashMap <String, Long>();

	/**
	 * @see lu.uni.fstc.proactivity.eteach.patterns.AbstractPattern#checkPattern(lu.uni.fstc.proactivity.eteach.events.UserEvent, ResultSet)
	 */
	@Override
	public boolean checkPattern(UserEvent e, ResultSet rs) {
		if (e == UserEvent.EOL) {
			state = STATE_EOL_READ;
			thisSite = Operations.getSiteFromTag(rs);
			Global.logger.fine("One EOL FOUND. site = '" + thisSite + "'. next state=" + state);
		}
		else if ((state == STATE_EOL_READ) && (e == UserEvent.MHC)) {
			long siteCount = Operations.checkSiteCount(siteList, thisSite);

			Global.logger.fine("One mhC FOUND after EOL. this SITE " + thisSite + ", siteCount=" + siteCount);
			if (siteCount == MAX_SITE_COUNT)
				return true;
		}
		return false;

	}

	@Override
	public void resetPatternInitialState() {
		state = STATE_INITIAL;
		thisSite = lu.uni.fstc.proactivity.utils.StringUtils.EMPTY_STRING;
		// siteList is not supposed to e cleared once the pattern is detected! 
//		siteList.clear();
	}
}