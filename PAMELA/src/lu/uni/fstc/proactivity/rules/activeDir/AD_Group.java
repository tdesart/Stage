package lu.uni.fstc.proactivity.rules.activeDir;

import java.util.Iterator;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_Group extends AbstractRule {
	private Iterator<SearchResult> listGroup;
	
	public AD_Group() {
		super();
		System.out.println(this.toString());
	}

	@Override
	protected void dataAcquisition() {
		this.listGroup = ((AbstractActiveDirWrapper) dataNativeSystem).search("OU=Groups,DC=uni,DC=lux","(&(objectClass=group))").iterator();
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
	}

	@Override
	protected boolean rulesGeneration() {
		createRule(new AD_Profil(this.listGroup,0));
		return true;
	}

	@Override
	public String toString() {
		return "AD_Group rule";
	}

}
