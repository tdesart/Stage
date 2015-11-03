package lu.uni.fstc.proactivity.eteach.relevance;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * Estimation of the <b>HIGH</b> or <b>LOW</b> relevance based on the <i>Bayesian</i> method (conditional probabilities).
 * <li>High = (probability of) High Relevance 
 * <li>Low  = (probability of) Low  Relevance <br><br>
 * 
 * <li>S = Satisfaction event
 * <li>D = Dissatisfaction event<br><br>
 * NOTE: all probabilities with range 0-100<br>
 * 
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014 
 *
 */
public class RelevanceEstimation {

	/**
	 * Threshold for High relevance, to trigger an external action 
	 */
	private final static double HIGHTHRESHOLD = 80.0;

	/**
	 * Threshold for Low relevance, to trigger an external action
	 */
	private final static double LOWTHRESHOLD  = 80.0;
	/**
	 * The minimum number of patterns found before taking into account the thresholds
	 */
	private final static int MINPATTERNS = 5;
//	private final static int MINPATTERNS = 3;  // TEST ONLY

	/**
	 * Conditional probabilities used to calculate the inverse conditional probabilities: highGivenS(), lowGivenD().
	 */
	final static private double SGivenHigh = 60.0;
	final static private double DGivenLow  = 60.0;

	final static private double DGivenHigh = 100-SGivenHigh;
	final static private double SGivenLow  = 100-DGivenLow;

	/**
	 * Medium Value of 'High', to be used as initial value, and eventually resets of the same value
	 * 
	 */
	private static double HMediumValue = 50; // Medium Value of H

	/**
	 * 'High' represents the High Relevance of the current state, at them moment of the creation of the record
	 * 
	 */
	private static double High = HMediumValue; // initial value set to the medium value, to be changed by the calls to HGivenS() and LGivenD()
//	private static double Low = 1-High; // initial value set to 50%, to be changed by the calls to HGivenS() and LGivenD()

	/** 
	 * the number of patterns found (5 or 10), to take into account when analysing the thresholds
	 */
	private static int nPatterns = 5;

	/**
	 * a possible entry point of this object and one its possible goals: to calculate H, given S.<br>
	 * Using the <i>Bayesian</i> method.
	 */
	static public synchronized void highGivenS(){
		double p = (SGivenHigh * getHigh()) / (SGivenHigh * getHigh() + SGivenLow * getLow())*100;
		Global.logger.finer("new HIGH value = ("+SGivenHigh+" * "+getHigh()+") / ("+SGivenHigh+" * "+getHigh()+" + "+SGivenLow+" * "+getLow() +") = " + (p));
		setHigh(p);
	}

	/**
	 * a possible entry point of this object and one its possible goals: to calculate L, given D.<br>
	 * Using the <i>Bayesian</i> method.
	 */
	static public synchronized void lowGivenD(){
		double p = (DGivenLow * getLow()) / (DGivenLow * getLow() + DGivenHigh * getHigh())*100;
		Global.logger.finer("new LOW value = ("+DGivenLow+" * "+getLow()+") / ("+DGivenLow+" * "+getLow()+" + "+DGivenHigh+" * "+getHigh() +") = " + (p));
		setLow(p);
	}

	/**
	 * @return <b><i>true</i></b> if the number of patterns found is greater than (or igual to ) the defined minimum and<br> 
	 * if the defined threshold for High is surpassed 
	 */
	static public synchronized boolean thresholdHigh() {
		boolean ret = (getnPatterns() >= MINPATTERNS) ? (getHigh() >= HIGHTHRESHOLD) : false;
		Global.logger.finer("High threshold reached = " + ret + ", after " + getnPatterns() + " Patterns.");
		return ret;
	}

	/**
	 * @return <b><i>true</i></b> if the number of patterns found is greater than (or igual to ) the defined minimum and<br> 
	 * if the defined threshold for Low is surpassed 
	 */
	static public synchronized boolean thresholdLow() {
		boolean ret = (getnPatterns() >= MINPATTERNS) ? (getLow() >= LOWTHRESHOLD) : false;
		if (ret)
			Global.logger.fine("Low threshold reached = " + ret);
		else
			Global.logger.finer("Low threshold reached = " + ret);

		return ret;
	}

	/**
	 * @param h the highRelevance level to set (range 0-100)
	 */
	static private synchronized void setHigh (final double h) {
		RelevanceEstimation.High = h;
	}

	/**
	 * @return the highRelevance level (range 0-100)
	 */
	static public synchronized double getHigh() {
		Global.logger.finest("HIGH value = " + (RelevanceEstimation.High));
		return RelevanceEstimation.High;
	}

	/**
	 * @param l the lowRelevance level to set (range 0-100)
	 */
	static private synchronized void setLow (final double l) {
		setHigh (100-l); 
	}

	/**
	 * @return the lowRelevance level (range 0-100)
	 */
	static public synchronized double getLow() {
		Global.logger.finest("LOW value = " + (100-RelevanceEstimation.High));
		return (100-getHigh());
	}

	/**
	 * increment the nPatterns counter
	 */
	public static void incnPatterns() {
		RelevanceEstimation.nPatterns++;
	}
	
	/**
	 * resets the nPatterns counter to 0
	 */
	public static void resetnPatterns() {
		RelevanceEstimation.nPatterns = 0;
	}
	
	/**
	 * @return the nPatterns
	 */
	public static int getnPatterns() {
		return RelevanceEstimation.nPatterns;
	}
}