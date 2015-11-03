package lu.uni.fstc.proactivity.ruleRunningSystem;

/**
 * <b>System Parameters for the Pro-active Learning System</b>
 *  
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 * 
 */
public class SystemParameters {

	private long id = 0;

	/**
	 * Time frequency of the activation periods. (in milliseconds)
	 */
	public long F;

	/**
	 * (maximum) Number of rules the engine runs in one activation period
	 */
	public long N;

	/**
	 * Minimum pause between activation periods to allow running the host system tasks.
	 * In effect, only if time to run N rules is close to F. (in milliseconds)
	 */
	public long P;

	/**
	 * @return the f
	 */
	public long getF() {
		return F;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the n
	 */
	public long getN() {
		return N;
	}

	/**
	 * @return the p
	 */
	public long getP() {
		return P;
	}

	/**
	 * @param f the f to set
	 */
	public void setF(long f) {
		F = f;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param n the n to set
	 */
	public void setN(long n) {
		N = n;
	}

	/**
	 * @param p the p to set
	 */
	public void setP(long p) {
		P = p;
	}

	/**
	 * Sets the default values for the three parameters
	 */
	public void setDefaulValues() {
		setF(5000);
		setP(2000);
		setN(100);
	}
}