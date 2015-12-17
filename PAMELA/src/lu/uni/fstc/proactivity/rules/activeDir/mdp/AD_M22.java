package lu.uni.fstc.proactivity.rules.activeDir.mdp;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;
import lu.uni.fstc.proactivity.rules.activeDir.AD_DropTable;
import lu.uni.fstc.proactivity.rules.activeDir.suggestion.Mail;

public class AD_M22 extends AbstractRule {

	private boolean finish;

	public AD_M22(){
		super();
	}
	
	@Override
	protected void dataAcquisition() {
		this.finish = ((AbstractActiveDirWrapper) dataNativeSystem).getFinish("Password30");

	}

	@Override
	protected boolean activationGuards() {
		// TODO Auto-generated method stub
		return this.finish;
	}

	@Override
	protected boolean conditions() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void actions() {
		// TODO Auto-generated method stub
		System.out.println("mail recap sent");
		new Mail("siu.ProactiveGroup@uni.lu", "Password Expiry Notification", ((AbstractActiveDirWrapper) dataNativeSystem).getExpiredPassword()).send();
	}

	@Override
	protected boolean rulesGeneration() {
		if(getActivated())
			createRule(new AD_DropTable("Password30"));
		else
			createRule(new AD_M22());
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
