package lu.uni.fstc.proactivity.log;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * @author Bill Venners
 * Downloaded from: <a href="http://www.javaworld.com/javaworld/jw-06-1998/jw-06-techniques.html?page=2">Java World</a>
 */
public class LogFile {

	private String fileName;
	
	LogFile(String fileName) {
		this.fileName = fileName;
	}
	// The writeToFile() method will catch any IOException
	// so that clients aren't forced to catch IOException
	// everywhere they write to the log file.  For now,
	// just fail silently. In the future, could put
	// up an informative non-modal dialog box that indicates
	// a logging error occurred. - bv 4/15/98
	void writeToFile(String message) {
		FileOutputStream fos = null;
		PrintWriter pw = null;
		try {
			fos = new FileOutputStream(fileName, true);
			try {
				pw = new PrintWriter(fos, false);
				pw.println("------------------");
				pw.println(message);
				pw.println();
			}
			finally {
				if (pw != null) {
					pw.close();
				}
			}
		}
		catch (IOException e) {
		}
		finally {
			if (fos != null) {
				try {
					fos.close();
				}
				catch (IOException e) {
				}
			}
		}
	}
}