package lu.uni.fstc.proactivity.rules.moodleCoPs;

import java.sql.ResultSet;
import java.sql.SQLException;

import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.utils.Global;


/**
 * @author Remus Dobrican
 * @version 1.0 - Remus Dobrican © 2013 
 *
 *	Checks for potential new groups and for uninscribed users
 */
public class M301 extends AbstractRuleCops{
	
	private long course_ID;
	private ResultSet cityGroups, usersCities, countryGroups, usersCountries;
	private String groupName;
	private boolean newCountryGroups, newCityGroups, newCountryStudents, newCityStudents;
	
	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private M301() {
		setType(RuleType.META);
	}
	/**
	 * @param course_ID 
	 */
	public M301(final long course_ID) {
		this();
		this.newCountryGroups = false;
		this.newCityGroups = false;
		this.newCountryStudents = false;
		this.newCityStudents = false;
		this.course_ID = course_ID;
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#dataAcquisition()
	 */
	@Override
	protected void dataAcquisition() {
		
		newCountryGroups = dataEngine.checkForNewCountryGroups(dataEngine.getAnswerID("Ok"));
		newCityGroups = dataEngine.checkForNewCityGroups(dataEngine.getAnswerID("Ok"));
		newCountryStudents = dataEngine.checkForNewCountryStudents(dataEngine.getAnswerID("Ok"));
		newCityStudents = dataEngine.checkForNewCityStudents(dataEngine.getAnswerID("Ok"));

		/*
		 * ================= Checking for groups =================
		 */
		if (newCountryGroups)
			countryGroups = dataEngine.getUncreatedGroups("country", dataEngine.getAnswerID("Ok"));
		else 
			Global.logger.finer("------No newCountryGroups------");

		if (newCityGroups)
			cityGroups = dataEngine.getUncreatedGroups("city", dataEngine.getAnswerID("Ok"));
		else 
			Global.logger.finer("------No newCityGroups------");

		/*
		 * ================= Checking for new users =================
		 */
		if (newCountryStudents)
			usersCountries = dataEngine.getCountriesCitiesFromUninscribedUsers("country",dataEngine.getAnswerID("Ok"));
		else 
			Global.logger.finer("------No newCountryStudents ------");
		
		if (newCityStudents)
			usersCities = dataEngine.getCountriesCitiesFromUninscribedUsers("city",dataEngine.getAnswerID("Ok"));
		else 
			Global.logger.finer("------No newCityStudents------");
	}
	
	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#activationGuards()
	 */
	@Override
	protected boolean activationGuards() {
		return (newCountryGroups || newCityGroups || newCountryStudents || newCityStudents);
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
		Global.logger.finer("--------------------- M301  actions() ----------------- ");
		
		/*
		 * ================= Checking for groups =================
		 */
		if(newCountryGroups){
			Global.logger.finer("************** newCountryGroups *****************");
			try {
				this.countryGroups.beforeFirst();
				while (!this.countryGroups.isClosed() && this.countryGroups.next()) {
					groupName = this.countryGroups.getString("country");

					// only if the group doesn't exists we create it
					if(!dataNativeSystem.groupExist(course_ID, groupName))
						Global.logger.fine("Creating Country group called = " + groupName);
						createRule(new S102(course_ID, groupName, 2));
						
				}
			}		
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(newCityGroups){
			Global.logger.finer("**************newCityGroups*****************");
			try {
				this.cityGroups.beforeFirst();
				while (!this.cityGroups.isClosed() && this.cityGroups.next()) {
					groupName = this.cityGroups.getString("city");

					// only if the group doesn't exists we create it
					if(!dataNativeSystem.groupExist(course_ID, groupName))
						Global.logger.fine("Creating City group called =  = " + groupName);
						createRule(new S102(course_ID, groupName, 3));
				}
			}		
			catch (SQLException e) {
				e.printStackTrace();
			}
		}

		/*
		 * ================= Checking for new users =================
		 */
		if(newCountryStudents){
			Global.logger.finer("**************newCountryStudents*****************");
			
			try {
				this.usersCountries.beforeFirst();
				while (!this.usersCountries.isClosed() && this.usersCountries.next()) {
					groupName = this.usersCountries.getString("country");

					// only if the group Exists we inscribe the user
					if(dataNativeSystem.groupExist(course_ID, groupName)){
						Global.logger.fine("Inscribing new users in Country group called = " + groupName);
						createRule(new S103(course_ID, groupName, "country"));
					}	
				}
			}		
			catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if(newCityStudents){
			Global.logger.finer("**************newCityStudents*****************");
			try {
				this.usersCities.beforeFirst();
				while (!this.usersCities.isClosed() && this.usersCities.next()) {
					groupName = this.usersCities.getString("city");

					// only if the group Exists we inscribe the user
					if(dataNativeSystem.groupExist(course_ID, groupName)){
						Global.logger.fine("Inscribing new users in City group called = " + groupName);
						createRule(new S103(course_ID, groupName, "city"));
					}	
				}
			}		
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see lu.uni.fstc.proactivity.rules.AbstractRule#rulesGeneration()
	 */
	@Override
	protected boolean rulesGeneration() {
		//Global.logger.info(this.toString());
		createRule(this);
		return true;
	}

	@Override
	public String toString() {
		return "MTA301 - Checking for eligible users for country and city-groups and sending them pop-ups with questions";
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