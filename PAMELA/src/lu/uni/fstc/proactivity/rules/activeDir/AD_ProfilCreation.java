package lu.uni.fstc.proactivity.rules.activeDir;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.db.Profil;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_ProfilCreation extends AbstractRule {
	private SearchResult group;
	private Profil profil;
	private int i;

	public AD_ProfilCreation(SearchResult group, int i) {
		super();
		this.group = group;
		this.i= i;
	}

	@Override
	protected void dataAcquisition() {

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
		this.profil = new Profil(this.group,i);
		this.profil.setMembers(((AbstractActiveDirWrapper) dataNativeSystem).search("OU=UNI-Users,DC=uni,DC=lux","(&(objectClass=person)(memberOf="+profil.getDistinguishedName()+"))"));

	}

	@Override
	protected boolean rulesGeneration() {
		if (getActivated()){
			createRule(new AD_CreateTable(this.profil.getNumber()));
			createRule(new AD_CreateTable(this.profil.getNumber()+"G"));
			createRule(new AD_CheckIfExists(this.profil));
		}
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
