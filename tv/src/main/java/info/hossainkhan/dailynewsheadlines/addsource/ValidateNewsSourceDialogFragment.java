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

package info.hossainkhan.dailynewsheadlines.addsource;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.support.v17.leanback.widget.GuidedActionsStylist;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.Callback;
import com.pkmmte.pkrss.PkRSS;

import java.util.List;

import info.hossainkhan.android.core.CoreApplication;
import info.hossainkhan.android.core.CoreConfig;
import info.hossainkhan.android.core.usersource.UserSourceManager;
import info.hossainkhan.android.core.usersource.UserSourceProvider;
import info.hossainkhan.dailynewsheadlines.R;
import info.hossainkhan.dailynewsheadlines.onboarding.Emoji;
import timber.log.Timber;

/**
 * This is the third screen of the rental wizard which will display a progressbar while waiting for
 * the server to process the rental. The server communication is faked for the sake of this example
 * by waiting four seconds until continuing.
 */
public class ValidateNewsSourceDialogFragment extends GuidedStepFragment implements Callback {
    /**
     * Unique screen name used for reporting and analytics.
     */
    private static final String ANALYTICS_SCREEN_NAME = "news_source_validate";

    private static final int ACTION_ID_PROCESSING = 1;

    private static final String BUNDLE_ARG_SOURCE_TITLE = "BUNDLE_KEY_TITLE";
    private static final String BUNDLE_ARG_SOURCE_URL = "BUNDLE_KEY_NEWS_SOURCE_URL";
    private String mNewsSourceUrl;
    private String mNewsSourceTitle;
    private UserSourceProvider mUserSourceProvider;


    /**
     * Creates new instance of this fragment with required data.
     *
     * @param sourceTitle News source title.
     * @param sourceUrl   News source URL.
     * @return Fragment instance.
     */
    public static ValidateNewsSourceDialogFragment newInstance(final String sourceTitle, final String sourceUrl) {
        ValidateNewsSourceDialogFragment fragment = new ValidateNewsSourceDialogFragment();

        Bundle args = new Bundle();
        args.putString(BUNDLE_ARG_SOURCE_TITLE, sourceTitle);
        args.putString(BUNDLE_ARG_SOURCE_URL, sourceUrl);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        CoreApplication.getAnalyticsReporter().reportScreenLoadedEvent(ANALYTICS_SCREEN_NAME);

        Context context = getActivity().getApplicationContext();
        mUserSourceProvider = new UserSourceManager(context);
        PkRSS.with(context)
                .load(mNewsSourceUrl)
                .callback(this)
                .async();
    }

    @Override
    public GuidedActionsStylist onCreateActionsStylist() {
        GuidedActionsStylist stylist = new GuidedActionsStylist() {
            @Override
            public int onProvideItemLayoutId() {
                return R.layout.dialog_progress_action_item;
            }

        };
        return stylist;
    }

    @Override
    public int onProvideTheme() {
        return R.style.Theme_Headlines_Leanback_GuidedStep_InformationDialog_NoSelector;
    }


    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        mNewsSourceTitle = arguments.getString(BUNDLE_ARG_SOURCE_TITLE);
        mNewsSourceUrl = arguments.getString(BUNDLE_ARG_SOURCE_URL);
        Timber.d("Validating news source '%s' with URL: %s", mNewsSourceTitle, mNewsSourceUrl);

        GuidanceStylist.Guidance guidance = new GuidanceStylist.Guidance(
                getString(R.string.add_news_source_feed_validating_progress_title),
                getString(R.string.add_news_source_feed_validating_progress_description, mNewsSourceUrl),
                mNewsSourceTitle, null);
        return guidance;
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction action = new GuidedAction.Builder(getActivity())
                .id(ACTION_ID_PROCESSING)
                .title(R.string.processing)
                .infoOnly(true)
                .build();
        actions.add(action);
    }

    private void onValidationFailed(final String message) {
        CoreApplication.getAnalyticsReporter().reportAddNewsSourceEvent(mNewsSourceTitle, false);

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

        popBackStackToGuidedStepFragment(ValidateNewsSourceDialogFragment.class, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void onValidationSucceeded() {
        CoreApplication.getAnalyticsReporter().reportAddNewsSourceEvent(mNewsSourceTitle, true);

        mUserSourceProvider.addSource(mNewsSourceTitle, mNewsSourceUrl);

        /// success_news_source_feed_added
        Toast.makeText(getActivity(),
                getString(R.string.success_news_source_feed_added, mNewsSourceTitle, Emoji.SMILEY),
                Toast.LENGTH_LONG).show();

        // Save it and finish
        finishGuidedStepFragments();
    }

    //
    // com.pkmmte.pkrss.Callback
    //

    @Override
    public void onPreload() {
        // do nothing, already showing progress dialog
    }

    @Override
    public void onLoaded(final List<Article> list) {
        // Show loaded x items and add to news source list.
        int totalFeedItems = list.size();
        if(totalFeedItems < CoreConfig.MINIMUM_FEED_ITEM_REQUIRED) {
            onValidationFailed(getString(R.string.error_msg_feed_url_not_enough_items, totalFeedItems));
        } else {
            onValidationSucceeded();
        }
    }

    @Override
    public void onLoadFailed() {
        onValidationFailed(getString(R.string.error_msg_feed_url_failed_loading));
    }
}
