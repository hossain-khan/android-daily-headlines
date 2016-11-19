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

package info.hossainkhan.dailynewsheadlines.search;

import android.os.Bundle;
import android.support.v17.leanback.app.SearchFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.text.TextUtils;

import java.util.List;

import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.android.core.search.SearchContract;
import info.hossainkhan.android.core.search.SearchPresenter;
import info.hossainkhan.dailynewsheadlines.browser.RowBuilderFactory;
import timber.log.Timber;

public class FeedSearchFragment extends SearchFragment implements SearchFragment.SearchResultProvider, SearchContract.View {

    private ArrayObjectAdapter mRowsAdapter;
    private SearchPresenter mPresenter;

    /**
     * Creates new instance of this fragment.
     *
     * @return Fragment instance.
     */
    public static FeedSearchFragment newInstance() {
        FeedSearchFragment fragment = new FeedSearchFragment();

        Bundle args = new Bundle();
        // Place where we set additional arguments.
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setSearchResultProvider(this);
        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(final Presenter.ViewHolder itemViewHolder, final Object item,
                                      final RowPresenter.ViewHolder rowViewHolder, final Row row) {
                Timber.d("onItemClicked() called with: itemViewHolder = [" + itemViewHolder + "], item = [" + item +
                        "], rowViewHolder = [" + rowViewHolder + "], row = [" + row + "]");
            }
        });
        mPresenter = new SearchPresenter(getActivity().getApplicationContext(), this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        return mRowsAdapter;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        Timber.d("onQueryTextChange() called with: newQuery = [%s]", newQuery);
        mRowsAdapter.clear();
        if (!TextUtils.isEmpty(newQuery)) {
            mPresenter.onSearchTermEntered(newQuery);

        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Timber.d("onQueryTextSubmit() called with: query = [%s]", query);
        mRowsAdapter.clear();
        if (!TextUtils.isEmpty(query)) {
            mPresenter.onSearchTermEntered(query);
        }
        return true;
    }

    @Override
    public void toggleLoadingIndicator(final boolean shouldShow) {
        Timber.d("toggleLoadingIndicator() called with: shouldShow = [%s]", shouldShow);
    }

    @Override
    public void showNoSearchResults() {
        Timber.d("showNoSearchResults() called.");
    }

    @Override
    public void showSearchResults(final List<CardItem> cardItems) {
        Timber.d("Found search items. Total: %d", cardItems.size());
        mRowsAdapter.add(RowBuilderFactory.buildSearchResultCardRow(getActivity().getApplicationContext(),
                cardItems));
        mRowsAdapter.notifyArrayItemRangeChanged(0, cardItems.size());
    }
}

