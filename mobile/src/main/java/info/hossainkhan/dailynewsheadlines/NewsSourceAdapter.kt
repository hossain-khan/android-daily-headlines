/*
 * MIT License
 *
 * Copyright (c) 2018 Hossain Khan
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

package info.hossainkhan.dailynewsheadlines

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import info.hossainkhan.android.core.model.NavigationRow
import kotlinx.android.synthetic.main.list_item_news_source.view.*

/**
 * Adapter to hold all the headlines from all news sources.
 */
class NewsSourceAdapter(private val headlines: List<NavigationRow>,
                        private val clickListener: (NavigationRow) -> Unit)
    : RecyclerView.Adapter<NewsSourceAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return headlines.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder!!.mContentView.text = headlines[position].title()

        holder.mContentView.setOnClickListener(View.OnClickListener {
            // TODO get adapter postion
            clickListener.invoke(headlines[position])
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.list_item_news_source, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemRootView: View) : RecyclerView.ViewHolder(itemRootView) {
        val mContentView: TextView = itemRootView.news_source_item_title
    }
}