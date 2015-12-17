package lu.uni.fstc.proactivity.rules.activeDir.mdp;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
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
	private String location;
	private String dn;
	private String manager;
	private long m;
	
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
			this.mail = this.user.getAttributes().get("mail").get().toString();
			this.dn = this.user.getAttributes().get("distinguishedName").get().toString();
			this.accountExpires = Long.parseLong(this.user.getAttributes().get("accountExpires").get().toString());
			this.displayName = this.user.getAttributes().get("displayName").get().toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		this.m = ((AbstractActiveDirWrapper) dataNativeSystem).getFrom("Password", this.dn);
	}

	@Override
	protected boolean activationGuards() {
		this.calendar.setTime(this.pwdLastSet);
		calendar.add(Calendar.DATE, 180);
		if(calendar.getTimeInMillis()<(System.currentTimeMillis()) && (this.accountExpires > System.currentTimeMillis() || this.accountExpires == 0))
			return true;
		else{
			((AbstractActiveDirWrapper) dataNativeSystem).deleteFrom("Password", this.dn);
			return false;
		}
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
		if((pwd + 1000*60*60*24*14L) > today ){
			String s = makeText(this.displayName ,30);
			if(this.m !=1){
				System.out.println("\t\t"+this.mail+" moins de 30 jours");
			 	((AbstractActiveDirWrapper) dataNativeSystem).insertInto("Password", this.dn, "1");
				//new Mail(this.mail, "Password Expiry Notification - First Reminder", s).send();
				new Mail("siu.ProactiveGroup@uni.lu", "Password Expiry Notification - First Reminder", s).send();
			}	
		}
		else if ((pwd + 1000*60*60*24*26L) > today){
			String s = makeText(this.displayName ,15);
			if(this.m != 2){
				System.out.println("\t\t"+this.mail+" moins de 15 jours");
				((AbstractActiveDirWrapper) dataNativeSystem).insertInto("Password", this.dn, "2");
				//new Mail(this.mail, "Password Expiry Notification - Second Reminder", s).send();
				new Mail("siu.ProactiveGroup@uni.lu", "Password Expiry Notification - Second Reminder", s).send();
			 }
		}
		else if ((pwd + 1000*60*60*24*29L) > today){
			String s = makeText(this.displayName ,3);
			if(this.m != 3){
				System.out.println("\t\t"+this.mail+" moins de 3 jours");
				((AbstractActiveDirWrapper) dataNativeSystem).insertInto("Password", this.dn, "3");
				//new Mail(this.mail, "Password Expiry Notification - Second Reminder", s).send();
				new Mail("siu.ProactiveGroup@uni.lu", "Password Expiry Notification - Third Reminder", s).send();
			}
		}
		else if ((pwd + 1000*60*60*24*30L) > today){
			String s = makeText(this.displayName ,1);
			try {
				this.manager = this.user.getAttributes().get("manager").get().toString();
				this.manager = ((AbstractActiveDirWrapper) dataNativeSystem).search(this.manager,"(&(objectClass=person))").get(0).getAttributes().get("mail").get().toString();
				System.out.println(this.manager);
			} catch (Exception e) {
				this.manager = "";
			}
			try {
				this.location = this.user.getAttributes().get("l").get().toString();
			} catch (Exception e) {
				this.location="";
			}
			this.location= this.getSIULoc(this.location);
			if(this.m != 4){
				System.out.println("\t\t"+this.mail+" moins de 1 jours");
				((AbstractActiveDirWrapper) dataNativeSystem).insertInto("Password", this.dn, "4");
				new Mail("siu.ProactiveGroup@uni.lu", "Password Expiry Notification - Fourth Reminder", s).send();
				//new Mail(this.mail, "Password Expiry Notification - Fourth Reminder", s).send(this.manager,this.location);
			}
		}
		else if (pwd + 1000*60*60*24*60L < today){
			((AbstractActiveDirWrapper) dataNativeSystem).insertInto("Password30", this.dn, null, "expired since "+((today -(pwd+1000*60*60*24*30L))/(1000*60*60*24L))+" days");
		}

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
		String s = "Dear "+this.displayName+", <br><br>";
		s+= "Your Active Directoy (Network) Login password will expire within "+day+" days.<br><br>";
		s+= "Please change it before this date or you will need to visit your SIU local unit before you will be able to access a computer/your email again.<br><br>";
		s+= "You can change your password from every networked computer connected to the Active Directory system (e.g. the computer provided to you or the ones in the classrooms). Alternatively you can use the Outlook web access interface (https://owa.uni.lu). Please go to Options->Change your password  to update your current password.<br><br>";
		s+= "Once you have changed your password it will be good for 210 days.<br><br>";
		if(day == 3)
			s+= "Your manager and the local SIU team will be inform in the next reminder, which is one day before expiration.<br><br>";
		s+= "Best regards,<br><br>";
		s+= "Your SIU Team<br><br>";
		s+= "This is an automatic email which is send to all users when their password is about to expire.";
		return s;
	}
	
	private String getSIULoc(String location){
		switch (location){
			case "Limpertsberg": 
				return "siu.cl@uni.lu";
			case "Belval": 
				return "siu.belval@uni.lu";
			case "Kirchberg": 
				return "siu.ck@uni.lu";
			case "Kirchberg K2": 
				return "siu.ck@uni.lu";
			case "Weicker": 
				return "siu.cl@uni.lu";
			default:
				return "siu@uni.lu";
		}
	}
}
