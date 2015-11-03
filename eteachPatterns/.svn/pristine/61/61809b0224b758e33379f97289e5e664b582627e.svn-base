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
public class PatternS04a extends AbstractPatternSatisfaction {

	// Maximum number of the same SITE until pattern is found
	private final int MAX_SITE_COUNT = 4;

	static private String lastSite;
	static long siteCount; 

	// Maximum interval time between clicks to consider (in milliseconds)
	private final long MAX_TIME_AVERAGE =   4*1000; // 4 seconds

	private long timeInitial;

	/**
	 * @see lu.uni.fstc.proactivity.eteach.patterns.AbstractPattern#checkPattern(lu.uni.fstc.proactivity.eteach.events.UserEvent, ResultSet)
	 */
	@Override
	public boolean checkPattern(UserEvent e, ResultSet rs) {
		if (e == UserEvent.EOL) {
			String thisSite = Operations.getSiteFromTag(rs);

			if (lastSite.equalsIgnoreCase(thisSite)) {
				siteCount++;

				if (siteCount == MAX_SITE_COUNT) {
					long timeFinal = Operations.getTimeFromRecord(rs);

					long average = (timeFinal-timeInitial)/(siteCount-1);
					Global.logger.finer(siteCount + "consecutive site found, Time average=" + average);
					if (average >= MAX_TIME_AVERAGE) 
						return true;
				}
			}
			else {
				lastSite = thisSite;
				timeInitial = Operations.getTimeFromRecord(rs);
				siteCount = 1;
			}

			Global.logger.fine("One more EOL FOUND. this SITE = " + thisSite + ", count=" + siteCount);
		}
		return false;
	}

	@Override
	public void resetPatternInitialState() {
		lastSite = lu.uni.fstc.proactivity.utils.StringUtils.EMPTY_STRING;
		siteCount = 0;
	}
}