/**
 * 
 */
package lu.uni.fstc.tests;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author sandro.reis
 *
 */
public class TestSubString {

	static private final String TAG = "TAG=";
	static private final String SUBTHEME = " -> ";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fieldValue = "SITE=Medscape; TAG=disease management -> diagnosis";
		extractTheme(fieldValue);

		String fieldValue2 = "SITE=Medscape; TAG=prevalence";
		extractTheme(fieldValue2);
//			theme = lu.uni.fstc.proactivity.utils.StringUtils.stringBefore(tag, SUBTHEME);

		
//		String tag = lu.uni.fstc.proactivity.utils.StringUtils.stringAfter(fieldValue, TAG);
//		theme = lu.uni.fstc.proactivity.utils.StringUtils.stringBefore(tag, SUBTHEME);
	}

	/**
	 * @param fieldValue
	 */
	public static void extractTheme(String fieldValue) {
		String theme = lu.uni.fstc.proactivity.utils.StringUtils.EMPTY_STRING;
		String subtheme = lu.uni.fstc.proactivity.utils.StringUtils.EMPTY_STRING;
		String tag = lu.uni.fstc.proactivity.utils.StringUtils.stringAfter(fieldValue, TAG);

		Global.logger.info("tag.lastIndexOf(SUBTHEME) = " + tag.lastIndexOf(SUBTHEME));
		Global.logger.info("SUBTHEME.length() = " + SUBTHEME.length());
		Global.logger.info("tag.length() = " + tag.length());

		theme = lu.uni.fstc.proactivity.utils.StringUtils.stringBefore(tag, SUBTHEME);
		subtheme = lu.uni.fstc.proactivity.utils.StringUtils.stringAfter(tag, SUBTHEME);

		Global.logger.info("fieldValue = " + fieldValue);
		Global.logger.info("tag = " + tag);
		Global.logger.info("theme = " + theme);
		Global.logger.info("subtheme = " + subtheme);
	}

}
