package lu.uni.fstc.proactivity.rules.moodleAssignments;

import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;
import lu.uni.fstc.proactivity.db.PAMDbWrapper;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2012 
 *
 */
public class HNT004_SaveText {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		// The Hint to Update its text/body
		int hint_id = 4;

		StringBuffer buf = new StringBuffer();

/*/		final AbstractMoodleDbWrapper db = MoodleCacheDbWrapper.getInstance("moodle.cfg.xml");
		if (!db.isConnected()) {
			Global.logger.severe("Unable to connect to the Moodle database. Exiting the application!");
			return;
		}
/**/
		final AbstractPAM2MoodleDbWrapper dbPam = PAMDbWrapper.getInstance("pamREAL2.cfg.xml");
		if (!dbPam.isConnected()) {
			Global.logger.severe("Unable to connect to the PAM database. Exiting the application!");
		}

		if (hint_id==1) {
			buf.append("Dear students,\n\n");
			buf.append("If you did not already fulfill the assignment, please find hereunder\n");
			buf.append("some useful hints on how to proceed:\n\n");
			buf.append("1. as a local variable may have the same name as a parameter in the\n");
			buf.append("function definition, and in this particular case the local variable instance\n");
			buf.append("must \"mask\" (with some caching mechanism) the actual parameter instance,\n");
			buf.append("there is need to create a new environment dedicated to local variables on\n");
			buf.append("top of the parameters environment\n\n");
			buf.append("2. in \"fun_parser.c\" file, there is a function called\n");
			buf.append("    \tboolean findVarList(varname, list)\n");
			buf.append("    \tchar *varname;\n");
			buf.append("    \tPParamList list;\n");
			buf.append("that might be useful in order to check and avoid two local variables\n");
			buf.append("from having the same name.\n\n");
			buf.append("Hope it helps!\n\n");
			buf.append("Best wishes,\nProf. Zampunieris\n");
		}
		else if (hint_id==2) {
			buf.append("Dear students,\n\n");
			buf.append("If you did not already fulfill the assignment (***Friday high noon is absolute deadline***), \n");
			buf.append("please find hereunder some additional useful hints on how to proceed:\n\n");
			buf.append("1. local variables should be considered and parsed like parameters\n");
			buf.append("(except that there is no need to pass some computed arguments to them at run time)\n");
			buf.append("- hence, have a look at the parameters processing code\n\n");
			buf.append("2. along the same lines, in \"L-interpreter.h\" file, there is need to extend\n");
			buf.append("the \"struct TFunDef\" definition/declaration, for example by adding\n");
			buf.append("    \tint nblocals\n");
			buf.append("    \tPParamList locals\n");
			buf.append("to it.\n\n");
			buf.append("Hope it helps!\n\nBest wishes,\nProf. Zampunieris");
		}
		else if (hint_id==3 || hint_id==4) {
			buf.append("Dear students,\n\n");
			buf.append("Please take into your attention the questionnaire about Moodle\''s functionality, \n");
			buf.append("which is now on the main page of your course,\n");
			buf.append("it will give you the possibility to answer the questionnaire online.\n");
			buf.append("This questionnaire is crucial to help us to improve Moodle\''s functionalities for better online experience for you and your future colleagues.\n\n");  
			buf.append("Thank you in advance for taking part in our survey.\n\n");
			buf.append("Denis Shirnin");
		}
		else
			throw new RuntimeException("NOT a valid HINT id!");

		dbPam.updateHintText(hint_id, buf.toString()); 
	}
}
