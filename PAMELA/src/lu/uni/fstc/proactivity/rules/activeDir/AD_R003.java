package lu.uni.fstc.proactivity.rules.activeDir;

import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_R003 extends AbstractRule{
	private String message;
	
	public AD_R003(String message){
		super();
		this.message = message;
	}
	
	@Override
	protected void dataAcquisition() {
		// TODO Auto-generated method stub
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
		System.out.println(this.toString());
	}

	@Override
	protected boolean rulesGeneration() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\t\t\t\tAD_R003 rule created : "+message;
	}

}
