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

package info.hossainkhan.android.core.headlines;


import android.content.Context;

import info.hossainkhan.android.core.R;
import info.hossainkhan.android.core.base.BasePresenter;
import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.android.core.util.StringUtils;
import timber.log.Timber;

public class HeadlinesDetailsViewPresenter extends BasePresenter<HeadlinesDetailsContract.View>
        implements HeadlinesDetailsContract.Presenter {

    private final Context mContext;
    private final CardItem mCardItem;

    public HeadlinesDetailsViewPresenter(final Context context, final HeadlinesDetailsContract.View view, final CardItem cardItem) {
        mContext = context;
        mCardItem = cardItem;

        attachView(view);
        initView();
    }

    private void initView() {
        getView().updateScreenTitle(mContext.getString(R.string.detail_view_title));
        getView().showHeadlineDetails(mCardItem);

        String imageUrl = mCardItem.imageUrl();
        if(StringUtils.isValidUri(imageUrl)) {
            Timber.d("Loading image URL: %s", imageUrl);
            getView().loadDetailsImage(imageUrl);
        } else {
            Timber.d("Invalid image URL: %s, loading default background instead", imageUrl);
            getView().loadDefaultImage();
        }
    }




    @Override
    public void onActionItemClicked(final int action) {
        switch (action) {
            case HeadlinesDetailsContract.ACTION_ID_OPEN_NEWS_URL:
                getView().openArticleWebUrl(mCardItem.contentUrl());
                break;
            default:
                Timber.d("onActionItemClicked() : Unsupported action id: %s", action);
                break;
        }
    }
}
