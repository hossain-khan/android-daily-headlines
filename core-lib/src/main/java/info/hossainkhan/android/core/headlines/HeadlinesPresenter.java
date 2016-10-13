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

import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.List;

import info.hossainkhan.android.core.CoreApplication;
import info.hossainkhan.android.core.R;
import info.hossainkhan.android.core.base.BasePresenter;
import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.android.core.model.NavigationRow;
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

    private final List<ArticleCategory> mArticleCategories;

    public HeadlinesPresenter(final HeadlinesContract.View view, List<ArticleCategory> articleCategories) {
        attachView(view);
        mArticleCategories = articleCategories;
        loadHeadlines(false);
    }

    @Override
    public void loadHeadlines(final boolean forceUpdate) {
        ApiClient apiClient = CoreApplication.getAppComponent().getApiClient();
        StoriesApi service = apiClient.createService(StoriesApi.class);

        int sectionSize = mArticleCategories.size();
        List<Observable<InlineResponse200>> observableList = new ArrayList<>(sectionSize);

        Timber.i("Loading categories: %s", mArticleCategories);

        // NOTE: Unable to use java8 lambda using jack. Error: Library projects cannot enable Jack (Java 8).
        // ASOP Issue # https://code.google.com/p/android/issues/detail?id=211386
        Observable.from(mArticleCategories).subscribe(new Action1<ArticleCategory>() {
            @Override
            public void call(ArticleCategory articleCategory) {
                observableList.add(service.sectionFormatGet(articleCategory.name(), ConsumptionFormat.json.name(), null));
            }
        });


        Subscription subscription = Observable.merge(observableList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .single()
                .subscribe(new Subscriber<List<InlineResponse200>>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("onCompleted() called");
                        getView().setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Failed to load responses.");

                        FirebaseCrash.report(e);
                        getView().showLoadingHeadlinesError();
                    }

                    @Override
                    public void onNext(List<InlineResponse200> inlineResponse200s) {
                        int totalResponseItemSize = inlineResponse200s.size();
                        Timber.i("Got total responses: %d", totalResponseItemSize);

                        if (totalResponseItemSize != sectionSize) {
                            // Error
                            FirebaseCrash.log("Unable to get all responses.");
                        } else {
                            List<NavigationRow> navigationHeadlines = new ArrayList<>(totalResponseItemSize);
                            for (int i = 0; i < totalResponseItemSize; i++) {
                                ArticleCategory articleCategory = mArticleCategories.get(i);
                                navigationHeadlines.add(
                                        new NavigationRow.Builder()
                                                .setTitle(articleCategory.name())
                                                .setCategory(articleCategory)
                                                .setCards(convertArticleToCardItems(inlineResponse200s.get(i).getResults()))
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
            cardItems.add(new CardItem(result));
        }
        return cardItems;
    }

    @Override
    public void openHeadlineDetails(@NonNull final CardItem cardItem) {

    }

    @Override
    public void onHeadlineItemSelected(@NonNull final CardItem cardItem) {
        if (cardItem.getImageUrl() !=null) {
            getView().showHeadlineBackdropBackground(cardItem.getImageURI());
        } else {
            Timber.i("Card object does not have HD background.");
        }
    }

    @Override
    public void onHeadlineItemClicked(@NonNull final CardItem cardItem) {
        int id = cardItem.getId();
        CardItem.Type type = cardItem.getType();
        if (type == CardItem.Type.ICON) {
            if (id == R.string.settings_card_item_news_source_title) {
                getView().showAppSettingsScreen();
            } else {
                Timber.w("Unable to handle settings item: %s", cardItem.getTitle());
            }
        }
    }
}
