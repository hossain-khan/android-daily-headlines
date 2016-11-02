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

package info.hossainkhan.dailynewsheadlines.about;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.hossainkhan.dailynewsheadlines.BuildConfig;
import info.hossainkhan.dailynewsheadlines.R;
import timber.log.Timber;

/**
 * Fragment that shows application information.
 */
public class AboutAppFragment extends GuidedStepFragment {
    private static final int ACTION_ID_POSITIVE = 1;

    public static AboutAppFragment newInstance() {
        return new AboutAppFragment();
    }


    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        // NOTE: Need to explore how to manipulate this.
        Context applicationContext = getActivity().getApplicationContext();
        GuidedAction action = new GuidedAction.Builder(applicationContext)
                .id(ACTION_ID_POSITIVE)
                .title(R.string.okay)
                .description(R.string.description_close_current_info_screen)
                .icon(R.drawable.vector_icon_checkbox_marked_circle_outline)
                .build();
        actions.add(action);

    }

    @Override
    public GuidanceStylist onCreateGuidanceStylist() {
        return new AboutAppGuidanceStylist();
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        if (ACTION_ID_POSITIVE == action.getId()) {
            getActivity().finish();
        } else {
            Timber.w("Action %s not supported.", action);
        }
    }


    /**
     * Internal {@link GuidanceStylist} to render about app info on content area.
     */
    private static class AboutAppGuidanceStylist extends GuidanceStylist {
        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Guidance guidance) {
            View view = inflater.inflate(R.layout.dialog_about_application, container, false);
            TextView versionText = (TextView) view.findViewById(R.id.about_app_info_text_version);
            versionText.setText(container.getResources().getString(R.string.app_info_version_number,
                    BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
            TextView releaseDateText = (TextView) view.findViewById(R.id.about_app_info_text_release_date);
            releaseDateText.setText(BuildConfig.BUILD_TIME);
            return view;
        }
    }

}
