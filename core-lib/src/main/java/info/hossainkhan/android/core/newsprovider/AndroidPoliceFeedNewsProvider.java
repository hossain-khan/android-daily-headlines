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
import android.support.annotation.NonNull;

import com.nytimes.android.external.store3.base.impl.BarCode;
import com.nytimes.android.external.store3.base.impl.Store;
import com.nytimes.android.external.store3.base.impl.StoreBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import info.hossainkhan.android.core.model.NavigationRow;
import info.hossainkhan.android.core.model.NewsSource;

/**
 * Android Police news provider using RSS feed.
 */
public class AndroidPoliceFeedNewsProvider extends RssFeedNewsProvider {
    private static final String FEED_URL = "http://feeds.feedburner.com/AndroidPolice";
    private NewsSource mNewsSource = NewsSource.Companion.create(
            "android_police",
            "Android Police",
            "Android Police is a blog dedicated to everything related to Android. We hope you enjoy our writing and subscribe to updates using the buttons on the right.",
            "http://www.androidpolice.com/",
            "http://www.androidpolice.com/wp-content/themes/ap2/images/android-police-logo-ns.png?nocache=1",
            TimeUnit.HOURS.toSeconds(24));

    public AndroidPoliceFeedNewsProvider(final Context context) {
        super(context);
    }

    @Override
    public String getFeedUrl() {
        return FEED_URL;
    }

    @Override
    public NewsSource getNewsSource() {
        return mNewsSource;
    }
}
