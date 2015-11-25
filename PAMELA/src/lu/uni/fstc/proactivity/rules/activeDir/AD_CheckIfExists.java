package lu.uni.fstc.proactivity.rules.activeDir;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.db.Profil;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_CheckIfExists extends AbstractRule {
	private String tableName;
	private boolean tableExist;
	private boolean tableGExist;
	private Profil group;

	public AD_CheckIfExists(Profil group) {
		super();
		this.group = group;
		this.tableName = group.getCn();
	}

	@Override
	protected void dataAcquisition() {
		this.tableExist = ((AbstractActiveDirWrapper) dataNativeSystem).tableExist(this.group.getCn());
		this.tableGExist = ((AbstractActiveDirWrapper) dataNativeSystem).tableExist(this.group.getCn()+"G");
		System.out.println(this.toString());

	}

	@Override
	protected boolean activationGuards() {
		return tableExist && tableGExist;
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
		if(getActivated()){
//			ArrayList<SearchResult> temp = new ArrayList<SearchResult>();
//			for(int i = 0;i<listUser.size();i++){
//				temp.add(this.listUser.get(i));
//				if(temp.size()==200){
//					createRule(new AD_R001(temp.iterator(),this.tableName));
//					temp = new ArrayList<SearchResult>();
//				}
//			}
			createRule(new AD_R001(this.group));
		}
		else
			createRule(new AD_CheckIfExists(this.group));
		return true;
	}

	@Override
	public String toString() {
		return "\tAD_CheckIfExists: " + this.tableName + " -> " + this.tableExist;
	}

}
