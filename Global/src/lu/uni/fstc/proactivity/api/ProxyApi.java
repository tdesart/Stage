package lu.uni.fstc.proactivity.api;

import java.util.HashMap;
import java.util.Hashtable;

import lu.uni.fstc.proactivity.db.MoodleCacheDbWrapper;

/**
 * <b>Creates a list of queries and their respective results</b><br>
 * Results and queries are saved in a data structure, to prevent unnecessary DB connections.
 * 
 * @deprecated <br>
 * This class has been replaced by lu.uni.fstc.db.MoodleCacheDbWrapper,<br>
 * an implementation of a Singleton Proxy object to use a cache to store results from lu.uni.fstc.db access<p>
 *  
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011 
 * @version 2.0 - Sergio Marques Dias <br> 
 * @version 1.0 - Yann Milin
 * @see MoodleCacheDbWrapper#getInstance(String configFilePath)
 */

@Deprecated
public class ProxyApi {
	//extends API but NOT for now!!!
	private Hashtable<String, HashMap<String, Object>> htable;
//	private HashMap hmap;

	/**
	 * 
	 */
	public ProxyApi() {
		//super();
	}
	
	/**
	 * Gets the course of the user from the Moodle DB
	 * @param course_id
	 * @return null
	 */
	public String get_course(int course_id) {
		return null;
	}

	/**
	 * Gets the first name of the user from the Moodle DB
	 * @param user_id
	 * @return null
	 */
	public String get_firstName(int user_id) {
		return null;
	}

	/**
	 * Gets the name of the user from the Moodle DB
	 * @param user_id
	 * @return null
	 */
	public String get_name(int user_id) {
		return null;
	}
	
//---------------- DB QUERIES -----------------------
	
	/**
	 * @return htable
	 */
	public Hashtable<String, HashMap<String, Object>> getHtable() {
		return htable;
	}
	
	/**
	 * Clears the HashTable htable
	 */
	public void initHTable() {
		htable.clear();
	}
	
	/**
	 * Gets the status of the user whether he's connected or not
	 * @param user_id
	 * @param course_id
	 * @return false
	 */
	public boolean isConnectedToCourse(int user_id, int course_id) {
		return false;
	}
	
	/**
	 * @param htable
	 */
	public void setHtable(Hashtable<String, HashMap<String, Object>> htable) {
		this.htable = htable;
	}
}
