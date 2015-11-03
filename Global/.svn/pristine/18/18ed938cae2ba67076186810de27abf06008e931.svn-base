package lu.uni.fstc.proactivity.utils;

import java.net.InetAddress;

import javax.naming.ConfigurationException;

import lu.uni.fstc.proactivity.parameters.MoodleConnectionParameters;

/**
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011/2012
 *
 */
public class Utils {

	/* **********************************************************************************
	 * Specific Method(s) to call Moodle script(s)
	 * **********************************************************************************
	 */
	/**
	 * @param connParams
	 * @param user_id
	 * @param subject
	 * @param body
	 * @return the URL created
	 * @throws ConfigurationException
	 */
	public static java.net.URL buildSendEmailURL(MoodleConnectionParameters connParams ,long user_id, String subject, String body) throws ConfigurationException {
		try {
			if (connParams == null)
				throw new ConfigurationException("Configuration Parameters Undefined.");

//			if (connParams.protocol == null || connParams.protocol.isEmpty() || 
//			connParams.sendMailHost == null || connParams.sendMailHost.isEmpty() || 
//			connParams.sendMailScript == null || connParams.sendMailScript.isEmpty())
			if ((StringUtils.isEmptyOrNullString(connParams.protocol)) ||
					(StringUtils.isEmptyOrNullString(connParams.sendMailHost))||
					(StringUtils.isEmptyOrNullString(connParams.sendMailScript)))
				throw new ConfigurationException("Configuration Parameters Incomplete.");

			/*String URL_protocol = connParams.protocol;
			String URL_server = sendMailHost;
			String URL_page = connParams.sendMailScript;
			*/
			String urlString;
			urlString = "&to=" + user_id;
			urlString += "&subject=" + subject;
			urlString += "&txt=" + body;

			// Create connection
			java.net.URI uri = new java.net.URI(connParams.protocol, connParams.sendMailHost, connParams.sendMailScript, urlString, null);

			java.net.URL url = uri.toURL();
			return url;
		} catch (java.net.MalformedURLException e) {
			e.printStackTrace();
		} catch (java.net.URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * taken from http://www.roseindia.net/java/java-get-example/get-computer-name.shtml
	 * @return the string representing the Computer name
	 */
	public static String getComputerName() {
		try {
			final String computername = InetAddress.getLocalHost().getCanonicalHostName();// .getHostName();
			return computername;
		} catch (final Exception e){
			Global.logger.severe("Exception caught =" + e.getMessage());
		}
		return null;
	}
}