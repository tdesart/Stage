package lu.uni.fstc.proactivity.db;

import java.util.ArrayList;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.parameters.MoodleConnectionParameters;
import lu.uni.fstc.proactivity.utils.Global;

/**
 * 
 * @author thomas.desart
 *
 */
public class ActiveDirWrapper extends AbstractActiveDirWrapper {
	
	private static ActiveDirWrapper theInstance = null;
	
	private ActiveDirWrapper() throws Exception {
		super();

		conn = mso.openConnection(connParams);

		if (conn == NO_CONNECTION) {
			setConnected(false);
			Global.logger.warning("Error opening DB connection.");
		}
		else 
			setConnected(true);
	}
	
	public static synchronized ActiveDirWrapper getInstance (final String configFilePath) throws Exception {
		if (theInstance == null) {
			connParams = new MoodleConnectionParameters(configFilePath);
			theInstance = new ActiveDirWrapper();
		}
		return theInstance;
	}

	@Override
	public ArrayList<SearchResult> searchUsers() {
		return ado.getUsers();
	}

	@Override
	public ArrayList<SearchResult> search1User(String name) {
		return ado.getUser(name);
	}
	
	@Override
	public void createTableGroup(){
		mso.createTable(conn);
	}

	@Override
	public boolean tableExist(String tableName) {
		if(mso.tableExist(conn,tableName) == -1) 
			return true;
		return false;
	}

	@Override
	public long countLines(String tableName) {
		// TODO Auto-generated method stub
		return mso.countLines(conn, tableName);
	}

	@Override
	public void insertIntoGroupe(String groupeName, boolean result) {
		mso.insertIntoGroupe(conn, groupeName, result);
		
	}

	@Override
	public void dropTable(String tableName) {
		mso.dropTable(conn,tableName);
		
	}
	
	

}
