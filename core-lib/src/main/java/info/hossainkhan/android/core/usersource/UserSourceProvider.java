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

package info.hossainkhan.android.core.usersource;


import java.util.Map;
import java.util.Set;

/**
 * Manages RSS/Atom based news sources that is added by user.
 */
public interface UserSourceProvider {

    /**
     * Adds a news source to the system.
     *
     * @param title News source title.
     * @param url   News source feed URL.
     */
    void addSource(String title, String url);

    /**
     * Removes a news source by URL.
     *
     * @param url Feed URL for news source.
     * @return The title of URL remove, or {@code null} if URL was never there.
     */
    String removeSource(String url);

    /**
     * Removes multiple news sources by URL.
     *
     * @param urls Unique urls to remove.
     */
    void removeSources(Set<String> urls);

    /**
     * Returns a map of news source paired by URL & Title
     *
     * @return Map of news sources. URL is key, and Title is value.
     */
    Map<String, String> getSources();
}
