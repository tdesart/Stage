package lu.uni.fstc.proactivity.rules.activeDir;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_G extends AbstractRule {
	
	private long currentTime;
	private long da;
	
	private AD_G(Long da){
		super();
		this.da = da;
		this.currentTime = System.currentTimeMillis();
	}
	
	public AD_G(){
		super();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			this.da = sdf2.parse(sdf.format(System.currentTimeMillis()+ 1000*60*60*24L)).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.currentTime = System.currentTimeMillis();
	}

	@Override
	protected void dataAcquisition() {
		// TODO Auto-generated method stub
		
	}
		

	@Override
	protected boolean activationGuards() {
		return (this.currentTime >= this.da);
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
		if(! getActivated()){
			createRule(new AD_G(this.da));
		}
		else{
			createRule(new AD_G(this.da + 1000*60*60*24*7L));
			createRule(new AD_Group());
		}
		return true;
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
