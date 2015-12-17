package lu.uni.fstc.proactivity.rules.activeDir.mdp;

import java.util.ArrayList;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_M0 extends AbstractRule {

	private ArrayList<SearchResult> listUser;

	public AD_M0() {
		super();
		System.out.println(this.toString());
	}

	@Override
	protected void dataAcquisition() {
		this.listUser = ((AbstractActiveDirWrapper) dataNativeSystem).search("OU=UNI-Users,DC=uni,DC=lux","(&(objectClass=person)(mail=*)(pwdLastSet=*)(!(pwdLastSet=0)))");
		System.out.println(this.listUser.size());

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
		((AbstractActiveDirWrapper) dataNativeSystem).createTable("Password30");
		((AbstractActiveDirWrapper) dataNativeSystem).createTable("Password");
		((AbstractActiveDirWrapper) dataNativeSystem).createTable("password_historic");
	}

	@Override
	protected boolean rulesGeneration() {
		createRule(new AD_M1(this.listUser.iterator()));
		createRule(new AD_M22());
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "AD_M0 rule";
	}

}
