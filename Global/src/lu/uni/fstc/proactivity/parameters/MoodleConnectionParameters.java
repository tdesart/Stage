package lu.uni.fstc.proactivity.parameters;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.StringUtils;

/**
 * Adds sendMailScript and protocol to the standard DbConnectionParameters.
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2012
 *
 */
public class MoodleConnectionParameters extends DbConnectionParameters {

	/**
	 * the protocol type
	 */
	public String protocol;

	/**
	 * the protocol type
	 */
	public String sendMailHost;

	/**
	 * the script file name for the send message utility
	 */
	public String sendMailScript;

	/**
	 * @param filePath
	 * @throws Exception ConfigurationException
	 */
	public MoodleConnectionParameters(final String filePath) throws Exception {
		super(filePath);
	}

	@Override
	public void readSettings(final Document doc) throws Exception {
		final NodeList connList = doc.getElementsByTagName("connection");
		final Node conn = connList.item(0);
		final NodeList settings = conn.getChildNodes();											

		for (int i = 0; i < settings.getLength(); i++) {
			final Node item = settings.item(i);
			if (item.getNodeType() == 1 ) {
				Global.logger.finest(" - level 3 item " + i + " type = " + item.getNodeType() + " itemName= '" + item.getNodeName() + "' ChildNodeValue= '" + item.getChildNodes().item(0).getNodeValue()+"'");
				// Get the item Node name. Ex: 'port'
				final String itemName = item.getNodeName();
				// Get its value from the first child Node name. Ex: '3306'
				final String itemValue = item.getChildNodes().item(0).getNodeValue();
				
				// Associate the node value with the correct connection parameter attribute
				if (itemName.equalsIgnoreCase("protocol") ) {
					protocol = itemValue;
				}
				else if (itemName.equalsIgnoreCase("host") ) {
					hostName = itemValue;
				}
				else if (itemName.equalsIgnoreCase("mail_host") ) {
					sendMailHost = itemValue;
				}
				else if (itemName.equalsIgnoreCase("mail_script") ) {
					sendMailScript = itemValue;
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
		String errorMsg = super.validateConfiguration();
		if (StringUtils.isEmptyOrNullString(protocol))
			errorMsg += "\nMissing 'Protocol' in configuration file!";
		if (StringUtils.isEmptyOrNullString(sendMailHost))
			errorMsg += "\nMissing 'send mail host' in configuration file!";
		if (StringUtils.isEmptyOrNullString(sendMailScript))
			errorMsg += "\nMissing 'send mail script' file name in configuration file!";
		return errorMsg;
	}
	
	@Override
	public String toString() {
		String ret = super.toString();
		ret += "\n\tProtocol\t= " + protocol;
		ret += "\n\tMail Host\t= " + sendMailHost;
		ret += "\n\tMail Script\t= " + sendMailScript;
		return ret;
	}
}