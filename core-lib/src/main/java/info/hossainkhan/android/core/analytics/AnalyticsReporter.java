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

package info.hossainkhan.android.core.analytics;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import info.hossainkhan.android.core.model.CardItem;

public class AnalyticsReporter {

    private static final String EVENT_NAME_HEADLINE_LOADING_ERROR = "headline_load_failed";
    private static final String EVENT_NAME_HEADLINE_DETAILS_LOAD = "details_content";
    private static final String EVENT_NAME_SETTINGS_LOAD = "application_settings";

    private final FirebaseAnalytics mAnalytics;

    /**
     * Creates analytics reporter with firebase backend reporting.
     * @param analytics Firebase analytics instance.
     */
    public AnalyticsReporter(final FirebaseAnalytics analytics) {
        mAnalytics = analytics;
    }


    public void reportHeadlineSelectedEvent(final CardItem card) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, card.contentUrl());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, card.title());
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, card.category());
        mAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public void reportHeadlineLoadingError() {
        mAnalytics.logEvent(EVENT_NAME_HEADLINE_LOADING_ERROR, null);
    }

    public void reportHeadlineDetailsLoadedEvent(CardItem card) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, card.contentUrl());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, card.title());
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, card.category());
        mAnalytics.logEvent(EVENT_NAME_HEADLINE_DETAILS_LOAD, bundle);
    }

    public void reportSettingsScreenLoadedEvent(String settingsName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, settingsName);
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "app_settings");
        mAnalytics.logEvent(EVENT_NAME_SETTINGS_LOAD, bundle);
    }
}
