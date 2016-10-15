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

import com.google.gson.annotations.SerializedName;

/**
 * A model class to define information on news source or publisher.
 */
public class NewsSource {

    /**
     * A unique identifier for the news-source/publication.
     */
    @SerializedName("id")
    private String id = null;

    /**
     * The publication's name.
     */
    @SerializedName("name")
    private String name = null;

    @SerializedName("description")
    private String description = null;

    /**
     * The URL to the publication's homepage
     */
    @SerializedName("url")
    private String url = null;

    @SerializedName("imageUrl")
    private String imageUrl = null;

    /**
     * Max cache duration in <b>SECONDS</b> for the publication content. Default is {@code 0}, which means no
     * limitation.
     */
    @SerializedName("cacheDuration")
    private long cacheDuration;

    /**
     * Creates news source with required information.
     *
     * @param id   Source ID.
     * @param name Name of news source that is used to display to user.
     */
    public NewsSource(@NonNull final String id, @NonNull final String name) {
        this.id = id;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(@NonNull final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Get max cache duration in <b>SECONDS</b> for the publication content.
     *
     * @return Duration in seconds, or {@code 0}, which means no limitation.
     */
    public long getCacheDuration() {
        return cacheDuration;
    }

    public void setCacheDuration(final long cacheDuration) {
        this.cacheDuration = cacheDuration;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final NewsSource that = (NewsSource) o;

        if (!id.equals(that.id)) return false;
        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
