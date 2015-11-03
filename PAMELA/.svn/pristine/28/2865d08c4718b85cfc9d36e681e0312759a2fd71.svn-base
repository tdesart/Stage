package lu.uni.fstc.proactivity.rules.moodleCoPs;

import java.sql.ResultSet;

import lu.uni.fstc.proactivity.db.MySQLOperations;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * 	@author remus.dobrican
 *
 *	Generates the questions and the answers
 */
public class S301 extends AbstractRuleCops {

	private long user_ID, course_ID;
	private ResultSet students;
	private final String question1 = "Do you want to be part of country-based social groups ?", question2 = "Do you want to be part of city-based social groups ?";
	private final int orderCountry = 1, orderCity = 2;
		
	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private S301() {
		setType(RuleType.SCENARIO);
	}

	/**
	 * @param course_ID
	 */
	public S301(long course_ID) {
		this();
		this.course_ID = course_ID;
	}

	/**
	 * registers the possible questions and answers in dbPam
	 * */
	@Override
	protected void dataAcquisition() {
			students = dataNativeSystem.getStudentsFromCourse(course_ID);		
			//	MySQLOperations.startTransaction(conn);
			// 	registers questions and question_possible_answers
			/**/
			dataEngine.registerAnswer("Ok");
			dataEngine.registerAnswer("No, thanks!");
			
			dataEngine.registerQuestion("question_country", question1, orderCountry);
			dataEngine.registerQuestionPossibleAnswer(dataEngine.getQuestionID(orderCountry), dataEngine.getAnswerID("Ok"));
			dataEngine.registerQuestionPossibleAnswer(dataEngine.getQuestionID(orderCountry), dataEngine.getAnswerID("No, thanks!"));
			/**/
			
			dataEngine.registerQuestion("question_city", question2, orderCity);
			dataEngine.registerQuestionPossibleAnswer(dataEngine.getQuestionID(orderCity), dataEngine.getAnswerID("Ok"));
			dataEngine.registerQuestionPossibleAnswer(dataEngine.getQuestionID(orderCity), dataEngine.getAnswerID("No, thanks!"));
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
		MySQLOperations mso = MySQLOperations.getInstance();
		Global.logger.finer("--------------------- S301 actions() ----------------- ");
		
		try {
			if (!mso.startTransaction(engine.dataEngine.getConn()))
				return;

			this.students.beforeFirst();
			while (!this.students.isClosed() && this.students.next()) {
				user_ID = this.students.getLong("id");				// gets the user's ID				
								
				if(!dataEngine.questionExists(dataEngine.getQuestionID(orderCountry), user_ID)){
					dataEngine.registerQuestion_User(dataEngine.getQuestionID(orderCountry), user_ID);
				}
				
				if(!dataEngine.questionExists(dataEngine.getQuestionID(orderCity), user_ID)){
					dataEngine.registerQuestion_User(dataEngine.getQuestionID(orderCity), user_ID);
				}
			}
			
			mso.commit(engine.dataEngine.getConn());
		}
		catch (final Exception e){
			e.printStackTrace();
			mso.rollback(engine.dataEngine.getConn());
			Global.logger.severe("Error in the transaction: rollback executed!");
		}
	}

	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		return true;
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	public String toString() {
		return "S301 - Registers questions for users in PAM ";
	}
	
	/**
	 * @return a long
	 */
	public long getCourse_ID() {
		return course_ID;
	}
	
	/**
	 * @param course_ID
	 */
	public void setCourse_ID(final long course_ID) {
		this.course_ID = course_ID;
	}
}