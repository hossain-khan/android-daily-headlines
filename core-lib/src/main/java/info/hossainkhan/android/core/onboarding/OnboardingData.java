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

package info.hossainkhan.android.core.onboarding;


import android.util.SparseArray;

import java.util.Arrays;
import java.util.List;

import info.hossainkhan.android.core.R;

/**
 * Static data for the on-boarding screens.
 */
public final class OnboardingData {
    /**
     * Unique screen name used for reporting and analytics.
     */
    public static final String ANALYTICS_SCREEN_NAME = "onboarding";

    public static final int[] pageTitles = {
            R.string.onboarding_title_welcome,
            R.string.onboarding_title_contribute,
            R.string.onboarding_title_relax
    };

    /**
     * String formatting arguments for the resource to avoid issue on older android. See {@link Emoji} for more information.
     */
    public static final SparseArray<List<String>> pageTitleFormatArgs = new SparseArray<>(pageTitles.length);

    static {
        pageTitleFormatArgs.put(R.string.onboarding_title_relax, Arrays.asList(Emoji.THUMBS_UP));
    }

    public static final int[] pageDescriptions = {
            R.string.onboarding_description_welcome,
            R.string.onboarding_description_contribute,
            R.string.onboarding_description_relax
    };

    /**
     * String formatting arguments for the resource to avoid issue on older android. See {@link Emoji} for more information.
     */
    public static final SparseArray<List<String>> pageDescriptionsFormatArgs = new SparseArray<>(pageTitles.length);

    static {
        pageDescriptionsFormatArgs.put(R.string.onboarding_description_welcome,
                Arrays.asList(Emoji.TELEVISION, Emoji.EYE_GLASS, Emoji.COFFEE, Emoji.NEWS_PAPER));
        pageDescriptionsFormatArgs.put(R.string.onboarding_description_contribute,
                Arrays.asList(Emoji.COMPUTER));
        pageDescriptionsFormatArgs.put(R.string.onboarding_description_relax,
                Arrays.asList(Emoji.HAMMER, Emoji.WRENCH, Emoji.LIGHT, Emoji.BUG, Emoji.LADY_BUG));
    }


    public static final int[] pageIcons = {
            R.drawable.vector_icon_newspaper,
            R.drawable.vector_icon_github_circle,
            R.drawable.vector_icon_lab_flask_outline
    };

}
