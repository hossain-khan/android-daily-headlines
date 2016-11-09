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

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import info.hossainkhan.android.core.gson.AutoGson;
import io.swagger.client.model.Article;
import io.swagger.client.model.ArticleMultimedia;
import timber.log.Timber;

/**
 * This is a model object that encapsulates an card item with required and optional information.
 */
@AutoValue @AutoGson
public abstract class CardItem {

    /**
     * Type of the cards supported by the TV browser.
     */
    public enum Type {
        /**
         * News headlines or article item.
         */
        HEADLINES,
        /**
         * Details for item. Not used yet.
         */
        DETAILS,
        /**
         * Action item, ususally icon based item for user action.
         */
        ACTION
    }

    @SerializedName("id")
    public abstract int id();

    @Nullable
    @SerializedName("title")
    public abstract String title();

    @Nullable
    @SerializedName("description")
    public abstract String description();

    @Nullable
    @SerializedName("extraText")
    public abstract String extraText();

    @Nullable
    @SerializedName("category")
    public abstract String category();

    @Nullable
    @SerializedName("dateCreated")
    public abstract String dateCreated();

    @Nullable
    @SerializedName("imageUrl")
    public abstract String imageUrl();

    @Nullable
    @SerializedName("contentUrl")
    public abstract String contentUrl();

    @DrawableRes
    @SerializedName("localImageResourceId")
    public abstract int localImageResourceId();

    @Nullable
    @SerializedName("footerColor")
    public abstract String footerColor();

    @Nullable
    @SerializedName("selectedColor")
    public abstract String selectedColor();

    @NonNull
    @SerializedName("type")
    public abstract CardItem.Type type();

    @SerializedName("width")
    public abstract int width();

    @SerializedName("height")
    public abstract int height();

    public static CardItem create(
            int id, @Nullable String title, @Nullable String description, @Nullable String extraText, @Nullable
            String category, @Nullable String dateCreated, @Nullable String imageUrl, @Nullable String contentUrl,
            int localImageResourceId, @Nullable String footerColor, @Nullable String selectedColor,
            CardItem.Type type, int width, int height) {
        return new AutoValue_CardItem(id,
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
                height);
    }

    /**
     * Creates a card item with {@link Article} model.
     *
     * @param article Article instance to convert to CardItem.
     */
    public static CardItem create(@NonNull final Article article) {

        List<ArticleMultimedia> multimedia = article.getMultimedia();
        int size = multimedia.size();
        String imageUrl = null;
        int width = 0;
        int height = 0;
        if (!multimedia.isEmpty()) {
            ArticleMultimedia articleMultimedia = multimedia.get(size - 1);
            imageUrl = articleMultimedia.getUrl();
            width = articleMultimedia.getWidth();
            height = articleMultimedia.getHeight();
        } else {
            String NO_IMAGE_MSG = "Article does not have image.";
            Timber.w("%s Total items: %d", NO_IMAGE_MSG, size);
        }

        return new AutoValue_CardItem(
                article.getUrl().hashCode(), // id,
                article.getTitle(), // title,
                article.getAbstract(), // description,
                "", //extraText,
                article.getSection(), //category,
                article.getCreatedDate(), // dateCreated,
                imageUrl, // imageUrl,
                article.getUrl(), // contentUrl,
                0, // localImageResourceId,
                "", // footerColor,
                "", // selectedColor,
                Type.HEADLINES, // type,
                width, // width,
                height);

    }



    public URI getImageURI() {
        if (imageUrl() == null) return null;
        try {
            return new URI(imageUrl());
        } catch (URISyntaxException e) {
            Timber.e(e);
            Timber.w("URI exception: ", imageUrl());
            return null;
        }
    }

}
