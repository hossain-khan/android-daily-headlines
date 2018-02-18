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

import android.support.annotation.DrawableRes
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import com.google.gson.annotations.SerializedName
import io.swagger.client.model.Article
import timber.log.Timber


/**
 * This is a model object that encapsulates an card item with required and optional information.
 */
data class CardItem(
        @SerializedName("id")
        val id: Int,

        @Nullable
        @SerializedName("title")
        val title: String?,

        @Nullable
        @SerializedName("description")
        val description: String? = null,

        @Nullable
        @SerializedName("extraText")
        val extraText: String? = null,

        @Nullable
        @SerializedName("category")
        val category: String? = null,

        @Nullable
        @SerializedName("dateCreated")
        val dateCreated: String? = null,

        @Nullable
        @SerializedName("imageUrl")
        val imageUrl: String? = null,

        @Nullable
        @SerializedName("contentUrl")
        val contentUrl: String? = null,

        @DrawableRes
        @SerializedName("localImageResourceId")
        val localImageResourceId: Int,

        @Nullable
        @SerializedName("footerColor")
        val footerColor: String? = null,

        @Nullable
        @SerializedName("selectedColor")
        val selectedColor: String? = null,

        @NonNull
        @SerializedName("type")
        val type: CardType,

        @SerializedName("width")
        val width: Int,

        @SerializedName("height")
        val height: Int) {

    companion object {
        /**
         * Creates a card item with {@link Article} model.
         *
         * @param article Article instance to convert to CardItem.
         */
        fun create(article: Article): CardItem {
            val multimedia = article.multimedia
            val size = multimedia.size
            var imageUrl: String? = null
            var width = 0
            var height = 0
            if (!multimedia.isEmpty()) {
                val articleMultimedia = multimedia[size - 1]
                imageUrl = articleMultimedia.url
                width = articleMultimedia.width
                height = articleMultimedia.height
            } else {
                Timber.w("Article '%s' does not have image.", article.title);
            }

            return CardItem(
                    id = article.getUrl().hashCode(),
                    title = article.getTitle(),
                    description = article.getAbstract(),
                    extraText = "",
                    category = article.getSection(),
                    dateCreated = article.getCreatedDate(),
                    imageUrl = imageUrl,
                    contentUrl = article.getUrl(),
                    localImageResourceId = 0,
                    footerColor = "",
                    selectedColor = "",
                    type = CardType.HEADLINES,
                    width = width,
                    height = height
            )
        }

        fun create(
                id: Int, title: String?, description: String?, extraText: String?, category: String?,
                dateCreated: String?, imageUrl: String?, contentUrl: String?,
                localImageResourceId: Int, footerColor: String?, selectedColor: String?,
                type: CardType, width: Int, height: Int): CardItem {
            return CardItem(id,
                    title,
                    description,
                    extraText,
                    category,
                    dateCreated,
                    imageUrl,
                    contentUrl,
                    localImageResourceId,
                    footerColor,
                    selectedColor,
                    type,
                    width,
                    height)
        }
    }
}
