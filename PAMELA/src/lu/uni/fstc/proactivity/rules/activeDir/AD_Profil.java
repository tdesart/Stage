package lu.uni.fstc.proactivity.rules.activeDir;

import java.util.Iterator;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_Profil extends AbstractRule {
	private Iterator<SearchResult> listGroup;
	private int i;
	
	public AD_Profil(Iterator<SearchResult> listGroup, int i) {
		super();
		this.listGroup = listGroup;
		this.i = i;
		System.out.println(this.toString());
	}

	@Override
	protected void dataAcquisition() {
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean activationGuards() {
		return this.listGroup.hasNext();
	}

	@Override
	protected boolean conditions() {
		return true;
	}

	@Override
	protected void actions() {
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean rulesGeneration() {
		if(getActivated()){
			createRule(new AD_ProfilCreation(this.listGroup.next(),i));
			createRule(new AD_Profil(this.listGroup,++i));
		}
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "AD_Profil rule";
	}

}
