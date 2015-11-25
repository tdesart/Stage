package lu.uni.fstc.proactivity.rules.activeDir;

import java.util.Date;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.db.Profil;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_Analyse extends AbstractRule {
	private long count;
	private boolean finish;
	private Profil group;

	public AD_Analyse(Profil group) {
		super();
		this.group = group;
	}

	@Override
	protected void dataAcquisition() {
		// TODO Auto-generated method stub
		this.count = ((AbstractActiveDirWrapper) dataNativeSystem).countLines(this.group.getCn());
		this.finish = ((AbstractActiveDirWrapper) dataNativeSystem).getFinish(this.group.getCn()+"G");
		System.out.println(this.toString()+"\t");
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
		System.out.println(((AbstractActiveDirWrapper) dataNativeSystem).getManager(this.group.getCn()));
		this.group.setManager(((AbstractActiveDirWrapper) dataNativeSystem).getManager(this.group.getCn()));
		for(int i = 0 ; i<3; i++)
		 System.out.println(((AbstractActiveDirWrapper) dataNativeSystem).getTopGroup(this.group.getCn()+"G")[i]);
		this.group.setTopGroup(((AbstractActiveDirWrapper) dataNativeSystem).getTopGroup(this.group.getCn()+"G"));
		System.out.println(new Date(((AbstractActiveDirWrapper) dataNativeSystem).getMax(this.group.getCn())));
		this.group.setMostRecentCreationDate(((AbstractActiveDirWrapper) dataNativeSystem).getMax(this.group.getCn()));
		System.out.println(new Date(((AbstractActiveDirWrapper) dataNativeSystem).getMean(this.group.getCn())));
		this.group.setMeanCreationDate(((AbstractActiveDirWrapper) dataNativeSystem).getMean(this.group.getCn()));
		((AbstractActiveDirWrapper) dataNativeSystem).checkProfil(this.group);
	}

	@Override
	protected boolean rulesGeneration() {
		// TODO Auto-generated method stub
		if(super.getActivated()){
			createRule(new AD_DropTable(this.group.getCn()));
			createRule(new AD_DropTable(this.group.getCn()+"G"));
		} else {
			createRule(new AD_Analyse(this.group));
		}
		return true;
	}

	@Override
	public String toString() {
		return "\tAD_Analyse rule: "+count+" lines && "+this.finish;
	}

}
