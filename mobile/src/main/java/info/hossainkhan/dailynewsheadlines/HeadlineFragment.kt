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

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_news_card.view.*


/**
 * Fragment to render a headline item.
 */
class HeadlineFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.item_news_card, container, false)
        val title = arguments.getString(ARG_TITLE)
        val imageUrl = arguments.getString(ARG_IMAGE_URL)
        rootView.headline_title.text = title

        if(!imageUrl.isNullOrEmpty()) {
            Picasso.with(activity).load(imageUrl).into(rootView.headline_background)
        }

        return rootView
    }

    companion object {
        private val ARG_TITLE = "headline_title"
        private val ARG_IMAGE_URL = "headline_image_url"

        /**
         * Returns a new instance of this fragment for the given required info.
         */
        fun newInstance(title: String?, imageUrl: String?): HeadlineFragment {
            val fragment = HeadlineFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putString(ARG_IMAGE_URL, imageUrl)
            fragment.arguments = args
            return fragment
        }
    }
}