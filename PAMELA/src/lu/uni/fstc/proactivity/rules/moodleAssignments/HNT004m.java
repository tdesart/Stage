package lu.uni.fstc.proactivity.rules.moodleAssignments;

import java.sql.ResultSet;
import java.sql.SQLException;


import lu.uni.fstc.proactivity.rules.Importance;
import lu.uni.fstc.proactivity.rules.RuleMessageMoodle;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;


/**
 * @author Sandro Reis
 * @author Gilles Neyens
 * @version 3.0 - Sandro Reis © 2012 
 * @version 3.1 - Gilles Neyens © 2013 
 *
 */
public class HNT004m extends AbstractRuleMoodle{

	private long assignment_id;
	private boolean enabled,exists;

	private long hint_id;
	private ResultSet students;
	private String subject, text;
	

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private HNT004m() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * @param assignment_id 
	 * @param hint_id 
	 */
	public HNT004m(final long assignment_id, final long hint_id) {
		this();
		setAssignment_id(assignment_id);
		setHint_id(hint_id);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		this.exists=dataNativeSystem.assignmentExists25(this.assignment_id);
		if(exists){
			this.enabled=dataNativeSystem.isScenarioEnabled(this.assignment_id,"hints");
			if(enabled){
				String assignment_name = dataNativeSystem.getAssignmentName25(this.assignment_id);
				long course_id = dataNativeSystem.getCourseIdFromAssignment25(this.assignment_id);
				String course_name = dataNativeSystem.getCourseName(course_id);
				
				this.subject = "New hint for assignment "+assignment_name+" in course "+course_name;
				this.text = dataNativeSystem.getStringScenarioConfigParam(assignment_id, "hints", "proasspaascenario_hint_text"+this.hint_id);
				this.students = dataNativeSystem.getAllStudentsNotSubmitted25(course_id, assignment_id);
			}
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		return true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#conditions()
	 */
	@Override
	protected boolean conditions() {
		return exists && enabled;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#actions()
	 */
	@Override
	protected void actions() {
		dataNativeSystem.setSentToHint(assignment_id,hint_id); 
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());

		if(exists){
			if(enabled){
				long id;
				try {
					this.students.beforeFirst();
					while (!this.students.isClosed() && this.students.next()) {
						id = this.students.getLong("id");
						createRule(new RuleMessageMoodle(id, this.subject, this.text, Importance.LOW));
						Global.logger.info("Creating new MSG001 for student [" + id + "] ");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
				return true;
			}
			return true;
		}else{
			return true;
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return "HNT004m - Specific hint on a particular assignment. Hint ID = " + this.hint_id;
	}

	/**
	 * @param assignment_id the assignment_id to set
	 */
	public void setAssignment_id(long assignment_id) {
		this.assignment_id = assignment_id;
	}

	/**
	 * @return the assignment_id
	 */
	public long getAssignment_id() {
		return this.assignment_id;
	}

	/**
	 * @return a long
	 */
	public long getHint_id() {
		return hint_id;
	}

	/**
	 * @param hint_id
	 */
	public void setHint_id(final long hint_id) {
		this.hint_id = hint_id;
	}
}
