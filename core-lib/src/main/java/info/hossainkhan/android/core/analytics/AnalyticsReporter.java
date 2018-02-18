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

    //
    // Custom event names
    //
    private static final String EVENT_NAME_HEADLINE_LOADING_ERROR = "headline_load_failed";
    private static final String EVENT_NAME_HEADLINE_DETAILS_LOAD = "details_content";
    private static final String EVENT_NAME_SETTINGS_LOAD = "application_settings";
    private static final String EVENT_NAME_NEWS_SOURCE_ADD = "news_source_add";
    private static final String EVENT_NAME_NEWS_SOURCE_REMOVE = "news_source_remove";

    //
    // Event custom param name
    //
    private static final String EVENT_PARAM_NAME_ACTION_SUCCESS = "is_success";

    //
    // Event param values
    //
    private static final String EVENT_PARAM_VALUE_CATEGORY_SCREEN = "ui_screen";
    private static final String EVENT_PARAM_VALUE_CATEGORY_SETTINGS = "app_settings";

    /**
     * Analytics instance that is used for reporting.
     */
    private final FirebaseAnalytics mAnalytics;

    /**
     * Creates analytics reporter with firebase backend reporting.
     *
     * @param analytics Firebase analytics instance.
     */
    public AnalyticsReporter(final FirebaseAnalytics analytics) {
        mAnalytics = analytics;
    }


    public void reportHeadlineSelectedEvent(final CardItem card) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, card.getContentUrl());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, card.getTitle());
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, card.getCategory());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, card.getType().name());
        mAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public void reportHeadlineLoadingError() {
        mAnalytics.logEvent(EVENT_NAME_HEADLINE_LOADING_ERROR, null);
    }

    public void reportHeadlineDetailsLoadedEvent(CardItem card) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, card.getContentUrl());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, card.getTitle());
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, card.getCategory());
        mAnalytics.logEvent(EVENT_NAME_HEADLINE_DETAILS_LOAD, bundle);
    }

    public void reportSettingsScreenLoadedEvent(String settingsName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, settingsName);
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, EVENT_PARAM_VALUE_CATEGORY_SETTINGS);
        mAnalytics.logEvent(EVENT_NAME_SETTINGS_LOAD, bundle);
    }

    /**
     * Reports a UI screen or component being loaded. Similar to page-view with page name.
     *
     * @param screenName Name of the screen or page.
     */
    public void reportScreenLoadedEvent(final String screenName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, EVENT_PARAM_VALUE_CATEGORY_SCREEN);
        mAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    public void reportOnBoardingTutorialBeingEvent() {
        mAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN, null);
    }

    public void reportOnBoardingTutorialCompleteEvent() {
        mAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, null);
    }

    /**
     * Reported when news an news source is added to the system.
     *
     * @param sourceName Name of news source being added.
     * @param isSuccess  is request success or failed due to validation reason.
     */
    public void reportAddNewsSourceEvent(String sourceName, boolean isSuccess) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, sourceName);
        bundle.putBoolean(EVENT_PARAM_NAME_ACTION_SUCCESS, isSuccess);
        mAnalytics.logEvent(EVENT_NAME_NEWS_SOURCE_ADD, bundle);
    }


    /**
     * Reported when news an news source is deleted from the system.
     *
     * @param sourceName Name of news source being deleted.
     */
    public void reportRemoveNewsSourceEvent(String sourceName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, sourceName);
        mAnalytics.logEvent(EVENT_NAME_NEWS_SOURCE_REMOVE, bundle);
    }
}
