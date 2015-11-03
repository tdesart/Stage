package lu.uni.fstc.proactivity.rules.activeDir;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRuleMessage;
import testJNDI.LDAPDate;
import testJNDI.Mail;

public class testMessageRule extends AbstractRuleMessage{
	
	private SearchResult user;

	@Override
	protected void dataAcquisition() {
		// TODO Auto-generated method stub
		NamingEnumeration<SearchResult> answer = ((AbstractActiveDirWrapper) dataNativeSystem).search1User("Test Ad");
		if (answer.hasMoreElements())
			this.user = answer.nextElement();
		
	}

	@Override
	protected boolean activationGuards() {
		// TODO Auto-generated method stub
		try {
			if(LDAPDate.filetimeToDate(Long.parseLong(user.getAttributes().get("pwdLastSet").get().toString())).after(LDAPDate.getDate("27/09/2015"))){
				System.out.println("yippie");
				return true;
			}
			
		} catch (NumberFormatException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@Override
	protected boolean conditions() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void actions() {
		// TODO Auto-generated method stub
		try {
			Mail mail = new Mail(user.getAttributes().get("mail").get().toString(),"test","test");
			System.out.println(user.getAttributes().get("mail").get().toString());
			mail.send();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		return null;
	}

}
