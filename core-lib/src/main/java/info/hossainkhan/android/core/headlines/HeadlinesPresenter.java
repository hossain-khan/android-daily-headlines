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

import android.support.annotation.NonNull;

import info.hossainkhan.android.core.CoreApplication;
import info.hossainkhan.android.core.base.BasePresenter;
import io.swagger.client.ApiClient;
import io.swagger.client.api.StoriesApi;
import io.swagger.client.model.Article;
import io.swagger.client.model.InlineResponse200;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class HeadlinesPresenter extends BasePresenter<HeadlinesContract.View> implements HeadlinesContract.Presenter {
    private static final String TAG = "HeadlinesPresenter";

    public HeadlinesPresenter(final HeadlinesContract.View view) {
        attachView(view);
        loadHeadlines(false);
    }

    @Override
    public void loadHeadlines(final boolean forceUpdate) {
        ApiClient apiClient = CoreApplication.getAppComponent().getApiClient();
        StoriesApi service = apiClient.createService(StoriesApi.class);

        Observable<InlineResponse200> observable = service.sectionFormatGet("home", "json", null);

        getView().setLoadingIndicator(true);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InlineResponse200>() {
                    @Override
                    public void onCompleted() {
                        Timber.d(TAG, "onCompleted() called");
                        getView().setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(final Throwable e) {
                        Timber.d(TAG, "onError() called with: e = [" + e + "]");
                        getView().showLoadingHeadlinesError();
                    }

                    @Override
                    public void onNext(final InlineResponse200 inlineResponse200) {
                        Timber.d(TAG, "onNext() called");
                        getView().showHeadlines(inlineResponse200.getResults());
                    }
                });
    }

    @Override
    public void openHeadlineDetails(@NonNull final Article article) {

    }
}
