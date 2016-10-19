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

package info.hossainkhan.dailynewsheadlines.details.listeners;

import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;

import java.lang.ref.WeakReference;

import info.hossainkhan.android.core.headlines.HeadlinesDetailsContract;
import timber.log.Timber;


public class DetailsViewInteractionListener implements OnItemViewClickedListener,
        OnItemViewSelectedListener {

    private final WeakReference<HeadlinesDetailsContract.Presenter> mPresenterRef;

    public DetailsViewInteractionListener(final HeadlinesDetailsContract.Presenter presenter) {
        this.mPresenterRef = new WeakReference<>(presenter);
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                              RowPresenter.ViewHolder rowViewHolder, Row row) {
        Timber.d("onItemClicked: %s", item);
        HeadlinesDetailsContract.Presenter presenter = mPresenterRef.get();
        if (presenter != null) {
            if (!(item instanceof Action)) return;
            Action action = (Action) item;
            presenter.onActionItemClicked((int) action.getId());
        }
    }

    @Override
    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                               RowPresenter.ViewHolder rowViewHolder, Row row) {

        Timber.d("onItemSelected: %s", item);
        HeadlinesDetailsContract.Presenter presenter = mPresenterRef.get();
        if (presenter != null) {
            // DEV NOTE: If in future we support related headlines or news row, we would need to
            // implement this and notify our presenter.
            //presenter.onItemSelected(item);
        }
    }

}