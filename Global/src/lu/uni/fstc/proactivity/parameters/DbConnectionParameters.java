package lu.uni.fstc.proactivity.parameters;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lu.uni.fstc.proactivity.utils.StringUtils;

/**
 * Class to store all the connection parameters needed to access a database <br>
 * The idea is to be filled with data from a configuration file to be provided in the constructor
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2012
 *
 */
public class DbConnectionParameters extends ConnectionParameters {

	/**
	 * the name of the server
	 */
	public String hostName;
	/**
	 * the port number on the server
	 */
	public String portNumber;
	/**
	 * the name of the database on the server
	 */
	public String schemaName;
	/**
	 * the user name on the database
	 */
	public String userName;
	/**
	 * the password of that user on the database
	 */
	public String password;

	/**
	 * @param filePath
	 * @throws Exception ConfigurationException
	 */
	public DbConnectionParameters(final String filePath) throws Exception {
		super(filePath);
	}

	/**
	 * <b><i>inspired by code from Master student Remus Dobrican</i></b>
	 * 
	 */
	@Override
	public void readSettings(final Document doc) throws Exception {
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
				if (itemName.equalsIgnoreCase("host") ) {
					hostName = itemValue;
				}
				else if (itemName.equalsIgnoreCase("port") ) {
					portNumber = itemValue;
				}
				else if (itemName.equalsIgnoreCase("schema") ) {
					schemaName = itemValue;
				}
				else if (itemName.equalsIgnoreCase("user") ) {
					userName = itemValue;
				}
				else if (itemName.equalsIgnoreCase("pass") ) {
					password = itemValue;
				} // if (itemName)
			} // if (item.getNodeType() == 1 )
		} // for 'i'
	}

	@Override
	public String validateConfiguration() {
		String errorMsg = "";
		if (StringUtils.isEmptyOrNullString(hostName))
			errorMsg += "\nMissing 'Host name' in configuration file!";
		if (StringUtils.isEmptyOrNullString(portNumber))
			errorMsg += "\nMissing 'Port number' in configuration file!";
		if (StringUtils.isEmptyOrNullString(schemaName))
			errorMsg += "\nMissing 'Schema (database) Name' in configuration file!";
		if (StringUtils.isEmptyOrNullString(userName))
			errorMsg += "\nMissing 'User name' in configuration file!";
		if (StringUtils.isEmptyOrNullString(password))
			errorMsg += "\nMissing 'User password' in configuration file!";

		return errorMsg;
	}

	@Override
	public String toString() {
		String ret = "Connection settings:";
		ret += "\n\tHost Name\t= " + hostName;
		ret += "\n\tPort Number\t= " + portNumber;
		ret += "\n\tSchema Name\t= " + schemaName;
		ret += "\n\tUser Name\t= " + userName;
//		ret += "\n\tPassword\t= <undisclosed>";
		ret += "\n\tPassword\t= " + password;
		return ret;
	}
}