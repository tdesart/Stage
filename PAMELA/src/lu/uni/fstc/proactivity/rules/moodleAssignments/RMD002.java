package lu.uni.fstc.proactivity.rules.moodleAssignments;

import java.sql.ResultSet;
import java.sql.SQLException;


import lu.uni.fstc.proactivity.rules.Importance;
import lu.uni.fstc.proactivity.rules.RuleMessageMoodle;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.StringUtils;
import lu.uni.fstc.proactivity.utils.Time;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011/2012
 *
 */
public class RMD002 extends AbstractRuleMoodle {

	private long event_id;
//	private Long event_date;
	private String event_name, course_name;
	private ResultSet students, teachers;
	private String subject, text;
	private String dateS = "-NA-";
	private String modName, eventType;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	public RMD002() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * @param event_id 
	 */
	public RMD002(long event_id) {
		this();
		setEvent_id(event_id);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		this.event_name = dataNativeSystem.getEventName(this.event_id);
		this.modName = dataNativeSystem.getEventModule(this.event_id);
		if (StringUtils.isEmptyOrNullString(this.modName) || StringUtils.isZeroString(this.modName))
			this.modName = "";

		this.eventType = dataNativeSystem.getEventType(this.event_id);
		if (StringUtils.isEmptyOrNullString(this.eventType) || StringUtils.isZeroString(this.eventType))
			this.eventType = "";

		long event_date = dataNativeSystem.getEventDate(this.event_id);
		this.dateS = Time.longDateSecondsToString(event_date);
		long course_id = dataNativeSystem.getCourseIdFromEvent(this.event_id);
		this.course_name = dataNativeSystem.getCourseName(course_id);

		this.students = dataNativeSystem.getStudentsFromCourse(course_id);
		this.teachers = dataNativeSystem.getTeachersFromCourse(course_id);
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
		this.subject = "Reminder: New Event Detected '" + this.event_name + "'";
		this.text = "A new event ";

		if (!StringUtils.isEmptyOrNullString(this.modName))
			this.text += "'" + this.modName + "' ";

		if (!StringUtils.isEmptyOrNullString(this.eventType))
		this.text += "of type '" + this.eventType + "' ";

		this.text += "was detected in your calendar.\n";
		this.text += "Please take into consideration the following information:\n";
		this.text += "The event named '" + this.event_name + "' is related to the course '" + this.course_name + "'";
		if (this.dateS.compareTo("-NA-") != 0)
			this.text += ",\nand the date for this event is '" + this.dateS + "'";
		this.text += ".\n";
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
				id = this.students.getLong("id");
				createRule(new RuleMessageMoodle(id, this.subject, this.text, Importance.HIGH));
				Global.logger.info("Creating new MSG001 for student [" + id + "] ");
			}
			this.teachers.beforeFirst();
			while (!this.teachers.isClosed() && this.teachers.next()) {
				id = this.teachers.getLong("id");
				createRule(new RuleMessageMoodle(id, this.subject, this.text, Importance.HIGH));
				Global.logger.info("Creating new MSG001 for teacher [" + id + "] ");
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
		return "RMD002 - Reminder for the upcoming event = " + this.event_id;
	}

	/**
	 * @param event_id the event_id to set
	 */
	public void setEvent_id(long event_id) {
		this.event_id = event_id;
	}

	/**
	 * @return the event_id
	 */
	public long getEvent_id() {
		return this.event_id;
	}
}
