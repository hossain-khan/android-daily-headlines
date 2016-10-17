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

package info.hossainkhan.dailynewsheadlines.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.app.BackgroundManager;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

import timber.log.Timber;

/**
 * Target to save as instance to avoid issue described in following SO: <br/>
 * http://stackoverflow.com/questions/24180805/onbitmaploaded-of-target-object-not-called-on-first-load
 */
public class PicassoImageTarget implements Target {

    private final WeakReference<BackgroundManager> mBackgroundManagerRef;

    public PicassoImageTarget(BackgroundManager backgroundManager) {
        mBackgroundManagerRef = new WeakReference<>(backgroundManager);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        Timber.d("onBitmapLoaded: %dx%d - %d bytes", bitmap.getHeight(), bitmap.getWidth(), bitmap.getByteCount());
        BackgroundManager backgroundManager = mBackgroundManagerRef.get();
        if (backgroundManager != null) {
            backgroundManager.setBitmap(bitmap);
        } else {
            Timber.w("Background manager is unavailable.");
        }
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        Timber.w("onBitmapFailed");

        BackgroundManager backgroundManager = mBackgroundManagerRef.get();
        if (backgroundManager != null) {
            backgroundManager.setDrawable(errorDrawable);
        } else {
            Timber.w("Background manager is unavailable.");
        }
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        Timber.d("onPrepareLoad");
    }
}
