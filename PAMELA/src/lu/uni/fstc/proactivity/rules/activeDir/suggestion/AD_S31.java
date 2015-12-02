package lu.uni.fstc.proactivity.rules.activeDir.suggestion;

import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_S31 extends AbstractRule{
	private SearchResult user;
	private String userManager;
	private String userName;

	public AD_S31(SearchResult user) {
		super();
		this.user = user;
	}
		
	@Override
	protected void dataAcquisition() {
		try {
			this.userManager = (String) user.getAttributes().get("manager").get();
			this.userName = (String) user.getAttributes().get("distinguishedName").get();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		((AbstractActiveDirWrapper) dataNativeSystem).findSuggestion(this.userManager,this.userName);
		
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
