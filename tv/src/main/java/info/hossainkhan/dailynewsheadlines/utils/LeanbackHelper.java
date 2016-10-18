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

package info.hossainkhan.dailynewsheadlines.utils;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.android.core.model.NavigationRow;
import info.hossainkhan.android.core.util.Validate;
import info.hossainkhan.dailynewsheadlines.R;

/**
 * Util classes related to leanback and application.
 */
public final class LeanbackHelper {

    /**
     * Builds a navigation header item.
     *
     * @param resources   Resources.
     * @param stringResId String res for the navigation header item.
     * @return {@link NavigationRow} for a header.
     */
    public static NavigationRow buildNavigationHeader(@NonNull Resources resources, @StringRes int stringResId) {
        return new NavigationRow.Builder()
                .setTitle(resources.getString(stringResId))
                .setType(NavigationRow.TYPE_SECTION_HEADER)
                .build();
    }

    /**
     * Builds a navigation divider item.
     *
     * @return {@link NavigationRow} for a divider.
     */
    public static NavigationRow buildNavigationDivider() {
        return new NavigationRow.Builder().setType(NavigationRow.TYPE_DIVIDER).build();
    }


    /**
     * Adds leanback app's navigation {@link android.support.v17.leanback.widget.Row} for sidebar.
     * Adds static navigation items like Menu and settings to existing list of navigation.
     *
     * @param resources Resources for using string res.
     * @param list      Existing list where setting will be added.
     */
    public static void addSettingsNavigation(final Resources resources, final List<NavigationRow> list) {
        Validate.notNull(list);

        // Begin settings section
        list.add(buildNavigationDivider());
        list.add(buildNavigationHeader(resources, R.string.navigation_header_item_settings_title));

        // Build settings items

        List<CardItem> settingsItems = new ArrayList<>();
        CardItem item = CardItem.create(
                R.string.settings_card_item_news_source_title, // id,
                resources.getString(R.string.settings_card_item_news_source_title), // title,
                null, // description,
                null, //extraText,
                null, //category,
                null, // dateCreated,
                null, // imageUrl,
                null, // contentUrl,
                R.drawable.ic_settings_settings, // localImageResourceId,
                null, // footerColor,
                null, // selectedColor,
                CardItem.Type.ICON, // type,
                0, // width,
                0 // height
        );

        settingsItems.add(item);

        list.add(new NavigationRow.Builder()
                .setTitle(resources.getString(R.string.settings_navigation_row_news_source_title))
                .setType(NavigationRow.TYPE_DEFAULT)
                .setCards(settingsItems)
                .useShadow(false)
                .build());

        // Add end divider
        list.add(buildNavigationDivider());
    }
}
