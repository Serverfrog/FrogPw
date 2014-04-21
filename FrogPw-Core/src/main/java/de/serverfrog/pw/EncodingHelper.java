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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author serverfrog
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

    private final List<Character> characters = new ArrayList<>();

    public EncodingHelper() {
    }

    public EncodingHelper small() {
        return small(1);
    }

    public EncodingHelper small(int count) {
        for (int i = 0; i < count; i++) {
            addAllChars(a_z);
        }
        return this;
    }

    public EncodingHelper big() {
        return big(1);
    }

    public EncodingHelper big(int count) {
        for (int i = 0; i < count; i++) {
            addAllChars(A_Z);
        }
        return this;
    }

    public EncodingHelper numbers() {
        return numbers(1);
    }

    public EncodingHelper numbers(int count) {
        for (int i = 0; i < count; i++) {
            addAllChars(NULL_NINE);
        }
        return this;
    }

    public EncodingHelper custome(char... chars) {
        return custome(1, chars);
    }

    public EncodingHelper custome(int count, char[] chars) {
        for (int i = 0; i < count; i++) {
            addAllChars(chars);
        }
        return this;
    }

    public String encode(byte[] decoded) {
        int listSize = characters.size();
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
                bu.append(characters.get(remaining % listSize));
                remaining -= remaining % listSize;
            } while (remaining < 0);
        }
        return bu.toString();
    }

    private void addAllChars(char[] charAr) {
        if (charAr == null || charAr.length == 0) {
            return;
        }
        for (char c : charAr) {
            characters.add(c);
        }
    }

    public List<Character> getCharacters() {
        return characters;
    }

}
