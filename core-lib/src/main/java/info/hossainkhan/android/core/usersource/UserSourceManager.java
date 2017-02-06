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

package info.hossainkhan.android.core.usersource;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import info.hossainkhan.android.core.CoreApplication;
import info.hossainkhan.android.core.util.Validate;
import timber.log.Timber;

/**
 * Persistent user's {@link UserSourceProvider} using simple shared preferences.
 */
public class UserSourceManager implements UserSourceProvider {

    private static final String PREF_KEY_NEWS_SOURCES = "PREF_KEY_user_news_source_feeds_map";
    /**
     * A hash map containing [URL => Title] for news source.
     */
    private Map<String, String> mNewsMap;
    private final SharedPreferences mSharedPreferences;

    /**
     * Creates user's news source manager.
     *
     * @param context Application context.
     */
    public UserSourceManager(final Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        loadSources();
    }

    @Override
    public void addSource(final String title, final String url) {
        Validate.notNull(title, url);
        mNewsMap.put(url, title);
        updateSources();
    }

    @Override
    public String removeSource(final String url) {
        String removedSourceTitle = mNewsMap.remove(url);
        updateSources();
        CoreApplication.getAnalyticsReporter().reportRemoveNewsSourceEvent(removedSourceTitle);
        return removedSourceTitle;
    }

    @Override
    public void removeSources(final Set<String> urls) {
        reportRemoveSource(urls);
        mNewsMap.keySet().removeAll(urls);
        updateSources();
    }

    @Override
    public Map<String, String> getSources() {
        return mNewsMap;
    }

    @Override
    public boolean isAdded(final String url) {
        boolean isAlreadyAdded = mNewsMap.containsKey(url);
        Timber.d("isAdded('%s') - returning %s", url, isAlreadyAdded);
        return isAlreadyAdded;
    }


    //
    // Internal methods
    //

    private void updateSources() {
        Gson gson = new GsonBuilder().create();
        String serializedJson = gson.toJson(mNewsMap);
        Timber.d("Saving news sources: %s", serializedJson);

        SharedPreferences.Editor preferencesEditor = mSharedPreferences.edit();
        preferencesEditor.putString(PREF_KEY_NEWS_SOURCES, serializedJson);
        preferencesEditor.apply();
    }

    private void loadSources() {
        String newsSources = mSharedPreferences.getString(PREF_KEY_NEWS_SOURCES, "{}");
        Timber.d("Loading news sources: %s", newsSources);

        Gson gson = new GsonBuilder().create();
        Type hasMapTypeToken = new TypeToken<Map<String, String>>() { }.getType();
        mNewsMap = gson.fromJson(newsSources, hasMapTypeToken); // This type must match TypeToken
    }

    /**
     * Convenient method to report removal of multiple URLs.
     *
     * @param urls List of URL.
     */
    private void reportRemoveSource(final Set<String> urls) {
        for (String url : urls) {
            if (mNewsMap.containsKey(url)) {
                CoreApplication.getAnalyticsReporter().reportRemoveNewsSourceEvent(mNewsMap.get(url));
            }
        }
    }

}
