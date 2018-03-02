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

package info.hossainkhan.dailynewsheadlines.onboarding

import android.content.Intent
import android.os.Bundle

import com.codemybrainsout.onboarder.AhoyOnboarderActivity
import com.codemybrainsout.onboarder.AhoyOnboarderCard
import info.hossainkhan.android.core.CoreApplication

import java.util.ArrayList

import info.hossainkhan.android.core.onboarding.OnboardingData
import info.hossainkhan.android.core.util.PreferenceUtils
import info.hossainkhan.dailynewsheadlines.HeadlinesBrowseActivity
import info.hossainkhan.dailynewsheadlines.R

class OnboardingActivity : AhoyOnboarderActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val onboardingScreens = createOnboardingScreens()

        setFinishButtonTitle(getString(R.string.button_text_finish_on_boarding))
        showNavigationControls(true)

        val colorList = ArrayList<Int>()
        colorList.add(R.color.palette_primary1)
        colorList.add(R.color.palette_secondary1)
        colorList.add(R.color.palette_secondary3)

        setColorBackground(colorList)

        setOnboardPages(onboardingScreens)
    }

    private fun createOnboardingScreens(): List<AhoyOnboarderCard> {
        val onboardingScreens = mutableListOf<AhoyOnboarderCard>()

        val resources = resources
        for (screenIndex in 0 until OnboardingData.getTotalPages()) {
            onboardingScreens.add(
                    AhoyOnboarderCard(
                            OnboardingData.getPageTitle(resources, screenIndex),
                            OnboardingData.getPageDescription(resources, screenIndex),
                            OnboardingData.pageIcons[screenIndex])
            )
        }


        for (page in onboardingScreens) {
            page.setTitleColor(R.color.black)
            page.setDescriptionColor(R.color.black)
            page.setBackgroundColor(R.color.white)
        }
        return onboardingScreens
    }

    override fun onFinishButtonPressed() {
        PreferenceUtils.updateOnboardingAsCompleted(applicationContext)
        CoreApplication.getAnalyticsReporter().reportOnBoardingTutorialCompleteEvent()
        startActivity(Intent(this, HeadlinesBrowseActivity::class.java))
        finish()
    }
}
