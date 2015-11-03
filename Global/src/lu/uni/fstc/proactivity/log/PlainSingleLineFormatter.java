package lu.uni.fstc.proactivity.log;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * original code from <a href="http://java.sys-con.com/node/44698">Jerason Banes</a><br>
 * I personalised it to my taste
 * @author Jerason Banes
 * @author Sandro Reis
 */
public class  PlainSingleLineFormatter extends Formatter {

//	private final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private final Date date = new Date();

	/**
	 * @param record
	 * @return the string with the new format
	 */
	@Override
	public String format(final LogRecord record)
	{
		date.setTime(System.currentTimeMillis());

//		return " * ["+record.getSourceClassName()+"." +record.getSourceMethodName() + "] " + record.getMessage()+"\r\n";
//		return " * ["+record.getSourceClassName() + "] " + record.getMessage()+"\r\n";
		return record.getMessage()+"\r\n";
	}
}