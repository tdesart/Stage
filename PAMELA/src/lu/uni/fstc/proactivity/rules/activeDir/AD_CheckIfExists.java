package lu.uni.fstc.proactivity.rules.activeDir;

import java.util.ArrayList;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_CheckIfExists extends AbstractRule {
	private String tableName;
	private boolean tableExist;
	private ArrayList<SearchResult> listUser;
	// private Iterator<SearchResult> listUser;

	public AD_CheckIfExists(String tableName, ArrayList<SearchResult> listUser) {// Iterator<SearchResult> listUser){
		super();
		this.tableName = tableName;
		this.listUser = listUser;
	}

	@Override
	protected void dataAcquisition() {
		this.tableExist = ((AbstractActiveDirWrapper) dataNativeSystem).tableExist(this.tableName);
		System.out.println(this.toString());

	}

	@Override
	protected boolean activationGuards() {
		return tableExist;
	}

	@Override
	protected boolean conditions() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void actions() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean rulesGeneration() {
		if(getActivated()){
			ArrayList<SearchResult> temp = new ArrayList<SearchResult>();
			for(int i = 0;i<1000;i++){
				temp.add(this.listUser.get(i));
				if(temp.size()==1){
					createRule(new AD_R001(temp.iterator()));
					temp = new ArrayList<SearchResult>();
				}
			}
			createRule(new AD_R001(temp.iterator()));
		}
		else
			createRule(new AD_CheckIfExists(this.tableName, this.listUser));
		return true;
	}

	@Override
	public String toString() {
		return "\tAD_CheckIfExists: " + this.tableName + " -> " + this.tableExist;
	}

}
