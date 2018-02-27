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

import java.util.Set;

import io.swagger.client.model.ArticleCategory;
import rx.Observable;


/**
 * Interface for news source provider.
 */
public interface NewsProvider {
    /**
     * Provide news source info for the provider.
     *
     * @return {@link NewsSource}
     */
    @NonNull
    NewsSource getNewsSource();

    /**
     * Provide list of supported categories for the current {@link NewsSource}.
     * @return Unique list of categories or empty if it does not support any.
     */
    @NonNull
    Set<ArticleCategory> getSupportedCategories();

    /**
     * Provides {@link Observable} containing all the headlines.
     * @return Observable.
     */
    @NonNull
    Observable<NewsHeadlines> getNewsObservable();
}
