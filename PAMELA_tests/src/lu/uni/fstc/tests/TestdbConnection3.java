package lu.uni.fstc.tests;

import java.sql.ResultSet;
import java.sql.SQLException;

import lu.uni.fstc.proactivity.db.MoodleDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 *
 */
public class TestdbConnection3 {
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		try {
			ResultSet students;
			MoodleDbWrapper conn1 = MoodleDbWrapper.getInstance("moodleREAL2.cfg.xml");
			Global.logger.info("Moodle server - " + MoodleDbWrapper.connParams.toString());

			//String lineToInsert = "sandro 10";
			//conn1.testConnectionInsert(lineToInsert);
			if (conn1 != null) {
				Global.logger.info("[TestdbConnection3] LIST");
				students = conn1.testgetStudentsFromCourse(481);
				students.beforeFirst();
				while (!students.isClosed() && students.next()) {
					long id = students.getLong("id");
					String n1 = students.getString("firstname");
					String n2 = students.getString("lastname");
					String role = students.getString("r.name");
					Global.logger.info("[TestdbConnection3] [" + id + "] " + role+ "-" + n1 + " " + n2 );
				}
			}
			else
				Global.logger.info("[TestdbConnection3] conn NULL");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}