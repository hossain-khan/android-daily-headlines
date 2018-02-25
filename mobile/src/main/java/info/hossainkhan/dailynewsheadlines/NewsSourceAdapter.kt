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
import info.hossainkhan.android.core.model.NewsHeadlines
import kotlinx.android.synthetic.main.list_item_news_source.view.*
import timber.log.Timber

/**
 * Adapter to hold all the headlines from all news sources.
 * This contains List of [NewsHeadlines] that contains the news source and their list of [NavigationRow]
 */
class NewsSourceAdapter(private val headlines: List<NewsHeadlines>,
                        private val clickListener: (NavigationRow) -> Unit)
    : RecyclerView.Adapter<NewsSourceAdapter.ViewHolder>() {

    val navigationRowSizes: List<Int> = headlines.map { it.headlines.size }


    override fun getItemCount(): Int {
        return headlines.sumBy { it.headlines.size }
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        Timber.d("onBindViewHolder called with holder: [$holder], position: [$position]")
        if (position.rem(2) == 0) {
            holder!!.itemContainer.background = holder.itemContainer.context
                    .getDrawable(R.color.news_source_item_highlight)
        }


        val navigationRow = getRowForPosition(position)
        Timber.d("Found navigation row [${navigationRow.title}] for position [$position]")
        holder!!.contentView.text = navigationRow.displayTitle ?: navigationRow.title

        holder.itemContainer.setOnClickListener({
            // NOTE - get adapter postion for current item
            clickListener.invoke(getRowForPosition(position))
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.list_item_news_source, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemRootView: View) : RecyclerView.ViewHolder(itemRootView) {
        val itemContainer = itemRootView
        val contentView: TextView = itemRootView.news_source_item_title
    }

    /**
     * Finds the navigation row for given position from [headlines] data set.
     *
     * ```
     *  NewsHeadlines -> [Source] -> List<NavigationRow>
     *  [0]                               - Row 1 [0]
     *  [1]                               - Row 2 [1]
     *  NewsHeadlines -> [Source] -> List<NavigationRow>
     *  [2]                               - Row 1 [0]
     *  [3]                               - Row 2 [1]
     *  [4]                               - Row 3 [2]
     *  NewsHeadlines -> [Source] -> List<NavigationRow>
     *  [5]                               - Row 1 [0]
     *
     *  getRowForPosition(position = 2)
     *  h1 = count = 2 | sum = 2
     *  h2 = count = 3 | sum = 5
     *  h3 = count = 1 | sum = 6
     * ```
     */
    private fun getRowForPosition(position: Int): NavigationRow {
        var sum = 0
        var headlineIndex = -1

        var index = 0
        for (size in navigationRowSizes) {
            Timber.d("Getting row for position %d, index=%d, size=%d", position, index, size)
            sum += size
            if (position < sum) {
                headlineIndex = index
                Timber.d("Found row for position %d, index=%d, sum=%d", position, index, sum)
                break
            } else {
                Timber.d("Unable to find row for position %d, index=%d, sum=%d", position, index, sum)
            }
            index++
        }

        return headlines[headlineIndex].headlines[sum - position - 1]
    }
}