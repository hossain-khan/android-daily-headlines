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

import java.util.Map;

import info.hossainkhan.android.core.base.MvpPresenter;
import info.hossainkhan.android.core.base.MvpView;

/**
 * MVP contract for user source.
 */
public interface UserSourceContract {

    interface View extends MvpView {

        /**
         * Callback to enable or disable remove action button.
         *
         * @param isActive Flag to activate or deactivate remove action.
         */
        void toggleRemoveAction(boolean isActive);

        /**
         * Close current screen.
         */
        void closeScreen();

        /**
         * Show remove source success feedback.
         */
        void showRemoveSourceSuccess();
    }

    interface Presenter extends MvpPresenter<UserSourceContract.View> {

        /**
         * Adds provided URL to list to remove.
         *
         * @param url      URL to remove.
         * @param isRemove Flag, to indicate if should be removed or not.
         */
        void onSourceSelected(String url, boolean isRemove);

        /**
         * Removes all the URLs that was added via {@link #onSourceSelected(String, boolean)}.
         */
        void onRemoveConfirm();

        /**
         * Cancels removal process.
         */
        void onCancelRemoval();

        /**
         * Get list of existing URLs saved by user.
         *
         * @return A map of &lt;URL, TITLE&gt; containing all the news source feed and title.
         */
        Map<String, String> getUserNewsSources();
    }
}
