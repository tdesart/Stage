package lu.uni.fstc.proactivity.rules.activeDir.mdp;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_M21 extends AbstractRule {
	
	private SearchResult user;
	private int userAccountControl;
	private Date pwdLastSet;
	private GregorianCalendar calendar = new GregorianCalendar();
	private String mail;
	
	/** Difference between Filetime epoch and Unix epoch (in ms). */
	private static final long FILETIME_EPOCH_DIFF = 11644473600000L;

	/** One millisecond expressed in units of 100s of nanoseconds. */
	private static final long FILETIME_ONE_MILLISECOND = 10 * 1000;

	public AD_M21(SearchResult user){
		this.user = user;
	}
		
	@Override
	protected void dataAcquisition() {
		try {
			this.userAccountControl = Integer.parseInt(this.user.getAttributes().get("userAccountControl").get().toString());
			this.pwdLastSet = new Date((Long.parseLong(this.user.getAttributes().get("pwdLastSet").get().toString())/FILETIME_ONE_MILLISECOND) - FILETIME_EPOCH_DIFF);
			this.mail = this.user.getAttributes().get("distinguishedName").get().toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(this.mail);
			e.printStackTrace();
		}

	}

	@Override
	protected boolean activationGuards() {
		this.calendar.setTime(this.pwdLastSet);
		calendar.add(Calendar.DATE, 180);
		return calendar.getTime().before(new Date());
	}

	@Override
	protected boolean conditions() {
		// TODO Auto-generated method stub
		return (!((this.userAccountControl & 0x00010000) == 0x00010000) && !((this.userAccountControl & 2) == 2));
	}

	@Override
	protected void actions() {
		System.out.println("\t\t"+this.mail);

	}

	@Override
	protected boolean rulesGeneration() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\t\tAD_M21 rule";
	}

}
