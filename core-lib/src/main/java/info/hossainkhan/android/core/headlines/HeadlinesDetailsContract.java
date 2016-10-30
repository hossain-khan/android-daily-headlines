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

import info.hossainkhan.android.core.base.MvpPresenter;
import info.hossainkhan.android.core.base.MvpView;
import info.hossainkhan.android.core.model.CardItem;

/**
 * Contract for headlines details view
 */
public interface HeadlinesDetailsContract {
    int ACTION_ID_OPEN_NEWS_URL = 101;

    interface View extends MvpView {
        void updateScreenTitle(String title);

        void openArticleWebUrl(String contentUrl);

        void showHeadlineDetails(CardItem cardItem);
    }

    interface Presenter extends MvpPresenter<HeadlinesDetailsContract.View> {
        void onActionItemClicked(final int action);
    }
}
