package lu.uni.fstc.proactivity.rules.activeDir.suggestion;

import java.util.Iterator;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_S21 extends AbstractRule {

	private Iterator<SearchResult> listUser;
	
	public AD_S21(Iterator<SearchResult> listUser) {
		super();
		this.listUser = listUser;
	}

	@Override
	protected void dataAcquisition() {
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean activationGuards() {
		return this.listUser.hasNext();
	}

	@Override
	protected boolean conditions() {
		return true;
	}

	@Override
	protected void actions() {
		System.out.println(this.toString());
	}

	@Override
	protected boolean rulesGeneration() {
		if(getActivated()){
			createRule(new AD_S31(this.listUser.next()));
			createRule(new AD_S21(this.listUser));
		}
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "AD_S21 rule";
	}

}