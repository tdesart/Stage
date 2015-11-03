package lu.uni.fstc.test_Remus;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import lu.uni.fstc.converter.ResultSetConverter;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.rules.AbstractRule;
import lu.uni.fstc.proactivity.rules.RuleType;
import lu.uni.fstc.proactivity.db.AbstractPAM2MoodleDbWrapper;

/**
 * @author sandro.reis
 *
 */
public class ResultSetToJSON extends AbstractRule {

	private long currentTime, start_time;
	//private String text, subject, cityGroupsName, user_City, groupName;
	private ResultSet onlineUsers;
	private JSONArray ja;

	private ResultSetToJSON() {
		setType(RuleType.SCENARIO);
	}
	/**
	 * @param start_time 
	 */
	public ResultSetToJSON(final long start_time) {
		this();
		this.start_time = start_time;
		ja = new JSONArray();
	}

	/*
	 * gets all the Social Groups based on the Study Program
	 * */
	@Override
	protected void dataAcquisition() {
		currentTime = System.currentTimeMillis();
		onlineUsers = ((AbstractPAM2MoodleDbWrapper)engine.dataEngine).getOnlineUsers();
	}

	@Override
	protected boolean activationGuards() {
		if (currentTime > start_time)
			return true;
		return false;
	}

	/*
	 * if users were not asked this question again regarding the same group
	 * */
	@Override
	protected boolean conditions() {
		return true;
	}
	
	@Override
	protected void actions() {		
		try {
			ja = ResultSetConverter.toJSONArray(onlineUsers);
		} catch (final SQLException e) {
			e.printStackTrace();
		} catch (final JSONException e) {
			e.printStackTrace();
		}
		
		
	}


	/*
	 *	Generates MTA301 which analyzes the answers of the students 
	 * */
	@Override
	protected boolean rulesGeneration() {
		Global.logger.finer(this.toString());
		Global.logger.info(ja.toString());
		
		try {
			Global.logger.info(ja.get(0).toString());
		} catch (final JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String toString() {
		return "ResultSetToJSON test";
	}
}