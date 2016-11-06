/*
 * MIT License
 *
 * Copyright (c) 2016 Hossain Khan
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

package info.hossainkhan.android.core.newsprovider;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import info.hossainkhan.android.core.model.NewsSource;


/**
 * News source for 9to5mac.
 */
public class Nine2FiveFeedNewsProvider extends RssFeedNewsProvider {
    public static final String FEED_URL = "https://9to5mac.com/feed/";
    private NewsSource mNewsSource = NewsSource.create(
            "9to5mac",
            "9to5 Mac",
            "At 9to5, we make great efforts to be a top influencer in the tech community by consistently breaking exclusive news and being the first to report information our readers care about.",
            "https://9to5mac.com/",
            "https://9to5mac.files.wordpress.com/2016/07/9to5-mac-logo-min.png",
            TimeUnit.HOURS.toSeconds(24));

    public Nine2FiveFeedNewsProvider(final Context context) {
        super(context);
    }

    @Override
    String getFeedUrl() {
        return FEED_URL;
    }

    @Override
    public NewsSource getNewsSource() {
        return mNewsSource;
    }
}
