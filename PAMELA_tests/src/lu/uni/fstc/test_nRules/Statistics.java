package lu.uni.fstc.test_nRules;

import lu.uni.fstc.proactivity.rules.RuleType;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011
 *  
 */
public class Statistics {

	private long id;
	private long execution_id; 

	/**
	 * time counters
	 */
	private long startDate, endDate, executionTime;

	/**
	 * rule and queue counters
	 */
	private long nRulesExecuted,	// for rules that enter execute()
				 nRulesExecuted_MTA, 
				 nRulesExecuted_SCN, 
				 nRulesExecuted_MSG,
				 nRulesActivated,	// for rules that pass activationGuards() (activated == TRUE)
				 nRulesActivated_MTA, 
				 nRulesActivated_SCN, 
				 nRulesActivated_MSG,
				 nRulesActed,		// for rules that enter actions() 
				 nRulesActed_MTA, 
				 nRulesActed_SCN, 
				 nRulesActed_MSG,
				 queueSizeAtEnd;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 * also used by the other constructor 
	 * initialises all statistic values
	 */
	public Statistics () {
		setStartDate();
	}

	/**
	 * non-default constructor
	 * initialises all statistic values
	 * @param execution_id the execution_id related to this iteration, or himself if already an execution_id
	 */
	public Statistics (long execution_id) {
		this();
		setExecution_id(execution_id);
	}

	/**
	 * to calculate final statistic values
	 * @param queueSize 
	 */
	public final void finalise (long queueSize) {
		setQueueSizeAtEnd(queueSize);
//		setnRulesExecuted();
		setEndDate();
	}

	/**
	 * @return the endDate
	 */
	public long getEndDate() {
		return endDate;
	}

	/**
	 * @return the execution_id
	 */
	public long getExecution_id() {
		return execution_id;
	}

	/**
	 * @return the executionTime
	 */
	public long getExecutionTime() {
		return executionTime;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the nRulesExecuted
	 */
	public long getnRulesExecuted() {
		return nRulesExecuted;
	}

	/**
	 * @return the nRulesExecuted_MSG
	 */
	public long getnRulesExecuted_MSG() {
		return nRulesExecuted_MSG;
	}

	/**
	 * @return the nRulesExecuted_MTA
	 */
	public long getnRulesExecuted_MTA() {
		return nRulesExecuted_MTA;
	}

	/**
	 * @return the nRulesExecuted_SCN
	 */
	public long getnRulesExecuted_SCN() {
		return nRulesExecuted_SCN;
	}

	/**
	 * @return the nRulesExecuted
	 */
	public long getnRulesActivated() {
		return nRulesActivated;
	}

	/**
	 * @return the nRulesExecuted_MSG
	 */
	public long getnRulesActivated_MSG() {
		return nRulesActivated_MSG;
	}

	/**
	 * @return the nRulesExecuted_MTA
	 */
	public long getnRulesActivated_MTA() {
		return nRulesActivated_MTA;
	}

	/**
	 * @return the nRulesExecuted_SCN
	 */
	public long getnRulesActivated_SCN() {
		return nRulesActivated_SCN;
	}

	/**
	 * @return the nRulesExecuted
	 */
	public long getnRulesActed() {
		return nRulesActed;
	}

	/**
	 * @return the nRulesExecuted_MSG
	 */
	public long getnRulesActed_MSG() {
		return nRulesActed_MSG;
	}

	/**
	 * @return the nRulesExecuted_MTA
	 */
	public long getnRulesActed_MTA() {
		return nRulesActed_MTA;
	}

	/**
	 * @return the nRulesExecuted_SCN
	 */
	public long getnRulesActed_SCN() {
		return nRulesActed_SCN;
	}

	/**
	 * @param nRulesExecuted the nRulesExecuted to set
	 */
	public void setnRulesExecuted(long nRulesExecuted) {
		this.nRulesExecuted = nRulesExecuted;
	}

	/**
	 * @param nRulesExecuted_MSG the nRulesExecuted_MSG to set
	 */
	public void setnRulesExecuted_MSG(long nRulesExecuted_MSG) {
		this.nRulesExecuted_MSG = nRulesExecuted_MSG;
	}

	/**
	 * @param nRulesExecuted_MTA the nRulesExecuted_MTA to set
	 */
	public void setnRulesExecuted_MTA(long nRulesExecuted_MTA) {
		this.nRulesExecuted_MTA = nRulesExecuted_MTA;
	}

	/**
	 * @param nRulesExecuted_SCN the nRulesExecuted_SCN to set
	 */
	public void setnRulesExecuted_SCN(long nRulesExecuted_SCN) {
		this.nRulesExecuted_SCN = nRulesExecuted_SCN;
	}

	/**
	 * @param nRulesActivated the nRulesActivated to set
	 */
	public void setnRulesActivated(long nRulesActivated) {
		this.nRulesActivated = nRulesActivated;
	}

	/**
	 * @param nRulesActivated_MSG the nRulesActivated_MSG to set
	 */
	public void setnRulesActivated_MSG(long nRulesActivated_MSG) {
		this.nRulesActivated_MSG = nRulesActivated_MSG;
	}

	/**
	 * @param nRulesActivated_MTA the nRulesActivated_MTA to set
	 */
	public void setnRulesActivated_MTA(long nRulesActivated_MTA) {
		this.nRulesActivated_MTA = nRulesActivated_MTA;
	}

	/**
	 * @param nRulesActivated_SCN the nRulesActivated_SCN to set
	 */
	public void setnRulesActivated_SCN(long nRulesActivated_SCN) {
		this.nRulesActivated_SCN = nRulesActivated_SCN;
	}

	/**
	 * @param nRulesActed the nRulesActed to set
	 */
	public void setnRulesActed(long nRulesActed) {
		this.nRulesActed = nRulesActed;
	}

	/**
	 * @param nRulesActed_MSG the nRulesActed_MSG to set
	 */
	public void setnRulesActed_MSG(long nRulesActed_MSG) {
		this.nRulesActed_MSG = nRulesActed_MSG;
	}

	/**
	 * @param nRulesActed_MTA the nRulesActed_MTA to set
	 */
	public void setnRulesActed_MTA(long nRulesActed_MTA) {
		this.nRulesActed_MTA = nRulesActed_MTA;
	}

	/**
	 * @param nRulesActed_SCN the nRulesActed_SCN to set
	 */
	public void setnRulesActed_SCN(long nRulesActed_SCN) {
		this.nRulesActed_SCN = nRulesActed_SCN;
	}

	/**
	 * @return the queueSizeAtEnd
	 */
	public long getQueueSizeAtEnd() {
		return queueSizeAtEnd;
	}

	/**
	 * @return the startDate
	 */
	public long getStartDate() {
		return startDate;
	}

	/**
	 * 
	 */
	public void setEndDate() {
		this.setEndDate(System.currentTimeMillis());
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	/**
	 * @param execution_id the execution_id to set
	 */
	public void setExecution_id(long execution_id) {
		this.execution_id = execution_id;
	}

	/**
	 * @param executionTime the executionTime to set
	 */
	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	/**
	 * @param executionTime the executionTime to add
	 */
	public void addExecutionTime(long executionTime) {
		this.executionTime += executionTime;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param queueSizeAtEnd the queueSizeAtEnd to set
	 */
	public void setQueueSizeAtEnd(long queueSizeAtEnd) {
		this.queueSizeAtEnd = queueSizeAtEnd;
	}

	/**
	 * 
	 */
	public void setStartDate() {
		this.setStartDate(System.currentTimeMillis());
	}

	/**
	 * @param startDate the endDate to set
	 */
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	/**
	 * Set all counters of this iteration/execution
	 */
	public void setCounts() {
		this.nRulesExecuted_MTA += RuleType.META.getExecutedCount(); 
		this.nRulesExecuted_SCN += RuleType.SCENARIO.getExecutedCount();  
		this.nRulesExecuted_MSG += RuleType.MESSAGE.getExecutedCount();  
//		addnRulesExecuted_MTA(RuleType.META.getExecutedCount());
//		addnRulesExecuted_SCN(RuleType.SCENARIO.getExecutedCount());
//		addnRulesExecuted_MSG(RuleType.MESSAGE.getExecutedCount());
		this.nRulesExecuted = this.nRulesExecuted_MTA +	this.nRulesExecuted_SCN + this.nRulesExecuted_MSG;

		this.nRulesActivated_MTA += RuleType.META.getActivatedCount(); 
		this.nRulesActivated_SCN += RuleType.SCENARIO.getActivatedCount();  
		this.nRulesActivated_MSG += RuleType.MESSAGE.getExecutedCount();  
		this.nRulesActivated = this.nRulesActivated_MTA + this.nRulesActivated_SCN + this.nRulesActivated_MSG;

		this.nRulesActed_MTA += RuleType.META.getActedCount(); 
		this.nRulesActed_SCN += RuleType.SCENARIO.getActedCount();  
		this.nRulesActed_MSG += RuleType.MESSAGE.getActedCount();  
		this.nRulesActed = this.nRulesActed_MTA + this.nRulesActed_SCN + this.nRulesActed_MSG;
	}

	/**
	 * Reset all counters to prepare for the next iteration
	 */
	static public void resetCounts() {
		RuleType.META.resetExecutedCount();
		RuleType.SCENARIO.resetExecutedCount();
		RuleType.MESSAGE.resetExecutedCount();

		RuleType.META.resetActivatedCount();
		RuleType.SCENARIO.resetActivatedCount();
		RuleType.MESSAGE.resetActivatedCount();

		RuleType.META.resetActedCount();
		RuleType.SCENARIO.resetActedCount();
		RuleType.MESSAGE.resetActedCount();
	}
}