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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import info.hossainkhan.android.core.model.NavigationRow;
import info.hossainkhan.android.core.model.NewsHeadlines;
import info.hossainkhan.android.core.model.NewsProvider;
import info.hossainkhan.android.core.usersource.UserSourceManager;
import info.hossainkhan.android.core.usersource.UserSourceProvider;
import info.hossainkhan.android.core.util.StringUtils;
import rx.Observable;
import timber.log.Timber;

/**
 * News provider manager that help provide supported news providers and related data.
 */
public class NewsProviderManager {

    private final List<NewsProvider> mNewsProviders;
    private final List<Observable<NewsHeadlines>> mProviderObservableList;
    private final UserSourceProvider mUserSourceProvider;

    public NewsProviderManager(final Context context) {
        mUserSourceProvider = new UserSourceManager(context);
        mNewsProviders = new ArrayList<>(5);
        mProviderObservableList = new ArrayList<>(5); // Prepares list of observables for each news providers.

        NyTimesNewsProvider nyTimesProvider = new NyTimesNewsProvider();
        mProviderObservableList.add(nyTimesProvider.getNewsObservable());
        mNewsProviders.add(nyTimesProvider);
        AndroidPoliceFeedNewsProvider androidPoliceProvider = new AndroidPoliceFeedNewsProvider(context);
        mProviderObservableList.add(androidPoliceProvider.getNewsObservable());
        mNewsProviders.add(androidPoliceProvider);
        Nine2FiveFeedNewsProvider nine2FiveProvider = new Nine2FiveFeedNewsProvider(context);
        mProviderObservableList.add(nine2FiveProvider.getNewsObservable());
        mNewsProviders.add(nine2FiveProvider);

        loadUerProviderFeeds(context);
    }

    /**
     * Loads user provided feed.
     * <p>
     * NOTE: In future release, this will eventually support multiple feed from user.
     *
     * @param context Application context.
     */
    private void loadUerProviderFeeds(final Context context) {

        Map<String, String> map = mUserSourceProvider.getSources();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String title = entry.getValue();
            String url = entry.getKey();

            if (StringUtils.isNotEmpty(title) && StringUtils.isNotEmpty(url)) {
                Timber.d("Loading user provided source `%s` with url `%s`", title, url);
                UrlFeedNewsProvider urlFeedNewsProvider = new UrlFeedNewsProvider(context, title, url);
                mProviderObservableList.add(urlFeedNewsProvider.getNewsObservable());
                mNewsProviders.add(urlFeedNewsProvider);
            }
        }
    }


    /**
     * Get list of supported news providers.
     *
     * @return News provider list.
     */
    public List<NewsProvider> getProviders() {
        return mNewsProviders;
    }


    /**
     * Get list of overservables for each {@link NewsProvider} available via {@link NewsProvider#getNewsObservable()}.
     *
     * @return List of observable for news provider.
     */
    public List<Observable<NewsHeadlines>> getProviderObservable() {
        return mProviderObservableList;
    }
}
