package lu.uni.fstc.proactivity.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import lu.uni.fstc.proactivity.utils.StringUtils;

/**
 * code from <a href="http://java.sys-con.com/node/44698">Jerason Banes</a><br>
 * I only added the right padding
 * @author Jerason Banes
 *
 */
public class  SingleLineFormatter extends Formatter {

	private final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private final Date date = new Date();
 
	/**
	 * @param record
	 * @return the string with the new format
	 */
	@Override
	public String format(final LogRecord record)
	{
		date.setTime(System.currentTimeMillis());
		return "["+dateformat.format(date)+"] "+StringUtils.padRightSpaces(record.getLevel().getName(), 7)+ " * "+record.getMessage()+"\r\n";
	}
}