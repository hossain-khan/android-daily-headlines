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

package info.hossainkhan.android.core.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import info.hossainkhan.android.core.R;
import io.swagger.client.model.ArticleCategory;
import timber.log.Timber;

/**
 * Provides android resource id for category/section name.
 */
public class CategoryNameResolver {
    private static Map<ArticleCategory, CategoryRes> CATEGORY_SECTION_MAP = new LinkedHashMap<>(10);

    /**
     * List of supported sections by the application.
     *
     * <p>
     * NOTE: Due to limitation <code>X-RateLimit-Limit-second: 5</code>
     * we can not make more than 5 concurrent request per seconds.
     * </p>
     *
     * The order is important, and that is why {@link LinkedHashMap} has been used.
     */
    static {
        CATEGORY_SECTION_MAP.put(ArticleCategory.home, new CategoryRes(R.string.category_name_top_news, R.string
                .prefs_key_content_category_top_headlines));
        CATEGORY_SECTION_MAP.put(ArticleCategory.world, new CategoryRes(R.string.category_name_world, R.string
                .prefs_key_content_category_world));
        CATEGORY_SECTION_MAP.put(ArticleCategory.business, new CategoryRes(R.string.category_name_business,
                R.string.prefs_key_content_category_business));
        CATEGORY_SECTION_MAP.put(ArticleCategory.technology, new CategoryRes(R.string.category_name_technology, R
                .string.prefs_key_content_category_technology));

        /* // Disable movies for now - exceeds 5 items
        CATEGORY_SECTION_MAP.put(ArticleCategory.movies, new CategoryRes(R.string.category_name_movies, R.string
                .prefs_key_content_category_movies)); // */
        CATEGORY_SECTION_MAP.put(ArticleCategory.sports, new CategoryRes(R.string.category_name_sports, R.string
                .prefs_key_content_category_sports));
    }

    /**
     * Internal class to represent category specific resources.
     */
    private static class CategoryRes {
        @StringRes
        public final int title;
        @StringRes
        public final int key;

        CategoryRes(@StringRes int titleRes, @StringRes int prefKeyRes) {
            title = titleRes;
            key = prefKeyRes;
        }
    }


    @StringRes
    public static int resolveCategoryResId(ArticleCategory category) {
        int categoryTextResId = R.string.category_name_default;

        if (CATEGORY_SECTION_MAP.containsKey(category)) {
            categoryTextResId = CATEGORY_SECTION_MAP.get(category).title;
        }
        return categoryTextResId;
    }

    /**
     * Provides list of supported categories by the application.
     *
     * @return List of {@link ArticleCategory}
     */
    public static ArrayList<ArticleCategory> getSupportedCategories() {
        return new ArrayList<>(CATEGORY_SECTION_MAP.keySet());
    }


    public static List<ArticleCategory> getPreferredCategories(final Context context) {
        ArrayList<ArticleCategory> supportedCategories = CategoryNameResolver.getSupportedCategories();
        Timber.d("getPreferredCategories() - Supported Total: %s,  %s", supportedCategories.size(),
                supportedCategories);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Resources resources = context.getResources();

        for (Map.Entry<ArticleCategory, CategoryRes> entry : CATEGORY_SECTION_MAP.entrySet()) {
            ArticleCategory category = entry.getKey();
            CategoryRes categoryRes = entry.getValue();
            if (!sharedPreferences.getBoolean(resources.getString(categoryRes.key),
                    true)) {
                supportedCategories.remove(category);
            }
        }
        Timber.d("getPreferredCategories() - Total: %s,  %s", supportedCategories.size(), supportedCategories);
        return supportedCategories;
    }
}
