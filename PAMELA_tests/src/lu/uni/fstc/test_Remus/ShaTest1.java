package lu.uni.fstc.test_Remus;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * @author remus.dobrican
 *
 */
public class ShaTest1 {

	private final String FILE = "PDF/SecondPdf.pdf";
	private static byte[] SHA1;
	private long length = 0;

	/**
	 * 
	 */
	public ShaTest1() {
		try {
			SHA1("");
		} catch (final NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		Global.logger.info(byteArrayToHexString(SHA1));

		final File file = new File(FILE);
		try {
			Global.logger.info(byteArrayToHexString(createSha1(file)));
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final ShaTest1 a = new ShaTest1();
		Global.logger.info("" + a.length);
	}

	/**
	 * @param file
	 * @return a byte array
	 * @throws Exception
	 */
	public byte[] createSha1(final File file) throws Exception {
		final MessageDigest digest = MessageDigest.getInstance("SHA-1");
		final InputStream fis = new FileInputStream(file);
		int n = 0;
		final byte[] buffer = new byte[8192];

		while (n != -1) {
			n = fis.read(buffer);

			length += n;
			Global.logger.info("" + length);
			if (n > 0) {
				digest.update(buffer, 0, n);
			}
		}
		fis.close();
		return digest.digest/**
		 * @param b
		 * @return
		 */
		();
	}

	/**
	 * @param b
	 * @return a string
	 */
	public String byteArrayToHexString(final byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	/**
	 * @param x
	 * @throws NoSuchAlgorithmException
	 */
	public static void SHA1(final String x) throws NoSuchAlgorithmException {
		final MessageDigest sha1 = MessageDigest.getInstance("SHA1");
		SHA1 = sha1.digest((x).getBytes());
	}
}