package lu.uni.fstc.proactivity.rules.activeDir.suggestion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_S0 extends AbstractRule {
	private ArrayList<SearchResult> listUser;
	private String day;
	private SimpleDateFormat ldapDateFormat = new SimpleDateFormat("yyyyMMdd000000");
	
	public AD_S0() {
		super();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -1);
		this.day = this.ldapDateFormat.format(calendar.getTime());
	}

	@Override
	protected void dataAcquisition() {
		this.listUser = ((AbstractActiveDirWrapper) dataNativeSystem).search("OU=UNI-Users,DC=uni,DC=lux","(&(objectClass=person)(whenCreated>="+this.day+".OZ)(manager=*))");
	}

	@Override
	protected boolean activationGuards() {
		// TODO Auto-generated method stub
		return (this.listUser.size()>0);
	}

	@Override
	protected boolean conditions() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void actions() {
		((AbstractActiveDirWrapper) dataNativeSystem).createTableSuggestion();

	}

	@Override
	protected boolean rulesGeneration() {
		if(getActivated())
			createRule(new AD_S1("Suggestion",this.listUser));
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "AD_S0 rule";
	}

}
