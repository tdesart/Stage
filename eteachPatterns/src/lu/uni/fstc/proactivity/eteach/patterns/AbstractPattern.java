package lu.uni.fstc.proactivity.eteach.patterns;

import java.sql.ResultSet;


import lu.uni.fstc.proactivity.db.EteachEngineDbWrapper;
import lu.uni.fstc.proactivity.eteach.events.UserEvent;
import lu.uni.fstc.proactivity.eteach.relevance.RelevanceEstimation;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * The abstract class needed to define a Pattern
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014 
 *
 */
public abstract class AbstractPattern {
	static private EteachEngineDbWrapper db;
	private long lastPatternRecordID;

	/**
	 * the method must be defined at the children level. 
	 */
	public abstract void recalculateRelevance();

	/**
	 * The method must be defined at the children level,<br>
	 * the idea being to initialise the specific pattern STATE that the child implements
	 */
	public abstract void resetPatternInitialState();

	/**
	 * The method must be defined at the children level,<br>
	 * the idea being to detect the specific pattern hat the child implements
	 * @param e 
	 * @param rs the full resultSet, because some implementation need more information
	 * @return true if a Pattern is found 
	 */
	public abstract boolean checkPattern(UserEvent e, ResultSet rs);

	/**
	 * @param event_ID
	 */
	public final void treatPattern (final long event_ID){ 
		// Set the value of the last Id in which I found a pattern of this instance, for future reference: ignore everything before it 
		setLastPatternRecordID(event_ID);

		// Increase (by 1) the number of Patterns found, to be considered when analysing the threshold
		RelevanceEstimation.incnPatterns();

		// Recalculate relevance based on the type of pattern found
		recalculateRelevance();
		saveRelevance();

		double l = Math.round(RelevanceEstimation.getLow()*100.00)/100.00;
		Global.logger.info(this.toString() + " Current L value is " + l);

		// Save the pattern in the database for statistics purposes
		savePattern();

		// Re-initialise the specific pattern STATE that the child implements
		resetPatternInitialState();
	}

	/**
	 * The method that is responsible to call the database method to save the new HighRelevance Probability on the database for debugging and future relevance
	 * @param h
	 */
	protected final void saveRelevance() {
		double h = RelevanceEstimation.getHigh();

		Global.logger.fine("Saving H = " + h);
		// H represents the High Relevance of the current state, at the moment of the creation of the record
		getDb().saveHValue(h);
	}

	/**
	 * The method that is responsible to call the database method to save the recently found pattern
	 */
 	protected final void savePattern() {
		Global.logger.fine("Saving " + this.toString());
 		getDb().saveNewPattern(this.getClass().getSimpleName(), this.getType().toString(), getLastPatternRecordID()); 
	}
 
	/**
	 * @return the correct type of the Pattern implementation: SATISFACTION/DISSATISFACTION
	 */
	abstract public PatternType getType();

	@Override
	public String toString() {
		return this.getClass().getSimpleName() +" of type " + getType() + " found.";
//		return "Pattern " + this.getClass().getSimpleName() +" of type " + getType() + " found, triggered by event ID = " + getLastPatternRecordID();
	}

	/**
	 * @param lastPatternRecordID the lastPatternRecordID to set
	 */
	public void setLastPatternRecordID(long lastPatternRecordID) {
		this.lastPatternRecordID = lastPatternRecordID;
	}

	/**
	 * @return the lastPatternRecordID
	 */
	public long getLastPatternRecordID() {
		return this.lastPatternRecordID;
	}

	/**
	 * @param db the common db to set
	 */
	public static void setDb(EteachEngineDbWrapper db) {
		Global.logger.finest("value IN SimpleLocalDbWrapper in db =" + db);
		AbstractPattern.db = db;
		Global.logger.finer("value OUT STATIC AbstractPattern.db =" + AbstractPattern.db);
	}

	/**
	 * @return the common db
	 */
	public static EteachEngineDbWrapper getDb() {
		return AbstractPattern.db;
	}
}