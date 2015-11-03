package lu.uni.fstc.proactivity.rules.activeDir;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_Analyse extends AbstractRule {
	private int listSize;
	private long count;
	
	public AD_Analyse(int listSize) {
		// TODO Auto-generated constructor stub
		this.listSize = listSize;
	}

	@Override
	protected void dataAcquisition() {
		// TODO Auto-generated method stub
		this.count = ((AbstractActiveDirWrapper) dataNativeSystem).countLines("Groupe");
		System.out.println(this.toString()+"\t");

	}

	@Override
	protected boolean activationGuards() {
		return (count == this.listSize);
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
		// TODO Auto-generated method stub
		if(super.getActivated()){
			createRule(new AD_DropTable("Groupe"));
		} else {
			createRule(new AD_Analyse(this.listSize));
		}
		return true;
	}

	@Override
	public String toString() {
		return "\tAD_Analyse rule: "+count+" lines";
	}

}
