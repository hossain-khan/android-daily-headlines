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

import com.google.gson.annotations.SerializedName;

/**
 * A model class to define information on news source or publisher.
 */
data class NewsSource(
        /**
         * A unique identifier for the news-source/publication.
         */
        @SerializedName("id")
        val id: String?,

        /**
         * The publication's name.
         */
        @SerializedName("name")
        val name: String?,

        @SerializedName("description")
        val description: String?,

        /**
         * The URL to the publication's homepage
         */
        @SerializedName("url")
        val url: String?,

        @SerializedName("imageUrl")
        val imageUrl: String?,

        /**
         * Max cache duration in <b>SECONDS</b> for the publication content. Default is {@code 0}, which means no
         * limitation.
         */
        @SerializedName("cacheDuration")
        val cacheDuration: Long) {
    companion object {
        fun create(id: String, name: String, description: String, url: String, imageUrl: String,
                   cacheDuration: Long): NewsSource {
            return NewsSource(id, name, description, url, imageUrl, cacheDuration)
        }
    }
}
