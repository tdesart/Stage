package lu.uni.fstc.proactivity.rules.moodleCoPs;

/**
 * @author sandro.reis
 *
 */
public class ModInfo {
	
	private String modinfo, tempString, mod;
	private int stdClass;
	private final String cm, a2, a4, a6, a8, a10, a12,
			a14, a16, a18, a20, a22, a24, a26;
	private final String 	a1 = "s:2:\"id\";", 
					a3 = "s:2:\"cm\";", 
					a5 = "s:3:\"mod\";",
					a7 = "s:7:\"section\";",
					a9 = "s:8:\"idnumber\";",
					a11 = "s:7:\"visible\";",
					a13 = "s:9:\"groupmode\";",
					a15 = "s:10:\"groupingid\";",
					a17	= "s:16:\"groupmembersonly\";",
					a19 = "s:6:\"indent\";",
					a21 = "s:10:\"completion\";",
					a23 = "s:5:\"extra\";",
					a25 = "s:4:\"name\";",
					a27 = "s:4:\"icon\";s:5:\"f/pdf\";";
			
	/**
	 * @param modinfo
	 * @param id
	 * @param stdClass 
	 * @param cm
	 * @param mod
	 * @param section
	 * @param groupingid
	 * @param name
	 */
	public ModInfo(String modinfo, final String id, final int stdClass, final String cm, final String mod, final String section, final String groupingid, final String name){
		modinfo = modinfo.replaceAll("\"", "\\\"");
		modinfo = modinfo.replaceAll("'", "\\'");

		this.cm = cm;
		this.modinfo = modinfo;
		this.stdClass = stdClass;
		this.mod = mod;

		a2 = "s:"+id.length()+":\""+Long.parseLong(id)+"\";";
		a4 = "s:"+cm.length()+":\""+Long.parseLong(cm)+"\";";
		a6 = "s:"+mod.length()+":\""+mod+"\";";
		a8 = "s:"+section.length()+":\""+Integer.parseInt(section)+"\";";
		a10 = "N;";
		a12 = "s:1:\"1\";";
		a14 = "s:1:\"0\";";
		a16 = "s:"+groupingid.length()+":\""+Long.parseLong(groupingid)+"\";";
		a18 = "s:1:\"1\";";
		a20 = "s:1:\"0\";";
		a22 = "s:1:\"0\";";
		a24 = "s:0:\"\";";
		a26 = "s:"+name.length()+":\""+name+"\";";
		
		buildFirstString();
		buildFinalString();
	}
	
	private void buildFirstString(){
		if(mod.equals("resource"))
			tempString = "i:"+ Long.parseLong(cm) +";O:8:\"stdClass\":"+ stdClass +":{"+ a1+ a2 + a3+ a4+ a5 + a6 + a7 + a8 + a9 + a10 
			+ a11 + a12 + a13+ a14 + a15 + a16 + a17 + a18 + a19 + a20 + a21 + a22 + a23 + a24 + a27 + a25 + a26 + "}";
		else if(mod.equals("forum") || mod.equals("chat"))
			tempString = "i:"+ Long.parseLong(cm) +";O:8:\"stdClass\":"+ stdClass +":{"+ a1+ a2 + a3+ a4+ a5 + a6 + a7 + a8 + a9 + a10 
			+ a11 + a12 + a13+ a14 + a15 + a16 + a17 + a18 + a19 + a20 + a21 + a22 + a23 + a24 + a25 + a26 + "}";
		else if(mod.equals("folder"))
			tempString = "i:"+ Long.parseLong(cm) +";O:8:\"stdClass\":"+ stdClass +":{"+ a1+ a2 + a3+ a4+ a5 + a6 + a7 + a8 + a9 + a10 
			+ a11 + a12 + a13+ a14 + a15 + a16 + a17 + a18 + a19 + a20 + a21 + a22 + a23 + a24 + a25 + a26 + "}";
	}
	
	private void buildFinalString(){
		final String findStr = "stdClass";
		int lastIndex = 0;
		int count =0;

		while(lastIndex != -1){

		       lastIndex = modinfo.indexOf(findStr,lastIndex);

		       if( lastIndex != -1){
		             count ++;
		             lastIndex+=findStr.length();
		      }
		}
		// added to get rid of the '{}'
		modinfo = modinfo.replace("{}", "{");
		modinfo = modinfo.replaceFirst(""+count, ""+(count+1));
		modinfo = modinfo.replace("}}", "}");
		modinfo = modinfo.concat(tempString+"}");	
	}
	
	/**
	 * @return a String
	 */
	public String getModinfo(){
		return modinfo;
	}
}
