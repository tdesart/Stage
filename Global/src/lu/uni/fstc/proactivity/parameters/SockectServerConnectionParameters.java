/**
 * 
 */
package lu.uni.fstc.proactivity.parameters;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lu.uni.fstc.proactivity.utils.StringUtils;

/**
 * Class to store all the connection parameters to open a secure (web) socket connection<br>
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2013
 *
 */
public class SockectServerConnectionParameters extends ConnectionParameters {

	/**
	 * the key store file name
	 */
	public String keyStoreFileName;
	/**
	 * the key store password
	 */
	public String keyStorePassword;
	/**
	 * the private key password 
	 */
	public String keyPassword;

	/**
	 * @param filePath
	 * @throws Exception ConfigurationException
	 */
	public SockectServerConnectionParameters(String filePath) throws Exception {
		super(filePath);
	}

	/**
	 * @see lu.uni.fstc.proactivity.parameters.ConnectionParameters#readSettings(org.w3c.dom.Document)
	 */
	@Override
	public void readSettings(Document doc) throws Exception {
		final NodeList connList = doc.getElementsByTagName("connection");
		final Node conn = connList.item(0);
		final NodeList settings = conn.getChildNodes();											

		for (int i = 0; i < settings.getLength(); i++) {
			final Node item = settings.item(i);
			if (item.getNodeType() == 1 ) {
				// Get the item Node name. Ex: 'port'
				final String itemName = item.getNodeName();
				// Get its value from the first child Node name. Ex: '3306'
				final String itemValue = item.getChildNodes().item(0).getNodeValue();
				
				// Associate the node value with the correct connection parameter attribute
				if (itemName.equalsIgnoreCase("keystore") ) {
					keyStoreFileName = itemValue;
				}
				else if (itemName.equalsIgnoreCase("storepassword") ) {
					keyStorePassword = itemValue;
				}
				else if (itemName.equalsIgnoreCase("keypassword") ) {
					keyPassword = itemValue;
				} // if (itemName)
			} // if (item.getNodeType() == 1 )
		} // for 'i'
	}

	/**
	 * @see lu.uni.fstc.proactivity.parameters.ConnectionParameters#validateConfiguration()
	 */
	@Override
	public String validateConfiguration() {
		String errorMsg = "";
		if (StringUtils.isEmptyOrNullString(keyStoreFileName))
			errorMsg += "\nMissing 'key store file name' in configuration file!";
		if (StringUtils.isEmptyOrNullString(keyStorePassword))
			errorMsg += "\nMissing 'key store password' in configuration file!";
		if (StringUtils.isEmptyOrNullString(keyPassword))
			errorMsg += "\nMissing 'private key password' in configuration file!";

		return errorMsg;
	}

	@Override
	public String toString() {
		String ret = "Connection settings:";
		ret += "\n\tKey Store File Name\t= " + keyStoreFileName;
		ret += "\n\tKey Store Password\t= " + keyStorePassword;
		ret += "\n\tPrivate Key Password\t= " + keyPassword;
		return ret;
	}
}