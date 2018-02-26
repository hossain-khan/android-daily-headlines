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

package info.hossainkhan.android.core.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import info.hossainkhan.android.core.gson.AutoGson;
import io.swagger.client.model.ArticleCategory;

/**
 * This class represents a navigation row with news headline newsHeadlines.
 */
@AutoGson
data class NavigationRow(
        /**
         * Type of navigation item.
         *
         * @param type Allowed values are [TYPE_DEFAULT], [TYPE_DIVIDER], [TYPE_DIVIDER]
         */
        @SerializedName("type")
        val type: Int = TYPE_DEFAULT,

        /**
         * Used to determine whether the row shall use shadows when displaying its newsHeadlines or not.
         */
        @SerializedName("shadow")
        val useShadow: Boolean? = true,

        @Nullable
        @SerializedName("title")
        val title: String? = null,

        @Nullable
        @SerializedName("displayTitle")
        val displayTitle: String? = null,

        /**
         * {@link NewsSource#id()} for current navigation row, when {@link #type()} is
         * {@link NavigationRow#TYPE_SECTION_HEADER} or {@link NavigationRow#TYPE_DEFAULT}.
         *
         * @param newsSourceId News source ID from {@link NewsSource#id()}
         * @return The builder instance.
         */
        @Nullable
        @SerializedName("news_source_id")
        val sourceId: String? = null,

        @Nullable
        @SerializedName("category")
        val category: ArticleCategory? = null,

        @Nullable
        @SerializedName("cards")
        val newsHeadlines: List<NewsHeadlineItem>? = emptyList()) {

    companion object {
        // default is a list of newsHeadlines
        const val TYPE_DEFAULT = 0
        // section header
        const val TYPE_SECTION_HEADER = 1
        // divider
        const val TYPE_DIVIDER = 2

        fun builder() = Builder()
    }

    class Builder {

        private var type: Int = TYPE_DEFAULT
        private var title: String? = null
        private var displayTitle: String? = null
        private var sourceId: String? = null
        private var category: ArticleCategory? = null
        private var newsHeadlines: List<NewsHeadlineItem>? = null
        private var useShadow: Boolean? = null

        /**
         * Type of navigation item.
         *
         * @param type Allowed values are {@link NavigationRow#TYPE_DEFAULT},
         *             {@link NavigationRow#TYPE_DIVIDER}, {@link NavigationRow#TYPE_DIVIDER}
         * @return The builder instance.
         */
        fun type(type: Int) = apply { this.type = type }

        fun title(title: String) = apply { this.title = title }

        fun displayTitle(title: String) = apply { this.displayTitle = title }

        /**
         * {@link NewsSource#id()} for current navigation row, when {@link #type()} is
         * {@link NavigationRow#TYPE_SECTION_HEADER} or {@link NavigationRow#TYPE_DEFAULT}.
         *
         * @param newsSourceId News source ID from {@link NewsSource#id()}
         * @return The builder instance.
         */
        fun sourceId(newsSourceId: String) = apply { this.sourceId = newsSourceId }

        fun category(category: ArticleCategory) = apply { this.category = category }

        fun cards(newsHeadlines: List<NewsHeadlineItem>) = apply { this.newsHeadlines = newsHeadlines }

        /**
         * Used to determine whether the row shall use shadows when displaying its newsHeadlines or not.
         *
         * @param useShadow flag for card shadow
         * @return Builder
         */
        fun useShadow(useShadow: Boolean) = apply { this.useShadow = useShadow }

        fun build(): NavigationRow {
            return NavigationRow(
                    type = type,
                    title = title,
                    displayTitle = displayTitle,
                    sourceId = sourceId,
                    category = category,
                    newsHeadlines = newsHeadlines,
                    useShadow = useShadow
            )
        }
    }
}
