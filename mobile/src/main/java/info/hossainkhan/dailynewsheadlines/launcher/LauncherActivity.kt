/*
 * MIT License
 *
 * Copyright (c) 2018 Hossain Khan
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

package info.hossainkhan.dailynewsheadlines.launcher

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import info.hossainkhan.android.core.util.PreferenceUtils
import info.hossainkhan.dailynewsheadlines.HeadlinesBrowseActivity
import info.hossainkhan.dailynewsheadlines.R
import info.hossainkhan.dailynewsheadlines.onboarding.OnboardingActivity
import timber.log.Timber

/**
 * Launcher activity which determines what activity to launch after possible splash+init.
 */
class LauncherActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        val context = applicationContext
        if (!PreferenceUtils.isOnBoardingCompleted(context)) {
            Timber.d("Launching onboarding screen.")
            // This is the first time running the app, let's go to onboarding
            startActivity(Intent(this, OnboardingActivity::class.java))
        } else {
            val appCurrentVersionCode = PreferenceUtils.getOnboardingCompletedAppVersionCode(context)
            val onboardingCompletedVersionCode = PreferenceUtils.getOnboardingCompletedAppVersionCode(context)
            Timber.d("On was shown on build %s, current build is %s. Launching browse screen.",
                    onboardingCompletedVersionCode, appCurrentVersionCode)
            startActivity(Intent(this, HeadlinesBrowseActivity::class.java))
        }

        // After launching proper activity, end the current launcher. It has no other purpose.
        finish()
    }
}
