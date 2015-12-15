package lu.uni.fstc.proactivity.rules.activeDir;

import java.util.Iterator;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.Profil;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_R001 extends AbstractRule {

	private Iterator<SearchResult> listUser;
	private Profil group;
	
	public AD_R001(){
		super();
	}
	
	public AD_R001(Iterator<SearchResult> listUser) {
		this();
		this.listUser = listUser;
	}
	
	public AD_R001(Iterator<SearchResult> listUser, Profil group) {
		this();
		this.listUser = listUser;
		this.group = group;
	}
	
	public AD_R001(Profil group){
		this();
		this.listUser = group.getMembers().iterator();
		this.group = group;
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
			for(int i = 0; i<100; i++){
				if(this.listUser.hasNext())
					createRule(new AD_InsertInto(this.listUser.next(),this.group));
			}
			createRule(new AD_R001(this.listUser, this.group));
		}
		else
			createRule(new AD_InsertInto("Finish", this.group));
		return true;
	}

	@Override
	public String toString() {
		return "\t\tAD_R001 rule";
	}

}
