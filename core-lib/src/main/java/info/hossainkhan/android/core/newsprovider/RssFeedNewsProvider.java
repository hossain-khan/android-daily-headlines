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
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.firebase.crash.FirebaseCrash;
import com.nytimes.android.external.store3.base.impl.BarCode;
import com.nytimes.android.external.store3.base.impl.Store;
import com.nytimes.android.external.store3.base.impl.StoreBuilder;
import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.PkRSS;

import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.android.core.model.CardType;
import info.hossainkhan.android.core.model.NavigationRow;
import info.hossainkhan.android.core.model.NewsProvider;
import io.swagger.client.model.ArticleCategory;
import rx.Emitter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A common class to represent RSS/Atom feed based news provider.
 */
public abstract class RssFeedNewsProvider implements NewsProvider {

    private final Context mContext;

    @NonNull
    private final Store<List<NavigationRow>, BarCode> store;

    /**
     * @return The RSS/Atom feed URL for the news provider.
     */
    public abstract String getFeedUrl();

    public RssFeedNewsProvider(final Context context) {
        this.mContext = context;

        store = StoreBuilder.<List<NavigationRow>>barcode()
                .fetcher(barCode -> RxJavaInterop.toV2Single(getNewsObservable().toSingle()))
                .open();
    }

    @Override
    public Set<ArticleCategory> getSupportedCategories() {
        return Collections.emptySet();
    }

    @NonNull
    @Override
    public Store<List<NavigationRow>, BarCode> getNewsStore() {
        return store;
    }

    @Override
    public Observable<List<NavigationRow>> getNewsObservable() {
        return Observable.<List<NavigationRow>>create(emitter -> {
            try {
                // Make Synchronous call to get all the data.
                List<Article> articleList = PkRSS.with(mContext)
                        .load(getFeedUrl())
                        .get();

                int totalResponseItemSize = articleList.size();
                List<NavigationRow> navigationHeadlines = new ArrayList<>(totalResponseItemSize + 1);
                navigationHeadlines.add(NavigationRow.Companion.builder()
                        .title(getNewsSource().getName())
                        .displayTitle(getNewsSource().getName())
                        .type(NavigationRow.TYPE_SECTION_HEADER)
                        .sourceId(getNewsSource().getId())
                        .build());


                navigationHeadlines.add(
                        NavigationRow.Companion.builder()
                                .title("Headlines")
                                .displayTitle(getNewsSource().getName())
                                .category(ArticleCategory.technology)
                                .cards(convertArticleToCardItems(articleList))
                                .sourceId(getNewsSource().getId())
                                .build()
                );
                emitter.onNext(navigationHeadlines);
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

            Uri image = article.getImage();
            Uri source = article.getSource();

            cardItems.add(
                    CardItem.Companion.create(
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
        return cardItems;
    }


}
