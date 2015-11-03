package lu.uni.fstc.tests;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 *
 */
public class TestBoolean {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean b1;
		boolean b2;
		
		b1 = true;
		b2 = true;
		Global.logger.info("T && T = " + (b1&&b2) + ".\n");
		b1 = false;
		b2 = true;
		Global.logger.info("T && F = " + (b1&&b2) + ".\n");
		b1 = true;
		b2 = false;
		Global.logger.info("F && T = " + (b1&&b2) + ".\n");
		b1 = false;
		b2 = false;
		Global.logger.info("F && F = " + (b1&&b2) + ".\n");
	}
}
