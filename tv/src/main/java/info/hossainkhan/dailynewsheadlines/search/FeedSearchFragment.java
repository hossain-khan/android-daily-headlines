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
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.app.SearchFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.android.core.search.SearchContract;
import info.hossainkhan.android.core.search.SearchPresenter;
import info.hossainkhan.android.core.util.StringUtils;
import info.hossainkhan.dailynewsheadlines.R;
import info.hossainkhan.dailynewsheadlines.addsource.ValidateNewsSourceDialogFragment;
import info.hossainkhan.dailynewsheadlines.browser.RowBuilderFactory;
import rx.Emitter;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Cancellable;
import timber.log.Timber;

public class FeedSearchFragment extends SearchFragment implements SearchContract.View {

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
        final SearchQueryObserver searchQueryObserver = new SearchQueryObserver(this, mRowsAdapter);
        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(final Presenter.ViewHolder itemViewHolder, final Object item,
                                      final RowPresenter.ViewHolder rowViewHolder, final Row row) {
                Timber.d("onItemClicked() called with: itemViewHolder = [" + itemViewHolder + "], item = [" + item +
                        "], rowViewHolder = [" + rowViewHolder + "], row = [" + row + "]");

                if(item instanceof CardItem) {
                    CardItem cardItem = (CardItem) item;
                    if(StringUtils.isNotEmpty(cardItem.contentUrl())) {
                        GuidedStepFragment fragment = ValidateNewsSourceDialogFragment
                                .newInstance(cardItem.title(), cardItem.contentUrl());
                        GuidedStepFragment.add(getFragmentManager(), fragment);
                    } else {
                        Toast.makeText(getActivity(), R.string.search_result_no_feed_url, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Timber.w("Unable to process item. Type unknown: %s", item);
                }

            }
        });
        mPresenter = new SearchPresenter(this, searchQueryObserver.getSearchQueryObservable());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
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
        mRowsAdapter.clear();
        mRowsAdapter.add(RowBuilderFactory.buildSearchResultCardRow(getActivity().getApplicationContext(),
                cardItems));
        mRowsAdapter.notifyArrayItemRangeChanged(0, cardItems.size());
    }

    public static class SearchQueryObserver {
        private final WeakReference<FeedSearchFragment> mSearchFragmentWeakRef;
        private final ArrayObjectAdapter mRowsAdapter;

        public SearchQueryObserver(FeedSearchFragment searchFragment, ArrayObjectAdapter rowsAdapter) {
            mSearchFragmentWeakRef = new WeakReference<FeedSearchFragment>(searchFragment);
            mRowsAdapter = rowsAdapter;
        }

        /**
         * Converts {@link SearchFragment.SearchResultProvider} to {@link Observable<String>} of search query.
         * <p>
         * References: <br>
         * https://medium.com/yammer-engineering/converting-callback-async-calls-to-rxjava-ebc68bde5831#.mr6g4k5gz
         * http://www.pacoworks.com/2016/08/21/this-is-not-an-rxjava-tutorial/
         *
         * @return Observable that provides stream of search query from user.
         */
        public Observable<String> getSearchQueryObservable() {
            return Observable.fromEmitter(new Action1<Emitter<String>>() {
                @Override
                public void call(final Emitter<String> emitter) {

                    SearchFragment.SearchResultProvider searchListener = new SearchResultProvider() {
                        @Override
                        public ObjectAdapter getResultsAdapter() {
                            return mRowsAdapter;
                        }

                        @Override
                        public boolean onQueryTextChange(final String newQuery) {
                            Timber.d("onQueryTextChange() called with: newQuery = [%s]", newQuery);
                            emitter.onNext(newQuery);
                            return true;
                        }

                        @Override
                        public boolean onQueryTextSubmit(final String query) {
                            Timber.d("onQueryTextSubmit() called with: query = [%s]", query);
                            emitter.onNext(query);
                            return true;
                        }
                    };

                    FeedSearchFragment feedSearchFragment = mSearchFragmentWeakRef.get();
                    if (feedSearchFragment != null) {
                        feedSearchFragment.setSearchResultProvider(searchListener);
                    }

                    // (Rx Contract # 1) - unregistering listener when unsubscribed
                    emitter.setCancellation(new Cancellable() {
                        @Override
                        public void cancel() throws Exception {
                            FeedSearchFragment feedSearchFragment = mSearchFragmentWeakRef.get();
                            if (feedSearchFragment != null) {
                                Timber.d("cancel() SearchResultProvider callback listener");
                                // NOTE: Setting litener to null crashes on SearchFragment$3.run(SearchFragment.java:165)
                                //feedSearchFragment.setSearchResultProvider(null);
                            } else {
                                Timber.w("FeedSearchFragment is already destroyed.");
                            }
                        }
                    });
                }
                // (Rx Contract # 4) - specifying the backpressure strategy to use
            }, Emitter.BackpressureMode.BUFFER);
        }
    }

}

