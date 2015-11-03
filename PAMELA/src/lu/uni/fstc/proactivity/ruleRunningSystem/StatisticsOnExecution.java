package lu.uni.fstc.proactivity.ruleRunningSystem;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011
 *  
 */
public class StatisticsOnExecution extends Statistics {

	/**
	 * number of iteration of the current executions
	 */
	private long nIterations = 0;

	/**
	 * system parameters used on the current execution
	 */
	private long sysParamF, sysParamN, sysParamP;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	public StatisticsOnExecution () {
		super();
	}

	/**
	 * non-default constructor
	 * initialises all statistic values
	 * @param param the set of system parameters for this run of the system 
	 */
	public StatisticsOnExecution (SystemParameters param) {
		this();
		setSysParamF(param.F);
		setSysParamN(param.N);
		setSysParamP(param.P);
}

	/**
	 * @return the nIterations
	 */
	public long getnIterations() {
		return this.nIterations;
	}

	/**
	 * @return the sysParamF
	 */
	public long getSysParamF() {
		return this.sysParamF;
	}

	/**
	 * @return the sysParamN
	 */
	public long getSysParamN() {
		return this.sysParamN;
	}

	/**
	 * @return the sysParamP
	 */
	public long getSysParamP() {
		return this.sysParamP;
	}

	/**
	 * 
	 */
	public void incnIterations() {
		this.nIterations++;
	}

	/**
	 * @param nIterations the nIterations to set
	 */
	public void setnIterations(long nIterations) {
		this.nIterations = nIterations;
	}

	/**
	 * @param sysParamF the sysParamF to set
	 */
	public void setSysParamF(long sysParamF) {
		this.sysParamF = sysParamF;
	}

	/**
	 * @param sysParamN the sysParamN to set
	 */
	public void setSysParamN(long sysParamN) {
		this.sysParamN = sysParamN;
	}

	/**
	 * @param sysParamP the sysParamP to set
	 */
	public void setSysParamP(long sysParamP) {
		this.sysParamP = sysParamP;
	}
}
