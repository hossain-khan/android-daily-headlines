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

import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ObjectAdapter;

import info.hossainkhan.android.core.model.NewsCategoryHeadlines;

/**
 * The {@link CardListRow} allows the {@link info.hossainkhan.dailynewsheadlines.cards.presenters.selectors.ShadowRowPresenterSelector}
 * to access the {@link NewsCategoryHeadlines} held by the row and determine whether to use a
 * {@link android.support.v17.leanback.widget.Presenter} with or without a shadow.
 */
public class CardListRow extends ListRow {

    private NewsCategoryHeadlines mNewsCategoryHeadlines;

    public CardListRow(HeaderItem header, ObjectAdapter adapter, NewsCategoryHeadlines newsCategoryHeadlines) {
        super(header, adapter);
        setNavigationRow(newsCategoryHeadlines);
    }

    public NewsCategoryHeadlines getNavigationRow() {
        return mNewsCategoryHeadlines;
    }

    public void setNavigationRow(NewsCategoryHeadlines newsCategoryHeadlines) {
        this.mNewsCategoryHeadlines = newsCategoryHeadlines;
    }
}
