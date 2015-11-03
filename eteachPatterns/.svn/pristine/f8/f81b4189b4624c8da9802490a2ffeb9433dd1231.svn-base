package lu.uni.fstc.proactivity.rules.eteach;

import java.sql.ResultSet;
import java.sql.SQLException;


import lu.uni.fstc.proactivity.eteach.events.UserEvent;
import lu.uni.fstc.proactivity.eteach.patterns.AbstractPattern;
import lu.uni.fstc.proactivity.eteach.patterns.PatternD01;
import lu.uni.fstc.proactivity.eteach.patterns.PatternD02;
import lu.uni.fstc.proactivity.eteach.patterns.PatternD03;
import lu.uni.fstc.proactivity.eteach.patterns.PatternD04;
import lu.uni.fstc.proactivity.eteach.patterns.PatternD05;
import lu.uni.fstc.proactivity.eteach.patterns.PatternD06;
import lu.uni.fstc.proactivity.eteach.patterns.PatternD07;
import lu.uni.fstc.proactivity.eteach.patterns.PatternD08;
import lu.uni.fstc.proactivity.eteach.patterns.PatternD09;
import lu.uni.fstc.proactivity.eteach.patterns.PatternD10;
import lu.uni.fstc.proactivity.eteach.patterns.PatternS01;
import lu.uni.fstc.proactivity.eteach.patterns.PatternS02;
import lu.uni.fstc.proactivity.eteach.patterns.PatternS03;
import lu.uni.fstc.proactivity.eteach.patterns.PatternS04a;
import lu.uni.fstc.proactivity.eteach.patterns.PatternS04b;
import lu.uni.fstc.proactivity.eteach.patterns.PatternS05;
import lu.uni.fstc.proactivity.eteach.patterns.PatternS06;
import lu.uni.fstc.proactivity.eteach.patterns.PatternS07;
import lu.uni.fstc.proactivity.eteach.patterns.PatternS08;
import lu.uni.fstc.proactivity.eteach.patterns.PatternS09;
import lu.uni.fstc.proactivity.eteach.patterns.PatternS10;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 4.0 - Sandro Reis © 2014 
 *
 */
public class MTA_context_monitor extends AbstractRuleEteach {

	private long last_event_id;
	private boolean newEvents;
	private ResultSet events;

	private static final int NUM_PATTERNS = 21;
	private static AbstractPattern[] patterns = new AbstractPattern[NUM_PATTERNS];
	private static boolean patternsCreated;
	
	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private MTA_context_monitor() {
		Global.logger.finer("Defaut constructor!");
		setType(RuleType.META);

		// patterns array needs to be static, because this is created in every iteration, and we need to store some info on each pattern
		// for that we can only create and initialise them once

		if (!patternsCreated) {
			Global.logger.config("Creating patterns for the first time!");
			
			createAllPatterns();
			initialiseAllPaternStates();// shouldn't be done at every iteration, but only once
			// set the common database wrapper to the one we use in the scenarios
	//			AbstractPattern.setDb(getDbPam());
			patternsCreated = true;
		}
		last_event_id = 0;
		Global.logger.finer("Defaut constructor END!!\n");

	}

	/**
	 * @param last_event_id 
	 */
	public MTA_context_monitor(final long last_event_id) {
		this();
		Global.logger.finer("Custom constructor (long last_event_id)!");
		setLast_event_id(last_event_id);
	}

	/**
	 * 	Checks if new events are present on the database, store in a Boolean variable (newEvents)
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		// I have two confliting principles:
		// on one hand I should use only one db per Rule
		// on the other hand I should read data from the host system's db, and write to the engine db.
		// By chance in this project they are the same...
		//		AbstractPattern.setDb(getDataEngine());
		AbstractPattern.setDb(getDataEngine());
		Global.logger.finer("rule = " +toString()+", db =" + getDataNativeSystem() + ", dbPam =" + getDataEngine());
		this.newEvents = getDataNativeSystem().isNewEvent(getLast_event_id());
	}

	/**
	 * Returns the value of newEvents
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		return this.newEvents;
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
		// Read the events from the database (in reverse order)
		this.events = getDataNativeSystem().getNewEventsOrdered(getLast_event_id());

		// Read the last event ID from the database, and stores it, 
		// so that the next iteration starts here
		setLast_event_id(getDataNativeSystem().getLastEvent());

		try {
			this.events.beforeFirst();
			// go through each event (remember its in reverse order!)
			while (!this.events.isClosed() && this.events.next()) {
				// Get the event ID
				long thisEventID = this.events.getLong("id");

				// Detect the type of the current event
				UserEvent thisEventType = UserEvent.detectEvent(this.events.getString("type"), this.events.getString("element"), this.events.getString("value"));
				Global.logger.fine("Event ["+thisEventID+"] detected of type = " + thisEventType);

				// if detected event is supposed to cancel everything, then reset all patterns
				if (thisEventType == UserEvent.STOP || thisEventType == UserEvent.SB1 || thisEventType == UserEvent.SB2) {
					initialiseAllPaternStates();
				} else if (thisEventType != UserEvent.IGNORE) {
					// only check all known patterns against this event if not IGNORE
					for (int i = 0; i < patterns.length; i++) {
						if (patterns[i].checkPattern(thisEventType, this.events))  // if pattern was detected, treat it accordingly
							patterns[i].treatPattern(thisEventID);
					} // for (each pattern defined)
				} // if !IGNORE

			} // while records exits (each event on the records read)
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Global.logger.finest("Last event treated [" + getLast_event_id() + "] ");
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		if (getActivated()) {
			// clone the rule, updating the value of the last event ID

			Global.logger.finest("Last event treated [" + getLast_event_id() + "] ");
			// This implementation has a design issue:
			// It relies on the dbWrapper to provide data ordered (in getNewAssignments), 
			// otherwise, the last_id saved might not be the greatest, thus enabling detecting the same assignments several times
			createRule(new MTA_context_monitor(getLast_event_id()));
		}
		else {
			// clone the rule, as is.
			createRule(this);
		}

		return true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
//		return "MTA_context_monitor - Meta-scenario: Context Monitoring - New event detection. Since event ID = " + this.getLast_event_id();
		return "Context Monitoring - New event detection. Since event ID = " + this.getLast_event_id();
	}

	/**
	 * Method that delegates to each implementation the task to properly initialise itself
	 */
	private void initialiseAllPaternStates() {
		Global.logger.finer("Initialising All Patterns...");
		for (int i = 0; i < patterns.length; i++)
			patterns[i].resetPatternInitialState();
	}

	/**
	 * Method that creates an entry for each type of pattern in the array
	 */
	private void createAllPatterns() {
		Global.logger.finer("creating All Patterns...");
		patterns [0] = new PatternD01();
		patterns [1] = new PatternD02();
		patterns [2] = new PatternD03();
		patterns [3] = new PatternD04();
		patterns [4] = new PatternD05();
		patterns [5] = new PatternD06();
		patterns [6] = new PatternD07();
		patterns [7] = new PatternD08();
		patterns [8] = new PatternD09();
		patterns [9] = new PatternD10();
		patterns[10] = new PatternS01();
		patterns[11] = new PatternS02();
		patterns[12] = new PatternS03();
		patterns[13] = new PatternS04a();
		patterns[14] = new PatternS04b();
		patterns[15] = new PatternS05();
		patterns[16] = new PatternS06();
		patterns[17] = new PatternS07();
		patterns[18] = new PatternS08();
		patterns[19] = new PatternS09();
		patterns[20] = new PatternS10();
	}

	/**
	 * @return the last_assignment_id
	 */
	public long getLast_event_id() {
		return this.last_event_id;
	}

	/**
	 * @param last_event_id the last_event to set
	 */
	public void setLast_event_id(long last_event_id) {
		this.last_event_id = last_event_id;
	}
}