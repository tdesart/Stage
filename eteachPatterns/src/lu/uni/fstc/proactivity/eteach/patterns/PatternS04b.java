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
public class PatternS04b extends AbstractPatternSatisfaction {

	// Maximum number of the same SITE until pattern is found
	private final int MAX_SITE_COUNT = 8;

	private HashMap<String, Long> siteList = new HashMap <String, Long>();

	/**
	 * @see lu.uni.fstc.proactivity.eteach.patterns.AbstractPattern#checkPattern(lu.uni.fstc.proactivity.eteach.events.UserEvent, ResultSet)
	 */
	@Override
	public boolean checkPattern(UserEvent e, ResultSet rs) {
		if (e == UserEvent.EOL) {
			String thisSite = Operations.getSiteFromTag(rs);
			long siteCount = Operations.checkSiteCount(siteList, thisSite);

			Global.logger.fine("One more EOL FOUND. this SITE " + thisSite + ", siteCount=" + siteCount);

			if (siteCount == MAX_SITE_COUNT)
				return true;
		}
		return false;
	}

	@Override
	public void resetPatternInitialState() {
		// siteList is not supposed to e cleared once the pattern is detected! 
//		siteList.clear();
	}
}