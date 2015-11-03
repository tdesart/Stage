package lu.uni.fstc.proactivity.utils;

import java.util.Date;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011/2012 
 *
 */
public class Time {

	/**
	 * the exact amount of seconds in one day
	 */
	public final static long _1DayInSecs = 86400; // Exactly
	/**
	 * the approximate amount of seconds in three days 
	 */
	public final static long _3DaysInSecs = 260000; // Approximately
	/**
	 * the approximate amount of seconds in 8.1 days
	 */
	public final static long _1WeekInSecs = 700000; // Approximately
	/**
	 * the approximate amount of seconds in 7 days
	 */
	public static final long _1WeekInSecsExac = 604800; // Exactly
	/**
	 * the approximate amount of seconds in 150 days 
	 */
	public final static long _150DaysInSecs = 13000000; // Approximately

	/**
	 * public <b>Constant</b> to represent the length of a semester in seconds<br>
	 * approximately 150 days, to include the exam period inside the semester.<br>
	 * <b><i>Rationale:</b> from September (13th) to February (10th) = 150 days</i>
	 */
	public final static long oneSemesterInSecs = _150DaysInSecs; // Approximately

	/**
	 * @param m the time in milliseconds the thread is supposed to sleep 
	 */
	public static void sleepNMilliseconds(long m) {
		try {
			Global.logger.finest("pause: " + m);
			Thread.sleep(m);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param s the time in seconds the thread is supposed to sleep
	 */
	public static void sleepNSeconds(double s) {
		sleepNMilliseconds ((long) (s*1000));
	}

	/**
	 * pause the thread one second 
	 */
	public static void sleepOneSecond() {
		sleepNSeconds(1);
	}

	/**
	 * Transform the input date into a format humans can read.
	 * @param date the Long date in milliseconds 
	 * @return the date in String format, if valid. '-NA-', otherwise<br>Example: 'Tue May 15 14:20:00 CEST 2012'
	 */
	public static String longDateMilisToString (final long date) {
		String dateS = "-NA-";
		if (date != 0) {
			Date dateD = new Date(date);
			dateS = dateD.toString();
		}
		return dateS;
	}

	/**
	 * Transform the input date into a format humans can read.<br>
	 * Since date from the database sometimes comes in seconds we need to transform it into milliseconds first.<br>
	 * @param date the Long date in seconds 
	 * @return the date in String format, if valid. '-NA-', otherwise<br>Example: 'Tue May 15 14:20:00 CEST 2012'
	 */
	public static String longDateSecondsToString (final long date) {
		return longDateMilisToString(date*1000);
	}
}