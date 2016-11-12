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

import info.hossainkhan.android.core.base.MvpPresenter;
import info.hossainkhan.android.core.base.MvpView;


/**
 * MVP contract for adding user source.
 *
 * @see UserSourceContract
 */
public interface UserSourceAddContract {

    interface View extends MvpView {
        /**
         * Show or hide progress indicator base on provided boolean value.
         *
         * @param isVisible Flag to indicate network validation in progress or finished.
         */
        void toggleValidationProgressIndicator(boolean isVisible);

        /**
         * Callback when news source is successfully added.
         */
        void showSourceAddedMessage();

        /**
         * Callback when source source failed due to lack of news items from source.
         *
         * @param totalFeedItems Total items found in the feed.
         */
        void showSourceValidationFailed(final int totalFeedItems);

        /**
         * Callback when provided feed URL can't be loaded.
         */
        void showUrlLoadFailedMessage();
    }

    interface Presenter extends MvpPresenter<UserSourceAddContract.View> {

        /**
         * Request to add new news source. News source should be validated by making actual network request.
         * When network request in progress listen for {@link View#toggleValidationProgressIndicator(boolean)}.
         *
         * @param newsSourceTitle Title of news source.
         * @param newsSourceUrl   The RSS feed URL for news source.
         */
        void addNewSource(String newsSourceTitle, String newsSourceUrl);

    }
}
