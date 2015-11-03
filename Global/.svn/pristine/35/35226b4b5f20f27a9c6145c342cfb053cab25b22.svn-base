package lu.uni.fstc.proactivity.parameters;

import java.io.File;

import javax.naming.ConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import lu.uni.fstc.proactivity.utils.StringUtils;

/**
 * Class to store all the connection parameters<br>
 * The idea is to be filled with data from a configuration file to be provided in the constructor
 * 
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2013
 *
 */
public abstract class ConnectionParameters {

	/**
	 * @param filePath
	 * @throws Exception ConfigurationException
	 */
	public ConnectionParameters(final String filePath) throws Exception {
		if (StringUtils.isEmptyOrNullString(filePath))
			throw new ConfigurationException("Missing Configuration File path.");
			
		final Document doc = openConfigFile(filePath);
		String errorMsg = null;
		if (doc == null)
			errorMsg = "Missing Configuration File path.";
		else {
			readSettings(doc);
			errorMsg = validateConfiguration();
		}
		if (!StringUtils.isEmptyOrNullString(errorMsg))
			throw new ConfigurationException(errorMsg);
	}

	/**
	 * Method that takes a DOM Document and read all relevant settings and sets them to the correct attributes <br>
	 * @param doc 
	 * @throws Exception 
	 * 
	 */
	abstract public void readSettings(final Document doc) throws Exception;

	/**
	 * Method that checks if all mandatory settings are set 
	 * @return the error Message. Empty if configuration is Valid
	 */
	abstract public String validateConfiguration();

	/**
	 * Opens the XML file with the configuration Setting<br>
	 * Loads it <br>
	 * Parses it into a Document Object Model object<br>
	 * Normalises it<br>
	 * <b><i>extracted from code of Master student Remus Dobrican</i></b>
	 * @param filePath 
	 * @return a Document Object Model object
	 * @throws Exception 
	 */
	final public static Document openConfigFile(String filePath) throws Exception {
/*/		// Simpler, cleaner code, but prone to errors (NULL POINTER EXCEPTIONS)
 		// And much more difficult to debug
		final File fXmlFile = new File(Thread.currentThread().getContextClassLoader().getResource(filePath).getFile());
/*/
		Document doc = null;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		java.net.URL myUrl = loader.getResource(filePath);	
		if (myUrl == null)
			throw new ConfigurationException("Missing Configuration File: " + filePath);

		String filename = myUrl.getFile();
		if (StringUtils.isEmptyOrNullString(filename))
			throw new ConfigurationException("Missing Configuration File: " + filePath);

		final File fXmlFile = new File(filename);
/**/
		final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		dBuilder = dbFactory.newDocumentBuilder();

		doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();	

		return doc;
	}
}