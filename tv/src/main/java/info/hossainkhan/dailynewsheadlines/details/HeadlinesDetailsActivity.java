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

package info.hossainkhan.dailynewsheadlines.details;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.DetailsFragment;

import com.google.gson.Gson;

import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.dailynewsheadlines.R;

/**
 * Contains a {@link DetailsFragment} in order to display more details for a given card.
 */
public class HeadlinesDetailsActivity extends Activity {
    private static final String BUNDLE_KEY_CARD_DATA = "KEY_CARD_DATA";
    private CardItem mCardItem;


    /**
     * Creates launch intent with required information.
     * @param activityContext Activity context.
     * @param cardItem The card item data.
     * @return
     */
    public static Intent createLaunchIntent(Context activityContext, CardItem cardItem) {
        Intent intent = new Intent(activityContext, HeadlinesDetailsActivity.class);

        intent.putExtra(BUNDLE_KEY_CARD_DATA, new Gson().toJson(cardItem));

        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bundle data needs to be extracted first, because the data is needed by the fragment as soon as
        // we "setContentView" with layout.
        Bundle extras = getIntent().getExtras();
        mCardItem = new Gson().fromJson(extras.getString(BUNDLE_KEY_CARD_DATA), CardItem.class);

        setContentView(R.layout.activity_headlines_details);
    }

    /* package private */ CardItem getCardItem() {
        return mCardItem;
    }

}
