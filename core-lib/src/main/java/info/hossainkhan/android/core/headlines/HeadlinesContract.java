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
import android.view.MenuItem;

import java.util.List;

import info.hossainkhan.android.core.base.MvpPresenter;
import info.hossainkhan.android.core.base.MvpView;
import info.hossainkhan.android.core.model.NewsHeadlineItem;
import info.hossainkhan.android.core.model.NewsHeadlines;
import info.hossainkhan.android.core.model.ScreenType;

/**
 * View and Presenter contract for main headline browsing view.
 */
public interface HeadlinesContract {

    interface View extends MvpView {

        void showHeadlines(@NonNull List<NewsHeadlines> headlines);

        void showHeadlineDetailsUi(NewsHeadlineItem newsHeadlineItem);

        void showAppSettingsScreen();

        void showHeadlineBackdropBackground(String imageUrl);

        /**
         * Called when headline has no background image associated with it.
         */
        void showDefaultBackground();

        /**
         * Toggles loading indicator based on boolean flag.
         *
         * @param active Flag to show or hide data loading indicator.
         */
        void toggleLoadingIndicator(@NonNull boolean active);

        /**
         * Shows when data loading has failed.
         */
        void showDataLoadingError();

        /**
         * Show empty data state.
         */
        void showDataNotAvailable();

        void showAddNewsSourceScreen();

        void showUiScreen(@NonNull ScreenType type);
    }

    interface Presenter extends MvpPresenter<HeadlinesContract.View> {

        /**
         * Loads all the headlines from different news sources added by user.
         *
         * @param forceUpdate Used to force update news sources to get freshest data.
         */
        void loadHeadlines(@NonNull boolean forceUpdate);

        /**
         * Called when an item is selected by user to preview it's content.
         * <p>
         * <i>For example, when using in TV app, this event will be triggered when user navigates
         * through the headlines card item without clicking through them. When an item is clicked,
         * then {@link #onHeadlineItemSelected(NewsHeadlineItem)} is invoked.</i>
         *
         * @param newsHeadlineItem The card item that was selected as part of headline browsing.
         */
        void onHeadlineItemSelected(@NonNull NewsHeadlineItem newsHeadlineItem);

        /**
         * Called when user clicks on a headline card item.
         * <p>
         * <i>NOTE: For TV application, all items are {@link NewsHeadlineItem} including app settings items.
         * So, the application must selectively handle the card item based on {@link NewsHeadlineItem.Type}</i>
         *
         * @param newsHeadlineItem The card item that was clicked.
         */
        void onHeadlineItemClicked(@NonNull NewsHeadlineItem newsHeadlineItem);

        /**
         * @param item The menu item that was clicked.
         * @return {@code true} when item is handled, {@code false} otherwise
         */
        boolean onMenuItemClicked(@NonNull MenuItem item);
    }
}
