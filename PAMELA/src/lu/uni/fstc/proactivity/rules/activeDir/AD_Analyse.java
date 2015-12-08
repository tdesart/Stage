package lu.uni.fstc.proactivity.rules.activeDir;

import java.util.Date;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.db.Profil;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_Analyse extends AbstractRule {
	private long count;
	private boolean finish;
	private Profil group;
	private String tableName;

	public AD_Analyse(Profil group) {
		super();
		this.group = group;
		this.tableName = group.getNumber();
	}

	@Override
	protected void dataAcquisition() {
		// TODO Auto-generated method stub
		this.count = ((AbstractActiveDirWrapper) dataNativeSystem).countLines(this.tableName);
		this.finish = ((AbstractActiveDirWrapper) dataNativeSystem).getFinish(this.tableName + "G");
		System.out.println(this.toString() + "\t");
	}

	@Override
	protected boolean activationGuards() {
		return (count == this.group.getMembersSize() && this.finish);
	}

	@Override
	protected boolean conditions() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void actions() {
		if (this.group.getMembersSize() != 0) {
			this.group.setManager(((AbstractActiveDirWrapper) dataNativeSystem).getManager(this.tableName));
			System.out.println(this.group.getManager());
			this.group.setTopGroup(((AbstractActiveDirWrapper) dataNativeSystem).getTopGroup(this.tableName + "G"));
			for (int i = 0; i < 3; i++)
				System.out.println(this.group.getTopGroup()[i]);
			this.group.setMostRecentCreationDate(((AbstractActiveDirWrapper) dataNativeSystem).getMax(this.tableName));
			System.out.println(this.group.getMostRecentCreationDate());
			this.group.setMeanCreationDate(((AbstractActiveDirWrapper) dataNativeSystem).getMean(this.tableName));
			System.out.println(this.group.getMeanCreationDate());
			this.group.setRole(((AbstractActiveDirWrapper) dataNativeSystem).getRole(this.tableName));
			System.out.println(this.group.getRole());
			((AbstractActiveDirWrapper) dataNativeSystem).checkProfil(this.group);
		}
	}

	@Override
	protected boolean rulesGeneration() {
		// TODO Auto-generated method stub
		if (super.getActivated()) {
			createRule(new AD_DropTable(this.tableName));
			createRule(new AD_DropTable(this.tableName + "G"));
		} else {
			createRule(new AD_Analyse(this.group));
		}
		return true;
	}

	@Override
	public String toString() {
		return "\tAD_Analyse rule: " + count + " lines && " + this.finish;
	}

}
