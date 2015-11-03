package lu.uni.fstc.proactivity.rules.activeDir;

import java.util.Iterator;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_R001 extends AbstractRule {

	private Iterator<SearchResult> listUser;
	
	public AD_R001(){
		super();
	}
	
	public AD_R001(Iterator<SearchResult> listUser) {
		this();
		this.listUser = listUser;
	}
	
	@Override
	protected void dataAcquisition() {
		// TODO Auto-generated method stub
//		if (this.listUser == null) 
//			this.listUser = ((AbstractActiveDirWrapper) dataNativeSystem).searchUsers().iterator();
	}

	@Override
	protected boolean activationGuards() {
		return this.listUser.hasNext();
	}

	@Override
	protected boolean conditions() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void actions() {
		// TODO Auto-generated method stub
		System.out.println(this.toString());

	}

	@Override
	protected boolean rulesGeneration() {
		// TODO Auto-generated method stub
		if(super.getActivated()){
			for(int i = 0; i<1000; i++){
				if(this.listUser.hasNext())
					createRule(new AD_R002(this.listUser.next()));
			}
			createRule(new AD_R001(this.listUser));
		}
		return true;
	}

	@Override
	public String toString() {
		return "\t\tAD_R001 rule";
	}

}
