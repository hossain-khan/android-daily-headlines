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

import java.net.URI;
import java.util.List;

import info.hossainkhan.android.core.base.MvpPresenter;
import info.hossainkhan.android.core.base.MvpView;
import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.android.core.model.NavigationRow;

public interface HeadlinesContract {

    interface View extends MvpView {

        void showHeadlines(List<NavigationRow> headlines);

        void showHeadlineDetailsUi(CardItem cardItem);

        void showAppSettingsScreen();

        void showHeadlineBackdropBackground(URI imageURI);

        /**
         * Toggles loading indicator based on boolean flag.
         *
         * @param active Flag to show or hide data loading indicator.
         */
        void toggleLoadingIndicator(boolean active);

        /**
         * Shows when data loading has failed.
         */
        void showDataLoadingError();

        /**
         * Show empty data state.
         */
        void showDataNotAvailable();

        void showAppAboutScreen();
    }

    interface Presenter extends MvpPresenter<HeadlinesContract.View> {
        
        void loadHeadlines(boolean forceUpdate);

        void openHeadlineDetails(@NonNull CardItem cardItem);

        void onHeadlineItemSelected(@NonNull CardItem cardItem);

        void onHeadlineItemClicked(@NonNull CardItem cardItem);
    }
}
