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

import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import info.hossainkhan.android.core.R;
import io.swagger.client.model.ArticleCategory;

/**
 * Provides android resource id for category/section name.
 */
public class CategoryNameResolver {
    private static Map<ArticleCategory, Integer> CATEGORY_SECTION_MAP = new LinkedHashMap<>(10);

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
        CATEGORY_SECTION_MAP.put(ArticleCategory.home, R.string.category_name_top_news);
        CATEGORY_SECTION_MAP.put(ArticleCategory.world, R.string.category_name_world);
        CATEGORY_SECTION_MAP.put(ArticleCategory.business, R.string.category_name_business);
        CATEGORY_SECTION_MAP.put(ArticleCategory.technology, R.string.category_name_technology);
        CATEGORY_SECTION_MAP.put(ArticleCategory.movies, R.string.category_name_movies);
        CATEGORY_SECTION_MAP.put(ArticleCategory.sports, R.string.category_name_sports);
    }


    @StringRes
    public static int resolveCategoryResId(ArticleCategory category) {
        int categoryTextResId = R.string.category_name_default;

        if (CATEGORY_SECTION_MAP.containsKey(category)) {
            categoryTextResId = CATEGORY_SECTION_MAP.get(category);
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
}
