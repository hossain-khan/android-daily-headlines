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

package info.hossainkhan.dailynewsheadlines.settings;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v17.preference.LeanbackPreferenceFragment;
import android.support.v17.preference.LeanbackSettingsFragment;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Stack;

import info.hossainkhan.dailynewsheadlines.R;
import timber.log.Timber;

public class SettingsExampleFragment extends LeanbackSettingsFragment implements DialogPreference.TargetFragment {

    public static final String BUNDLE_KEY_ROOT_SCREEN_ID = "root";
    public static final String BUNDLE_KEY_PREFERENCE_RESOURCE_ID = "preferenceResource";
    private final Stack<Fragment> fragmentStack = new Stack<Fragment>();

    @Override
    public void onPreferenceStartInitialScreen() {
        startPreferenceFragment(buildPreferenceFragment(R.xml.preferences_screen, null));
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragment preferenceFragment,
                                             Preference preference) {
        return false;
    }

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragment preferenceFragment,
                                           PreferenceScreen preferenceScreen) {
        PreferenceFragment frag = buildPreferenceFragment(R.xml.preferences_screen, preferenceScreen.getKey());
        startPreferenceFragment(frag);
        return true;
    }

    @Override
    public Preference findPreference(CharSequence prefKey) {
        Timber.d("findPreference() called with: prefKey = [" + prefKey + "]");
        return ((PreferenceFragment) fragmentStack.peek()).findPreference(prefKey);
    }

    private PreferenceFragment buildPreferenceFragment(int preferenceResId, String root) {
        Timber.d("buildPreferenceFragment() called with: preferenceResId = [" + preferenceResId + "], root = [" + root + "]");
        PreferenceFragment fragment = new PrefFragment();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_KEY_PREFERENCE_RESOURCE_ID, preferenceResId);
        args.putString(BUNDLE_KEY_ROOT_SCREEN_ID, root);
        fragment.setArguments(args);
        return fragment;
    }

    private class PrefFragment extends LeanbackPreferenceFragment {

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            String root = getArguments().getString(BUNDLE_KEY_ROOT_SCREEN_ID, null);
            int prefResId = getArguments().getInt(BUNDLE_KEY_PREFERENCE_RESOURCE_ID);
            if (root == null) {
                addPreferencesFromResource(prefResId);
            } else {
                setPreferencesFromResource(prefResId, root);
            }
        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            final String[] keys = {"prefs_content_category_business"};
            if (Arrays.asList(keys).contains(preference.getKey())) {
                Toast.makeText(getActivity(), "Implement your own action handler.", Toast.LENGTH_SHORT).show();
                return true;
            }
            return super.onPreferenceTreeClick(preference);
        }

        @Override
        public void onAttach(Context context) {
            fragmentStack.push(this);
            Timber.d("onAttach() called with: context = [%s], stack size: %d", context, fragmentStack.size());
            super.onAttach(context);
        }

        @Override
        public void onAttach(final Activity activity) {
            if(!fragmentStack.contains(this)) {
                fragmentStack.push(this);
            } else {
                Timber.i("Fragment [%s] already exist in the stack", this);
            }
            Timber.d("onAttach() called with: activity = [%s], stack size: %d", activity, fragmentStack.size());
            super.onAttach(activity);
        }

        @Override
        public void onDetach() {
            Timber.d("onDetach() called, stack size: %d", fragmentStack.size());
            fragmentStack.pop();
            super.onDetach();
        }
    }
}
