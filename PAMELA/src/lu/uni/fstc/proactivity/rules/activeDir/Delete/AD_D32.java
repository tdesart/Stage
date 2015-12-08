package lu.uni.fstc.proactivity.rules.activeDir.Delete;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_D32 extends AbstractRule {

	private String tableName;

	public AD_D32(String tableName) {
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

	}

	@Override
	protected boolean rulesGeneration() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
