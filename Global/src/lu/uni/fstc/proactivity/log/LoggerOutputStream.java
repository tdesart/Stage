package lu.uni.fstc.proactivity.log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * code from <a href="http://java.sys-con.com/node/44698">Jerason Banes</a><br>
 * @author Jerason Banes
 *
 */
public class LoggerOutputStream extends OutputStream
{
	private Logger logger;
	private StringBuffer buffer = new StringBuffer();
	private Level level;

	/**
	 * @param logger
	 */
	public LoggerOutputStream(final Logger logger)
	{
		this(logger, Level.INFO); 
	}

	/**
	 * @param logger
	 * @param level
	 */
	public LoggerOutputStream(final Logger logger, final Level level)
	{
		this.logger = logger;
		this.level = level;
	}

	@Override
	public void write(int c) throws IOException
	{
		if(c == '\n')
		{
			logger.log(new LogRecord(level, buffer.toString()));
			buffer = new StringBuffer();
		}
		else
		{
			buffer.append((char)c);
		}
	}
}