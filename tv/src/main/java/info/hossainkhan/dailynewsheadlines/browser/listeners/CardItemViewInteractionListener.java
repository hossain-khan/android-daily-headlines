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

package info.hossainkhan.dailynewsheadlines.browser.listeners;

import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;

import java.lang.ref.WeakReference;

import info.hossainkhan.android.core.headlines.HeadlinesContract;
import info.hossainkhan.android.core.model.NewsHeadlineItem;
import timber.log.Timber;


public class CardItemViewInteractionListener implements OnItemViewClickedListener, OnItemViewSelectedListener {

    private final WeakReference<HeadlinesContract.Presenter> mHeadlinesPresenterRef;

    public CardItemViewInteractionListener(final HeadlinesContract.Presenter presenter) {
        this.mHeadlinesPresenterRef = new WeakReference<>(presenter);
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                              RowPresenter.ViewHolder rowViewHolder, Row row) {
        Timber.d("onItemClicked: %s", item);
        final NewsHeadlineItem card = (NewsHeadlineItem) item;

        HeadlinesContract.Presenter presenter = mHeadlinesPresenterRef.get();
        if(presenter != null) {
            presenter.onHeadlineItemClicked(card);
        }
    }

    @Override
    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                               RowPresenter.ViewHolder rowViewHolder, Row row) {

        Timber.d("onItemSelected: %s", item);

        if (item instanceof NewsHeadlineItem) {
            NewsHeadlineItem newsHeadlineItem = ((NewsHeadlineItem) item);

            HeadlinesContract.Presenter presenter = mHeadlinesPresenterRef.get();
            if(presenter != null) {
                presenter.onHeadlineItemSelected(newsHeadlineItem);
            }
        }
    }
}