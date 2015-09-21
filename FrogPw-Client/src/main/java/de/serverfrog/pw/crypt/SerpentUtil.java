/*
 * Copyright (C) 2014 Serverfrog.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.serverfrog.pw.crypt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Bastian Venz
 */
public class SerpentUtil {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String CIPHER_ALGORITHM = "Serpent";
    private static final String KEY_ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";

    /**
     * This Method builds a CipherOutputStream with the Serpent Cipher.
     *
     * @param os is the output Stream where the Encrypted Data should went.
     * @param key is the Key for the Encryption.
     * @return a CipherOutputStream with the Serpent Cipher.
     */
    public static CipherOutputStream getOutputStream(OutputStream os, Key key) {
        try {
            Cipher instance = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
            instance.init(Cipher.ENCRYPT_MODE, key, generateIV(instance));
            return new CipherOutputStream(os, instance);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidAlgorithmParameterException ex) {

            Logger.getLogger(SerpentUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Creates a CipherInputStream which reads from a InputStream where exist
     * Data which is encrypted with Serpent.
     *
     * @param is is the InputStream which contains the encrypted Data.
     * @param key is the Key for the encryption
     * @return a CipherInputStream which wraps the given InputStream.
     * @throws IOException if the Password is wrong.
     */
    public static CipherInputStream getInputStream(InputStream is, Key key) throws IOException {
        try {
            Cipher instance = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
            instance.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[instance.getBlockSize()]));
            return new CipherInputStream(is, instance);
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException ex) {
            throw new IOException(ex);
        } catch (InvalidKeyException ex) {
            throw new IOException("Invalid Key", ex);
        }
    }

    /**
     * Generate the key for the en-/decryption from the password and the Salt.
     *
     * @param password is the password for the en-/decryption.
     * @param salt is the salt for the en-/decryption.
     * @return the SecretKey for the en-/decryption.
     * @throws IOException is thrown if Something went wrong while the Key is
     * generated. Should not happend.
     */
    public static SecretKey generateKey(byte[] password, byte[] salt) throws IOException {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM, "BC");
            PBEKeySpec spec = new PBEKeySpec(new String(password).toCharArray(), salt, 100000);
            SecretKey temp = factory.generateSecret(spec);
            byte[] keybytes = new byte[16];
            System.arraycopy(temp.getEncoded(), 0, keybytes, 0, keybytes.length);
            return new SecretKeySpec(keybytes, CIPHER_ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException ex) {
            Logger.getLogger(SerpentUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex);
        }
    }

    /**
     * Generate the IvParameterSpec.
     *
     * @param c is the Cipher for the en-/decryption.
     * @return the IvParameterSpec for the given Cipher.
     */
    private static IvParameterSpec generateIV(Cipher c) {
        try {
            byte[] iv = new byte[c.getBlockSize()];
            SecureRandom.getInstanceStrong().nextBytes(iv);
            return new IvParameterSpec(iv);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
}
