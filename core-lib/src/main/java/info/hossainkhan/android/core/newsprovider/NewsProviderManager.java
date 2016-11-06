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

import info.hossainkhan.android.core.model.NavigationRow;
import info.hossainkhan.android.core.model.NewsProvider;
import rx.Observable;

/**
 * News provider manager that help provide supported news providers and related data.
 */
public class NewsProviderManager {

    private final List<NewsProvider> newsProviders;
    private final List<Observable<List<NavigationRow>>> mProviderObservableList;

    public NewsProviderManager(final Context context) {
        newsProviders = new ArrayList<>(5);
        mProviderObservableList = new ArrayList<>(5); // Prepares list of observables for each news providers.

        NyTimesNewsProvider nyTimesProvider = new NyTimesNewsProvider();
        mProviderObservableList.add(nyTimesProvider.getNewsObservable());
        newsProviders.add(nyTimesProvider);
        AndroidPoliceFeedNewsProvider androidPoliceProvider = new AndroidPoliceFeedNewsProvider(context);
        mProviderObservableList.add(androidPoliceProvider.getNewsObservable());
        newsProviders.add(androidPoliceProvider);
        Nine2FiveFeedNewsProvider nine2FiveProvider = new Nine2FiveFeedNewsProvider(context);
        mProviderObservableList.add(nine2FiveProvider.getNewsObservable());
        newsProviders.add(nine2FiveProvider);

    }


    /**
     * Get list of supported news providers.
     *
     * @return News provider list.
     */
    public List<NewsProvider> getProviders() {
        return newsProviders;
    }


    /**
     * Get list of overservables for each {@link NewsProvider} available via {@link NewsProvider#getNewsObservable()}.
     *
     * @return List of observable for news provider.
     */
    public List<Observable<List<NavigationRow>>> getProviderObservable() {
        return mProviderObservableList;
    }
}
