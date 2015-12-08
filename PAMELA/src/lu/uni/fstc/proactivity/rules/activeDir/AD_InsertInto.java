package lu.uni.fstc.proactivity.rules.activeDir;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TimeZone;

import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

import lu.uni.fstc.proactivity.db.AbstractActiveDirWrapper;
import lu.uni.fstc.proactivity.db.Profil;
import lu.uni.fstc.proactivity.rules.AbstractRule;

public class AD_InsertInto extends AbstractRule {
	
	private SearchResult user;
	private String manager;
	private String tableName;
	private Profil group;
	private ArrayList memberOf = null;
	private String endMessage = "";
	private String whenCreated;
	private String role;
		
	public AD_InsertInto(SearchResult user,Profil group){
		super();
		this.user = user;
		this.tableName = group.getNumber();
		this.group = group;
	}
	
	public AD_InsertInto(String endMessage, Profil group){
		super();
		this.group = group;
		this.endMessage  = endMessage;
		this.tableName = group.getNumber();
	}

	@Override
	protected void dataAcquisition() {
		try {
			if(endMessage.equals("")){
				this.whenCreated = user.getAttributes().get("whenCreated").get().toString();
				this.memberOf = Collections.list(user.getAttributes().get("memberOf").getAll());
				this.manager = user.getAttributes().get("manager").get().toString();
				this.role = user.getAttributes().get("extensionAttribute4").get().toString();
			}
		} catch (Exception e) {
			//e.printStackTrace();
			this.manager="noManager";
		}
		try {
			this.role = user.getAttributes().get("extensionAttribute4").get().toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.role="noRole";
		}
		
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		if(endMessage.equals("")){
			try {
				((AbstractActiveDirWrapper) dataNativeSystem).insertInto(this.tableName,this.manager,Long.toString(sdf.parse(this.whenCreated).getTime()),this.role);
			} catch (Exception e) {				
				e.printStackTrace();
			}
			if(this.memberOf != null){
				((AbstractActiveDirWrapper) dataNativeSystem).insertInto(this.tableName+"G",this.memberOf,this.group.getDistinguishedName());
			}
		}
		else {
			((AbstractActiveDirWrapper) dataNativeSystem).insertInto(this.tableName+"G",(String) this.endMessage,null,null);
		}
	}

	@Override
	protected boolean rulesGeneration() {
		return false;
	}

	@Override
	public String toString() {
		return null;
	}

}
