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

import java.util.List;

import info.hossainkhan.android.core.CoreApplication;
import info.hossainkhan.android.core.R;
import info.hossainkhan.android.core.base.BasePresenter;
import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.android.core.model.NavigationRow;
import info.hossainkhan.android.core.model.NewsProvider;
import info.hossainkhan.android.core.newsprovider.NyTimesNewsProvider;
import rx.Subscriber;
import rx.Subscription;
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
        getView().toggleLoadingIndicator(true);
        Subscription subscription = newsProvider.getNewsObservable()
                .subscribe(new Subscriber<List<NavigationRow>>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("onCompleted() called");
                        getView().toggleLoadingIndicator(false);
                    }

                    @Override
                    public void onError(final Throwable e) {
                        Timber.e(e, "Failed to load responses.");
                        getView().toggleLoadingIndicator(false);

                        getView().showDataLoadingError();
                        CoreApplication.getAnalyticsReporter().reportHeadlineLoadingError();
                        FirebaseCrash.report(e);
                    }

                    @Override
                    public void onNext(final List<NavigationRow> navigationRows) {
                        getView().showHeadlines(navigationRows);
                    }
                });
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
