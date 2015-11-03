package lu.uni.fstc.proactivity.rules.moodleAssignments;


import java.sql.ResultSet;
import java.sql.SQLException;


import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.rules.moodleAssignments.AbstractRuleMoodle;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @author Gilles Neyens
 * @version 3.0 - Sandro Reis © 2012 
 * @version 3.1 - Gilles Neyens © 2013 
 *
 */
public class MTA001m extends AbstractRuleMoodle {

	private long last_assignment_id;
	private boolean newAssignment;
	private ResultSet assignments;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private MTA001m() {
		setType(RuleType.META);
	}

	/**
	 * @param last_assignment_id 
	 */
	public MTA001m(long last_assignment_id) {
		this();
		setLast_assignment_id(last_assignment_id);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		this.newAssignment = dataNativeSystem.isNewAssignment25(this.last_assignment_id);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		return this.newAssignment;
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
			long assignment_ID = this.last_assignment_id;
			this.assignments = dataNativeSystem.getNewProactiveAssignmentsOrdered(this.last_assignment_id);
			try {
				this.assignments.beforeFirst();
				while (!this.assignments.isClosed() && this.assignments.next()) {
					assignment_ID = this.assignments.getLong("id");
					long course_id = dataNativeSystem.getCourseIdFromAssignment25(assignment_ID);
					long assignment_creator=dataNativeSystem.getAssignmentCreator25(course_id,assignment_ID);
					Global.logger.info("Creating MTA002bm, MTA002am, MTA008m, MTA002cm, RMD001m, MTA001mNTF001m  for the new assignment [" + assignment_ID + "]");
					createRule(new MTA001mNTF001m(assignment_ID,assignment_creator));
					createRule(new MTA002bmRMD   (assignment_ID));
					createRule(new MTA002bmPPR   (assignment_ID,assignment_creator));
					createRule(new MTA002am      (assignment_ID,assignment_creator));
					createRule(new MTA002cm      (assignment_ID,assignment_creator));
					createRule(new MTA008m       (assignment_ID,assignment_creator));
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}

			Global.logger.finest("Last assignment treated [" + assignment_ID + "] ");
			// This implementation has a design issue:
			// It relies on the dbWrapper to provide data ordered (in getNewAssignments), 
			// otherwise, the last_id saved might not be the greatest, thus enabling detecting the same assignments several times
			createRule(new MTA001m(assignment_ID));
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
		return "MTA001m - Meta-scenario: New assignment detection. Since assignment ID = " + this.last_assignment_id;
	}

	/**
	 * @return the last_assignment_id
	 */
	public long getLast_assignment_id() {
		return this.last_assignment_id;
	}

	/**
	 * @param last_assignment_id the last_assignment_id to set
	 */
	public void setLast_assignment_id(long last_assignment_id) {
		this.last_assignment_id = last_assignment_id;
	}
}
