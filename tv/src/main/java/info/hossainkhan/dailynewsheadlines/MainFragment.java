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

package info.hossainkhan.dailynewsheadlines;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.DividerRow;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.SectionRow;
import android.util.DisplayMetrics;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import info.hossainkhan.android.core.headlines.HeadlinesContract;
import info.hossainkhan.android.core.headlines.HeadlinesPresenter;
import info.hossainkhan.android.core.model.NavigationRow;
import io.swagger.client.model.Article;
import io.swagger.client.model.ArticleMultimedia;
import timber.log.Timber;


public class MainFragment extends BrowseFragment implements HeadlinesContract.View {

    private static final int BACKGROUND_UPDATE_DELAY = 300;

    private final Handler mHandler = new Handler();
    private ArrayObjectAdapter mRowsAdapter;
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private URI mBackgroundURI;
    private BackgroundManager mBackgroundManager;
    private Target mBackgroundDrawableTarget;
    HeadlinesPresenter mHeadlinesPresenter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Timber.i("onCreate");
        super.onActivityCreated(savedInstanceState);


        prepareBackgroundManager();

        setupUIElements();

        mHeadlinesPresenter = new HeadlinesPresenter(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mBackgroundTimer) {
            Timber.d("onDestroy: " + mBackgroundTimer.toString());
            mBackgroundTimer.cancel();
        }
    }

    private void loadRows(final List<NavigationRow> list) {
        // Prepare/inject additional items for the navigation
        list.add(0, new NavigationRow.Builder().setTitle("NYTimes").setType(NavigationRow.TYPE_SECTION_HEADER).build());
        list.add(new NavigationRow.Builder().setType(NavigationRow.TYPE_DIVIDER).build());
        list.add(new NavigationRow.Builder().setTitle("Settings").setType(NavigationRow.TYPE_SECTION_HEADER).build());


        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        int totalNavigationItems = list.size();
        int i;
        for (i = 0; i < totalNavigationItems; i++) {
            NavigationRow navigationRow = list.get(i);
            mRowsAdapter.add(createCardRow(navigationRow));
        }

        setAdapter(mRowsAdapter);
    }

    /**
     * Creates appropriate {@link Row} item based on {@link NavigationRow} type.
     * @param navigationRow Navigation row
     * @return {@link Row}
     */
    private Row createCardRow(final NavigationRow navigationRow) {
        switch (navigationRow.getType()) {
            case NavigationRow.TYPE_SECTION_HEADER:
                return new SectionRow(new HeaderItem(navigationRow.getTitle()));
            case NavigationRow.TYPE_DIVIDER:
                return new DividerRow();
            case NavigationRow.TYPE_DEFAULT:
            default:
                List<Article> articles = navigationRow.getCards();
                int totalArticleSize = articles.size();
                TextCardPresenter cardPresenter = new TextCardPresenter(getActivity().getApplicationContext());
                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
                for (int j = 0; j < totalArticleSize; j++) {
                    listRowAdapter.add(articles.get(j));
                }
                HeaderItem header = new HeaderItem(navigationRow.getTitle());

                return new ListRow(header, listRowAdapter);
        }
    }

    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mBackgroundDrawableTarget = new PicassoImageTarget(mBackgroundManager);
        mDefaultBackground = getResources().getDrawable(R.drawable.default_background);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void setupUIElements() {
        // setBadgeDrawable(getActivity().getResources().getDrawable(
        // R.drawable.videos_by_google_banner));
        setTitle(getString(R.string.browse_title)); // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(getResources().getColor(R.color.fastlane_background));
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new MainFragment.ItemViewClickedListener());
        setOnItemViewSelectedListener(new MainFragment.ItemViewSelectedListener());
    }

    protected void updateBackground(String uri) {
        Timber.d("Updating background: %s", uri);

        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;

        Picasso.with(getActivity())
                .load(uri)
                .resize(width, height)
                .centerCrop()
                .error(mDefaultBackground)
                .into(mBackgroundDrawableTarget);
        mBackgroundTimer.cancel();
    }

    private void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new MainFragment.UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    @Override
    public void setLoadingIndicator(final boolean active) {

    }

    @Override
    public void showHeadlines(final List<NavigationRow> headlines) {

        loadRows(headlines);

        setupEventListeners();
    }

    @Override
    public void showHeadlineDetailsUi(final Article article) {

    }

    @Override
    public void showLoadingHeadlinesError() {

    }

    @Override
    public void showNoHeadlines() {

    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            Timber.d("onItemClicked: " + item);
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {

            Timber.d("onItemSelected");

            if (item instanceof Article) {
                List<ArticleMultimedia> multimedia = ((Article) item).getMultimedia();
                int size = multimedia.size();
                if (size >= 5) {
                    String url = multimedia.get(4).getUrl();
                    Timber.d("Loading HD background URL: %s", url);
                    mBackgroundURI = URI.create(url);
                    startBackgroundTimer();
                } else {
                    Timber.i("Article does not have HD background. Total items: %d", size);
                }
            }
        }
    }

    /**
     * Target to save as instance to avoid issue described in following SO: <br/>
     * http://stackoverflow.com/questions/24180805/onbitmaploaded-of-target-object-not-called-on-first-load
     */
    private final static class PicassoImageTarget implements Target {

        private final WeakReference<BackgroundManager> mBackgroundManagerRef;

        PicassoImageTarget(BackgroundManager backgroundManager) {
            mBackgroundManagerRef = new WeakReference<>(backgroundManager);
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Timber.d("onBitmapLoaded: %dx%d - %d bytes", bitmap.getHeight(), bitmap.getWidth(), bitmap.getByteCount());
            BackgroundManager backgroundManager = mBackgroundManagerRef.get();
            if (backgroundManager != null) {
                backgroundManager.setBitmap(bitmap);
            } else {
                Timber.w("Background manager is unavailable.");
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Timber.w("onBitmapFailed");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Timber.d("onPrepareLoad");
        }
    }

    private class UpdateBackgroundTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(() -> {
                if (mBackgroundURI != null) {
                    updateBackground(mBackgroundURI.toString());
                }
            });

        }
    }


}
