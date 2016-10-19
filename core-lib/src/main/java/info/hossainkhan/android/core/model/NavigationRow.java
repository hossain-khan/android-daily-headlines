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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import info.hossainkhan.android.core.gson.AutoGson;
import io.swagger.client.model.ArticleCategory;

/**
 * This class represents a navigation row with news headline cards.
 */
@AutoValue
@AutoGson
public abstract class NavigationRow {

    // default is a list of cards
    public static final int TYPE_DEFAULT = 0;
    // section header
    public static final int TYPE_SECTION_HEADER = 1;
    // divider
    public static final int TYPE_DIVIDER = 2;

    @SerializedName("type")
    public abstract int type();

    // Used to determine whether the row shall use shadows when displaying its cards or not.
    @SerializedName("shadow")
    public abstract boolean useShadow();

    @NonNull
    @SerializedName("title")
    public abstract String title();

    @Nullable
    @SerializedName("category")
    public abstract ArticleCategory category();

    @Nullable
    @SerializedName("cards")
    public abstract List<CardItem> cards();

    public static Builder builder() {
        // Provides the builder with some default values
        return new AutoValue_NavigationRow.Builder()
                .type(TYPE_DEFAULT)
                .useShadow(true);
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder type(int type);
        @NonNull
        public abstract Builder title(String title);
        @Nullable
        public abstract Builder category(ArticleCategory category);
        @Nullable
        public abstract Builder cards(List<CardItem> cards);
        /**
         * Used to determine whether the row shall use shadows when displaying its cards or not.
         * @param useShadow flag for card shadow
         * @return Builder
         */
        public abstract Builder useShadow(boolean useShadow);

        public abstract NavigationRow build();
    }
}
