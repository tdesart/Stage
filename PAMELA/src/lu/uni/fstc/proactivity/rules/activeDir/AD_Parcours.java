package lu.uni.fstc.proactivity.rules.activeDir;

import java.util.Iterator;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;
import lu.uni.fstc.proactivity.rules.activeDir.Delete.AD_D21;

public class AD_Parcours extends AbstractRule {
	private Iterator<SearchResult> listGroup;
	private int i;
	
	public AD_Parcours(Iterator<SearchResult> listGroup, int i) {
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
		if(super.getActivated()){
			for(int j = 0; j<5; j++){
				if(this.listGroup.hasNext()){
					SearchResult x = this.listGroup.next();
					createRule(new AD_D21(x));
					createRule(new AD_ProfilCreation(x,i+j));
				}
			}
			createRule(new AD_Parcours(this.listGroup,i+5));
		}

		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "AD_Profil rule";
	}

}
