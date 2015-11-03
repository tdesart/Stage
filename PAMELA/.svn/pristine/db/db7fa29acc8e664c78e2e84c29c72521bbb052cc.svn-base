package lu.uni.fstc.proactivity.rules.moodleAssignments;

import java.sql.ResultSet;
import java.sql.SQLException;


import lu.uni.fstc.proactivity.rules.Importance;
import lu.uni.fstc.proactivity.rules.RuleMessageMoodle;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 *
 */
public class HNT003 extends AbstractRuleMoodle {

	private long assignment_id;
	private long previousAccess;
	private long thisAccess;
	private String assignment_name;
	private String course_name;
	private ResultSet students;
	private String subject, text;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private HNT003() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * @param assignment_id 
	 * @param previousAccess 
	 * @param thisAccess 
	 */
	public HNT003(long assignment_id, long previousAccess, long thisAccess) {
		this();
		setAssignment_id(assignment_id);
		setPreviousAccess(previousAccess);
		setThisAccess(thisAccess);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		this.assignment_name = dataNativeSystem.getAssignmentName(this.assignment_id);
		long course_id = dataNativeSystem.getCourseIdFromAssignment(this.assignment_id);
		this.course_name = dataNativeSystem.getCourseName(course_id);

		this.students = dataNativeSystem.getStudentsWithFirstAccess (this.assignment_id, course_id, this.previousAccess, this.thisAccess);
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
		return true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#actions()
	 */
	@Override
	protected void actions() {
		StringBuffer buf = new StringBuffer();

		this.subject = "Hint: for a better writing experience";
		buf.append("This hint message is related to your assignment '" + this.assignment_name + "' for the course '" + this.course_name + "'. \n");
		buf.append("Please take into consideration the following online sources that provide the explanations on how to perform a writing task:\n");
		buf.append("\t - http://kimberlychapman.com/essay/essay.html\n");
		buf.append("\t - http://www2.actden.com/writ_den/tips/essay/\n");
		buf.append("\t - http://grammar.ccc.commnet.edu/grammar/composition/literature.htm\n");
		buf.append("\t - http://www1.aucegypt.edu/academic/writers/\n");
		buf.append("\t - http://writesite.cuny.edu/\n");
		buf.append("\t - http://owl.english.purdue.edu/\n");
		buf.append("\t - http://lklivingston.tripod.com/essay/links.html\n");
		buf.append("\nAnd a list of online reference resources.\n");
		buf.append("\t - http://dictionary.reference.com/\n");
		buf.append("\t - http://thesaurus.reference.com/\n");
		buf.append("\t - http://wordreference.com/\n");
		buf.append("\t - http://www.pons.eu/\n");
		buf.append("\t - http://www.wolframalpha.com/\n");

		this.text = buf.toString();
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		long id;
		try {
			this.students.beforeFirst();
			while (!this.students.isClosed() && this.students.next()) {
				id = this.students.getLong("userid");
				createRule(new RuleMessageMoodle(id, this.subject, this.text, Importance.LOW));
				Global.logger.info("Creating new MSG001 for student [" + id + "] ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return "HNT003 - Suggestion on how to perform the writing tasks. Assignment ID = " + this.assignment_id;
	}

	/**
	 * @return the assignment_id
	 */
	public long getAssignment_id() {
		return this.assignment_id;
	}

	/**
	 * @param assignment_id the assignment_id to set
	 */
	public void setAssignment_id(long assignment_id) {
		this.assignment_id = assignment_id;
	}

	/**
	 * @param previousAccess the previousAccess to set
	 */
	public void setPreviousAccess(long previousAccess) {
		this.previousAccess = previousAccess;
	}

	/**
	 * @return the previousAccess
	 */
	public long getPreviousAccess() {
		return this.previousAccess;
	}

	/**
	 * @param thisAccess the thisAccess to set
	 */
	public void setThisAccess(long thisAccess) {
		this.thisAccess = thisAccess;
	}

	/**
	 * @return the thisAccess
	 */
	public long getThisAccess() {
		return this.thisAccess;
	}
}
