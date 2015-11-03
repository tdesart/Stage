package lu.uni.fstc.proactivity.rules;

/**
 * Enumerator member classes are implicitly <i>static</i>, so I can use counters by Type.<br>
 * Added counters to statistics on <b>activated</b> and <b>active</b> rules by iteration and execution.<br>
 * Individual increments on those new statistics are done in <code>AbstractRule.execute()</code><br>
 * and sets and resets were added to <code>Statistics.setCounts</code> and <code>Statistics.resetCounts</code><br>
 * <br>Existing Rule Types are:
 * <li>Meta-scenario
 * <li>Scenario
 * <li>Message<br>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 *
 */
public enum RuleType {
	/**
	 * a Meta-scenario
	 */
	META,

	/**
	 * a Scenario
	 */
	SCENARIO,

	/**
	 * messaging rules
	 */
	MESSAGE;

	private long executedCount;
	private long activatedCount;
	private long actedCount;

	private RuleType() {
	}

	/**
	 * @param executedCount the executedCount to set
	 */
	public void setExecutedCount(long executedCount) {
		this.executedCount = executedCount;
	}

	/**
	 * @param activatedCount the activatedCount to set
	 */
	public void setActivatedCount(long activatedCount) {
		this.activatedCount = activatedCount;
	}

	/**
	 * @param actedCount the actedCount to set
	 */
	public void setActedCount(long actedCount) {
		this.actedCount = actedCount;
	}

	/**
	 * @return the count
	 */
	public long getExecutedCount ()
	{
		return this.executedCount;
	}

	/**
	 * @return the activatedCount
	 */
	public long getActivatedCount() {
		return this.activatedCount;
	}

	/**
	 * @return the actedCount
	 */
	public long getActedCount() {
		return this.actedCount;
	}

	/**
	 * 
	 */
	public void incExecutedCount() {
		this.executedCount++;
	}

	/**
	 * 
	 */
	public void incActivatedCount() {
		this.activatedCount++;
	}

	/**
	 * 
	 */
	public void incActedCount() {
		this.actedCount++;
	}

	/**
	 * 
	 */
	public void resetExecutedCount() {
		this.executedCount = 0;
	}

	/**
	 * 
	 */
	public void resetActivatedCount() {
		this.activatedCount = 0;
	}

	/**
	 * 
	 */
	public void resetActedCount() {
		this.actedCount = 0;
	}
}