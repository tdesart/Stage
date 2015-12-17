package lu.uni.fstc.proactivity.db;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;

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
	public void insertInto(String groupeName, boolean result,String reason) {
		mso.insertInto(conn, groupeName, result,reason);
		
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
	public void insertInto(String tableName, ArrayList memberOf,String distinguishedName) {
		mso.insertInto(conn, tableName, memberOf, distinguishedName);
	}
	
	public void insertInto(String tableName, String s,String s1,String role) {
		mso.insertInto(conn, tableName, s, s1,role);		
	}
	
	public void insertInto(String tableName, String s,String s1) {
		mso.insertInto(conn, tableName, s, s1);
		mso.insertInto(conn, "password_historic",s,s1, new Date().toString());
	}
	
	public void deleteFrom(String tableName, String name){
		mso.deleteFrom(conn, tableName, name);
	}
	
	public String getManager(String tableName){
		return mso.getManager(conn, tableName);
	}
	
	public String getRole(String tableName){
		return mso.getRole(conn, tableName);
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
				if(!rs.getString("Name").equals("Finish")){
					top[i] = rs.getString("Name");
					i++;
				}
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

	@Override
	public void cleanDb() {
		mso.cleanDb(conn);
		
	}

	@Override
	public void findSuggestion(String manager, String userName, String role) {
		ResultSet rs = null;
		LinkedHashSet<String> hs = new LinkedHashSet<String>();
		if(!role.equals("noRole"))
			 rs = mso.findProfil(conn, manager,role);
		try {
			while(rs.next() && !rs.equals(null)){
				hs.add((rs.getString("Group1")));
				hs.add((rs.getString("Group2")));
				hs.add((rs.getString("Group3")));
			}
			mso.insertSuggestion(conn, hs, userName, "Manager and role are matching");
			if(hs.size() == 0){
				rs = mso.findProfil(conn, manager);
				while(rs.next()){
					hs.add((rs.getString("Group1")));
					hs.add((rs.getString("Group2")));
					hs.add((rs.getString("Group3")));
				}
				if(hs.size()>0)
					mso.insertSuggestion(conn, hs, userName, "Only manager is matching");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	

	@Override
	public void createTableSuggestion() {
		mso.createTableSuggestion(conn);
		
	}
	
	public long countSuggestion(){
		return mso.countSuggestion(conn);
	}
	
	public String getSuggestion(){
		ResultSet rs = mso.getSuggestion(conn);
		String name ="";
		String name2="";
		String result="";
		String pw="";
		try {
			while(rs.next()){
				if(name2.equals("")){
					name2 = rs.getString("name");
					String reason = rs.getString("reason");
					if(reason.equals(""))
						result += "<li>"+name2+"</li>";
					else
						result += "<li>"+name2+"</li>  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp&nbsp"+rs.getString("reason")+"<br><br>";
				}
				name = rs.getString("name");
				if(name.equals(name2)){
					result += ("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp - &nbsp"+rs.getString("groupe"))+"<br>";
					if(!rs.getString("groupe").equals("No Suggestion"))
						pw += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp Add-ADGroupMember '"+name+"' '"+rs.getString("groupe")+"'<br>";
				}
				else{
					result += "<br>"+ pw ;
					pw="";
					name2 = rs.getString("name");
					String reason = rs.getString("reason");
					if(reason.equals(""))
						result += "<li>"+name2+"</li>";
					else
						result +="<li>"+ name2+"</li> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp&nbsp"+rs.getString("reason")+"<br><br>";
					result += ("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp - &nbsp"+rs.getString("groupe"))+"<br>";
					if(!rs.getString("groupe").equals("No Suggestion"))
						pw += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp Add-ADGroupMember '"+name+"' '"+rs.getString("groupe")+"'<br>";
				}
			}
			result += "<br>"+pw;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "<ul>"+result+"</ul>";
	}

	@Override
	public String getGroupDelete() {
		ResultSet rs = mso.getGroupDelete(conn);
		int i = 0;
		String result="";
		try {
			while(rs.next()){
				i++;
				result += "<li>"+rs.getString("name")+"</li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp"+rs.getString("reason")+"<br>" ;
				result +="&nbsp;&nbsp;&nbsp;&nbsp;&nbspRemove-ADGroup '"+rs.getString("name")+"'<br>";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  "<b>"+i+"</b> groups have been detected as useless :<br><ul>"+result+"<ul>";
	}
	
	public String getExpiredPassword(){
		ResultSet rs = mso.getExpiredPassword(conn);
		String result="";
		int i = 0;
		try {
			while(rs.next()){
				i++;
				result+="&nbsp;&nbsp;- "+rs.getString("name")+" "+rs.getString("role")+"<br>";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "<b>"+i+"</b> users have an expired password since at least 30 days <br><br>"+result;
	}
	
	public long getFrom(String tableName, String name){
		return mso.getFrom(conn,tableName,name);
	}

}
