package lu.uni.fstc.proactivity.rules.activeDir.mdp;

import java.util.Iterator;

import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;
import lu.uni.fstc.proactivity.rules.activeDir.AD_InsertInto;
import lu.uni.fstc.proactivity.rules.activeDir.AD_R001;

public class AD_M1 extends AbstractRule {

	private Iterator<SearchResult> listUser;

	public AD_M1(Iterator<SearchResult> listUser) {
		super();
		this.listUser = listUser;
		System.out.println(this.toString());
	}

	@Override
	protected void dataAcquisition() {

	}

	@Override
	protected boolean activationGuards() {
		return this.listUser.hasNext();
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
		if(super.getActivated()){
			for(int i = 0; i<500; i++){
				if(this.listUser.hasNext())
					createRule(new AD_M21(this.listUser.next()));
			}
			createRule(new AD_M1(this.listUser));
		}
		else 
			((AbstractActiveDirWrapper) dataNativeSystem).insertInto("Password30", "Finish", null, null);
		return true;
	}

	@Override
	public String toString() {
		return "\tAD_M1 rule";
	}
}
