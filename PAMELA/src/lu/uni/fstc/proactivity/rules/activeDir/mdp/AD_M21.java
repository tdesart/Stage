package lu.uni.fstc.proactivity.rules.activeDir.mdp;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.rules.AbstractRule;
import lu.uni.fstc.proactivity.rules.activeDir.suggestion.Mail;

public class AD_M21 extends AbstractRule {
	
	private SearchResult user;
	private int userAccountControl;
	private Date pwdLastSet;
	private GregorianCalendar calendar = new GregorianCalendar();
	private String mail;
	private long accountExpires;
	private String displayName;
	
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
			this.accountExpires = Long.parseLong(this.user.getAttributes().get("accountExpires").get().toString());
			this.displayName = this.user.getAttributes().get("displayName").get().toString();
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
		return (calendar.getTimeInMillis()<(System.currentTimeMillis()) && (this.accountExpires > System.currentTimeMillis() || this.accountExpires == 0));
	}

	@Override
	protected boolean conditions() {
		// TODO Auto-generated method stub
		return (!((this.userAccountControl & 0x00010000) == 0x00010000) && !((this.userAccountControl & 2) == 2));
	}

	@Override
	protected void actions() {
		Long today = System.currentTimeMillis();
		Long pwd = calendar.getTimeInMillis();
		if((pwd + 1000*60*60*24*14) > today ){
			System.out.println("\t\t"+this.mail+" moins de 30 jours");
			String s = makeText(this.displayName ,30);
			new Mail("thomas.desart@ext.uni.lu", "Password expiration", s).send();
		}
		else if ((pwd + 1000*60*60*24*36) > today){
			System.out.println("\t\t"+this.mail+" moins de 15 jours");
			String s = makeText(this.displayName ,15);
			new Mail("thomas.desart@ext.uni.lu", "Password expiration", s).send();
		}
		else if ((pwd + 1000*60*60*24*39) > today){
			System.out.println("\t\t"+this.mail+" moins de 3 jours");
			String s = makeText(this.displayName ,3);
			new Mail("thomas.desart@ext.uni.lu", "Password expiration", s).send();
		}
		else if ((pwd + 1000*60*60*24*40) > today){
			System.out.println("\t\t"+this.mail+" moins de 1 jours");
			String s = makeText(this.displayName ,1);
			new Mail("thomas.desart@ext.uni.lu", "Password expiration", s).send();
		}
		else
			System.out.println("\t\t"+this.mail+" expiré");

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
	
	private String makeText(String name, int day){
		String s = "Dear "+this.displayName+", <br><br>"
				+ "Your password will expired within "+day+" days, we ask you to change it.<br><br>"
				+ "Best regards.";
		return s;
	}
}
