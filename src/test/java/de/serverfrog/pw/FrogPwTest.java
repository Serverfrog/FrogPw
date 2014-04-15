/*
 * The MIT License
 *
 * Copyright 2014 Bastian Venz.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.serverfrog.pw;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bastianvenz
 */
public class FrogPwTest {

    @Test
    public void testPwTwentyTimes() {
        for (int i = 0; i < 20; i++) {
            testPw();
        }
    }

    public void testPw() {
        WebsiteBuilder wb = new WebsiteBuilder();
        wb.setAddress("https://https://github.com/Serverfrog/FrogPw");
        wb.setType(WebsiteType.L33T);
        String generatedCase1 = SHA3Util.generatePassword(wb.createWebsite(),
                "TestCase1".getBytes(), 24);
        assertEquals(24, generatedCase1.length());
        String generatedCase2 = SHA3Util.generatePassword(wb.createWebsite(),
                "TestCase2".getBytes(), 24, 2, 2, 2);
        assertEquals(24, generatedCase2.length());
        String generatedCase3 = SHA3Util.generatePassword(wb.createWebsite(),
                "TestCase2".getBytes(), 24, 1, 1, 1, 1, "!\"§$%&".toCharArray());
        assertEquals(24, generatedCase3.length());

    }
}
