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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import info.hossainkhan.android.core.model.NewsHeadlineItem;
import info.hossainkhan.android.core.picasso.BlurTransformation;
import info.hossainkhan.android.core.picasso.GrayscaleTransformation;
import info.hossainkhan.android.core.util.DateUtils;
import info.hossainkhan.android.core.util.StringUtils;
import info.hossainkhan.dailynewsheadlines.R;
import timber.log.Timber;

public class TextCardView extends BaseCardView {

    public TextCardView(Context context) {
        super(context, null, R.style.TextCardStyle);
        LayoutInflater.from(getContext()).inflate(R.layout.text_icon_card, this);
        setFocusable(true);
    }

    public void updateUi(NewsHeadlineItem newsHeadlineItem) {
        final TextView primaryHeadline = (TextView) findViewById(R.id.primary_headline_text);
        final TextView summaryText1 = (TextView) findViewById(R.id.summary_text_1);
        final TextView summaryText2 = (TextView) findViewById(R.id.summary_text_2);
        final ImageView mainContentBackground = (ImageView) findViewById(R.id.main_content_background);


        primaryHeadline.setText(newsHeadlineItem.getTitle());
        summaryText1.setText(newsHeadlineItem.getCategory());
        summaryText2.setText(String.format(getContext().getString(R.string.headline_elapsed_time),
                DateUtils.getElapsedTime(newsHeadlineItem.getDateCreated())));

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
                    .centerCrop()
                    .into(mainContentBackground);
        } else {
            Timber.w("Unable to load thumb image.");
        }
    }

}
