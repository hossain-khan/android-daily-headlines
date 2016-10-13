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

import java.util.List;

import io.swagger.client.model.ArticleCategory;

/**
 * This class represents a navigation row with news headline cards.
 */
public class NavigationRow {

    // default is a list of cards
    public static final int TYPE_DEFAULT = 0;
    // section header
    public static final int TYPE_SECTION_HEADER = 1;
    // divider
    public static final int TYPE_DIVIDER = 2;

    @SerializedName("type")
    private int mType = TYPE_DEFAULT;

    // Used to determine whether the row shall use shadows when displaying its cards or not.
    @SerializedName("shadow")
    private boolean mShadow;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("category")
    private ArticleCategory mCategory;

    @SerializedName("cards")
    private List<CardItem> mCards;

    private NavigationRow(int type, String title, ArticleCategory category, List<CardItem> cards, boolean useShadow) {
        mTitle = title;
        mCategory = category;
        mType = type;
        mCards = cards;
        mShadow = useShadow;
    }

    public int getType() {
        return mType;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean useShadow() {
        return mShadow;
    }

    public List<CardItem> getCards() {
        return mCards;
    }

    public ArticleCategory getCategory() {
        return mCategory;
    }


    public static class Builder {
        private int type;
        private String title;
        private ArticleCategory category;
        private List<CardItem> cards;
        private boolean useShadow = true;

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Used to determine whether the row shall use shadows when displaying its cards or not.
         * @param useShadow
         * @return
         */
        public Builder useShadow(boolean useShadow) {
            this.useShadow = useShadow;
            return this;
        }

        public Builder setCategory(ArticleCategory category) {
            this.category = category;
            return this;
        }

        public Builder setCards(List<CardItem> cards) {
            this.cards = cards;
            return this;
        }

        public NavigationRow build() {
            return new NavigationRow(type, title, category, cards, useShadow);
        }
    }
}
