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
package org.ajax4jsf.util.base64;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.faces.FacesException;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:59:10 $
 */
public class Codec {
    private Cipher d = null;
    private Cipher e = null;

    /**
     *
     */
    public Codec() {
    }

    /**
     *
     */
    public Codec(String p) throws Exception {
        setPassword(p);
    }

    /**
     * @param p
     * @throws java.security.InvalidKeyException
     *
     * @throws java.io.UnsupportedEncodingException
     *
     * @throws java.security.spec.InvalidKeySpecException
     *
     * @throws java.security.NoSuchAlgorithmException
     *
     * @throws javax.crypto.NoSuchPaddingException
     *
     */
    public void setPassword(String p) throws FacesException {
        byte[] s = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03 };

        try {
            KeySpec keySpec = new DESKeySpec(p.getBytes("UTF8"));
            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);

            e = Cipher.getInstance(key.getAlgorithm());
            d = Cipher.getInstance(key.getAlgorithm());

            // Prepare the parameters to the cipthers
            // AlgorithmParameterSpec paramSpec = new IvParameterSpec(s);
            e.init(Cipher.ENCRYPT_MODE, key);
            d.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception e) {
            throw new FacesException("Error set encryption key", e);
        }
    }

    public String decode(String str) throws Exception {
        byte[] src = str.getBytes("UTF8");
        byte[] utf8 = decode(src);

        // Decode using utf-8
        return new String(utf8, "UTF8");
    }

    public String encode(String str) throws Exception {

        // try {
        byte[] src = str.getBytes("UTF8");

        // int len = (src.length/8+1)*8;
        // byte[] block = new byte[len];
        // Arrays.fill(block,0,len,(byte)0x20);
        // System.arraycopy(src,0,block,0,src.length);
        // Decrypt
        byte[] utf8 = encode(src);

        // Decode using utf-8
        return new String(utf8, "UTF8");

        // } catch (Exception e) {
        // // TODO: handle exception
        // return null;
        // }
    }

    public byte[] decode(byte[] src) throws Exception {
        byte[] dec = URL64Codec.decodeBase64(src);

        // Decrypt
        if (null != d) {
            return d.doFinal(dec);
        } else {
            return dec;
        }
    }

    public byte[] encode(byte[] src) throws Exception {
        byte[] dec;

        if (null != e) {
            dec = e.doFinal(src);
        } else {
            dec = src;
        }

        // Decrypt
        return URL64Codec.encodeBase64(dec);
    }
}
