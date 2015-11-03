package lu.uni.fstc.proactivity.rules.activeDir;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;
import lu.uni.fstc.proactivity.rules.RuleType;

public class testRules001 extends AbstractRule {
	
	private String message;
	private int number;
	private String distinguishedName;
	/**
	 * Default constructor, mandatory for Hibernate to build object
	 */
	private testRules001(){
		setType(RuleType.SCENARIO);
	}
	
	public testRules001(String m, int i){
		this();
		this.message = m;
		this.number = i;
	}

	@Override
	protected void dataAcquisition() {
		// TODO Auto-generated method stub
		NamingEnumeration<SearchResult> answer = ((AbstractActiveDirWrapper) dataNativeSystem).search1User("DEV1 User");
		try {
			this.distinguishedName = answer.next().getAttributes().get("distinguishedName").get().toString();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	protected boolean activationGuards() {
		// TODO Auto-generated method stub
		if (number<10) 	
			return true;
		else
			return false;
	}

	@Override
	protected boolean conditions() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void actions() {
		// TODO Auto-generated method stub
		System.out.println(this.message+" "+this.number+" "+distinguishedName);
		
	}

	@Override
	protected boolean rulesGeneration() {
		// TODO Auto-generated method stub
		if(super.getActivated())
			createRule(new testRules001("iteration", ++this.number));
		else {
			System.out.println("The end!");
		}
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
