package lu.uni.fstc.test_Remus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
/**
 * @author remus.dobrican
 * 
 */
public class PdfBuilderSimple {
	private String FILE, contenthashFile,  pathnamehashString, contenthashString, pathnamehashFile,  fileName;
	private final String pdfDescription = "<p>Bravo !</p>", displayOptions = "a:2:{s:12:\"printheading\";i:0;s:10:\"printintro\";i:1;}" ;
	
	private long fileSize, stringSize = 0;
	private Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	
	
	/**
	 * @param groupName
	 * @param fileName 
	 */
	public PdfBuilderSimple(
			String groupName, 
			final String fileName) {
		this.fileName = fileName;
		this.FILE = "stats/"+fileName+".pdf";

		try {
			final Document document = new Document();
			@SuppressWarnings("unused")
			final PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILE));
			document.open();

			addMetaData(document);
			addTitlePage(document);

			document.close();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * iText allows to add metadata to the PDF which can be viewed in your Adobe Reader under File -> Properties
	 * @param document
	 */
	private void addMetaData(final Document document) {
		document.addTitle("Statistics");
		document.addSubject("Statistics about the Social Groups");
		document.addKeywords("Social Groups");
		document.addAuthor("Dobrican Remus");
		document.addCreator("Dobrican Remus");
	}
	
	/**
	 * @param pathnamehashString
	 * @param pathnamehashFile
	 */
	public void buildHashs(final String pathnamehashString, final String pathnamehashFile){
		try {			
			// encrypting the contenthash with sha1 encryption for the EmptySpace
			contenthashString = byteArrayToHexString(SHA1(""));
			
			// encrypting the contenthash with sha1 encryption for the PDF file
			contenthashFile = byteArrayToHexString(createSha1(new File(FILE)));
			
			// encrypting the pathnamehash with sha1 encryption for the EmptySpace
			// "/35609/mod_resource/content/0/."
			this.pathnamehashString = byteArrayToHexString(SHA1(pathnamehashString));
			
			// encrypting the pathnamehash with sha1 encryption for the PDF file
			// "/35609/mod_resource/content/0/SecondPdf.pdf"
			this.pathnamehashFile = byteArrayToHexString(SHA1(pathnamehashFile));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void addTitlePage(final Document document)
			throws DocumentException {
		final Paragraph preface = new Paragraph();
		// We add one empty line
		addEmptyLine(preface, 1);
		// Lets write a big header
		preface.add(new Paragraph("Statistics", titleFont));

		// We add one empty line
		addEmptyLine(preface, 1);
		
		document.add(preface);
		// Start a new page
		document.newPage();
	}

	
	

	/**
	 * @param paragraph
	 * @param number
	 */
	private void addEmptyLine(final Paragraph paragraph, final int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	/**
	 * @param file
	 * @return a byte array
	 * @throws Exception
	 */
	private byte[] createSha1(final File file) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		InputStream fis = new FileInputStream(file);
		int n = 0;
		byte[] buffer = new byte[8192];

		while (n != -1) {
			n = fis.read(buffer);
			fileSize += n;
			if (n > 0) {
				digest.update(buffer, 0, n);
			}
		}
		fis.close();
		return digest.digest();
	}

	/**
	 * @param b
	 * @return a String
	 */
	private String byteArrayToHexString(final byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	/**
	 * @param text
	 * @return a byte array
	 * @throws NoSuchAlgorithmException
	 */
	public byte[] SHA1(String text) throws NoSuchAlgorithmException{
	    MessageDigest sha1 = MessageDigest.getInstance("SHA1");
	    return sha1.digest((text).getBytes()); 
	}
	
	

	/**
	 * @return the contenthashFile
	 */
	public String getContenthashFile() {
		return contenthashFile;
	}

	/**
	 * @param contenthashFile the contenthashFile to set
	 */
	public void setContenthashFile(String contenthashFile) {
		this.contenthashFile = contenthashFile;
	}

	/**
	 * @return the pathnamehashString
	 */
	public String getPathnamehashString() {
		return pathnamehashString;
	}

	/**
	 * @param pathnamehashString the pathnamehashString to set
	 */
	public void setPathnamehashString(String pathnamehashString) {
		this.pathnamehashString = pathnamehashString;
	}

	/**
	 * @return the contenthashString
	 */
	public String getContenthashString() {
		return contenthashString;
	}

	/**
	 * @param contenthashString the contenthashString to set
	 */
	public void setContenthashString(String contenthashString) {
		this.contenthashString = contenthashString;
	}

	/**
	 * @return the pathnamehashFile
	 */
	public String getPathnamehashFile() {
		return pathnamehashFile;
	}

	/**
	 * @param pathnamehashFile the pathnamehashFile to set
	 */
	public void setPathnamehashFile(String pathnamehashFile) {
		this.pathnamehashFile = pathnamehashFile;
	}

	/**
	 * @return the pdfDescription
	 */
	public String getPdfDescription() {
		return pdfDescription;
	}

	/**
	 * @return the displayOptions
	 */
	public String getDisplayOptions() {
		return displayOptions;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the stringSize
	 */
	public long getStringSize() {
		return stringSize;
	}

	/**
	 * @param stringSize the stringSize to set
	 */
	public void setStringSize(long stringSize) {
		this.stringSize = stringSize;
	}

	/**
	 * @return the fileSize
	 */
	public long getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
}
