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
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.firebase.crash.FirebaseCrash;
import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.PkRSS;

import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import info.hossainkhan.android.core.BuildConfig;
import info.hossainkhan.android.core.model.NewsHeadlineItem;
import info.hossainkhan.android.core.model.CardType;
import info.hossainkhan.android.core.model.NewsCategoryHeadlines;
import info.hossainkhan.android.core.model.NewsHeadlines;
import info.hossainkhan.android.core.model.NewsProvider;
import io.swagger.client.model.ArticleCategory;
import rx.Emitter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


/**
 * A common class to represent RSS/Atom feed based news provider.
 */
public abstract class RssFeedNewsProvider implements NewsProvider {

    private final Context mContext;

    /**
     * @return The RSS/Atom feed URL for the news provider.
     */
    @NonNull
    public abstract String getFeedUrl();

    public RssFeedNewsProvider(@NonNull final Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public Set<ArticleCategory> getSupportedCategories() {
        return Collections.emptySet();
    }

    @NonNull
    @Override
    public Observable<NewsHeadlines> getNewsObservable() {
        return Observable.<NewsHeadlines>create(emitter -> {
            try {
                // Make Synchronous call to get all the data.
                PkRSS pkRSS = PkRSS.with(mContext);
                Timber.d("Requesting %s with debug on = %s", getFeedUrl(), BuildConfig.DEBUG);
                pkRSS.setLoggingEnabled(BuildConfig.DEBUG);

                List<Article> articleList = pkRSS
                        .load(getFeedUrl())
                        .get();

                List<NewsCategoryHeadlines> navigationHeadlines = new ArrayList<>(1);
                navigationHeadlines.add(
                        NewsCategoryHeadlines.Companion.builder(getNewsSource().getId())
                                .title(getNewsSource().getName())
                                .displayTitle(getNewsSource().getName())
                                .category(ArticleCategory.technology)
                                .cards(convertArticleToCardItems(articleList))
                                .build()
                );
                emitter.onNext(new NewsHeadlines(getNewsSource(), navigationHeadlines));
                emitter.onCompleted();
            } catch (IOException e) {
                FirebaseCrash.report(e);
                emitter.onError(e);
            }

        }, Emitter.BackpressureMode.BUFFER)
                // By default network call must be done on non-ui thread.
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(Observable.empty());
    }

    /**
     * Converts feed article into applications {@link NewsHeadlineItem}.
     *
     * @param articleList Feed article list.
     * @return List of card items.
     */
    private List<NewsHeadlineItem> convertArticleToCardItems(final List<Article> articleList) {
        List<NewsHeadlineItem> newsHeadlineItems = new ArrayList<>(articleList.size());

        int MAX_TAGS = 3;
        for (final Article article : articleList) {

            // Reduce tags for UI space limitation
            List<String> tags = article.getTags();
            int tagsSize = tags.size();
            tags = tags.subList(0, (tagsSize > MAX_TAGS) ? MAX_TAGS : tagsSize);

            Uri image = article.getImage();
            Uri source = article.getSource();

            newsHeadlineItems.add(
                    NewsHeadlineItem.Companion.create(
                            article.getId(), // id,
                            article.getTitle(), // title,
                            article.getDescription(), // description,
                            article.getContent(), //extraText,
                            TextUtils.join(", ", tags), //category,
                            ISODateTimeFormat.dateTime().print(article.getDate()), // dateCreated,
                            (image != null) ? image.toString() : null, // imageUrl,
                            (source != null) ? source.toString() : null, // contentUrl,
                            0, // localImageResourceId,
                            null, // footerColor,
                            null, // selectedColor,
                            CardType.HEADLINES, // type,
                            0, // width,
                            0 // height
                    )
            );
        }
        return newsHeadlineItems;
    }


}
