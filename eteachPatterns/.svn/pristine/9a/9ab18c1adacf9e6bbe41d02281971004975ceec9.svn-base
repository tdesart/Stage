package lu.uni.fstc.proactivity.rules.eteach;

import java.sql.ResultSet;
import java.sql.SQLException;


import lu.uni.fstc.proactivity.eteach.relevance.RelevanceEstimation;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014 
 *
 */
public class MTA_model_monitor extends AbstractRuleEteach {

	private boolean thresholdPassed;

	// how many distinct patterns we want to show the user
	private final long PATTERN_TO_SHOW = 10;
	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private MTA_model_monitor() {
		setType(RuleType.META);
	}

	/**
	 * 	Checks if Low relevance has passed the defined threshold
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		thresholdPassed = RelevanceEstimation.thresholdLow();
	}

	/**
	 * Returns the value of thresholdPassed
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		return thresholdPassed;
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
		try {
			ResultSet tags = dataNativeSystem.getLastDistinctAssociatedTag(PATTERN_TO_SHOW);
			tags.beforeFirst();
			long nTag = 0;
			StringBuffer buf = new StringBuffer();

			// Build Numbered Tag List
			while (!tags.isClosed() && tags.next()) {
				String thisTag = tags.getString("tag");
				nTag++;

				// add this tag to the list
				String n2digits = lu.uni.fstc.proactivity.utils.StringUtils.padLeftSpaces("" + nTag, 2);
				buf.append("\t * " + n2digits + " - " + thisTag + "\n");

				// update shown to 1 (TRUE)
				dataNativeSystem.updateShownOnTag(thisTag);
			} // while records exits (each event on the records read)

			String list = buf.toString();
			if (lu.uni.fstc.proactivity.utils.StringUtils.isEmptyOrNullString(list))
				Global.logger.info("There is no List of Associated tags to show the user!");
			else {
				sendTagsToUser(list);
				RelevanceEstimation.resetnPatterns();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param buf
	 */
	private void sendTagsToUser(String tagList) {
		Global.logger.info("List of provided associated tags:\n" + tagList);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		createRule(this);
		return true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
//		return "MTA_model_monitor - Meta-scenario: Model Monitoring - Checking H againt threshold + providing last " + PATTERN_SHOWN + " distinct new tags to the user";
		return "Model Monitoring - Checking H againt threshold, providing last " + PATTERN_TO_SHOW + " distinct new tags to the user";
	}
}