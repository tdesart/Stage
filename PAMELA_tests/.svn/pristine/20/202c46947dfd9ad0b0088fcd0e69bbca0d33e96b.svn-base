package lu.uni.fstc.tests;

import lu.uni.fstc.proactivity.db.AbstractMoodleDbWrapper;
import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.Time;

/**
 * @author Sandro Reis
 *
 */
public class TestEngine implements Runnable {
	/**
	 * 
	 */
	public AbstractMoodleDbWrapper db;
	/**
	 * 
	 */
	public AbstractPAM2MoodleDbWrapper dbPam;

	/**
	 * @param db
	 * @param dbPam 
	 */
	public TestEngine(AbstractMoodleDbWrapper db, AbstractPAM2MoodleDbWrapper dbPam) {
		this.db = db;
		this.dbPam = dbPam;
	}

	@Override
	public void run() {
		setStopping(false);
		long t1 = System.currentTimeMillis();
		long t2;
		while (!isStopping()) {
			t2 = System.currentTimeMillis();
			Global.logger.info("While... " + (t2-t1));
			Time.sleepNSeconds(3);
			t1 = System.currentTimeMillis();
		}
		Global.logger.info("Leaving While! ");
	}

	/**
	 * 
	 */
	public void forceStop () {
		setStopping(true);
	} 

	/**
	 * @param toStop the stopping to set
	 */
	public void setStopping(boolean toStop) {
		long t1 = System.currentTimeMillis();
		this.dbPam.setScheduleToStop(toStop);
		long t2 = System.currentTimeMillis();
		Global.logger.info("setStopping ... " + (t2-t1));
	}

	/**
	 * @return the stopping
	 */
	public boolean isStopping() {
		return this.dbPam.isScheduleToStop();
	}
}