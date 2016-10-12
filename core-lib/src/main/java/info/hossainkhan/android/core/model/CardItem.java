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

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import io.swagger.client.model.Article;
import io.swagger.client.model.ArticleMultimedia;
import timber.log.Timber;

/**
 * This is a model object that encapsulates an card item with required and optional information.
 */
public class CardItem {



    /**
     * Type of the cards supported by the TV browser.
     */
    public enum Type {
        HEADLINES,
        DETAILS,
        ICON
    }

    @SerializedName("title")
    private String mTitle = "";

    @SerializedName("description")
    private String mDescription = "";

    @SerializedName("extraText")
    private String mExtraText = "";

    @SerializedName("category")
    private String mCategory;

    @SerializedName("dateCreated")
    private String mDateCreated;

    @SerializedName("imageUrl")
    private String mImageUrl;

    @SerializedName("footerColor")
    private String mFooterColor = null;

    @SerializedName("selectedColor")
    private String mSelectedColor = null;

    @SerializedName("type")
    private CardItem.Type mType;

    @SerializedName("id")
    private int mId;

    @SerializedName("width")
    private int mWidth;

    @SerializedName("height")
    private int mHeight;

    /**
     * Creates a card item with {@link Article} model.
     * @param article
     */
    public CardItem(@NonNull final Article article) {
        mTitle = article.getTitle();
        mDescription = article.getAbstract();
        mType = Type.HEADLINES;
        mId = article.getUrl().hashCode();

        mCategory = article.getSection();
        mDateCreated = article.getCreatedDate();


        List<ArticleMultimedia> multimedia = article.getMultimedia();
        int size = multimedia.size();
        if(!multimedia.isEmpty()) {
            ArticleMultimedia articleMultimedia = multimedia.get(size-1);
            mImageUrl = articleMultimedia.getUrl();
            mWidth = articleMultimedia.getWidth();
            mHeight = articleMultimedia.getHeight();
        } else {
            String NO_IMAGE_MSG = "Article does not have image.";
            Timber.w("%s Total items: %d", NO_IMAGE_MSG, size);
            FirebaseCrash.log(NO_IMAGE_MSG);
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setType(Type type) {
        mType = type;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getId() {
        return mId;
    }

    public CardItem.Type getType() {
        return mType;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(String mDateCreated) {
        this.mDateCreated = mDateCreated;
    }


    public String getExtraText() {
        return mExtraText;
    }

    public void setExtraText(String extraText) {
        mExtraText = extraText;
    }

    public int getFooterColor() {
        if (mFooterColor == null) return -1;
        return Color.parseColor(mFooterColor);
    }

    public void setFooterColor(String footerColor) {
        mFooterColor = footerColor;
    }

    public int getSelectedColor() {
        if (mSelectedColor == null) return -1;
        return Color.parseColor(mSelectedColor);
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setSelectedColor(String selectedColor) {
        mSelectedColor = selectedColor;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public URI getImageURI() {
        if (getImageUrl() == null) return null;
        try {
            return new URI(getImageUrl());
        } catch (URISyntaxException e) {
            FirebaseCrash.report(e);
            Timber.w("URI exception: ", getImageUrl());
            return null;
        }
    }

}
