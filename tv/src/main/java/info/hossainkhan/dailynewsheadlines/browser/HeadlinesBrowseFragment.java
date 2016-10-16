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

package info.hossainkhan.dailynewsheadlines.browser;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.util.DisplayMetrics;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import info.hossainkhan.android.core.headlines.HeadlinesContract;
import info.hossainkhan.android.core.headlines.HeadlinesPresenter;
import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.android.core.model.NavigationRow;
import info.hossainkhan.android.core.util.UiUtils;
import info.hossainkhan.dailynewsheadlines.R;
import info.hossainkhan.dailynewsheadlines.browser.listeners.CardItemViewInteractionListener;
import info.hossainkhan.dailynewsheadlines.browser.listeners.PicassoImageTarget;
import info.hossainkhan.dailynewsheadlines.cards.presenters.selectors.ShadowRowPresenterSelector;
import info.hossainkhan.dailynewsheadlines.details.HeadlinesDetailsActivity;
import info.hossainkhan.dailynewsheadlines.settings.SettingsActivity;
import timber.log.Timber;

import static info.hossainkhan.android.core.data.CategoryNameResolver.getPreferredCategories;
import static info.hossainkhan.dailynewsheadlines.browser.RowBuilderFactory.buildCardRow;
import static info.hossainkhan.dailynewsheadlines.utils.LeanbackHelper.buildNavigationDivider;
import static info.hossainkhan.dailynewsheadlines.utils.LeanbackHelper.buildNavigationHeader;

/**
 * Leanback browser fragment that is responsible for showing all the headlines.
 */
public class HeadlinesBrowseFragment extends BrowseFragment implements HeadlinesContract.View {

    private static final int BACKGROUND_UPDATE_DELAY = 300;

    private final Handler mHandler = new Handler();
    private ArrayObjectAdapter mRowsAdapter;
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private URI mBackgroundURI;
    private BackgroundManager mBackgroundManager;
    private Target mBackgroundDrawableTarget;
    private HeadlinesPresenter mHeadlinesPresenter;
    private Resources mResources;
    private Context mApplicationContext;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Timber.i("onCreate");
        super.onActivityCreated(savedInstanceState);
        mResources = getResources();
        mApplicationContext = getActivity().getApplicationContext();

        if(savedInstanceState == null) {
            prepareEntranceTransition();
        }


        prepareBackgroundManager();

        setupUIElements();


        mHeadlinesPresenter = new HeadlinesPresenter(mApplicationContext, this, getPreferredCategories
                (mApplicationContext));
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
        applyStaticNavigationItems(list);


        mRowsAdapter = new ArrayObjectAdapter(new ShadowRowPresenterSelector());

        int totalNavigationItems = list.size();
        int i;
        for (i = 0; i < totalNavigationItems; i++) {
            NavigationRow navigationRow = list.get(i);
            mRowsAdapter.add(buildCardRow(mApplicationContext, navigationRow));
        }

        setAdapter(mRowsAdapter);
    }

    /**
     * Adds static navigation items like Menu and settings to existing list of navigation.
     * @param list
     */
    private void applyStaticNavigationItems(final List<NavigationRow> list) {
        // Prepare/inject additional items for the navigation
        // TODO: This news source heading item should be dynamic once multiple news source is allowed
        list.add(0, buildNavigationHeader(mResources, R.string.navigation_header_item_news_source_nytimes_title));

        // Begin settings section
        list.add(buildNavigationDivider());
        list.add(buildNavigationHeader(mResources, R.string.navigation_header_item_settings_title));

        // Build settings items

        List<CardItem> settingsItems = new ArrayList<>();
        CardItem item = new CardItem(CardItem.Type.ICON);
        item.setId(R.string.settings_card_item_news_source_title);
        item.setTitle(getString(R.string.settings_card_item_news_source_title));
        item.setLocalImageResourceId(R.drawable.ic_settings_settings);
        settingsItems.add(item);

        list.add(new NavigationRow.Builder()
                .setTitle(getString(R.string.settings_navigation_row_news_source_title))
                .setType(NavigationRow.TYPE_DEFAULT)
                .setCards(settingsItems)
                .useShadow(false)
                .build());
    }

    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mBackgroundDrawableTarget = new PicassoImageTarget(mBackgroundManager);
        mDefaultBackground = mResources.getDrawable(R.drawable.default_background);
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
        CardItemViewInteractionListener listener = new CardItemViewInteractionListener(mHeadlinesPresenter);
        setOnItemViewClickedListener(listener);
        setOnItemViewSelectedListener(listener);
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
        mBackgroundTimer.schedule(new HeadlinesBrowseFragment.UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        Timber.d("setLoadingIndicator() called with: active = [" + active + "]");
        if (!active) {
            startEntranceTransition();
        }
    }

    @Override
    public void showHeadlines(final List<NavigationRow> headlines) {

        loadRows(headlines);

        setupEventListeners();
    }

    @Override
    public void showHeadlineDetailsUi(final CardItem item) {
        Timber.d("Load details view for item: %s", item);
        startActivity(HeadlinesDetailsActivity.createLaunchIntent(getActivity().getBaseContext(),item));
    }

    @Override
    public void showLoadingHeadlinesError() {
        UiUtils.showToast(getActivity(), "Unable to load headlines");
    }

    @Override
    public void showNoHeadlines() {
        Timber.d("showNoHeadlines() called");
    }

    @Override
    public void showAppSettingsScreen() {
        Intent intent = null;
        intent = new Intent(getActivity().getBaseContext(), SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void showHeadlineBackdropBackground(final URI imageURI) {
        mBackgroundURI = imageURI;
        Timber.d("Loading HD background URL: %s", mBackgroundURI);
        startBackgroundTimer();
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
