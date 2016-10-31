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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;

import info.hossainkhan.android.core.util.Validate;
import info.hossainkhan.dailynewsheadlines.R;
import info.hossainkhan.dailynewsheadlines.dialog.AboutAppFragment;


/**
 * Activity that is used to show different kind of information using
 * {@link android.support.v17.leanback.app.GuidedStepFragment}. Based on requirement this activity uses
 * {@link R.style#Theme_Leanback_GuidedStep}.
 * <p>For list of supported info, see {@link InfoDialogType}</p>
 * <p>
 * See https://developer.android.com/reference/android/support/v17/leanback/app/GuidedStepFragment.html
 */
public class InformationActivity extends Activity {

    private static final String INTENT_KEY_INFO_DIALOG_TYPE = "KEY_INFO_DIALOG_TYPE";

    /**
     * List of information dialog type supported by this activity.
     */
    public enum InfoDialogType {
        ABOUT_APPLICATION
    }

    private InfoDialogType mDialogType;

    /**
     * Creates a start intent for this activity.
     *
     * @param context    App context.
     * @param dialogType {@link InfoDialogType}
     * @return Intent
     */
    public static Intent createStartIntent(@NonNull Context context, @NonNull InfoDialogType dialogType) {
        Validate.notNull(dialogType);
        Intent intent = new Intent(context, InformationActivity.class);
        intent.putExtra(INTENT_KEY_INFO_DIALOG_TYPE, dialogType);
        return intent;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view is not required because we are adding fragment to "android.R.id.content"
        // getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#21272A")));
        extractBundleData(getIntent());

        if (savedInstanceState == null) {
            GuidedStepFragment.addAsRoot(
                    InformationActivity.this,
                    getFragmentForType(InfoDialogType.ABOUT_APPLICATION),
                    android.R.id.content);
        }
    }

    /**
     * Internal method to extract bundle data and prepare required local instances.
     */
    private void extractBundleData(final Intent intent) {
        mDialogType = (InfoDialogType) intent.getSerializableExtra(INTENT_KEY_INFO_DIALOG_TYPE);

        // Intent data must be available to continue
        Validate.notNull(mDialogType);
    }


    private GuidedStepFragment getFragmentForType(final InfoDialogType type) {
        GuidedStepFragment fragment = null;
        switch (type) {
            case ABOUT_APPLICATION:
                fragment = AboutAppFragment.newInstance("Title", "Desc");
                break;
            default:
                throw new UnsupportedInformationTypeException();
        }

        return fragment;
    }

    public static class UnsupportedInformationTypeException extends RuntimeException {
        UnsupportedInformationTypeException() {
            super("Provided information type is not supported.");
        }
    }
}
