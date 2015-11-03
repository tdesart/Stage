package lu.uni.fstc.proactivity.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;

/**
	KeyStoreImport.java<br>
	Adds a specified certificate chain and associated RSA private key to a Java keystore.<br>

	Usage: <br>    <i>java KeyStoreImport KEYSTORE CERTS KEY ALIAS</i><br><br>
		<b>KEYSTORE</b> is the name of the file containing the Java keystore<br>
		<b>CERTS</b>        is the name of a file containing a chain of concatenated DER-encoded X.509 certificates<br>
		<b>KEY</b>             is the name of a file containing a DER-encoded PKCS#8 RSA private key<br>
		<b>ALIAS</b>         is the alias for the private key entry in the keystore<br><br>

	©Neal Groothuis<br>
	2006-08-08<br>
	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.<br>
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.<br>
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA<br><br>
 *	
 * @author © Neal Groothuis<br>
 *		2006-08-08<br>
 * Downloaded from: <a href="http://www.nealgroothuis.name/import-a-private-key-into-a-java-keystore/">neal groothuis</a>
 */
public class KeyStoreImport {
	/**
	 * @param args 
	 */
	public static void main(final String args[]) {
		try {
			//Meaningful variable names for the arguments
			final String keyStoreFileName 			= args[0];
			final String certificateChainFileName 	= args[1];
			final String privateKeyFileName 		= args[2];
			final String entryAlias 				= args[3];

			//Get the password for the keystore.
			System.out.println("Keystore password: ");
			final String keyStorePassword = (new BufferedReader(new InputStreamReader(System.in))).readLine();

			//Load the keystore
			final KeyStore keyStore = KeyStore.getInstance("jks");
			final FileInputStream keyStoreInputStream = new FileInputStream(keyStoreFileName);

			if (keyStorePassword != null)
				keyStore.load(keyStoreInputStream, keyStorePassword.toCharArray());
			else {
				System.out.println("ERROR: Keystore Password is empty!");
				return;
			}
			keyStoreInputStream.close();

			//Load the certificate chain (in X.509 DER encoding).
			final FileInputStream certificateStream = new FileInputStream(certificateChainFileName);
			final CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			//Required because Java is STUPID.  You can't just cast the result of toArray to Certificate[].
			java.security.cert.Certificate[] chain = {};
			chain = certificateFactory.generateCertificates(certificateStream).toArray(chain);
			certificateStream.close();

			//Load the private key (in PKCS#8 DER encoding).
			final File keyFile = new File(privateKeyFileName);
			final byte[] encodedKey = new byte[(int)keyFile.length()];
			final FileInputStream keyInputStream = new FileInputStream(keyFile);
			keyInputStream.read(encodedKey);
			keyInputStream.close();
			final KeyFactory rSAKeyFactory = KeyFactory.getInstance("RSA");
			final PrivateKey privateKey = rSAKeyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));

			//Add the new entry
			System.out.println("Private key entry password:  ");
			final String privateKeyEntryPassword = (new BufferedReader(new InputStreamReader(System.in))).readLine();

			if (privateKeyEntryPassword != null) {
				keyStore.setEntry(entryAlias, new KeyStore.PrivateKeyEntry(privateKey, chain), new KeyStore.PasswordProtection(privateKeyEntryPassword.toCharArray()));
			}
			else{
				System.out.println("ERROR: Private key password is empty!");
				return;
			}
			//Write out the keystore
			final FileOutputStream keyStoreOutputStream = new FileOutputStream(keyStoreFileName);
			keyStore.store(keyStoreOutputStream, keyStorePassword.toCharArray());
			keyStoreOutputStream.close();
		}
		catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}