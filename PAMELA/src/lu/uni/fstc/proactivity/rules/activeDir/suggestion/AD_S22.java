package lu.uni.fstc.proactivity.rules.activeDir.suggestion;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_S22 extends AbstractRule {
	private int size;
	private long count;

	public AD_S22(int size) {
		super();
		this.size = size;
		
	}

	@Override
	protected void dataAcquisition() {
		this.count = ((AbstractActiveDirWrapper) dataNativeSystem).countSuggestion();

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
		new Mail("siu.ProactiveGroup@uni.lu","Suggestion","<b>"+this.count+"</b> user(s) have been created : <br>"+((AbstractActiveDirWrapper) dataNativeSystem).getSuggestion()).send();
		
	}

	@Override
	protected boolean rulesGeneration() {
		if(getActivated()){
			createRule(new AD_S32("Suggestion"));
		}
		else
			createRule(new AD_S22(this.size));
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
