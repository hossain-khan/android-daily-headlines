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

package info.hossainkhan.dailynewsheadlines.details;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.dailynewsheadlines.R;
import info.hossainkhan.dailynewsheadlines.browser.listeners.PicassoImageTarget;
import info.hossainkhan.dailynewsheadlines.cards.CardListRow;
import timber.log.Timber;

/**
 * Displays a card with more details using a {@link DetailsFragment}.
 */
public class HeadlinesDetailsFragment extends DetailsFragment implements OnItemViewClickedListener,
        OnItemViewSelectedListener {


    private ArrayObjectAdapter mRowsAdapter;

    private BackgroundManager mBackgroundManager;
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private PicassoImageTarget mBackgroundDrawableTarget;

    private Context mApplicationContext;
    private HeadlinesDetailsActivity mAttachedHeadlinesActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareBackgroundManager();
        setupUi();
        setupEventListeners();
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        Timber.d("onAttach() called with: context = [" + context + "]");
        mAttachedHeadlinesActivity = (HeadlinesDetailsActivity) context;
    }

    private void setupUi() {

        // Load the card we want to display from a JSON resource. This JSON data could come from
        // anywhere in a real world app, e.g. a server.
        CardItem data = mAttachedHeadlinesActivity.getCardItem();


        // Setup fragment
        setTitle(getString(R.string.detail_view_title));

        FullWidthDetailsOverviewRowPresenter rowPresenter = new FullWidthDetailsOverviewRowPresenter(
                new HeadlinesDetailsPresenter(getActivity())) {

            @Override
            protected RowPresenter.ViewHolder createRowViewHolder(ViewGroup parent) {
                // Customize Actionbar and Content by using custom colors.
                RowPresenter.ViewHolder viewHolder = super.createRowViewHolder(parent);

                View actionsView = viewHolder.view.
                        findViewById(R.id.details_overview_actions_background);
                actionsView.setBackgroundColor(getActivity().getResources().
                        getColor(R.color.detail_view_actionbar_background));

                View detailsView = viewHolder.view.findViewById(R.id.details_frame);
                detailsView.setBackgroundColor(
                        getResources().getColor(R.color.detail_view_background));
                return viewHolder;
            }
        };

        FullWidthDetailsOverviewSharedElementHelper mHelper = new FullWidthDetailsOverviewSharedElementHelper();
        mHelper.setSharedElementEnterTransition(getActivity(), "adsaddadasdasdasdsad");
        rowPresenter.setListener(mHelper);
        rowPresenter.setParticipatingEntranceTransition(false);
        prepareEntranceTransition();

        ListRowPresenter shadowDisabledRowPresenter = new ListRowPresenter();
        shadowDisabledRowPresenter.setShadowEnabled(false);

        // Setup PresenterSelector to distinguish between the different rows.
        ClassPresenterSelector rowPresenterSelector = new ClassPresenterSelector();
        rowPresenterSelector.addClassPresenter(DetailsOverviewRow.class, rowPresenter);
        rowPresenterSelector.addClassPresenter(CardListRow.class, shadowDisabledRowPresenter);
        rowPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
        mRowsAdapter = new ArrayObjectAdapter(rowPresenterSelector);

        // Setup action and detail row.
        DetailsOverviewRow detailsOverview = new DetailsOverviewRow(data);
        detailsOverview.setImageDrawable(getResources().getDrawable(R.drawable.stars_white));
        mApplicationContext = getActivity().getApplicationContext();
        Picasso.with(mApplicationContext).load(data.getImageUrl()).into(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, final Picasso.LoadedFrom from) {
                Timber.d("onBitmapLoaded() called with: bitmap = [" + bitmap + "], from = [" + from + "]");
                detailsOverview.setImageBitmap(mApplicationContext, bitmap);
            }

            @Override
            public void onBitmapFailed(final Drawable errorDrawable) {
                Timber.d("onBitmapFailed() called with: errorDrawable = [" + errorDrawable + "]");
            }

            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                Timber.d("onPrepareLoad() called with: placeHolderDrawable = [" + placeHolderDrawable + "]");
            }
        });


        ArrayObjectAdapter actionAdapter = new ArrayObjectAdapter();
        actionAdapter.add(new Action(1, "Read More"));
        detailsOverview.setActionsAdapter(actionAdapter);
        mRowsAdapter.add(detailsOverview);


        setAdapter(mRowsAdapter);
        updateBackground(data.getImageUrl());

        // NOTE: Move this when data is loaded
        startEntranceTransition();
    }

    private void setupEventListeners() {
        setOnItemViewSelectedListener(this);
        setOnItemViewClickedListener(this);
    }

    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(mAttachedHeadlinesActivity);
        mBackgroundManager.attach(mAttachedHeadlinesActivity.getWindow());
        mBackgroundDrawableTarget = new PicassoImageTarget(mBackgroundManager);
        mDefaultBackground = getResources().getDrawable(R.drawable.default_background);
        mMetrics = new DisplayMetrics();
        mAttachedHeadlinesActivity.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
        Timber.d("prepareBackgroundManager() called : metrics %s", mMetrics);

    }

    protected void updateBackground(String uri) {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;

        Picasso.with(mAttachedHeadlinesActivity)
                .load(uri)
                .resize(width, height)
                .centerCrop()
                .error(mDefaultBackground)
                .into(mBackgroundDrawableTarget);
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                              RowPresenter.ViewHolder rowViewHolder, Row row) {
        if (!(item instanceof Action)) return;
        Action action = (Action) item;
        if (action.getId() == 3) {
            setSelectedPosition(1);
        } else {
            Toast.makeText(mAttachedHeadlinesActivity, "Action Clicked", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                               RowPresenter.ViewHolder rowViewHolder, Row row) {
        if (mRowsAdapter.indexOf(row) > 0) {
            int backgroundColor = getResources().getColor(R.color.detail_view_related_background);
            getView().setBackgroundColor(backgroundColor);
        } else {
            getView().setBackground(null);
        }
    }
}
