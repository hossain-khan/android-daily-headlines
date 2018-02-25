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

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;

import java.util.ArrayList;
import java.util.List;

import info.hossainkhan.android.core.CoreApplication;
import info.hossainkhan.android.core.headlines.HeadlinesContract;
import info.hossainkhan.android.core.headlines.HeadlinesPresenter;
import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.android.core.model.NewsHeadlines;
import info.hossainkhan.android.core.model.ScreenType;
import info.hossainkhan.android.core.model.NavigationRow;
import info.hossainkhan.android.core.newsprovider.NewsProviderManager;
import info.hossainkhan.dailynewsheadlines.R;
import info.hossainkhan.dailynewsheadlines.about.DisplayInfoActivity;
import info.hossainkhan.dailynewsheadlines.addsource.AddNewsSourceActivity;
import info.hossainkhan.dailynewsheadlines.browser.listeners.CardItemViewInteractionListener;
import info.hossainkhan.dailynewsheadlines.cards.presenters.selectors.ShadowRowPresenterSelector;
import info.hossainkhan.dailynewsheadlines.details.HeadlinesDetailsActivity;
import info.hossainkhan.dailynewsheadlines.dialog.ErrorFragment;
import info.hossainkhan.dailynewsheadlines.search.SearchActivity;
import info.hossainkhan.dailynewsheadlines.settings.SettingsActivity;
import info.hossainkhan.dailynewsheadlines.utils.PicassoBackgroundManager;
import timber.log.Timber;

import static info.hossainkhan.dailynewsheadlines.browser.RowBuilderFactory.buildCardRow;
import static info.hossainkhan.dailynewsheadlines.utils.LeanbackNavigationRowHelper.addSettingsNavigation;

/**
 * Leanback browser fragment that is responsible for showing all the headlines.
 */
public class HeadlinesBrowseFragment extends BrowseFragment implements HeadlinesContract.View {
    /**
     * Unique screen name used for reporting and analytics.
     */
    private static final String ANALYTICS_SCREEN_NAME = "headlines_browse";

    private ArrayObjectAdapter mRowsAdapter;
    private HeadlinesPresenter mHeadlinesPresenter;
    private Resources mResources;
    private Context mApplicationContext;
    private PicassoBackgroundManager mPicassoBackgroundManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Timber.i("onCreate");
        super.onActivityCreated(savedInstanceState);
        mResources = getResources();
        mApplicationContext = getActivity().getApplicationContext();

        if(savedInstanceState == null) {
            prepareEntranceTransition();
        }


        mPicassoBackgroundManager = new PicassoBackgroundManager(getActivity());
        mPicassoBackgroundManager.updateBackgroundWithDelay();

        setupUIElements();


        NewsProviderManager newsProviderManager = new NewsProviderManager(mApplicationContext);
        mHeadlinesPresenter = new HeadlinesPresenter(mApplicationContext, this, newsProviderManager);
    }

    @Override
    public void onStart() {
        super.onStart();
        CoreApplication.getAnalyticsReporter().reportScreenLoadedEvent(ANALYTICS_SCREEN_NAME);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHeadlinesPresenter.detachView();
        if (null != mPicassoBackgroundManager) {
            Timber.d("onDestroy: " + mPicassoBackgroundManager.toString());
            mPicassoBackgroundManager.destroy();
        }
    }


    private void setupUIElements() {
        // setBadgeDrawable(getActivity().getResources().getDrawable(
        // R.drawable.videos_by_google_banner));
        setTitle(getString(R.string.browse_title)); // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set search icon color
        setSearchAffordanceColor(getResources().getColor(R.color.search_opaque));

        // set fastLane (or headers) background color
        setBrandColor(getResources().getColor(R.color.fastlane_background));
    }

    private void setupEventListeners() {
        CardItemViewInteractionListener listener = new CardItemViewInteractionListener(mHeadlinesPresenter);
        setOnItemViewClickedListener(listener);
        setOnItemViewSelectedListener(listener);

        // Setting callback listener makes In-app search icon to be visible
        setOnSearchClickedListener(view -> startActivity(new Intent(getActivity(), SearchActivity.class)));
    }

    @Override
    public void toggleLoadingIndicator(final boolean active) {
        Timber.d("toggleLoadingIndicator() called with: active = [" + active + "]");
        if (!active) {
            startEntranceTransition();
        }
    }

    @Override
    public void showHeadlines(final List<NewsHeadlines> headlines) {
        loadRows(headlines);
        setupEventListeners();
    }

    @Override
    public void showHeadlineDetailsUi(final CardItem item) {
        Timber.d("Load details view for item: %s", item);
        startActivity(HeadlinesDetailsActivity.createLaunchIntent(getActivity().getBaseContext(),item));
    }

    @Override
    public void showDataLoadingError() {
        ErrorFragment errorFragment = ErrorFragment.newInstance(getString(R.string.oops),
                getString(R.string.error_msg_unable_to_load_content));

        // This shows the error dialog replacing the browse fragment
        // Because we were not able to load content.
        // FIXME: This is a BAD UX - but until better headline loading is implemented, leave it.
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, errorFragment);
        transaction.commit();
    }

    @Override
    public void showDataNotAvailable() {
        Timber.d("showDataNotAvailable() called");
    }

    @Override
    public void showAddNewsSourceScreen() {
        startActivity(AddNewsSourceActivity.createStartIntent(getActivity(), "TV-App"));
    }

    @Override
    public void showUiScreen(final ScreenType type) {
        startActivity(DisplayInfoActivity.createStartIntent(getActivity(), type));
    }

    @Override
    public void showAppSettingsScreen() {
        Intent intent = null;
        intent = new Intent(getActivity().getBaseContext(), SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void showHeadlineBackdropBackground(final String imageUrl) {
        Timber.d("Loading HD background URL: %s", imageUrl);
        mPicassoBackgroundManager.updateBackgroundWithDelay(imageUrl);
    }

    @Override
    public void showDefaultBackground() {
        mPicassoBackgroundManager.updateBackgroundWithDelay();
    }


    private void loadRows(final List<NewsHeadlines> list) {
        ArrayList<NavigationRow> navigationRows = new ArrayList<>();
        for (final NewsHeadlines newsHeadlines : list) {

            // Builds the header for each news source.
            navigationRows.add(NavigationRow.Companion.builder()
                    .title(newsHeadlines.getNewsSource().getName())
                    .displayTitle(newsHeadlines.getNewsSource().getName())
                    .type(NavigationRow.TYPE_SECTION_HEADER)
                    .sourceId(newsHeadlines.getNewsSource().getId())
                    .build());

            navigationRows.addAll(newsHeadlines.getHeadlines());
        }

        applyStaticNavigationItems(navigationRows);


        mRowsAdapter = new ArrayObjectAdapter(new ShadowRowPresenterSelector());

        int totalNavigationItems = list.size();
        int i;
        for (i = 0; i < totalNavigationItems; i++) {
            NavigationRow navigationRow = navigationRows.get(i);
            mRowsAdapter.add(buildCardRow(mApplicationContext, navigationRow));
        }

        setAdapter(mRowsAdapter);
    }

    /**
     * Adds static navigation items like Menu and settings to existing list of navigation.
     * @param list Existing list of items.
     */
    private void applyStaticNavigationItems(final List<NavigationRow> list) {
        addSettingsNavigation(mResources, list);
    }

}
