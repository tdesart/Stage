package lu.uni.fstc.proactivity.utils;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2012
 *
 */
public class CallSecureURLThread extends Thread {
	private final URL url;

	/**
	 * @param url the url to a secure connection, to call
	 */
	public CallSecureURLThread (URL url) {
		this.url = url;
	}

	@Override
	public void run() {
		long init;
		init = System.currentTimeMillis();
		try {
			if (this.url == null)
				return;
			final HttpsURLConnection theHttpsConnection = (HttpsURLConnection) this.url.openConnection();

			// Force to accept all certificates (because our Test server, has the Production certificate)
			theHttpsConnection.setHostnameVerifier(new javax.net.ssl.HostnameVerifier() {		
				@Override
				public boolean verify(final String arg0, final javax.net.ssl.SSLSession arg1) {
					return true;
				}
			});

			theHttpsConnection.connect();

			// Where the magic happens ...
			final Object obj = theHttpsConnection.getContent();
			if (obj == null)
				Global.logger.severe("ERROR callURL to: " + url.getPath() + ", Content returned NULL!");

			theHttpsConnection.disconnect();

		} catch (final Exception e) {
			e.printStackTrace();
		}
		Global.logger.info(url.getHost() + url.getPath() + ", total time : "  + (System.currentTimeMillis()-init));
  	}

	/* **********************************************************************************
	 * Generic HTTP(s) Methods
	 * **********************************************************************************
	 */
	/**
	 * @param theConnection
	 * @param obj
	 * @param toPrint
	 * @return a String with the full content of the page called
	 */
	public static String getContentFromHttpsConnection (final HttpsURLConnection theConnection, final Object obj, final boolean toPrint) {
		StringBuffer buf = new StringBuffer();
		String content = null;
    	java.io.BufferedReader reader = null;
		try {
			String contentType = theConnection.getHeaderField("Content-Type");
			java.io.FilterInputStream response = (java.io.FilterInputStream) obj;

			// Get charset
	        String charset = null;
			for (String param : contentType.replace(" ", "").split(";"))
	            if (param.startsWith("charset=")) {
	                charset = param.split("=", 2)[1];
	                break;
	            }

	        // Get Content from Response
			if (charset != null)
				reader = new java.io.BufferedReader(new java.io.InputStreamReader(response, charset));
	        else
                reader = new java.io.BufferedReader(new java.io.InputStreamReader(response));

			for (String line; (line = reader.readLine()) != null;)
				buf.append(line);

			content = buf.toString();

			if (toPrint) {
				// Print HTML response code (status) 
				Global.logger.finest("Status=" + theConnection.getResponseCode());
		        // Print Header
				Global.logger.finest("Header=");
				for (java.util.Map.Entry<String, java.util.List<String>> header : theConnection.getHeaderFields().entrySet()) {
					Global.logger.finest("\t" + header.getKey() + "=" + header.getValue());
				}
				Global.logger.finest("Content=\n" + content);
	        }

		} catch (java.io.IOException e) {
			e.printStackTrace();
        } finally {
            if (reader != null) 
            	try { 
            		reader.close(); 
            	} catch (java.io.IOException logOrIgnore) {
            		Global.logger.severe(logOrIgnore.getMessage());
            	}
        }
		return content;
	}
}
