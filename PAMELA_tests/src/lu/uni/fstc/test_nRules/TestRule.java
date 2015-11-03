package lu.uni.fstc.test_nRules;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lu.uni.fstc.proactivity.utils.Global;

import lu.uni.fstc.proactivity.rules.AbstractRule;


/**
 * @author sandro.reis
 *
 */
public class TestRule extends AbstractRule{

	private String name;
	
	/**
	 * 
	 */
	public TestRule(){
	}
	
	/**
	 * @param s
	 */
	public TestRule(final String s){
		setName(s);
	}
	
	@Override
	protected void dataAcquisition(){
		
	}

	@Override
	protected boolean activationGuards() {
		return true;
	}

	@Override
	protected boolean conditions() {
		return true;
	}

	@Override
	protected void actions() {
		Global.logger.info(this.toString());
	}

	@Override
	protected boolean rulesGeneration() {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		final Date date = new Date();
		createRule(new TestRule("Remus " + dateFormat.format(date)));
		
		return false;
	}

	@Override
	public String toString() {
		return "TestRule " + this.getId() + " name: " + this.getName();
	}

	/**
	 * @return a String
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(final String name) {
		this.name = name;
	}
}