package lu.uni.fstc.proactivity.rules.activeDir;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_R002 extends AbstractRule {
	
	private SearchResult user;
	private String user_cn;
	private Boolean exception = false;
		
	public AD_R002(SearchResult user){
		super();
		this.user = user;
	}

	@Override
	protected void dataAcquisition() {
		try {
			this.user_cn = user.getAttributes().get("cn").get().toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.exception=true;
		}
		
	}

	@Override
	protected boolean activationGuards() {
		if(this.exception)
			return false;
		else
			return true;
	}

	@Override
	protected boolean conditions() {
		return true;
	}

	@Override
	protected void actions() {
		System.out.println(this.toString());
		((AbstractActiveDirWrapper) dataNativeSystem).insertIntoGroupe(this.user_cn, true);
	}

	@Override
	protected boolean rulesGeneration() {
		// TODO Auto-generated method stub
		if(!getActivated()){
			createRule(new AD_R003("An error occur"));
		}
		return true;
	}

	@Override
	public String toString() {
		return "\t\t\tAD_R002 rule: "+this.user_cn;
	}

}
