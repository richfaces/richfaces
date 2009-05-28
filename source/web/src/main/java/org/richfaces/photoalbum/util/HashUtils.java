/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
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
