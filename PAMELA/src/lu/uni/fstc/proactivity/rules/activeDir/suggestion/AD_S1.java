package lu.uni.fstc.proactivity.rules.activeDir.suggestion;

import java.util.ArrayList;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;
import lu.uni.fstc.proactivity.rules.activeDir.AD_CheckIfExists;

public class AD_S1 extends AbstractRule {

	private String tableName;
	private boolean tableExist;
	private ArrayList<SearchResult> listUser;

	public AD_S1(String tableName, ArrayList<SearchResult> listUser) {
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
			createRule(new AD_S21(this.listUser.iterator()));
			createRule(new AD_S22(this.listUser.size()));
		}
		else
			createRule(new AD_S1(this.tableName, this.listUser));
		return true;
	}

	@Override
	public String toString() {
		return "\tAD_S1: " + this.tableName + " -> " + this.tableExist;
	}
}
