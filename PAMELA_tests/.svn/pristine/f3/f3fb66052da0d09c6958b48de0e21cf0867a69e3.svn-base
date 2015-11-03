package lu.uni.fstc.test_Remus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author sandro.reis
 *
 */
public class URLConnectionReader {
	
	static {
	    //for localhost testing only
	    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
	    new javax.net.ssl.HostnameVerifier(){
 
	        @Override
			public boolean verify(final String hostname,
	                final javax.net.ssl.SSLSession sslSession) {
	            if (hostname.equals("moodle-test.uni.lux")) {
	                return true;
	            }
	            return false;
	        }
	    });
	}
	
    /**
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        
    	//https://moodle-test.uni.lux/enrol/users.php?id=1417&page=0&perpage=100&sort=lastname&dir=ASC&action=addmember&user=2305
    	final URL moodle = new URL("https://moodle-test.uni.lux/enrol/users_Remus.php?id=1417&page=0&perpage=100&sort=lastname&dir=ASC&action=addmember&user=2305");
        final URLConnection yc = moodle.openConnection();
        
        final BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            System.out.println(inputLine);
        in.close();
    }
}