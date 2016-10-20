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

import info.hossainkhan.dailynewsheadlines.R;
import timber.log.Timber;

/**
 * This class demonstrates how to extend ErrorFragment
 * <p>
 * <code>
 * ErrorFragment mErrorFragment = new ErrorFragment();
 * getFragmentManager().beginTransaction().add(R.id.container, mErrorFragment).commit();
 * </code>
 */
public class ErrorFragment extends android.support.v17.leanback.app.ErrorFragment {
    private static final boolean TRANSLUCENT = true;
    private static final String BUNDLE_ARG_TITLE = "BUNDLE_KEY_TITLE";
    private static final String BUNDLE_ARG_MESSAGE = "BUNDLE_KEY_MESSAGE";

    private String mDialogTitle;
    private String mDialogMessage;

    public static ErrorFragment newInstance(final String title, final String message) {
        ErrorFragment fragment = new ErrorFragment();

        Bundle args = new Bundle();
        args.putString(BUNDLE_ARG_TITLE, title);
        args.putString(BUNDLE_ARG_MESSAGE, message);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate() called with: savedInstanceState = [%s]", savedInstanceState);
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        mDialogTitle = arguments.getString(BUNDLE_ARG_TITLE);
        mDialogMessage = arguments.getString(BUNDLE_ARG_MESSAGE);

        Timber.d("onCreate() called with: title = [%s], message = [%s]", mDialogTitle, mDialogMessage);

        setTitle(mDialogTitle);
        setErrorContent(mDialogMessage);
    }

    void setErrorContent(final String dialogMessage) {
        setImageDrawable(getResources().getDrawable(R.drawable.lb_ic_sad_cloud));
        setMessage(dialogMessage);
        setDefaultBackground(TRANSLUCENT);

        setButtonText(getResources().getString(R.string.dismiss_error));

        setButtonClickListener(view -> {
            // Listener must be attached on activity attach.
            getFragmentManager().beginTransaction().remove(ErrorFragment.this).commit();
        });
    }
}
