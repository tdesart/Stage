package lu.uni.fstc.test_Remus;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author sandro.reis
 *
 */
public class ReadAndUsePdf {
  private static String INPUTFILE = "SecondPdf.pdf";
  private static String OUTPUTFILE = "ReadPdf.pdf";

  /**
 * @param args
 * @throws DocumentException
 * @throws IOException
 */
public static void main(final String[] args) throws DocumentException,
      IOException {
    final Document document = new Document();

    final PdfWriter writer = PdfWriter.getInstance(document,
        new FileOutputStream(OUTPUTFILE));
    document.open();
    final PdfReader reader = new PdfReader(INPUTFILE);
    final int n = reader.getNumberOfPages();
    PdfImportedPage page;
    // Go through all pages
    for (int i = 1; i <= n; i++) {
      // Only page number 2 will be included
      if (i == 2) {
        page = writer.getImportedPage(reader, i);
        final Image instance = Image.getInstance(page);
        document.add(instance);
      }
    }
    document.close();

  }

} 
