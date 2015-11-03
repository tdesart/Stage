package lu.uni.fstc.test_Remus;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

/**
 * @author sandro.reis
 *
 */
public class ThirdPdf {
	
//	private static String FILE = "PDF/ThirdPdf.pdf";

	private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
			Font.BOLD);
	
	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		try {
			final Document document = new Document();
//			final PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILE));
			document.open();

			addTitlePage(document);

			document.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	private static void addTitlePage(final Document document)
			throws DocumentException {
		final Paragraph preface = new Paragraph();
		// We add one empty line
		addEmptyLine(preface, 1);
		// Lets write a big header
		preface.add(new Paragraph("Statistics", titleFont));

		document.add(preface);
		// Start a new page
		document.newPage();
	}

	private static void addEmptyLine(final Paragraph paragraph, final int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
}