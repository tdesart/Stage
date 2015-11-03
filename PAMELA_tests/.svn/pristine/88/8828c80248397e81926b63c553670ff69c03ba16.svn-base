package lu.uni.fstc.tests;

import java.util.Date;
import java.util.GregorianCalendar;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 *
 */
public class TestDates {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GregorianCalendar cal = new GregorianCalendar();
//		date1 = new SimpleDateFormat();

		if (cal.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.FRIDAY)
			Global.logger.info("Finally, friday ! " + (cal.get(GregorianCalendar.DAY_OF_WEEK))); 
		else
			Global.logger.info("Today is not Friday ! " + (cal.get(GregorianCalendar.DAY_OF_WEEK))); 

		Global.logger.info("HOUR_OF_DAY: " + cal.get(GregorianCalendar.HOUR_OF_DAY) );
		Global.logger.info("WEEK_OF_YEAR: " + cal.get(GregorianCalendar.WEEK_OF_YEAR) );

		Date dateD = new Date(1330530900);
//		GregorianCalendar cal = new GregorianCalendar(); // Initialised with the current time. Perfect!

Global.logger.info("[TestDates] read Date [" + dateD.toString() + "] ");
	}
}
