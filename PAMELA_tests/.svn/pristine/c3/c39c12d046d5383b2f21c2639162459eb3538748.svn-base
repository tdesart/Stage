package lu.uni.fstc.test_Remus;

import java.text.Normalizer;
import java.text.Normalizer.Form;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author REMUS
 *
 */
public class TestNamesOfPdfFiles {
	
	/**
	 * 
	 */
	public TestNamesOfPdfFiles(){
		buildString();
	}
	
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final TestNamesOfPdfFiles b = new TestNamesOfPdfFiles();
		b.toString();
	}
	
	/**
	 * 
	 */
	public void buildString(){
		
		final String a = "Bachelor  en Sciences de la Vie (académique) - Biologie'";
/*/		byte[] ptext = new byte[200];
		
		try {
			 ptext = a.getBytes("UTF-8");
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}
/**/		
//		String b = new String(ptext);
		
		Global.logger.info(removeAccents(a));
	}
	
	@Override
	public String toString(){
		return "blaaa";
	}
	
	/**
	 * @param text
	 * @return a String without accents
	 */
	public String removeAccents(final String text) {
	    return text == null ? null
	        : Normalizer.normalize(text, Form.NFD)
	            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
}