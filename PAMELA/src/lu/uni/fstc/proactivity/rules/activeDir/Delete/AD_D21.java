package lu.uni.fstc.proactivity.rules.activeDir.Delete;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_D21 extends AbstractRule {

	private SearchResult group;
	private String groupName;
	private ArrayList<SearchResult> listMember;
	private ArrayList<SearchResult> listMemberDisabled;
	private ArrayList<SearchResult> listMemberAccountExpires;
	private int totalMembers;
	private long whenChanged;
	private String mail;

	public AD_D21(SearchResult group) {
		super();
		this.group = group;

	}

	@Override
	protected void dataAcquisition() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			this.groupName = this.group.getAttributes().get("distinguishedName").get().toString();
			this.whenChanged = sdf.parse(this.group.getAttributes().get("whenChanged").get().toString()).getTime();
			this.mail = this.group.getAttributes().get("mail").get().toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.mail = "";
		}
		this.listMember = ((AbstractActiveDirWrapper) dataNativeSystem).search("OU=UNI-Users,DC=uni,DC=lux","(&(objectClass=person)(memberOf="+this.groupName+")(!(userAccountControl:1.2.840.113556.1.4.803:=2)))");
		this.listMemberDisabled = ((AbstractActiveDirWrapper) dataNativeSystem).search("OU=UNI-Users,DC=uni,DC=lux","(&(objectClass=person)(memberOf="+this.groupName+")(userAccountControl:1.2.840.113556.1.4.803:=2))");
		this.listMemberDisabled.addAll(((AbstractActiveDirWrapper) dataNativeSystem).search("OU=UNI-DisabledUsers,DC=uni,DC=lux","(&(objectClass=person)(memberOf="+this.groupName+"))"));
		this.listMemberAccountExpires = ((AbstractActiveDirWrapper) dataNativeSystem).search("OU=UNI-Users,DC=uni,DC=lux","(&(objectClass=person)(memberOf="+this.groupName+")(accountExpires<="+Long.toString(new Date().getTime())+")(!(accountExpires=0)))");
		this.totalMembers = listMember.size() + listMemberDisabled.size() + listMemberAccountExpires.size();
	}

	@Override
	protected boolean activationGuards() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected boolean conditions() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void actions() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.YEAR, -5);
		if(this.totalMembers == 0) {
			((AbstractActiveDirWrapper) dataNativeSystem).insertInto(this.groupName, true, "No Members");
		}
		else if((((double) this.listMemberDisabled.size()+(double) this.listMemberAccountExpires.size())/ (double) this.totalMembers) >= 0.7) {
			((AbstractActiveDirWrapper) dataNativeSystem).insertInto(this.groupName, true, (double) this.listMemberDisabled.size()/(double) this.totalMembers*100+"% of members disabled or have an expired account");
		}
		else if(this.whenChanged < cal.getTimeInMillis()){
			((AbstractActiveDirWrapper) dataNativeSystem).insertInto(this.groupName, true, "No modification since "+new Date(this.whenChanged));
		}
		else if(!this.mail.equals("") && false){
			//DO SOMETHING
			((AbstractActiveDirWrapper) dataNativeSystem).insertInto(this.groupName, true, "No mail sent since "+new Date());
		}
		else {
			((AbstractActiveDirWrapper) dataNativeSystem).insertInto(this.groupName, false, "");
		}

	}

	@Override
	protected boolean rulesGeneration() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
