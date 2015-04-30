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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Bastian Venz
 */
public class SerpentUtil {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static CipherOutputStream getOutputStream(OutputStream os, Key key) {
        try {
            Cipher instance = Cipher.getInstance("Serpent", "BC");
            instance.init(Cipher.ENCRYPT_MODE, key);
            return new CipherOutputStream(os, instance);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException ex) {
            Logger.getLogger(SerpentUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static CipherInputStream getInputStream(InputStream os, Key key) throws IOException {
        try {
            Cipher instance = Cipher.getInstance("Serpent", "BC");
            instance.init(Cipher.DECRYPT_MODE, key);
            return new CipherInputStream(os, instance);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException ex) {
            if (ex.getClass().equals(InvalidKeyException.class)) {
                throw new IOException("Password Wrong");
            }
            Logger.getLogger(SerpentUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Print all Security providers and their algos
     *
     * @param args args
     * @throws Exception Exception
     */
    public static void main(String[] args) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SecretKeySpec keySpec = new SecretKeySpec("test".getBytes("UTF-8"), "AES");
        try (CipherOutputStream outputStream = SerpentUtil.getOutputStream(byteArrayOutputStream, keySpec)) {
            IOUtils.write("TEST", outputStream);
        }
        System.out.println(Arrays.toString(byteArrayOutputStream.toByteArray()));
        System.out.println(byteArrayOutputStream.toString("UTF-8"));
        byte[] toByteArray = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(toByteArray);
        CipherInputStream inputStream = SerpentUtil.getInputStream(arrayInputStream, new SecretKeySpec("test".getBytes("UTF-8"), "AES"));
        byte[] bytes = new byte[1048576];

        IOUtils.read(inputStream, bytes);

        System.out.println(new String(bytes, "UTF-8").trim());

//
//        for (Provider provider : Security.getProviders()) {
//            System.out.println("");
//            System.out.println("");
//            System.out.println("");
//            System.out.println("-------------------------------");
//            System.out.println("Name: " + provider.getName());
//            System.out.println("Info: " + provider.getInfo());
//            for (Map.Entry<Object, Object> entry : provider.entrySet()) {
//                System.out.println("Key: Class:" + entry.getKey().getClass() + " String: " + entry.getKey());
//                System.out.println("Value: Class:" + entry.getValue().getClass() + " String: " + entry.getValue());
//            }
//            for (Provider.Service service : provider.getServices()) {
//                System.out.println("Service: Algorithm:" + service.getAlgorithm()
//                        + " ClassName:" + service.getClassName()
//                        + " Type:" + service.getType() + " toString:" + service.toString());
//            }
//            for (Object object : provider.values()) {
//                System.out.println("Value: " + object.getClass() + " toString:" + object.toString());
//
//            }
//            System.out.println("-------------------------------");
//        }
    }
}
