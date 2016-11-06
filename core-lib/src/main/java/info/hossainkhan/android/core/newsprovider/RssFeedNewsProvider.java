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

package info.hossainkhan.android.core.newsprovider;

import android.content.Context;
import android.text.TextUtils;

import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.PkRSS;

import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.android.core.model.NavigationRow;
import info.hossainkhan.android.core.model.NewsProvider;
import io.swagger.client.model.ArticleCategory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A common class to represent RSS/Atom feed based news provider.
 */
public abstract class RssFeedNewsProvider implements NewsProvider {

    private final Context mContext;

    /**
     * @return The RSS/Atom feed URL for the news provider.
     */
    public abstract String getFeedUrl();

    public RssFeedNewsProvider(final Context context) {
        this.mContext = context;
    }

    @Override
    public Set<ArticleCategory> getSupportedCategories() {
        return Collections.emptySet();
    }

    @Override
    public Observable<List<NavigationRow>> getNewsObservable() {
        return Observable.create(new Observable.OnSubscribe<List<NavigationRow>>() {
            @Override
            public void call(final Subscriber<? super List<NavigationRow>> subscriber) {
                try {
                    // Make Synchronous call to get all the data.
                    List<Article> articleList = PkRSS.with(mContext)
                            .load(getFeedUrl())
                            .get();

                    int totalResponseItemSize = articleList.size();
                    List<NavigationRow> navigationHeadlines = new ArrayList<>(totalResponseItemSize + 1);
                    navigationHeadlines.add(NavigationRow.builder()
                            .title(getNewsSource().name())
                            .type(NavigationRow.TYPE_SECTION_HEADER)
                            .build());


                    navigationHeadlines.add(
                            NavigationRow.builder()
                                    .title("Headlines")
                                    .category(ArticleCategory.technology)
                                    .cards(convertArticleToCardItems(articleList))
                                    .build()
                    );
                    subscriber.onNext(navigationHeadlines);
                } catch (IOException e) {
                    subscriber.onError(e);
                }

                subscriber.onCompleted();
            }
        })
                // By default network call must be done on non-ui thread.
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Converts feed article into applications {@link CardItem}.
     *
     * @param articleList Feed article list.
     * @return List of card items.
     */
    private List<CardItem> convertArticleToCardItems(final List<Article> articleList) {
        List<CardItem> cardItems = new ArrayList<>(articleList.size());

        int MAX_TAGS = 3;
        for (final Article article : articleList) {

            // Reduce tags for UI space limitation
            List<String> tags = article.getTags();
            int tagsSize = tags.size();
            tags = tags.subList(0, (tagsSize > MAX_TAGS) ? MAX_TAGS : tagsSize);

            cardItems.add(
                    CardItem.create(
                            article.getId(), // id,
                            article.getTitle(), // title,
                            article.getDescription(), // description,
                            article.getContent(), //extraText,
                            TextUtils.join(", ", tags), //category,
                            ISODateTimeFormat.dateTime().print(article.getDate()), // dateCreated,
                            article.getImage().toString(), // imageUrl,
                            article.getSource().toString(), // contentUrl,
                            0, // localImageResourceId,
                            null, // footerColor,
                            null, // selectedColor,
                            CardItem.Type.HEADLINES, // type,
                            0, // width,
                            0 // height
                    )
            );
        }
        return cardItems;
    }


}
