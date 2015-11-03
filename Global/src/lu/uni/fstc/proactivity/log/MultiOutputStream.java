package lu.uni.fstc.proactivity.log;

import java.io.IOException;
import java.io.OutputStream;

/**
 * code from <a href="http://java.sys-con.com/node/44698">Jerason Banes</a><br>
 * @author Jerason Banes
 *
 */
public class MultiOutputStream extends OutputStream
{
	private ThreadLocal<OutputStream> lookup = new InheritableThreadLocal<OutputStream>();

	@Override
	public void write(int data) throws IOException
	{
		OutputStream out = lookup.get();

		out.write(data);
	}

	/**
	 * @param out
	 */
	public void setOutputStream(final OutputStream out)
	{
		lookup.set(out);
	}
}
