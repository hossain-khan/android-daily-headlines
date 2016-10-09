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

package info.hossainkhan.dailynewsheadlines;

import android.content.Context;
import android.support.v17.leanback.widget.BaseCardView;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import info.hossainkhan.android.core.picasso.GrayscaleTransformation;
import io.swagger.client.model.Article;

public class TextCardView extends BaseCardView {

    public TextCardView(Context context) {
        super(context, null, R.style.TextCardStyle);
        LayoutInflater.from(getContext()).inflate(R.layout.text_icon_card, this);
        setFocusable(true);
    }

    public void updateUi(Article article) {
        final TextView primaryHeadline = (TextView) findViewById(R.id.primary_headline_text);
        final TextView summaryText = (TextView) findViewById(R.id.summary_text);
        final ImageView mainContentBackground = (ImageView) findViewById(R.id.main_content_background);


        primaryHeadline.setText(article.getTitle());
        summaryText.setText(article.getAbstract());

        Picasso.with(getContext())
                .load(article.getMultimedia().get(4).getUrl())
                .resize((int) getContext().getResources().getDimension(R.dimen.card_text_container_width),
                        (int) getContext().getResources().getDimension(R.dimen.card_text_container_height))
                .transform(new GrayscaleTransformation(Picasso.with(getContext())))
                .centerCrop()
                .into(mainContentBackground);
    }

}
