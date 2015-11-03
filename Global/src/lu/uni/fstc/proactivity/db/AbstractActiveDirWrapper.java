package lu.uni.fstc.proactivity.db;

import java.util.ArrayList;

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
	
	public abstract void insertIntoGroupe(String groupeName,boolean result);

	public abstract void dropTable(String tableName) ;

}
