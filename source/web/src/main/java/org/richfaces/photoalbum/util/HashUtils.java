package org.richfaces.photoalbum.util;
/**
 * Convenience class to hash user passwords.
 *
 * @author Andrey Markhel
 */
import java.security.MessageDigest;

import org.jboss.seam.util.Hex;

public class HashUtils {
	private static String digestAlgorithm = "SHA-1";
	
	private static String charset = "UTF-8";
	/**
	 * Convenience method to hash user passwords.
	 *
	 * @param plainTextPassword - password to hash
	 */
	public static String hash(String plainTextPassword) {
		try {
			MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
			digest.update(plainTextPassword.getBytes(charset));
			byte[] rawHash = digest.digest();
			return new String(Hex.encodeHex(rawHash));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
