package lu.uni.fstc.proactivity.rules.moodleAssignments;

import java.sql.ResultSet;
import java.sql.SQLException;


import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011
 *
 */
public class MTA005a extends AbstractRuleMoodle {

	private long last_event_id;
	private boolean newEvents;
	private ResultSet events;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private MTA005a() {
		setType(RuleType.META);
	}

	/**
	 * @param last_event_id 
	 */
	public MTA005a(long last_event_id) {
		this();
		setLast_event_id(last_event_id);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		this.newEvents = dataNativeSystem.isNewEvent(this.last_event_id);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		return this.newEvents;
	}

	/**
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
		// no actions, only Rule Generation
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		if (getActivated()) {
			long event_ID = this.last_event_id;
			this.events = dataNativeSystem.getNewEventsOrdered(this.last_event_id);
			try {
				this.events.beforeFirst();
				while (!this.events.isClosed() && this.events.next()) {
					event_ID = this.events.getLong("id");
					Global.logger.info("Creating RMD002 for the new event [" + event_ID + "]");
					createRule(new RMD002(event_ID));
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}

			Global.logger.finest("last event treated [" + event_ID + "] ");
			// This implementation has a design issue:
			// It relies on the dbWrapper to provide data ordered (in getNewEvents), 
			// otherwise, the last_id saved might not be the greatest, thus enabling detecting the same assignments several times
			createRule(new MTA005a(event_ID));
		}
		else 
			createRule(this);

		return true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return "MTA005a - Meta-scenario: New (calendar) event detection. Since event ID = " + this.last_event_id;
	}

	/**
	 * @return the last_event_id
	 */
	public long getLast_event_id() {
		return this.last_event_id;
	}

	/**
	 * @param last_event_id the last_event_id to set
	 */
	public void setLast_event_id(long last_event_id) {
		this.last_event_id = last_event_id;
	}
}
