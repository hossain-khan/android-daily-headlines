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

package info.hossainkhan.android.core.headlines;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.List;

import info.hossainkhan.android.core.CoreApplication;
import info.hossainkhan.android.core.R;
import info.hossainkhan.android.core.base.BasePresenter;
import info.hossainkhan.android.core.data.CategoryNameResolver;
import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.android.core.model.NavigationRow;
import info.hossainkhan.android.core.model.NewsProvider;
import info.hossainkhan.android.core.newsprovider.NyTimesNewsProvider;
import io.swagger.client.ApiClient;
import io.swagger.client.api.ConsumptionFormat;
import io.swagger.client.api.StoriesApi;
import io.swagger.client.model.Article;
import io.swagger.client.model.ArticleCategory;
import io.swagger.client.model.InlineResponse200;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class HeadlinesPresenter extends BasePresenter<HeadlinesContract.View> implements HeadlinesContract.Presenter {

    private final List<NewsProvider> mNewsProviders;
    private final Context mContext;

    public HeadlinesPresenter(final Context context, final HeadlinesContract.View view, final List<NewsProvider>
            newsProviders) {
        attachView(view);
        mContext = context;
        mNewsProviders = newsProviders;
        loadHeadlines(false);
    }

    @Override
    public void loadHeadlines(final boolean forceUpdate) {
        for (final NewsProvider newsProvider : mNewsProviders) {
            if(NyTimesNewsProvider.PROVIDER_ID_NYTIMES.equals(newsProvider.getNewsSource().id())) {
                loadNyTimesHeadlines(newsProvider);
            }
            else {
                // In future need to support RSS/ATOM based provider loading
                Timber.w("Unsupported news provider: %s", newsProvider);
            }
        }

    }

    private void loadNyTimesHeadlines(final NewsProvider newsProvider) {
        ApiClient apiClient = CoreApplication.getAppComponent().getApiClient();
        StoriesApi service = apiClient.createService(StoriesApi.class);

        final List<ArticleCategory> categories = new ArrayList<>(CategoryNameResolver.getPreferredCategories(mContext));

        int sectionSize = categories.size();
        List<Observable<InlineResponse200>> observableList = new ArrayList<>(sectionSize);

        Timber.i("Loading categories: %s", categories);

        // NOTE: Unable to use java8 lambda using jack. Error: Library projects cannot enable Jack (Java 8).
        // ASOP Issue # https://code.google.com/p/android/issues/detail?id=211386
        Observable.from(categories).subscribe(new Action1<ArticleCategory>() {
            @Override
            public void call(ArticleCategory articleCategory) {
                observableList.add(service.sectionFormatGet(articleCategory.name(), ConsumptionFormat.json.name(), null));
            }
        });


        getView().toggleLoadingIndicator(true);
        Subscription subscription = Observable.merge(observableList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .single()
                .subscribe(new Subscriber<List<InlineResponse200>>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("onCompleted() called");
                        getView().toggleLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Failed to load responses.");
                        getView().toggleLoadingIndicator(false);

                        getView().showDataLoadingError();
                        CoreApplication.getAnalyticsReporter().reportHeadlineLoadingError();
                        FirebaseCrash.report(e);
                    }

                    @Override
                    public void onNext(List<InlineResponse200> inlineResponse200s) {
                        int totalResponseItemSize = inlineResponse200s.size();
                        Timber.i("Got total responses: %d", totalResponseItemSize);

                        if (totalResponseItemSize != sectionSize) {
                            // Error
                            FirebaseCrash.log("Unable to get all responses.");
                        } else {
                            List<NavigationRow> navigationHeadlines = new ArrayList<>(totalResponseItemSize+1);
                            navigationHeadlines.add(NavigationRow.builder()
                                    .title(newsProvider.getNewsSource().name())
                                    .type(NavigationRow.TYPE_SECTION_HEADER)
                                    .build());

                            for (int i = 0; i < totalResponseItemSize; i++) {
                                ArticleCategory articleCategory = categories.get(i);
                                navigationHeadlines.add(
                                        NavigationRow.builder()
                                                .title(mContext.getString(CategoryNameResolver
                                                        .resolveCategoryResId(articleCategory)))
                                                .category(articleCategory)
                                                .cards(convertArticleToCardItems(inlineResponse200s.get(i).getResults()))
                                                .build()
                                );
                            }
                            getView().showHeadlines(navigationHeadlines);
                        }
                    }
                });
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
            cardItems.add(CardItem.create(result));
        }
        return cardItems;
    }

    @Override
    public void openHeadlineDetails(@NonNull final CardItem cardItem) {

    }

    @Override
    public void onHeadlineItemSelected(@NonNull final CardItem cardItem) {
        CoreApplication.getAnalyticsReporter().reportHeadlineSelectedEvent(cardItem);
        if (cardItem.imageUrl() !=null) {
            getView().showHeadlineBackdropBackground(cardItem.getImageURI());
        } else {
            Timber.i("Card object does not have HD background.");
        }
    }

    @Override
    public void onHeadlineItemClicked(@NonNull final CardItem cardItem) {
        int id = cardItem.id();
        CardItem.Type type = cardItem.type();
        if (type == CardItem.Type.ICON) {
            if (id == R.string.settings_card_item_news_source_title) {
                CoreApplication.getAnalyticsReporter().reportSettingsScreenLoadedEvent(mContext.getString(id));
                getView().showAppSettingsScreen();
            } else if(id == R.string.settings_card_item_about_app_title) {
                CoreApplication.getAnalyticsReporter().reportSettingsScreenLoadedEvent(mContext.getString(id));
                getView().showAppAboutScreen();
            } else {
                Timber.w("Unable to handle settings item: %s", cardItem.title());
            }
        } else if(type == CardItem.Type.HEADLINES) {
            CoreApplication.getAnalyticsReporter().reportHeadlineDetailsLoadedEvent(cardItem);
            getView().showHeadlineDetailsUi(cardItem);
        }
    }
}
