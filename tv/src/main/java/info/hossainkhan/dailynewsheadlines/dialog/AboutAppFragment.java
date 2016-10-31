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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist.Guidance;
import android.support.v17.leanback.widget.GuidedAction;

import java.util.List;

/**
 * Fragment that shows application information.
 */
public class AboutAppFragment extends GuidedStepFragment {


    private static final String BUNDLE_ARG_TITLE = "BUNDLE_KEY_TITLE";
    private static final String BUNDLE_ARG_MESSAGE = "BUNDLE_KEY_MESSAGE";

    private String mDialogTitle;
    private String mDialogMessage;

    public static AboutAppFragment newInstance(final String title, final String message) {
        AboutAppFragment fragment = new AboutAppFragment();

        Bundle args = new Bundle();
        args.putString(BUNDLE_ARG_TITLE, title);
        args.putString(BUNDLE_ARG_MESSAGE, message);
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Guidance onCreateGuidance(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        mDialogTitle = arguments.getString(BUNDLE_ARG_TITLE);
        mDialogMessage = arguments.getString(BUNDLE_ARG_MESSAGE);

        Guidance guidance = new Guidance(mDialogTitle,mDialogMessage,"", null);
        return guidance;
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {

    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {

    }
}
