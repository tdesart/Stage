package lu.uni.fstc.proactivity.rules.activeDir;

import java.util.ArrayList;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_R1 extends AbstractRule {

	private /*Iterator*/ArrayList<SearchResult> listUser;
	private int listSize;

	public AD_R1(){
		super();
	}
	
	
	@Override
	protected void dataAcquisition() {
		ArrayList<SearchResult> list = ((AbstractActiveDirWrapper) dataNativeSystem).searchUsers();
		this.listSize = list.size();
		this.listUser = list;//.iterator();
	}

	@Override
	protected boolean activationGuards() {
		return true;
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
		createRule(new AD_CreateTableGroup());
		createRule(new AD_CheckIfExists("Groupe", this.listUser));
		createRule(new AD_Analyse(1000));//this.listSize));
		return true;
	}

	@Override
	public String toString() {
		return "AD_R1 rule "+this.listSize +" elements";
	}

}
