package lu.uni.fstc.proactivity.rules.moodleAssignments;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


import lu.uni.fstc.proactivity.rules.Importance;
import lu.uni.fstc.proactivity.rules.RuleMessageMoodle;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.StringUtils;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 *
 */
public class RMD003 extends AbstractRuleMoodle {

	private long student_id;
	private ResultSet events;
	private String subject, text;

	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	public RMD003() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * @param student_id 
	 */
	public RMD003(long student_id) {
		this();
		setStudent_id(student_id);
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		this.events = dataNativeSystem.getNextWeekEventsFromStudent(student_id);
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

		
		this.subject = "Reminder: Next week events";
		this.text = "This reminder is related to your calendar.\n";
		this.text += "Your scheduled events for the next week are: \n";

		StringBuffer buf = new StringBuffer();
		try {
//			buf.append("<HTML  lang='fr' xml:lang='fr' xmlns='http://www.w3.org/1999/xhtml'>");
//			buf.append("<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'>");
		
			buf.append("This is the list of events detected in your calendar for next week.\n\n");
//			buf.append("<table border='1' cellpadding='3' cellspacing='1' lang='FR-LU'><tr><th>Event Type</th><th>Event Name</th><th>Course Name</th><th>Event Date</th></tr>");
			buf.append("Event Type\t - \tEvent Name\t - \tCourse Name\t - \tEvent Date\n");
			this.events.beforeFirst();
			while (!this.events.isClosed() && this.events.next()) {
				String modName = this.events.getString("modulename");
				if (StringUtils.isEmptyOrNullString(modName) || StringUtils.isZeroString(modName))
					modName = "";
				else
					modName = modName.concat(" ");
				
				String eventType = this.events.getString("eventtype");
				if (StringUtils.isEmptyOrNullString(eventType) || StringUtils.isZeroString(eventType))
					eventType = "";

				String event = "";
				event = event.concat(modName);
				event = event.concat(eventType);

				if (StringUtils.isEmptyOrNullString(event) || StringUtils.isZeroString(event))
					event = "-NA-";
				
				String courseName = this.events.getString("CourseName");
				if (StringUtils.isEmptyOrNullString(courseName) || StringUtils.isZeroString(courseName))
					courseName = "-NA-";
				
				String eventName = this.events.getString("EventName");
				if (StringUtils.isEmptyOrNullString(eventName) || StringUtils.isZeroString(eventName))
					eventName = "-NA-";

				long dateL = this.events.getLong("timestart");
				String dateS = "-NA-";
				if (dateL != 0) {
					Date dateD = new Date(dateL*1000); // because date from database comes in seconds, need to transform it into milliseconds
					dateS = dateD.toString();
				}
//				buf.append("<tr><td>" + event + "</td><td>" + eventName + "</td><td>" + courseName + "</td><td>" + dateS + "</td></tr>");
				buf.append(event + "\t - \t" + eventName + "\t - \t" + courseName + "\t - \t" + dateS + "\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
//			buf.append("</table>");
//			buf.append("</HTML>\n");
			this.text = buf.toString();
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		createRule(new RuleMessageMoodle(student_id, this.subject, this.text, Importance.HIGH));
		Global.logger.info("Creating new MSG001 for student [" + student_id + "] ");
	
		return true;
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#toString()
	 */
	@Override
	public String toString() {
		return "RMD003 - Reminder for next week events for student = " + this.student_id;
	}

	/**
	 * @param student_id the student_id to set
	 */
	public void setStudent_id(long student_id) {
		this.student_id = student_id;
	}

	/**
	 * @return the student_id
	 */
	public long getStudent_id() {
		return this.student_id;
	}
}
