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

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.support.v17.leanback.widget.GuidedActionsStylist;

import java.util.List;

import info.hossainkhan.dailynewsheadlines.R;

/**
 * This is the third screen of the rental wizard which will display a progressbar while waiting for
 * the server to process the rental. The server communication is faked for the sake of this example
 * by waiting four seconds until continuing.
 */
public class ValidateNewsSourceDialogFragment extends GuidedStepFragment {

    private static final int ACTION_ID_PROCESSING = 1;
    private final Handler mFakeHttpHandler = new Handler();

    private static final String BUNDLE_ARG_SOURCE_TITLE = "BUNDLE_KEY_TITLE";
    private static final String BUNDLE_ARG_SOURCE_URL = "BUNDLE_KEY_NEWS_SOURCE_URL";


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

        // Fake Http call by creating some sort of delay.
        mFakeHttpHandler.postDelayed(fakeHttpRequestRunnable, 4000L);
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

    @Override
    public void onStop() {
        super.onStop();

        // Make sure to cancel the execution of the Runnable in case the fragment is stopped.
        mFakeHttpHandler.removeCallbacks(fakeHttpRequestRunnable);
    }

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        String newsSourceTitle = arguments.getString(BUNDLE_ARG_SOURCE_TITLE);
        String newsSourceUrl = arguments.getString(BUNDLE_ARG_SOURCE_URL);

        GuidanceStylist.Guidance guidance = new GuidanceStylist.Guidance(
                getString(R.string.add_news_source_feed_validating_progress_title),
                getString(R.string.add_news_source_feed_validating_progress_description, newsSourceUrl),
                newsSourceTitle, null);
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

    private final Runnable fakeHttpRequestRunnable = new Runnable() {
        @Override
        public void run() {
            // DO SOMETHING ON VALIDATIOn
        }
    };

}
