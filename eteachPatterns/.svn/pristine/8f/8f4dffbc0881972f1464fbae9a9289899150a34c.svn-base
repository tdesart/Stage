package lu.uni.fstc.proactivity.rules.eteach;


import lu.uni.fstc.proactivity.eteach.events.Operations;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/** 
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014 
 *
 */
public class MTA_history_monitor extends AbstractRuleEteach {

	// possible STATES for this scenario
	private final int STATE_INITIAL = 0;
	private final int STATE_CREATESET = 1;
	private final int STATE_CLOSESET = 2;

	private long last_pattern_id;
	private long last_EOL_id;
//	private long temp_pattern_id;
	private static long temp_EOL_id;
	
	// Current STATE 
	static private int state;
	// Current Patterns counter
	static private long patternCount;

	private boolean isNewPatternS2orS3;
	private boolean isNewEOL;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private MTA_history_monitor() {
		setType(RuleType.META);
//		state = STATE_INITIAL;
	}

	/**
	 * @param last_pattern_id 
	 * @param last_EOL_id 
	 */
	public MTA_history_monitor(final long last_pattern_id, final long last_EOL_id) {
		this();
		Global.logger.finer("Custom constructor (long last_pattern_id, long last_EOL_id)!");
		setLast_pattern_id(last_pattern_id);
		setLast_EOL_id(last_EOL_id);
	}

	/**
	 * 	Checks if new events are present on the database, store in a Boolean variable (newEvents)
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		isNewPatternS2orS3 = getDataNativeSystem().isNewPatternS2orS3(getLast_pattern_id());
		if (isNewPatternS2orS3)
			setLast_pattern_id(getDataNativeSystem().getNextPattern(getLast_pattern_id()));

		isNewEOL = getDataNativeSystem().isNewEventEOL(getLast_EOL_id());
		if (isNewEOL) {
			temp_EOL_id = getLast_EOL_id();
			setLast_EOL_id(getDataNativeSystem().getNextEventEOL(getLast_EOL_id()));
//			temp_EOL_id = getDb().getNextEventEOL(getLast_EOL_id());
		}
//		Global.logger.fine("isNewPatternS2orS3=" + isNewPatternS2orS3 + ", isNewEOL=" + isNewEOL);
	}

	/**
	 * Returns the value of newEvents
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
//		return isNewEOL;
//		return true;
		// activate this META only if:
		// (isNewEOL and state = (INITIAL or CLOSE)) or
		// (isNewPatternS2orS3 and state = CREATE)
		Global.logger.finer("state=" + state + ", isNewPatternS2orS3=" + isNewPatternS2orS3 + ", isNewEOL=" + isNewEOL);
		return (state != STATE_CREATESET && isNewEOL) || (state == STATE_CREATESET && isNewPatternS2orS3);
	}

	/**
	 * Returns true – proceed in all cases
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#conditions()
	 */
	@Override
	protected boolean conditions() {
		return true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#actions()
	 */
	@Override
	protected void actions() {
		Global.logger.finer(" ** last_EOL_id [" + getLast_EOL_id() + "]. temp_EOL_id [" + temp_EOL_id + "].last_pattern_id [" + getLast_pattern_id() + "]");
//		if (state == STATE_INITIAL && isNewEOL) {
		if (state == STATE_INITIAL) {
			Global.logger.fine("state == STATE_INITIAL --> Skip EOL");
			state = STATE_CREATESET;
		}
//		else if (state == STATE_CREATESET && isNewPatternS2orS3) {
		else if (state == STATE_CREATESET && temp_EOL_id != getLast_EOL_id()) {
			Global.logger.fine("state == STATE_CREATESET --> Operations.createSet() + Operations.collectAndSaveAssotiatedTags()");
			// Update the EOL we should be treating. 
			// Cannot assume it's the last one, and we can skip a few: until WL
			// use the pattern_id to get the event id, and then get the previous EOL 
			long previous_EOL_id = getDataNativeSystem().getPreviousEOLEventFromPattern(getLast_pattern_id());

			Operations.createSet(previous_EOL_id);
			patternCount++;

			Global.logger.fine("STATE_CREATESET --> Operations.collectAndSaveAssotiatedTags()");
			Operations.collectAndSaveAssotiatedTags(previous_EOL_id);

			// not sure if necessary, I think that in this stage, it's always TRUE
//			if (isNewPatternS2orS3)
//			setLast_pattern_id(temp_pattern_id);
			setLast_EOL_id(previous_EOL_id);
			Global.logger.fine(" ** saving new previous_EOL_id [" + previous_EOL_id + "].");

			state = STATE_CLOSESET;
		}
//		else if (state == STATE_CLOSESET && isNewEOL) {
		else if (state == STATE_CLOSESET) {
			Global.logger.fine("state == STATE_CLOSESET --> Operations.closeSet()");
			Operations.closeSet(getLast_EOL_id());
			state = STATE_CREATESET;

			// update this temp, to skip pattern on the next iteration
			temp_EOL_id = getLast_EOL_id();
		}
		else {
			Global.logger.finer("Skipping Pattern, because EOL was already used to close set, on a previous iteration");
		}

		// not sure if necessary, I think that in this stage, it's always TRUE
//		if (isNewEOL) 
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		if (getActivated()) {
			// clone the rule, updating the value of the parameters
			Global.logger.finest("Last pattern treated [" + getLast_pattern_id() + "], Last event treated [" + getLast_EOL_id() + "]  ");
			createRule(new MTA_history_monitor(getLast_pattern_id(), getLast_EOL_id()));
		}
		else {
			// clone the rule, as is.
			createRule(this);
		}
		return true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
//		return "MTA_history_monitor - Meta-scenario: History Monitoring - fetching similar tags. Since pattern ID = " + this.getLast_pattern_id() + ", EOL ID = " + getLast_EOL_id();
		return "History Monitoring - fetching similar tags. Since pattern ID = " + this.getLast_pattern_id() + ", EOL ID = " + getLast_EOL_id();
	}

	/**
	 * @param last_pattern_id the last_pattern_id to set
	 */
	public void setLast_pattern_id(long last_pattern_id) {
//		Global.logger.fine(" ** last_pattern_id [" + last_pattern_id + "].");
		this.last_pattern_id = last_pattern_id;
	}

	/**
	 * @return the last_pattern_id
	 */
	public long getLast_pattern_id() {
		return this.last_pattern_id;
	}

	/**
	 * @param last_EOL_id the last_EOL_id to set
	 */
	public void setLast_EOL_id(long last_EOL_id) {
//		Global.logger.fine(" ** last_EOL_id [" + last_EOL_id + "].");
		this.last_EOL_id = last_EOL_id;
	}

	/**
	 * @return the last_EOL_id
	 */
	public long getLast_EOL_id() {
		return this.last_EOL_id;
	}
}