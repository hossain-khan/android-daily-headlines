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
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;

import info.hossainkhan.android.core.util.Validate;


/**
 * Host activity that allows news source to be added by users.
 * See {@link AddSourceDialogFragment} for more information.
 * <p>
 * See https://developer.android.com/reference/android/support/v17/leanback/app/GuidedStepFragment.html
 */
public class AddNewsSourceActivity extends Activity {

    private static final String INTENT_KEY_INPUT_TYPE = "KEY_INPUT_TYPE";

    /**
     * Creates a start intent for this activity.
     *
     * @param context    App context.
     * @param type Type
     * @return Intent
     */
    public static Intent createStartIntent(@NonNull Context context, @NonNull String type) {
        Validate.notNull(type);
        Intent intent = new Intent(context, AddNewsSourceActivity.class);
        intent.putExtra(INTENT_KEY_INPUT_TYPE, type);
        return intent;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view is not required because we are adding fragment to "android.R.id.content"

        if (savedInstanceState == null) {
            GuidedStepFragment.addAsRoot(
                    AddNewsSourceActivity.this,
                    AddSourceDialogFragment.newInstance(),
                    android.R.id.content);
        }
    }

}
