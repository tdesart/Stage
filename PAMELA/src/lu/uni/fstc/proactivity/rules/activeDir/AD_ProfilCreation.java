package lu.uni.fstc.proactivity.rules.activeDir;

import java.util.ArrayList;
import java.util.Iterator;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.db.Profil;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_ProfilCreation extends AbstractRule {
	private String cnGroup;
	private Iterator<SearchResult> group;
	private Profil profil;
	

	public AD_ProfilCreation(String cnGroup) {
		super();
		this.cnGroup = cnGroup;
	}

	@Override
	protected void dataAcquisition() {
		this.group = ((AbstractActiveDirWrapper) dataNativeSystem).search("OU=SecurityGroups,OU=Groups,DC=uni,DC=lux","(&(objectClass=group)(CN="+cnGroup+"))").iterator();
	}

	@Override
	protected boolean activationGuards() {
		// TODO Auto-generated method stub
		return group.hasNext();
	}

	@Override
	protected boolean conditions() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void actions() {
		this.profil = new Profil(this.group.next(),cnGroup);
		this.profil.setMembers(((AbstractActiveDirWrapper) dataNativeSystem).search("OU=UNI-Users,DC=uni,DC=lux","(&(objectClass=person)(memberOf="+profil.getDistinguishedName()+"))"));

	}

	@Override
	protected boolean rulesGeneration() {
		if (getActivated()){
			createRule(new AD_CreateTable(this.profil.getCn()));
			createRule(new AD_CreateTable(this.profil.getCn()+"G"));
			createRule(new AD_Analyse(this.profil));
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
