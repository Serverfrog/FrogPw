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
package de.serverfrog.pw;

import de.serverfrog.pw.crypt.SerpentUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Bastian Venz
 */
public class ConfigurationUtil {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    private static final String configPath = System.getProperty("user.home") + "/.FrogPw";
    private static final String configFilePath = System.getProperty("user.home") + "/.FrogPw/config.crypted";

    private static final String saltFilePath = System.getProperty("user.home") + "/.FrogPw/salt.crypted";

    /**
     * This Method write a List of Configurations into the default File. The
     * File is with Seprent encrypted and has a 256 Bit Salt.
     *
     * @param c is the list of configurations which should be saved.
     * @param password is the password with which the file is encrypted.
     * @throws IOException is thrown if something went wrong while the IO
     * Process.
     */
    public static void writeConfig(ArrayList<Configuration> c, byte[] password) throws IOException {
        try {
            File configFolder = new File(configPath);

            if (!configFolder.exists()) {
                configFolder.mkdirs();
            }
            if (!configFolder.canRead() || !configFolder.canWrite()) {
                throw new IOException("Can't Read or Write into Configuration Folder \""
                        + configFolder.getAbsolutePath() + "\"");
            }
            File configFile = new File(configFilePath);
            if (!configFile.exists()) {
                configFile.createNewFile();
            }
            File saltFile = new File(saltFilePath);

            byte[] salt = new byte[256];

            if (!saltFile.exists()) {
                saltFile.createNewFile();
                salt = SecureRandom.getInstanceStrong().generateSeed(256);

                try (FileOutputStream fos = new FileOutputStream(saltFile);) {
                    fos.write(salt);
                }
            }
            FileReader fr = new FileReader(saltFile);
            int i = 0;
            while (fr.ready()) {
                salt[i] = (byte) fr.read();
            }

            SecretKey generateKey = SerpentUtil.generateKey(password, salt);

            try (FileOutputStream fos = new FileOutputStream(configFilePath);
                    CipherOutputStream outputStream = SerpentUtil.getOutputStream(fos, generateKey);
                    ObjectOutputStream oss = new ObjectOutputStream(outputStream)) {
                oss.writeObject(c);
            } catch (Exception ex) {
                configFile.delete();
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ConfigurationUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This Method reads from the default Location the "config.crypted" and the
     * "salt.crypted" File an return that Encrypted Configuration File which is
     * encrypted with the given password.
     *
     * @param password is the password of the encrypted configuration file.
     * @return a List of Configurations whiche were saved in the encrypted
     * files.
     * @throws IOException if something was wrong while reading the files and/or
     * converting them into objects.
     */
    public static ArrayList<Configuration> readConfig(byte[] password) throws IOException {
        try {
            ObjectInputStream ois = null;
            File saltFile = new File(saltFilePath);

            byte[] salt = new byte[256];

            FileReader fr = new FileReader(saltFile);
            int i = 0;
            while (fr.ready()) {
                salt[i] = (byte) fr.read();
            }

            SecretKey generateKey = SerpentUtil.generateKey(password, salt);
            try (FileInputStream fis = new FileInputStream(configFilePath);
                    CipherInputStream inputStream = SerpentUtil.getInputStream(fis, generateKey);) {
                byte[] toByteArray = IOUtils.toByteArray(inputStream);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(toByteArray);
                try {
                    ois = new ObjectInputStream(byteArrayInputStream);
                } catch (java.lang.NullPointerException ex) {
                    throw new IOException(ex);
                }
                ArrayList readObject = (ArrayList) ois.readObject();
                ArrayList<Configuration> configs = new ArrayList<>();
                for (Object object : readObject) {
                    if (!(object instanceof Configuration)) {
                        throw new RuntimeException("Loaded Class was not from type Configuration. Class=" + object.getClass());
                    }
                    configs.add((Configuration) object);
                }
                return configs;
            } finally {
                if (ois != null) {
                    ois.close();
                }
            }
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Could not Find class.", ex);
        }
    }

    /**
     * Add a Configuration to the encrypted Savefile.
     *
     * @param c is the Configration which should be added.
     * @param password is the password of the encrypted file.
     * @throws IOException is thrown if something went wrong in the IO Process.
     */
    public static void addConfiguration(Configuration c, byte[] password) throws IOException {
        ArrayList<Configuration> readConfig = new ArrayList<>();
        if (exist()) {
            readConfig = readConfig(password);
        }
        if (readConfig.contains(c)) {
            readConfig.add(readConfig.indexOf(c), c);
        } else {
            readConfig.add(c);
        }
        writeConfig(readConfig, password);
    }

    /**
     * This method checks if every file which is needed to read or write a
     * configuration is existing.
     *
     * @return True if the encrypted configuration File and the Salt file exist.
     */
    public static boolean exist() {
        return new File(configFilePath).exists() && new File(saltFilePath).exists();
    }
}
