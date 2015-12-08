package lu.uni.fstc.proactivity.db;

import java.util.ArrayList;
import java.util.HashSet;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.parameters.MoodleConnectionParameters;

/**
 * 
 * @author thomas.desart
 *
 */
public abstract class AbstractActiveDirWrapper extends AbstractHostDataAccessWrapper {
	
	final MySQLOperations mso = MySQLOperations.getInstance ();
	final ActiveDirOperations ado = ActiveDirOperations.getInstance();
	
	protected AbstractActiveDirWrapper() {
		// TODO Auto-generated constructor stub
	}
	
	public static MoodleConnectionParameters connParams = null;
	
	public abstract ArrayList<SearchResult> searchUsers();
	
	public abstract ArrayList<SearchResult> search1User(String name);
	
	public abstract void createTableGroup();

	public abstract boolean tableExist(String tableName);
	
	public abstract long countLines(String tableName);
	
	public abstract void insertInto(String groupeName,boolean result,String reason);

	public abstract void dropTable(String tableName) ;
	
	public abstract ArrayList<SearchResult> search(String searchBase, String SearchFilter);
	
	public abstract void createTable(String tableName);
		
	public abstract void insertInto(String tableName,String s, String s1, String role);
	
	public abstract String getManager(String tableName);
	
	public abstract boolean getFinish(String tableName);
	
	public abstract String[] getTopGroup(String tableName);
	
	public abstract long getMax(String tableName);
	
	public abstract long getMean(String tableName);
	
	public abstract String getRole(String tableName);
	
	public abstract void checkProfil(Profil group);

	public abstract void insertInto(String tableName, ArrayList memberOf, String distinguishedName);
	
	public abstract void cleanDb();
	
	public abstract void findSuggestion(String manager, String userName, String role);
	
	public abstract void createTableSuggestion();
	
	public abstract long countSuggestion();
	
	public abstract String getSuggestion();

	public abstract String getGroupDelete() ;

}
