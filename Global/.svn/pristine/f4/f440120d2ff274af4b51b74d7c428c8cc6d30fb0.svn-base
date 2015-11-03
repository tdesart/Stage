package lu.uni.fstc.proactivity.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.text.Normalizer.Form;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

/**
 * @author remus.dobrican
 * 
 */
public abstract class PdfBuilder {

	private String type ;
	protected String FILE;
	
	/**
	 * 
	 */
	public PdfBuilder() {
		setType("PdfBuilder");
	}
	
	/**
	 * iText allows to add metadata to the PDF which can be viewed in your Adobe Reader under File -> Properties
	 * @param document
	 */
	abstract protected void addMetaData(final Document document);
	
	/**
	 * 
	 */
	public void buildDocument(){
		try {
			final Document document = new Document();
			final PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(FILE));
			document.open();

			addMetaData(document);
			addTitlePage(document, writer);
			document.close();
			
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param text
	 */
	protected String removeAccents(String text) {
	    return text == null ? null
	        : Normalizer.normalize(text, Form.NFD)
	            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
	
	/**
	 * @param pathnamehashString
	 * @param pathnamehashFile
	 */
	abstract void buildHashs(final String pathnamehashString, final String pathnamehashFile);
	
	/**
	 * @param document
	 * @param writer
	 * @throws DocumentException 
	 */
	abstract protected void addTitlePage(final Document document, final PdfWriter writer) throws DocumentException ;

	/**
	 * @param preface
	 */
	abstract protected void addContent(final Paragraph preface);
	
	/**
	 * @param writer
	 * @param preface
	 * @throws DocumentException
	 */
	abstract protected void createImages(final PdfWriter writer, final Paragraph preface) throws DocumentException;

	/**
	 * @param file
	 * @return a byte array
	 * @throws Exception
	 */
	abstract protected byte[] createSha1(final File file) throws Exception;

	/**
	 * @param b
	 * @return a String
	 */
	abstract protected String byteArrayToHexString(final byte[] b);

	/**
	 * @param text
	 * @return a byte array
	 * @throws NoSuchAlgorithmException
	 */
	abstract protected byte[] SHA1(String text) throws NoSuchAlgorithmException;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
}
