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
 * Android Police news provider using RSS feed.
 */
public class UrlFeedNewsProvider extends RssFeedNewsProvider {
    private final String mFeedUrl;
    private final NewsSource mNewsSource;

    /**
     * URL based feed news provider.
     * @param context Application context.
     * @param providerName News provider name.
     * @param feedUrl News provider feed URL.
     */
    public UrlFeedNewsProvider(final Context context, final String providerName, final String feedUrl) {
        super(context);

        mFeedUrl = feedUrl;
        mNewsSource = NewsSource.create(
                providerName,
                providerName,
                "",
                "",
                "",
                TimeUnit.HOURS.toSeconds(24));
    }

    @Override
    public String getFeedUrl() {
        return mFeedUrl;
    }

    @Override
    public NewsSource getNewsSource() {
        return mNewsSource;
    }
}
