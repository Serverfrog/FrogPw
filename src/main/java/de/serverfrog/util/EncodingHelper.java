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
package de.serverfrog.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bastian Venz <admin@serverfrog.de>
 */
public class EncodingHelper {

    public static final char[] a_z
            = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    public static final char[] A_Z
            = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static final char[] NULL_NINE
            = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private final int BYTESIZE = 255;

    private final List<Character> character = new ArrayList<>();

    public EncodingHelper() {
    }

    public EncodingHelper small() {
        return small(1);
    }

    public EncodingHelper small(int count) {
        for (int i = 0; i < count; i++) {
            addAllChars(character, a_z);
        }
        return this;
    }

    public EncodingHelper big() {
        return big(1);
    }

    public EncodingHelper big(int count) {
        for (int i = 0; i < count; i++) {
            addAllChars(character, A_Z);
        }
        return this;
    }

    public EncodingHelper numbers() {
        return numbers(1);
    }

    public EncodingHelper numbers(int count) {
        for (int i = 0; i < count; i++) {
            addAllChars(character, NULL_NINE);
        }
        return this;
    }

    public EncodingHelper custome(char... chars) {
        return custome(1, chars);
    }

    public EncodingHelper custome(int count, char[] chars) {
        for (int i = 0; i < count; i++) {
            addAllChars(character, chars);
        }
        return this;
    }

    public String encode(byte[] decoded) {
        int listSize = character.size();
        if (listSize > BYTESIZE) {
            System.err.println("Amount of Avilable Characters is above the Size"
                    + " of Byte.The Last ones are never used. Chars:" + listSize
                    + ", Byte:255. Thrown away:" + (listSize - BYTESIZE));
        }
        StringBuilder bu = new StringBuilder();
        int remaining;
        for (byte b : decoded) {
            remaining = (int) (Byte.MIN_VALUE * -1) + b;
            do {
                bu.append(character.get(remaining % listSize));
                remaining -= remaining % listSize;
            } while (remaining < 0);
        }
        return bu.toString();
    }

    private static List<Character> addAllChars(List<Character> charList,
            char[] character) {
        if (character == null || character.length == 0) {
            return charList;
        }
        for (char c : character) {
            charList.add(c);
        }
        return charList;
    }

}
