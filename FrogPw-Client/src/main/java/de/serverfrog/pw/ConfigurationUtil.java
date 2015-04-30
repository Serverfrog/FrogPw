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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Bastian Venz
 */
public class ConfigurationUtil {

    private static final String configPath = System.getProperty("user.home") + "/.FrogPw";
    private static final String configFilePath = System.getProperty("user.home") + "/.FrogPw/config.crypted";

    public static void writeConfig(ArrayList<Configuration> c, byte[] password) throws IOException {
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
        SecretKeySpec keySpec = new SecretKeySpec(password, "AES");
        try (FileOutputStream fos = new FileOutputStream(configFilePath);
                CipherOutputStream outputStream = SerpentUtil.getOutputStream(fos, keySpec);
                ObjectOutputStream oss = new ObjectOutputStream(outputStream)) {
            oss.writeObject(c);
        }
    }

    public static ArrayList<Configuration> readConfig(byte[] password) throws IOException {
        try {
            ObjectInputStream ois = null;
            SecretKeySpec keySpec = new SecretKeySpec(password, "AES");
            try (FileInputStream fis = new FileInputStream(configFilePath);
                    CipherInputStream inputStream = SerpentUtil.getInputStream(fis, keySpec);) {

                try {
                    ois = new ObjectInputStream(inputStream);
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

    public static boolean exist() {
        return new File(configFilePath).exists();
    }
}
