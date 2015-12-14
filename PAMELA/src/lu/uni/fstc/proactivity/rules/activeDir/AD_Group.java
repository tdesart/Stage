package lu.uni.fstc.proactivity.rules.activeDir;

import java.util.ArrayList;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;
import lu.uni.fstc.proactivity.rules.activeDir.Delete.AD_D22;

public class AD_Group extends AbstractRule {
	private ArrayList<SearchResult> listGroup;
	
	public AD_Group() {
		super();
		System.out.println(this.toString());
	}

	@Override
	protected void dataAcquisition() {
		this.listGroup = ((AbstractActiveDirWrapper) dataNativeSystem).search("OU=Groups,DC=uni,DC=lux","(&(objectClass=group))");
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
		((AbstractActiveDirWrapper) dataNativeSystem).cleanDb();
		((AbstractActiveDirWrapper) dataNativeSystem).createTableGroup();
	}

	@Override
	protected boolean rulesGeneration() {
		createRule(new AD_Parcours(this.listGroup.iterator(),0));
		createRule(new AD_D22(this.listGroup.size()));
		return true;
	}

	@Override
	public String toString() {
		return "AD_Group rule";
	}

}
