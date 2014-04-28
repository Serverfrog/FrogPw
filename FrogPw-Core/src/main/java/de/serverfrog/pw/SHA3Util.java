/*
 * Copyright (C) 2014 Bastian Venz
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

import org.fsf.maandree.ConcurrentSHA3;

/**
 *
 * @author serverfrog
 */
public final class SHA3Util {

    private SHA3Util() {
    }

    public static String generatePassword(Website website, byte[] password,
            int size) {
        return generatePassword(website, password, size, 1, 1, 1);
    }

    public static String generatePassword(Website website, byte[] password,
            int size, int smallCount, int bigCount, int numberCount) {
        return generatePassword(website, password, size, smallCount, bigCount,
                numberCount, 0, null);
    }

    public static String generatePassword(Website website, byte[] password,
            int size, int smallCount, int bigCount, int numberCount,
            int customeCount, char[] customeChars) {
        String passwordEnd;
        EncodingHelper eh = new EncodingHelper();
        if (smallCount > 0) {
            eh.small(smallCount);
        }
        if (bigCount > 0) {
            eh.big(bigCount);
        }
        if (numberCount > 0) {
            eh.numbers(numberCount);
        }
        if (customeCount > 0) {
            if (customeChars != null && customeChars.length > 0) {
                eh.custome(customeCount, customeChars);
            }
        }
        StringBuilder sb1 = new StringBuilder();
        for (Character character : eh.getCharacters()) {
            sb1.append(character);
        }
        String chars = sb1.toString();
        byte[] hash = password;
        hash = add(hash, website.getAddress().getBytes());
        hash = add(hash, website.getType().getName().getBytes());
        hash = add(hash, website.getType().getKey().getBytes());
        hash = add(hash, chars.getBytes());
        hash = hash(add(hash, hash(new byte[]{Integer.valueOf(size).byteValue()})), size);
        passwordEnd = eh.encode(hash);
        return passwordEnd;
    }

    private static byte[] hash(byte[] bytes) {
        return hash(bytes, 256);
    }

    private static byte[] hash(byte[] bytes, int size) {
        byte[] hashbytes = new byte[bytes.length];
        SHA3Util.arraycopy(bytes, 0, hashbytes, 0, bytes.length);
        ConcurrentSHA3 sHA3 = new ConcurrentSHA3();
        sHA3.initialise(512, 512, size * 8);
        for (byte b : hashbytes) {
            sHA3.update(new byte[]{b, 1});
            sHA3.simpleSqueeze(32);
        }
        return sHA3.digest();
    }

    public static void arraycopy(long[] src, int srcPos,
            long[] dest, int destPos, int length) {
        for (int i = 0; i < length; i++) {
            dest[destPos + i] = src[srcPos + i];
        }
    }

    public static void arraycopy(byte[] src, int srcPos,
            byte[] dest, int destPos, int length) {
        for (int i = 0; i < length; i++) {
            dest[destPos + i] = src[srcPos + i];
        }
    }

    private static byte[] add(byte[] bytes, byte[] toAdd) {
        byte[] addBytes = new byte[toAdd.length + bytes.length];
        SHA3Util.arraycopy(bytes, 0, addBytes, 0, bytes.length);
        SHA3Util.arraycopy(toAdd, 0, addBytes, bytes.length, toAdd.length);
        return addBytes;
    }

}
