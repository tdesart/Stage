package lu.uni.fstc.proactivity.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

public class Profil {
	private long whenCreated;
	private long whenChanged;
	private String manager;
	private String[] topGroup;
	private long MeanCreationDate;
	private long MostRecentCreationDate;
	private String distinguishedName;
	private String cn;
	private ArrayList<SearchResult> members;
	private int number;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
	
	public Profil(SearchResult group,int i) {
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Attributes attrs = group.getAttributes();
		if(attrs != null){
			try {
				this.whenCreated = sdf.parse((String) attrs.get("whenCreated").get()).getTime();
				this.whenChanged = sdf.parse((String) attrs.get("whenChanged").get()).getTime();
				this.distinguishedName = ((String) attrs.get("distinguishedName").get());
				this.cn = ((String) attrs.get("cn").get());
				this.number = i;
			} catch (ParseException | NamingException e) {
				e.printStackTrace();
			}
		}
			
	}
	
	public String getNumber() {
		return String.valueOf(number);
	}

	public long getWhenCreated() {
		return whenCreated;
	}

	public long getWhenChanged() {
		return whenChanged;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getDistinguishedName() {
		return distinguishedName;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public int getMembersSize() {
		return members.size();
	}
	
	public int getSizeMeasure(){
		int size = getMembersSize();
		if(size <=10)
			return 1;
		if(size <=100)
			return 2;
		if(size > 100)
			return 3;
		return 0;
	}

	public ArrayList<SearchResult> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<SearchResult> members) {
		this.members = members;
	}

	public String[] getTopGroup() {
		return topGroup;
	}

	public void setTopGroup(String[] topGroup) {
		this.topGroup = topGroup;
	}

	public long getMostRecentCreationDate() {
		return MostRecentCreationDate;
	}

	public void setMostRecentCreationDate(long mostRecentCreationDate) {
		MostRecentCreationDate = mostRecentCreationDate;
	}

	public long getMeanCreationDate() {
		return MeanCreationDate;
	}

	public void setMeanCreationDate(long meanCreationDate) {
		MeanCreationDate = meanCreationDate;
	}

}
