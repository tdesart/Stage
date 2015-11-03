package lu.uni.fstc.proactivity.eteach.patterns;

import lu.uni.fstc.proactivity.eteach.relevance.RelevanceEstimation;

/**
 * The interface needed to define a Satisfaction Pattern
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis � 2014 
 *
 */
public abstract class AbstractPatternSatisfaction extends AbstractPattern {
	/**
	 * definition of the pattern of type SATISFACTION 
	 */
	final PatternType type = PatternType.SATISFACTION;

	/**
	 * @return the correct type of this Pattern implementation: SATISFACTION
	 */
	@Override
	public final PatternType getType() {
		return this.type;
	} 

	/**
	 * applying the correct Bayesian formula,<br>
	 * according to the corresponding Pattern Type
	 */
	@Override
	public void recalculateRelevance() {
		RelevanceEstimation.highGivenS();
	}
}