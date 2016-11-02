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

package info.hossainkhan.dailynewsheadlines.about;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import info.hossainkhan.dailynewsheadlines.R;

/**
 * Fragment that shows information on how to contribute.
 */
public class ContributionInfoFragment extends GuidedStepFragment {
    private Context mContext;

    public static ContributionInfoFragment newInstance() {
        return new ContributionInfoFragment();
    }


    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        // NOTE: Need to explore how to manipulate this.
        mContext = getActivity().getApplicationContext();
        GuidedAction action;

        //
        // Prepare the credits action item
        //
        String creditsTitle = "Credits";
        String creditsDescription = "List of other amazing open-source libraries used in this project.";
        final SpannableStringBuilder titleSpan = new SpannableStringBuilder(creditsTitle);
        titleSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, creditsTitle.length(), Spannable
                .SPAN_INCLUSIVE_INCLUSIVE);
        titleSpan.setSpan(new RelativeSizeSpan(1.2f), 0, creditsTitle.length(), Spannable
                .SPAN_INCLUSIVE_INCLUSIVE);
        final SpannableStringBuilder descriptionSpan = new SpannableStringBuilder(creditsDescription);
        descriptionSpan.setSpan(new StyleSpan(Typeface.ITALIC), 0, creditsDescription.length(), Spannable
                .SPAN_INCLUSIVE_INCLUSIVE);

        action = new GuidedAction.Builder(mContext)
                .title(titleSpan)
                .description(descriptionSpan)
                .icon(R.drawable.vector_icon_human_handsup)
                .focusable(false)
                .build();
        actions.add(action);


        //
        // Prepare all the static info of the libraries used in the app. No need to convert them to string resource.
        //
        actions.add(buildLibraryInfo("Firebase", "Firebase is a mobile and web application platform with tools and infrastructure designed to help developers build high-quality apps."));
        actions.add(buildLibraryInfo("Dagger", "Dagger - A fast dependency injector for Android and Java."));
        actions.add(buildLibraryInfo("RxJava", "RxJava - Reactive Extensions for the JVM"));
        actions.add(buildLibraryInfo("RxAndroid", "Reactive Extensions for Android."));
        actions.add(buildLibraryInfo("Picasso", "A powerful image downloading and caching library for Android" +
                "Android\nhttp://square.github.io/picasso/\nApache License, Version 2.0, January 2004"));
        actions.add(buildLibraryInfo("Leakcanary", "A memory leak detection library for Android and Java."));
        actions.add(buildLibraryInfo("AutoValue", "Generated immutable value classes for Java 1.6+"));
        actions.add(buildLibraryInfo("Butter Knife", "Bind Android views and callbacks to fields and methods."));
        actions.add(buildLibraryInfo("Retrofit", "A type-safe HTTP client for Android and Java"));
        actions.add(buildLibraryInfo("Joda-Time", "Joda-Time provides a quality replacement for the Java date and time classes."));
    }

    private GuidedAction buildLibraryInfo(final String title, final String description) {
        return new GuidedAction.Builder(mContext)
                .title(title)
                .description(description)
                .infoOnly(true)
                .build();
    }

    @Override
    public GuidanceStylist onCreateGuidanceStylist() {
        return new AboutAppGuidanceStylist();
    }


    /**
     * Internal {@link GuidanceStylist} to render about app info on content area.
     */
    private static class AboutAppGuidanceStylist extends GuidanceStylist {
        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Guidance guidance) {
            return inflater.inflate(R.layout.dialog_contribution_info, container, false);
        }
    }

}
