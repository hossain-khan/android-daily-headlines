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
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import info.hossainkhan.android.core.CoreApplication;
import info.hossainkhan.android.core.data.CategoryNameResolver;
import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.android.core.model.NavigationRow;
import info.hossainkhan.android.core.model.NewsHeadlines;
import info.hossainkhan.android.core.model.NewsProvider;
import info.hossainkhan.android.core.model.NewsSource;
import io.swagger.client.ApiClient;
import io.swagger.client.api.ConsumptionFormat;
import io.swagger.client.api.StoriesApi;
import io.swagger.client.model.Article;
import io.swagger.client.model.ArticleCategory;
import io.swagger.client.model.InlineResponse200;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


/**
 * New York Times news provider.
 * <p>
 * <h3>ATTRIBUTION</h3>
 * <p>
 * <p>Your use of any of the API content, whether served from your Web site or from a client application,
 * must appropriately attribute The New York Times by adhering to the usage guidelines in
 * <a href="/branding">The New York Times API Branding Guide</a>.</p>
 * <p>
 * <p>Any URLs that are delivered in the API content must link in each instance to the related New York Times URL.
 * You shall not display the API content in such a manner that does not allow for successful linking and redirection to,
 * and delivery of, NYTIMES.COM's Web page, nor may you frame any NYTIMES.COM Web page.</p>
 * <p>
 * <a href="https://developer.nytimes.com/attribution">https://developer.nytimes.com/attribution</a>
 */
public final class NyTimesNewsProvider implements NewsProvider {

    public static final String PROVIDER_ID_NYTIMES = "nytimes";
    /**
     * For applications that do not easily support logos or where Times data are used in alternative media formats,
     * the written attribution "Data provided by The New York Times" can be substituted.
     */
    private static final String PROVIDER_NAME = "The New York Times";

    /**
     * NYTimes source info.
     */
    private static final String PROVIDER_DESCRIPTION = "Data provided by The New York Times";

    /**
     * All applications must be accompanied by a Times API logo on any page or screen that displays
     * Times API content or data. The logo must link directly to http://developer.nytimes.com.
     */
    private static final String PROVIDER_URL = "http://developer.nytimes.com";
    /**
     * Taken from https://developer.nytimes.com/branding
     */
    private static final String PROVIDER_IMAGE_URL = "http://static01.nytimes.com/packages/images/developer/logos/poweredby_nytimes_200a.png";

    /**
     * RESTRICTION: Unless otherwise consented to or permitted by NYTIMES.COM, you will not archive or cache any
     * of the API content for access by users for more than 24 hours after you have finished using the service;
     * or for any period of time if your account is terminated.
     */
    private static final long MAX_CACHE_LENGTH = TimeUnit.HOURS.toSeconds(24);


    /**
     * Create the NYTimes news source instance with required info.
     */
    private NewsSource mNewsSource = NewsSource.Companion.create(PROVIDER_ID_NYTIMES, PROVIDER_NAME, PROVIDER_DESCRIPTION,
            PROVIDER_URL, PROVIDER_IMAGE_URL, MAX_CACHE_LENGTH);

    @Override
    public NewsSource getNewsSource() {
        return mNewsSource;
    }

    @Override
    public Set<ArticleCategory> getSupportedCategories() {
        Set<ArticleCategory> categories = new HashSet<>();
        categories.add(ArticleCategory.home);
        categories.add(ArticleCategory.world);
        categories.add(ArticleCategory.business);
        categories.add(ArticleCategory.technology);
        categories.add(ArticleCategory.movies);
        categories.add(ArticleCategory.sports);

        return categories;
    }

    @Override
    public Observable<NewsHeadlines> getNewsObservable() {
        /*
         * NY Times has multiple categories.
         * For each category, load headlines
         * Map in to a list of Navigation Rows
         * Finally, provide the headlines with news source.
         */
        final Context mContext = CoreApplication.getAppComponent().getContext();
        ApiClient apiClient = CoreApplication.getAppComponent().getApiClient();
        StoriesApi service = apiClient.createService(StoriesApi.class);

        final List<ArticleCategory> categories = new ArrayList<>(CategoryNameResolver.getPreferredCategories(mContext));

        Timber.i("Loading categories: %s", categories);

        return Observable.from(categories)
                .flatMap(category ->
                        service.sectionFormatGet(category.name(), ConsumptionFormat.json.name(), null)
                                .map(response -> new Pair<>(category, response))

                ).map(categoryResponse ->
                        convertResponseToNavigationRow(mContext, categoryResponse.first, categoryResponse.second)
                )
                .toList()
                .map(list ->
                        new NewsHeadlines(mNewsSource, list)
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    private NavigationRow convertResponseToNavigationRow(@NonNull final Context mContext,
                                                         @NonNull final ArticleCategory category,
                                                         @NonNull final InlineResponse200 response200) {
        return NavigationRow.Companion.builder()
                .title(mContext.getString(CategoryNameResolver
                        .resolveCategoryResId(category)))
                .category(category)
                .sourceId(mNewsSource.getId())
                .cards(convertArticleToCardItems(response200.getResults()))
                .build();
    }


    /**
     * Converts {@link Article} list into generic {@link CardItem} model.
     * <p>
     * <br/>
     * Check if we can use "adapter" or "factory" pattern to standardize this.
     *
     * @param articles List of articles.
     * @return List of converted {@link CardItem}.
     */
    private List<CardItem> convertArticleToCardItems(final List<Article> articles) {
        List<CardItem> cardItems = new ArrayList<>(articles.size());
        for (Article result : articles) {
            cardItems.add(CardItem.Companion.create(result));
        }
        return cardItems;
    }
}
