package lu.uni.fstc.proactivity.rules.activeDir;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import lu.uni.fstc.proactivity.rules.AbstractRule;

public class testRules001 extends AbstractRule {
	
	private long currentTime;
	private long da;
	
	private testRules001(Long da){
		super();
		this.da = da;
		this.currentTime = System.currentTimeMillis();
	}
	
	public testRules001(){
		super();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			this.da = sdf.parse("10/12/2015 11:59:00").getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.currentTime = System.currentTimeMillis();
	}

	@Override
	protected void dataAcquisition() {

		}
		

	@Override
	protected boolean activationGuards() {
		System.out.println(this.currentTime +"  "+this.da);
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
		System.out.println("test");		
	}

	@Override
	protected boolean rulesGeneration() {
		if(! getActivated()){
			createRule(new testRules001(this.da));
			System.out.println("not");
		}
		else{
			createRule(new testRules001(this.da + 1000*60*2));
			System.out.println("yes");
		}
		return true;
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return (Long.toString(this.currentTime));
	}

}
