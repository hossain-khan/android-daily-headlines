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

package info.hossainkhan.dailynewsheadlines.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist.Guidance;
import android.support.v17.leanback.widget.GuidedAction;
import android.widget.Toast;

import java.util.List;

import info.hossainkhan.android.core.CoreApplication;
import info.hossainkhan.dailynewsheadlines.R;

/**
 * A dialog fragment with positive and negative options.
 */
public class ConfirmationDialogFragment extends GuidedStepFragment {
    /**
     * Unique screen name used for reporting and analytics.
     */
    private static final String ANALYTICS_SCREEN_NAME = "confirm";

    private static final int ACTION_ID_POSITIVE = 1;
    private static final int ACTION_ID_NEGATIVE = ACTION_ID_POSITIVE + 1;

    private static final String BUNDLE_ARG_TITLE = "BUNDLE_KEY_TITLE";
    private static final String BUNDLE_ARG_MESSAGE = "BUNDLE_KEY_MESSAGE";

    public static ConfirmationDialogFragment newInstance(final String title, final String message) {
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();

        Bundle args = new Bundle();
        args.putString(BUNDLE_ARG_TITLE, title);
        args.putString(BUNDLE_ARG_MESSAGE, message);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        CoreApplication.getAnalyticsReporter().reportScreenLoadedEvent(ANALYTICS_SCREEN_NAME);
    }

    @NonNull
    @Override
    public Guidance onCreateGuidance(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        final String dialogTitle = arguments.getString(BUNDLE_ARG_TITLE);
        final String dialogMessage = arguments.getString(BUNDLE_ARG_MESSAGE);

        Guidance guidance = new Guidance(dialogTitle, dialogMessage,"", null);
        return guidance;
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        Context applicationContext = getActivity().getApplicationContext();
        GuidedAction action = new GuidedAction.Builder(applicationContext)
                .id(ACTION_ID_POSITIVE)
                .title(getString(R.string.dialog_button_positive)).build();
        actions.add(action);
        action = new GuidedAction.Builder(applicationContext)
                .id(ACTION_ID_NEGATIVE)
                .title(getString(R.string.dialog_button_negative)).build();
        actions.add(action);
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        if (ACTION_ID_POSITIVE == action.getId()) {
            Toast.makeText(getActivity(), "Positive button clicked",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Negative button clicked",
                    Toast.LENGTH_SHORT).show();
        }
        getActivity().finish();
    }
}
