/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Bastian Venz
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.serverfrog.pw;

import java.util.Arrays;
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
        hash = hash(add(hash, new byte[]{Integer.valueOf(size).byteValue()}), size);
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
