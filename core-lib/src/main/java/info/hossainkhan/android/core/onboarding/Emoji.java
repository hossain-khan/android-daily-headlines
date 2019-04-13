/*
 * MIT License
 *
 * Copyright (c) 2018 Hossain Khan
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

package info.hossainkhan.android.core.onboarding;

/**
 * Emoji constants using unicode to avoid issue found in following sources
 * - http://stackoverflow.com/questions/16786739/how-to-use-unicode-in-android-resource
 * - https://code.google.com/p/android/issues/detail?id=81341
 * <p>
 * <pre>
 * The workaround (for older phones) appears to be not putting the Emoji into your XML, but instead defining it in code.
 *
 * So you can do something like this:
 * <string name="hooray">Hooray! %1$s</string>
 *
 * Then in code:
 * final String PACKAGE_EMOJI = "\uD83C\uDF81";
 *
 * getString(R.string.hooray, PACKAGE_EMOJI);
 * </pre>
 */
public class Emoji {
    public static final String EYES = "\uD83D\uDC40";
    public static final String EYE_GLASS = "\uD83D\uDC53";
    public static final String TELEVISION = "\uD83D\uDCFA";
    public static final String SMILEY = "\uD83D\uDE03";
    public static final String COMPUTER = "\uD83D\uDCBB";
    public static final String NEWS_PAPER = "\uD83D\uDCF0";
    public static final String COFFEE = "\u2615";
    public static final String LIGHT = "\uD83D\uDCA1";
    public static final String WORM_BUG = "\uD83D\uDC1B";
    public static final String LADY_BUG = "\uD83D\uDC1E";
    public static final String HAMMER = "\uD83D\uDD28";
    public static final String WRENCH = "\uD83D\uDD27";
    public static final String THUMBS_UP = "\uD83D\uDC4D";
}
