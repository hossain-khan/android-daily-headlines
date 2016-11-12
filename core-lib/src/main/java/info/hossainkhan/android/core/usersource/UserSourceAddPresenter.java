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

package info.hossainkhan.android.core.usersource;

import android.content.Context;

import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.Callback;
import com.pkmmte.pkrss.PkRSS;

import java.util.List;

import info.hossainkhan.android.core.CoreApplication;
import info.hossainkhan.android.core.CoreConfig;
import info.hossainkhan.android.core.base.BasePresenter;
import timber.log.Timber;

public class UserSourceAddPresenter
        extends BasePresenter<UserSourceAddContract.View>
        implements UserSourceAddContract.Presenter, Callback {

    private final UserSourceProvider mUserSourceProvider;
    private final Context mContext;
    private String mNewsSourceTitle;
    private String mNewsSourceUrl;

    public UserSourceAddPresenter(final Context context, final UserSourceAddContract.View view,
                                  final UserSourceProvider userSourceProvider) {
        mContext = context;
        mUserSourceProvider = userSourceProvider;
        attachView(view);

    }


    @Override
    public void addNewSource(final String newsSourceTitle, final String newsSourceUrl) {
        mNewsSourceTitle = newsSourceTitle;
        mNewsSourceUrl = newsSourceUrl;


        // Initiate the real network request to do validation
        PkRSS.with(mContext)
                .load(newsSourceUrl)
                .callback(this)
                .async();
    }

    //
    // com.pkmmte.pkrss.Callback
    //

    @Override
    public void onPreload() {
        getView().toggleValidationProgressIndicator(true);
    }

    @Override
    public void onLoaded(final List<Article> list) {
        if (isViewAttached()) {
            getView().toggleValidationProgressIndicator(false);
            // Show loaded x items and add to news source list.
            int totalFeedItems = list.size();
            if (totalFeedItems < CoreConfig.MINIMUM_FEED_ITEM_REQUIRED) {
                CoreApplication.getAnalyticsReporter().reportAddNewsSourceEvent(mNewsSourceTitle, false);
                getView().showSourceValidationFailed(totalFeedItems);
            } else {
                CoreApplication.getAnalyticsReporter().reportAddNewsSourceEvent(mNewsSourceTitle, true);
                mUserSourceProvider.addSource(mNewsSourceTitle, mNewsSourceUrl);
                getView().showSourceAddedMessage();
            }
        } else {
            Timber.d("View is already detached. Ignore validation result.");
        }
    }

    @Override
    public void onLoadFailed() {
        if (isViewAttached()) {
            CoreApplication.getAnalyticsReporter().reportAddNewsSourceEvent(mNewsSourceTitle, false);
            getView().toggleValidationProgressIndicator(false);
            getView().showUrlLoadFailedMessage();
        } else {
            Timber.d("View is already detached. Ignore validation result.");
        }
    }

}
