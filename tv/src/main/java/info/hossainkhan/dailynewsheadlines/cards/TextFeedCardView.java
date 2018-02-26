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

package info.hossainkhan.dailynewsheadlines.cards;

import android.content.Context;
import android.content.res.Resources;
import android.support.v17.leanback.widget.BaseCardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

import info.hossainkhan.android.core.model.NewsHeadlineItem;
import info.hossainkhan.android.core.picasso.BlurTransformation;
import info.hossainkhan.android.core.picasso.GrayscaleTransformation;
import info.hossainkhan.android.core.usersource.UserSourceManager;
import info.hossainkhan.android.core.util.StringUtils;
import info.hossainkhan.dailynewsheadlines.R;
import timber.log.Timber;


/**
 * Text based card view for feed.
 */
public class TextFeedCardView extends BaseCardView {

    /**
     * User source manager used to check if source is already added.
     */
    private final UserSourceManager mUserSourceManager;

    public TextFeedCardView(final Context context) {
        super(context, null, R.style.TextCardStyle);
        LayoutInflater.from(getContext()).inflate(R.layout.text_icon_card_feed, this);
        setFocusable(true);

        mUserSourceManager = new UserSourceManager(context);
    }

    public void updateUi(NewsHeadlineItem newsHeadlineItem) {
        final TextView primaryHeadline = (TextView) findViewById(R.id.primary_headline_text);
        final TextView summaryText1 = (TextView) findViewById(R.id.summary_text_1);
        final TextView summaryText2 = (TextView) findViewById(R.id.summary_text_2);
        final ImageView mainContentBackground = (ImageView) findViewById(R.id.main_content_background);
        final ImageView iconImage = (ImageView) findViewById(R.id.feed_provider_icon);
        final ImageView bookmarkedBadge = (ImageView) findViewById(R.id.feed_subscribed_marker_badge);

        if (mUserSourceManager.isAdded(newsHeadlineItem.getContentUrl())) {
            bookmarkedBadge.setVisibility(View.VISIBLE);
        } else {
            bookmarkedBadge.setVisibility(View.INVISIBLE);
        }


        primaryHeadline.setText(newsHeadlineItem.getTitle());
        summaryText1.setText(newsHeadlineItem.getDescription());

        // Reusing the "NewsHeadlineItem.width" to get total subscriber stats
        summaryText2.setText(String.format(getContext().getString(R.string.feed_stats_overview),
                NumberFormat.getInstance().format(newsHeadlineItem.getWidth())));

        Context context = getContext();
        Picasso picasso = Picasso.with(context);
        Resources resources = context.getResources();
        if (StringUtils.isNotEmpty(newsHeadlineItem.getImageUrl())) {
            picasso
                    .load(newsHeadlineItem.getImageUrl())
                    .resize((int) resources.getDimension(R.dimen.card_text_container_width),
                            (int) resources.getDimension(R.dimen.card_text_container_height))
                    .transform(new GrayscaleTransformation(picasso))
                    .transform(new BlurTransformation(context))
                    .centerInside()
                    .into(mainContentBackground);
        } else {
            Timber.w("Unable to load thumb image.");
        }

        // Reusing the "NewsHeadlineItem.extraText" to get the icon image URL
        if (StringUtils.isNotEmpty(newsHeadlineItem.getExtraText())) {
            picasso
                    .load(newsHeadlineItem.getExtraText())
                    .into(iconImage);
        } else {
            Timber.w("Unable to load icon image.");
        }
    }
}
