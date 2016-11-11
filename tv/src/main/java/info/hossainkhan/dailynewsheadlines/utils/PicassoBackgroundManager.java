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

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v17.leanback.app.BackgroundManager;
import android.util.DisplayMetrics;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.Timer;
import java.util.TimerTask;

import info.hossainkhan.android.core.picasso.BlurTransformation;
import info.hossainkhan.android.core.picasso.GrayscaleTransformation;
import info.hossainkhan.android.core.util.StringUtils;
import info.hossainkhan.dailynewsheadlines.R;
import timber.log.Timber;

/**
 * Modified source taken from https://github.com/corochann/AndroidTVappTutorial
 */
public class PicassoBackgroundManager {

    private static int BACKGROUND_UPDATE_DELAY = 500;
    private final int DEFAULT_BACKGROUND_RES_ID = R.drawable.default_background;
    private static Drawable mDefaultBackground;
    // Handler attached with main thread
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private Activity mActivity;
    private BackgroundManager mBackgroundManager = null;
    private DisplayMetrics mMetrics;
    private String mBackgroundUrl;
    private PicassoImageTarget mBackgroundTarget;

    private Timer mBackgroundTimer; // null when no UpdateBackgroundTask is running.
    private TransformType mTransformType = null;

    public enum TransformType {
        GREYSCALE,
        BLUR
    }

    public PicassoBackgroundManager(Activity activity) {
        mActivity = activity;
        mDefaultBackground = activity.getDrawable(DEFAULT_BACKGROUND_RES_ID);
        mBackgroundManager = BackgroundManager.getInstance(activity);
        mBackgroundManager.attach(activity.getWindow());
        mBackgroundTarget = new PicassoImageTarget(mBackgroundManager);
        mMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);

    }

    /**
     * if UpdateBackgroundTask is already running, cancel this task and start new task.
     */
    private void startBackgroundTimer() {
        if (mBackgroundTimer != null) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        /* set delay time to reduce too much background image loading process */
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    /**
     * Should be called to stop and cleanup resources
     */
    public void destroy() {
        Timber.d("Stopping background manager.");
        if (mBackgroundTimer != null) {
            mBackgroundTimer.cancel();
        }
        mBackgroundManager.release();
        mBackgroundManager = null;
        mBackgroundTarget = null;
        mActivity = null;
    }


    private class UpdateBackgroundTask extends TimerTask {
        @Override
        public void run() {
            /* Here is TimerTask thread, may not be UI thread */
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                     /* Here is main (UI) thread */
                    if (mBackgroundUrl != null && StringUtils.isNotEmpty(mBackgroundUrl)) {
                        updateBackground(mBackgroundUrl);
                    } else {
                        updateBackground();
                    }
                }
            });
        }
    }

    /**
     * update background to default image
     */
    public void updateBackgroundWithDelay() {
        mBackgroundUrl = null;
        startBackgroundTimer();
    }


    /**
     * updateBackground with delay
     * delay time is measured in other Timer task thread.
     * @param imageUrl Image URI to load
     */
    public void updateBackgroundWithDelay(String imageUrl) {
        updateBackgroundWithDelay(imageUrl, null);
    }

    public void updateBackgroundWithDelay(String imageUrl, TransformType transformType) {
        mBackgroundUrl = imageUrl;
        mTransformType = transformType;
        startBackgroundTimer();
    }

    private void updateBackground(String imageUrl) {
        try {
            Picasso picasso = Picasso.with(mActivity);
            RequestCreator requestCreator = picasso
                    .load(imageUrl)
                    .resize(mMetrics.widthPixels, mMetrics.heightPixels)
                    .centerCrop()
                    .error(mDefaultBackground);

            // Check if any transformation needs to be applied.
            if(TransformType.GREYSCALE == mTransformType) {
                requestCreator.transform(new GrayscaleTransformation(picasso));
            }

            if(TransformType.BLUR == mTransformType) {
                requestCreator.transform(new BlurTransformation(mActivity));
            }

            requestCreator
                    .into(mBackgroundTarget);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    /**
     * update background to default image
     */
    private void updateBackground() {
        try {
            Picasso.with(mActivity)
                    .load(DEFAULT_BACKGROUND_RES_ID)
                    .resize(mMetrics.widthPixels, mMetrics.heightPixels)
                    .centerCrop()
                    .error(mDefaultBackground)
                    .into(mBackgroundTarget);
        } catch (Exception e) {
            Timber.e(e);
        }
    }
}