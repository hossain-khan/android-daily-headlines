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

package info.hossainkhan.android.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import com.google.firebase.crash.FirebaseCrash;

import timber.log.Timber;

/**
 * Util for dealing to application preference.
 */
public class PreferenceUtils {
    private static final String PREF_KEY_FEED_URL = "KEY_app_feed_url";
    private static final String PREF_KEY_ONBOARDING_COMPLETED = "KEY_app_onboarding_completed";
    private static final String PREF_KEY_ONBOARDING_COMPLETE_TIMESTAMP = "KEY_app_onboarding_complete_timestamp";
    /**
     * Save the app version code when the onboarding was showed. We may want to re-set and
     * re-launch the onboarding based on major feature.
     */
    private static final String PREF_KEY_ONBOARDING_COMPLETE_VERSION = "KEY_app_onboarding_complete_version_code";

    public static String getFeedUrl(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(PREF_KEY_FEED_URL, null);
    }

    public static void saveFeedUrl(final Context context, String url) {
        SharedPreferences.Editor preferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(context).edit();
        preferencesEditor.putString(PREF_KEY_FEED_URL, url);
        preferencesEditor.apply();
    }

    public static boolean isOnBoardingCompleted(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(PREF_KEY_ONBOARDING_COMPLETED, false);
    }

    public static void updateOnboardingAsCompleted(final Context context) {
        SharedPreferences.Editor preferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(context).edit();
        preferencesEditor.putBoolean(PREF_KEY_ONBOARDING_COMPLETED, true);
        preferencesEditor.putLong(PREF_KEY_ONBOARDING_COMPLETE_TIMESTAMP, System.currentTimeMillis());
        preferencesEditor.putInt(PREF_KEY_ONBOARDING_COMPLETE_VERSION, getAppVersionCode(context));
        preferencesEditor.apply();
    }

    public static int getOnboardingCompletedAppVersionCode(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(PREF_KEY_ONBOARDING_COMPLETE_VERSION, 0);
    }


    public static int getAppVersionCode(final Context context) {
        int versionCode = Integer.MAX_VALUE;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e, "Unable to get package info.");
            FirebaseCrash.report(e);
        }
        Timber.d("Got version code: %s", versionCode);
        return versionCode;
    }
}
