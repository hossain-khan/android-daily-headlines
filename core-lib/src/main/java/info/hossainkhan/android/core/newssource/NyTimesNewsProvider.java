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

package info.hossainkhan.android.core.newssource;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import info.hossainkhan.android.core.model.NewsSource;
import io.swagger.client.model.ArticleCategory;


/**
 * New York Times news provider.
 * <p>
 * <h3>ATTRIBUTION</h3>
 * <p>
 * <p>Your use of any of the API content, whether served from your Web site or from a client application,
 * must appropriately attribute The New York Times by adhering to the usage guidelines in
 * <a href="/branding">The New York Times API Branding Guide</a>.</p>
 * <p>
 * <p>Any URLs that are delivered in the API content must link in each instance to the related New York Times URL.
 * You shall not display the API content in such a manner that does not allow for successful linking and redirection to,
 * and delivery of, NYTIMES.COM's Web page, nor may you frame any NYTIMES.COM Web page.</p>
 * <p>
 * <a href="https://developer.nytimes.com/attribution">https://developer.nytimes.com/attribution</a>
 */
public final class NyTimesNewsProvider implements NewsProvider {

    public static final String PROVIDER_ID_NYTIMES = "nytimes";

    @Override
    public NewsSource getNewsSource() {
        // TODO Refactor this - not a correct way to provide implementation
        NewsSource nyTimesNewsSource = new NewsSource(PROVIDER_ID_NYTIMES, "The New York Times");

        /*
         * All applications must be accompanied by a Times API logo on any page or screen that displays
         * Times API content or data. The logo must link directly to http://developer.nytimes.com.
         */
        nyTimesNewsSource.setUrl("http://developer.nytimes.com");

        /*
         * For applications that do not easily support logos or where Times data are used in alternative media formats,
         * the written attribution "Data provided by The New York Times" can be substituted.
         */
        nyTimesNewsSource.setName("Data provided by \nThe New York Times");

        /*
         * Taken from https://developer.nytimes.com/branding
         */
        nyTimesNewsSource.setImageUrl("http://static01.nytimes.com/packages/images/developer/logos/poweredby_nytimes_200a.png");

        /*
         * RESTRICTION: Unless otherwise consented to or permitted by NYTIMES.COM, you will not archive or cache any
         * of the API content for access by users for more than 24 hours after you have finished using the service;
         * or for any period of time if your account is terminated.
         */
        nyTimesNewsSource.setCacheDuration(TimeUnit.HOURS.toSeconds(24));

        return nyTimesNewsSource;
    }

    @Override
    public Set<ArticleCategory> getSupportedCategories() {
        Set<ArticleCategory> categories = new HashSet<>();
        categories.add(ArticleCategory.home);
        categories.add(ArticleCategory.world);
        categories.add(ArticleCategory.business);
        categories.add(ArticleCategory.technology);
        categories.add(ArticleCategory.movies);
        categories.add(ArticleCategory.sports);

        return categories;
    }
}
