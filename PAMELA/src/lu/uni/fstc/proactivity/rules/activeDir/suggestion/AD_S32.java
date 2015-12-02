package lu.uni.fstc.proactivity.rules.activeDir.suggestion;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_S32 extends AbstractRule {

	private String tableName;

	public AD_S32(String tableName) {
		super();
		this.tableName = tableName;
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
		((AbstractActiveDirWrapper) dataNativeSystem).dropTable(this.tableName);
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
		return "AD_S32";
	}

}
