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
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import info.hossainkhan.android.core.CoreApplication;
import info.hossainkhan.android.core.usersource.UserSourceManager;
import info.hossainkhan.android.core.usersource.UserSourceProvider;
import timber.log.Timber;

/**
 * Enables user to enable/disable added news sources.
 */
public class ManageNewsSourceDialogFragment extends GuidedStepFragment {
    /**
     * Unique screen name used for reporting and analytics.
     */
    private static final String ANALYTICS_SCREEN_NAME = "news_source_manage";

    private Set<String> mRemovedSourcesQueue = new HashSet<>();

    /**
     * Creates new add news source dialog fragment.
     *
     * @return Creates and instance of this fragment.
     */
    public static ManageNewsSourceDialogFragment newInstance() {
        return new ManageNewsSourceDialogFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        CoreApplication.getAnalyticsReporter().reportScreenLoadedEvent(ANALYTICS_SCREEN_NAME);
    }

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = "Manage your news sources";
        String description = "Enable or disable them, you know what to do ;-)";

        GuidanceStylist.Guidance guidance = new GuidanceStylist.Guidance(title, description,
                null, null);
        return guidance;
    }

    @Override
    public void onCreateActions(List<GuidedAction> actions, Bundle savedInstanceState) {
        UserSourceProvider userSourceProvider = new UserSourceManager(getActivity().getApplicationContext());
        Map<String, String> newsSources = userSourceProvider.getSources();

        for (Map.Entry<String, String> entry : newsSources.entrySet()) {
            actions.add(getSourceAction(entry));
        }
    }

    /**
     * Builds a GuidedAction item from news source set.
     * @param newsSourceEntry News source map entry.
     * @return Checkbox GuidedAction
     */
    private GuidedAction getSourceAction(final Map.Entry<String, String> newsSourceEntry) {
        return new GuidedAction.Builder(getActivity())
                .id(newsSourceEntry.getKey().hashCode())
                .title(newsSourceEntry.getValue())
                .description(newsSourceEntry.getKey())
                .checkSetId(GuidedAction.CHECKBOX_CHECK_SET_ID)
                .build();
    }

    @Override
    public void onCreateButtonActions(@NonNull List<GuidedAction> actions,
                                      Bundle savedInstanceState) {
        actions.add(new GuidedAction.Builder(getActivity())
                .clickAction(GuidedAction.ACTION_ID_OK)
                .build()
        );
        actions.get(actions.size() - 1).setEnabled(false); // Disable OK, can't be enabled without validation
        actions.add(new GuidedAction.Builder(getActivity())
                .clickAction(GuidedAction.ACTION_ID_CANCEL)
                .build()
        );
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        if (GuidedAction.ACTION_ID_OK == action.getId()) {
            Timber.d("Going to remove all URLs from queue: %s", mRemovedSourcesQueue);
        } else if (GuidedAction.ACTION_ID_CANCEL == action.getId()) {
            getActivity().finish();
        } else {
            Timber.i("Processing news source: %s.", action);
            String url = action.getDescription().toString();
            if (action.isChecked()) {
                Timber.d("Adding URL for removal: %s", url);
                mRemovedSourcesQueue.add(url);
            } else {
                Timber.d("Removing URL from removal queue: %s", url);
                mRemovedSourcesQueue.remove(url);
            }

            updateOkButton(!mRemovedSourcesQueue.isEmpty());
        }
    }


    private void updateOkButton(boolean enabled) {
        findButtonActionById(GuidedAction.ACTION_ID_OK).setEnabled(enabled);
        notifyButtonActionChanged(findButtonActionPositionById(GuidedAction.ACTION_ID_OK));
    }
}
