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

package info.hossainkhan.dailynewsheadlines.details;

import android.content.Context;
import android.support.v17.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.android.core.util.DateUtils;
import info.hossainkhan.android.core.util.ResourceCache;
import info.hossainkhan.dailynewsheadlines.R;

/**
 * This presenter is used to render a {@link info.hossainkhan.android.core.model.CardItem} in the {@link
 * HeadlinesDetailsFragment}.
 */
public class HeadlinesDetailsPresenter extends Presenter {

    private ResourceCache mResourceCache = new ResourceCache();
    private Context mContext;

    public HeadlinesDetailsPresenter(Context context) {
        mContext = context;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.detail_view_content, null);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        TextView primaryText = mResourceCache.getViewById(viewHolder.view, R.id.primary_text);
        TextView sndText1 = mResourceCache.getViewById(viewHolder.view, R.id.secondary_text_first);
        TextView sndText2 = mResourceCache.getViewById(viewHolder.view, R.id.secondary_text_second);
        TextView extraText = mResourceCache.getViewById(viewHolder.view, R.id.extra_text);
        // ImageView newsSourceImage = mResourceCache.getViewById(viewHolder.view, R.id.headlines_source_image);

        CardItem card = (CardItem) item;
        primaryText.setText(card.title());
        sndText1.setText(card.category());
        sndText2.setText(DateUtils.getFormattedDateTime(card.dateCreated()));
        extraText.setText(card.description());
    }

    @Override public void onUnbindViewHolder(ViewHolder viewHolder) {
        // Nothing to do here.
    }
}
