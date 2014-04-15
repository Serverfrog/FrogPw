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

/**
 *
 * @author bastianvenz
 */

public enum WebsiteType {

    DEFAULT("default", "$3A=mC6X5^*Qbae-GfUr,7V&@KD%p&'rP;%EKzYC.rf!e^aR]dX=.L"
            + "d?}^u.dC!8DE&Rd_DEb?9Jts646}DaP]]s)g!WsP@G#ST2qemWN(N.vCD#h*Y4!"
            + "+UGNRZkR6Ys"),
    INFORMATION("Information", "j8RP<[KW%,8)E3cL%[EmH77>=m5=jh=KZ$vP,M86V/4hq4z"
            + "-kqr.K8AK5-X<>dxcq%3Q$b/_'Tyh-Hzu-)8qh[*XdgCMCMQ$a#g\"=H8kJc,{S@"
            + "!NG;7LDH3R'[RT:[3T"),
    SOCIAL("Social", "W~TDa?=X?`-8:>()F!TCet9W&:;jj4xn7Wm&kqvb+95q7.aHHs$4A^J8K"
            + "T(+)DV%923RA)&3B8H<bZ`P*@E`aemqCTr*e9).-G?F[vj[c9{R}}SgV%cu-4u'"
            + "dwNa9Vgy"),
    DEVELOPMENT("Development", "7H$Rtsg-)DZT2v_j@B2cGWF{b~)tH+kNppk}`T.K9P'bshL"
            + "4S'T(@JrXbScVN~MD&D/A<77Gj[_uNj:_9Dk.`%e<zFBx&~2]hKM;s'5m'TU6UTL"
            + ":P~5rV,vyyanD*@Jb"),
    GAMES("Games", "bb4fL3vQ@CfFG8Zf%D9&k%YxG#L8:8Atx7=egHc2RW4CRw2@3#*vj6-T6<*"
            + "NS.>M>&_KK9bBd}`t;5^N@cr&WXJ(s$2=m25YS6;qtQEk,.=j79/5yZR_g)~SM'2"
            + ",BM5E"),
    MEDIA("Media", "([t9c',`'{/'$Q'_ZG6C[uQv2'Yz`>z~9ZDb8*)&*,bwHy.aHMuD/SmM2xq"
            + "(y-[')?/sHKFDP6%9{YBeWW$#AWY57T[Z,D_mb:bWr_3CJ*b~eC58eGS5V7K,p2H"
            + "CVt()"),
    FUN("Fun", "RaK6h+5NgBLEu/6~R*aHv;N>ynT5/}8;cs<tS3}T#NuYY<y%qFX3;WVAe~SXkf("
            + "WhK[B+w3N;$Vg9%%aG..LUq4D&_{2Q+h}gct$gH=vQ(8EXu8W5_nZ}x:,sYras["
            + "6-"),
    L33T("1337", "NT~:xj9Q/w\"'UWva+Xw>]%`)H~RC55Ept4D2R6vX;YW=5g6[;MbP2uJ}2L~u"
            + "4[qUJk`THqtc\"sL\",gt/BM\"XUUN^hWH\"au.kyRqs&2VBCnXVa$GrH(dw'huZ"
            + ",$g>He=E"),
    UTILITY("Utility", "tB!4SdNT~:+-3WzQN:3v'R4[M8u96fdnYzNbNc5f?;2/H?/Z/6B_c)P"
            + "]\";vmhG.7s#~;d`'s;aA)s\"<WuBUD:@[,L#XS3j'.yC@?XCD5en3qDQC=U%YVU"
            + "_b}R]y^:;?U"),
    REMOTE_LOGIN("Remote Login", "g-8rY9-L~8K`btK))8u'FPq.=NL2z56@S<,m!m}=#yQF.["
            + "'<kc9!VP}$<`'+pKQSaB7MdK,MX8A%^W78J.2@B?*wmcx8sq9^Q)'Vr9hrht5:f?"
            + "VGnM[YyW,~cP;P^@(;");

    private final String name;

    private final String key;

    private WebsiteType(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

}
