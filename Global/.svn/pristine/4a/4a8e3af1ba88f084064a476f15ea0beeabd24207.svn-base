package lu.uni.fstc.proactivity.log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Bill Venners
 * http://www.javaworld.com/javaworld/jw-06-1998/jw-06-techniques.html?page=2
 */
public class LogFileManager {
	private FileOutputStream fos;
	private PrintWriter pw;
	private boolean logFileOpen = false;
	
	LogFileManager() {
	}
	
	LogFileManager(String fileName) throws IOException {
		openLogFile(fileName);
	}
	
	void openLogFile(String fileName) throws IOException {
		if (!logFileOpen) {
			try {
				fos = new FileOutputStream(fileName, true);
				pw = new PrintWriter(fos, false);
				logFileOpen = true;
			}
			catch (IOException e) {
				if (pw != null) {
					pw.close();
					pw = null;
				}
				if (fos != null) {
					fos.close();
					fos = null;
				}
				throw e;
			}
		}
	}
	
	void closeLogFile() throws IOException {
		if (logFileOpen) {
			pw.close();
			pw = null;
			fos.close();
			fos = null;
			logFileOpen = false;
		}
	}
	
	boolean isOpen() {
		return logFileOpen;
	}
	
	void writeToFile(String message) {
		pw.println("------------------");
		pw.println(message);
		pw.println();
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (logFileOpen) {
			try {
				closeLogFile();
			}
			finally {
				super.finalize();
			}
		}
	}
}