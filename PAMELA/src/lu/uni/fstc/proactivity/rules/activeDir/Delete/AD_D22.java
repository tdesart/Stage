package lu.uni.fstc.proactivity.rules.activeDir.Delete;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;
import lu.uni.fstc.proactivity.rules.activeDir.suggestion.Mail;

public class AD_D22 extends AbstractRule {
	private int size;
	private long count;

	public AD_D22(int size) {
		super();
		this.size = size;
		
	}

	@Override
	protected void dataAcquisition() {
		this.count = ((AbstractActiveDirWrapper) dataNativeSystem).countLines("Group_delete");

	}

	@Override
	protected boolean activationGuards() {
		return (this.count == this.size);
	}

	@Override
	protected boolean conditions() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void actions() {
		System.out.println(((AbstractActiveDirWrapper) dataNativeSystem).getGroupDelete());
		//new Mail("siu.ProactiveGroup@uni.lu","Suppression",((AbstractActiveDirWrapper) dataNativeSystem).getGroupDelete().send();
		new Mail("thomas.desart@ext.uni.lu","Suppression",((AbstractActiveDirWrapper) dataNativeSystem).getGroupDelete()).send();

	}

	@Override
	protected boolean rulesGeneration() {
		if(getActivated()){
			createRule(new AD_D32("Group_delete"));
		}
		else
			createRule(new AD_D22(this.size));
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}