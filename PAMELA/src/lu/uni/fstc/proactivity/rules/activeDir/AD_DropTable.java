package lu.uni.fstc.proactivity.rules.activeDir;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_DropTable extends AbstractRule {
	String tableName;

	public AD_DropTable(String tableName) {
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
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
		return "\t\tAD_DropTable rule: "+this.tableName;
	}

}
