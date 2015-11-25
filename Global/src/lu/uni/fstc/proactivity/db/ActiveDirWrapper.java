package lu.uni.fstc.proactivity.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.directory.SearchControls;
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
	
	public ArrayList<SearchResult> search(String searchBase, String searchFilter){
		SearchControls sCtrl = new SearchControls();
		sCtrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
		return ado.search(searchBase, searchFilter, sCtrl);
		
	}

	@Override
	public void createTable(String tableName) {
		mso.createTable(conn,tableName);
		
	}

	@Override
	public void insertInto(String tableName, String s) {
		mso.insertInto(conn, tableName, s);
	}
	
	public void insertInto(String tableName, String s,String s1) {
		mso.insertInto(conn, tableName, s, s1);
		
	}
	
	public String getManager(String tableName){
		return mso.getManager(conn, tableName);
	}

	@Override
	public boolean getFinish(String tableName) {
		if (mso.getFinish(conn, tableName).equals("Finish"))
			return true;
		return false;
	}
	
	public String[] getTopGroup(String tableName){
		ResultSet rs = mso.getTopGroup(conn, tableName);
		int i = 0;
		String[] top = new String[3];
		try {
			while(rs.next()){
				top[i] = rs.getString("Name");
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return top;
	}

	@Override
	public long getMax(String tableName) {
		// TODO Auto-generated method stub
		return mso.getMax(conn, tableName);
	}

	@Override
	public long getMean(String tableName) {
		// TODO Auto-generated method stub
		return mso.getMean(conn, tableName);
	}
	
	public void checkProfil(Profil group){
		ResultSet rs = mso.checkProfile(conn, group);
		try {
			if(rs.next()){
				System.out.println("Profil exisant "+rs.getString("idProfil"));
				mso.insertGroup(conn, group, rs.getInt("idProfil"));
			}
			else{
				System.out.println("New profil");
				mso.createNewProfile(conn, group);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
				
	}

}
