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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.support.v17.leanback.widget.GuidedActionsStylist;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import java.util.List;

import info.hossainkhan.android.core.CoreApplication;
import info.hossainkhan.android.core.usersource.UserSourceAddContract;
import info.hossainkhan.android.core.usersource.UserSourceAddPresenter;
import info.hossainkhan.android.core.usersource.UserSourceManager;
import info.hossainkhan.dailynewsheadlines.R;
import info.hossainkhan.android.core.onboarding.Emoji;
import timber.log.Timber;

/**
 * This is the third screen of the rental wizard which will display a progressbar while waiting for
 * the server to process the rental. The server communication is faked for the sake of this example
 * by waiting four seconds until continuing.
 */
public class ValidateNewsSourceDialogFragment extends GuidedStepFragment implements UserSourceAddContract.View {
    /**
     * Unique screen name used for reporting and analytics.
     */
    private static final String ANALYTICS_SCREEN_NAME = "news_source_validate";

    private static final int ACTION_ID_PROCESSING = 1;

    private static final String BUNDLE_ARG_SOURCE_TITLE = "BUNDLE_KEY_TITLE";
    private static final String BUNDLE_ARG_SOURCE_URL = "BUNDLE_KEY_NEWS_SOURCE_URL";
    private String mNewsSourceUrl;
    private String mNewsSourceTitle;
    private UserSourceAddPresenter mPresenter;


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
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        Context context = activity.getApplicationContext();
        mPresenter = new UserSourceAddPresenter(context, this, new UserSourceManager(context));
    }

    @Override
    public void onStart() {
        super.onStart();
        CoreApplication.getAnalyticsReporter().reportScreenLoadedEvent(ANALYTICS_SCREEN_NAME);
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
        mPresenter.addNewSource(mNewsSourceTitle, mNewsSourceUrl);

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
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

        popBackStackToGuidedStepFragment(ValidateNewsSourceDialogFragment.class, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void showSourceAddedMessage() {
        Toast.makeText(getActivity(),
                getString(R.string.success_news_source_feed_added, mNewsSourceTitle, Emoji.SMILEY),
                Toast.LENGTH_LONG).show();

        // Save it and finish
        finishGuidedStepFragments();
    }

    @Override
    public void toggleValidationProgressIndicator(final boolean isVisible) {
        // Nothing to do, since progress is shown by default.
        Timber.d("toggleValidationProgressIndicator() called with: isVisible = [" + isVisible + "]");
    }

    @Override
    public void showSourceValidationFailed(final int totalFeedItems) {
        onValidationFailed(getString(R.string.error_msg_feed_url_not_enough_items, totalFeedItems));
    }

    @Override
    public void showUrlLoadFailedMessage() {
        onValidationFailed(getString(R.string.error_msg_feed_url_failed_loading));
    }
}
