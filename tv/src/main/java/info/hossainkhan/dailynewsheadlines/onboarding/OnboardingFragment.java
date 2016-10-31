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

package info.hossainkhan.dailynewsheadlines.onboarding;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.hossainkhan.android.core.util.PreferenceUtils;
import info.hossainkhan.dailynewsheadlines.R;
import info.hossainkhan.dailynewsheadlines.browser.MainActivity;

public class OnboardingFragment extends android.support.v17.leanback.app.OnboardingFragment {

    private static final int[] pageTitles = {
            R.string.onboarding_title_welcome,
            R.string.onboarding_title_contribute,
            R.string.onboarding_title_relax
    };

    /**
     * String formatting arguments for the resource to avoid issue on older android. See {@link Emoji} for more information.
     */
    private static final SparseArray<List<String>> pageTitleFormatArgs = new SparseArray<>(pageTitles.length);
    static {
        pageTitleFormatArgs.put(R.string.onboarding_title_relax, Arrays.asList(Emoji.THUMBS_UP));
    }

    private static final int[] pageDescriptions = {
            R.string.onboarding_description_welcome,
            R.string.onboarding_description_contribute,
            R.string.onboarding_description_relax
    };

    /**
     * String formatting arguments for the resource to avoid issue on older android. See {@link Emoji} for more information.
     */
    private static final SparseArray<List<String>> pageDescriptionsFormatArgs = new SparseArray<>(pageTitles.length);
    static {
        pageDescriptionsFormatArgs.put(R.string.onboarding_description_welcome, Arrays.asList(Emoji.TELEVISION, Emoji.EYE_GLASS, Emoji.COFFEE, Emoji.NEWS_PAPER));
        pageDescriptionsFormatArgs.put(R.string.onboarding_description_contribute, Arrays.asList(Emoji.COMPUTER));
        pageDescriptionsFormatArgs.put(R.string.onboarding_description_relax, Arrays.asList(Emoji.HAMMER, Emoji.WRENCH, Emoji.LIGHT, Emoji.BUG, Emoji.LADY_BUG));
    }


    private static final int[] pageIcons = {
            R.drawable.icon_newspaper,
            R.drawable.icon_github_circle,
            R.drawable.icon_lab_flask_outline
    };

    private static final long ANIMATION_DURATION = 500;
    private Animator mContentAnimator;
    private ImageView mContentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set the logo to display a splash animation
        setLogoResourceId(R.drawable.headlines_splash_banner);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void onFinishFragment() {
        super.onFinishFragment();
        // Our onboarding is done, mark it as completed
        Activity parentActivity = getActivity();
        PreferenceUtils.updateOnboardingAsCompleted(parentActivity.getApplicationContext());

        // Onboarding is completed, launch the
        startActivity(new Intent(parentActivity, MainActivity.class));
        // Also end the onboarding screen, so that user can't get back to it.
        parentActivity.finish();
    }

    @Override
    protected int getPageCount() {
        return pageTitles.length;
    }

    @Override
    protected String getPageTitle(int pageIndex) {
        int pageTitleResId = pageTitles[pageIndex];

        List<String> formatArgs = pageTitleFormatArgs.get(pageTitleResId);
        if(formatArgs != null) {
            return getString(pageTitleResId, formatArgs.toArray());
        }
        return getString(pageTitleResId);
    }

    @Override
    protected String getPageDescription(int pageIndex) {
        int pageDescriptionResId = pageDescriptions[pageIndex];

        List<String> formatArgs = pageDescriptionsFormatArgs.get(pageDescriptionResId);
        if(formatArgs != null) {
            return getString(pageDescriptionResId, formatArgs.toArray());
        }
        return getString(pageDescriptionResId);

    }

    @Nullable
    @Override
    protected View onCreateBackgroundView(LayoutInflater inflater, ViewGroup container) {
        View bgView = new View(getActivity());
        bgView.setBackgroundColor(getResources().getColor(R.color.fastlane_background));
        return bgView;
    }

    @Nullable
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
        mContentView = new ImageView(getActivity());
        mContentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mContentView.setPadding(0, 32, 0, 32);
        return mContentView;
    }

    @Nullable
    @Override
    protected View onCreateForegroundView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }


    @Override
    protected void onPageChanged(final int newPage, int previousPage) {
        if (mContentAnimator != null) {
            mContentAnimator.end();
        }
        ArrayList<Animator> animators = new ArrayList<>();
        Animator fadeOut = createFadeOutAnimator(mContentView);

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mContentView.setImageDrawable(getResources().getDrawable(pageIcons[newPage]));
            }
        });
        animators.add(fadeOut);
        animators.add(createFadeInAnimator(mContentView));
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animators);
        set.start();
        mContentAnimator = set;
    }
    @Override
    protected Animator onCreateEnterAnimation() {
        mContentView.setImageDrawable(getResources().getDrawable(pageIcons[0]));
        mContentAnimator = createFadeInAnimator(mContentView);
        return mContentAnimator;
    }
    private Animator createFadeInAnimator(View view) {
        return ObjectAnimator.ofFloat(view, View.ALPHA, 0.0f, 1.0f).setDuration(ANIMATION_DURATION);
    }

    private Animator createFadeOutAnimator(View view) {
        return ObjectAnimator.ofFloat(view, View.ALPHA, 1.0f, 0.0f).setDuration(ANIMATION_DURATION);
    }

}