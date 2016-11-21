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

package com.feedly.cloud;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface SearchApi {
  /**
   * Find feeds
   * Find feeds based on title, url or topic (Authorization is optional)
   * @param query The search query. Can be a feed url, a site title, a site url or a topic (required)
   * @param count The number of results. default value is 20 (optional)
   * @param locale hint the search engine to return feeds in that locale (e.g. \&quot;pt\&quot;, \&quot;fr_FR\&quot;) topic (optional)
   * @return Call&lt;SearchResponse&gt;
   */
  
  @GET("search/feeds")
  Observable<SearchResponse> searchFeedsGet(
          @Query("query") String query, @Query("count") Integer count, @Query("locale") String locale
  );

}
