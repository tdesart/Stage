package lu.uni.fstc.proactivity.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;

/**
 * 
 * @author thomas.desart
 *
 */
public final class ActiveDirOperations {
	
	private static ActiveDirOperations theInstance = null;
	
	private  ActiveDirOperations() {
		// TODO Auto-generated constructor stub
	}
	
	public static synchronized ActiveDirOperations getInstance () {
		if (theInstance == null) {
			theInstance = new ActiveDirOperations();
		}
		return theInstance;
	}
	
	private LdapContext getContext(){
		Hashtable<String, String> env = new Hashtable<String,String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldaps://duchesse.uni.lux:636");//"ldap://192.168.60.2:389");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "thomas.desart@uni.lux");//"cn=Administrator,cn=Users,dc=demo,dc=com");
		env.put(Context.SECURITY_CREDENTIALS,"yfphpa7J");// "Musique2015$");
		try {
			// Create the initial context
			LdapContext ctx = new InitialLdapContext(env,null);
			return ctx;
		} catch (NamingException e) {
			System.err.println(e);
			return null;
		}
	}
	
	public ArrayList<SearchResult> getUsers(){
		String searchBase = "ou=UNI-Users,dc=uni,dc=lux";//"ou=Users-demo,dc=demo,dc=com";
		String searchFilter = "(objectClass=person)";
		SearchControls sCtrl = new SearchControls();
		sCtrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
		ArrayList<SearchResult> temp = search("ou=BOG,"+searchBase,searchFilter,sCtrl);
		temp.addAll(search("ou=Administration,ou=FDEF,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Assistants,ou=FDEF,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Research,ou=FDEF,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Teachers,ou=FDEF,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Administration,ou=FLSHASE,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Assistants,ou=FLSHASE,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Research,ou=FLSHASE,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Teachers,ou=FLSHASE,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Administration,ou=FSTC,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Assistants,ou=FSTC,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Research,ou=FSTC,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Teachers,ou=FSTC,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Administration,ou=LCSB,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Assistants,ou=LCSB,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Research,ou=LCSB,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Teachers,ou=LCSB,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Administration,ou=SNT,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Assistants,ou=SNT,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Research,ou=SNT,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Teachers,ou=SNT,ou=Faculties,"+searchBase,searchFilter,sCtrl));
		temp.addAll(search("ou=Rectorat,"+searchBase,searchFilter,sCtrl));
		return temp;
	}
	
	public ArrayList<SearchResult> getUser(String name){
		String searchBase = "ou=Users-demo,dc=demo,dc=com";
		String searchFilter = "(&(objectClass=person)(cn="+name+"))";
		SearchControls sCtrl = new SearchControls();
		sCtrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
		return search(searchBase,searchFilter,sCtrl);
	}
	
	private ArrayList<SearchResult> search(String searchBase, String searchFilter, SearchControls sCtrl){
		ArrayList<SearchResult> results = new ArrayList<>();

		try {
			LdapContext ctx = getContext();

			// Activate paged results
			int pageSize = 1000;
			byte[] cookie = null;
			ctx.setRequestControls(new Control[] { new PagedResultsControl(pageSize, Control.NONCRITICAL) });
			do {
				/* perform the search */
				NamingEnumeration<SearchResult> temp = ctx.search(searchBase, searchFilter, sCtrl);
				results.addAll(Collections.list(temp));
				/* for each entry print out name + all attrs and values */
				while (temp != null && temp.hasMore()) {
					SearchResult entry = (SearchResult) temp.next();
					System.out.println(entry.getName());
				}

				// Examine the paged results control response
				Control[] controls = ctx.getResponseControls();
				if (controls != null) {
					for (int i = 0; i < controls.length; i++) {
						if (controls[i] instanceof PagedResultsResponseControl) {
							PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
							cookie = prrc.getCookie();
						}
					}
				} else {
					System.out.println("No controls were sent from the server");
				}
				// Re-activate paged results
				ctx.setRequestControls(new Control[] { new PagedResultsControl(pageSize, cookie, Control.CRITICAL) });

			} while (cookie != null);

			ctx.close();

		} catch (NamingException e) {
			System.err.println("PagedSearch failed.");
			e.printStackTrace();
		} catch (IOException ie) {
			System.err.println("PagedSearch failed.");
			ie.printStackTrace();
		}
		return results;
	}
}
