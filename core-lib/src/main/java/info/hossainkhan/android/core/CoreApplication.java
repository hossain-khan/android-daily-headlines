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

package info.hossainkhan.android.core;

import android.app.Application;
import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.leakcanary.LeakCanary;

import info.hossainkhan.android.core.analytics.AnalyticsReporter;
import info.hossainkhan.android.core.dagger.components.AppComponent;
import info.hossainkhan.android.core.dagger.components.DaggerAppComponent;
import info.hossainkhan.android.core.dagger.modules.AppModule;
import info.hossainkhan.android.core.dagger.modules.InteractorsModule;
import info.hossainkhan.android.core.dagger.modules.NetworkModule;
import info.hossainkhan.android.core.logging.FirebaseCrashLogTree;
import timber.log.Timber;

/**
 * Extended {@link Application} that is shared among all the android application modules.
 */
public class CoreApplication extends Application {
    private static final String TAG = "CoreApplication";

    private static AppComponent sAppComponent;
    private static final boolean ENABLE_LOGGING = true; // BuildConfig.DEBUG for library project not working.
    private static CoreApplication sContext;
    private static AnalyticsReporter sAnalyticsReporter;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = this;
        sAnalyticsReporter = new AnalyticsReporter(FirebaseAnalytics.getInstance(sContext));

        initLeakCanary();
        initAppComponent();
        initLogger();
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    /**
     * Initializes application wide logging.
     * <p>
     * Quote from Timber "There are no Tree implementations installed by default because every time you log in
     * production, a puppy dies."
     *
     * So, need to be responsible about it ^_^
     */
    private void initLogger() {
        if (ENABLE_LOGGING) {
            android.util.Log.i(TAG, "Planting tree for timber debug logger.");
            Timber.plant(new Timber.DebugTree(), new FirebaseCrashLogTree());
        } else {
            android.util.Log.i(TAG, "Planting tree for production logger.");
            Timber.plant(new FirebaseCrashLogTree());
        }
    }

    private void initAppComponent() {
        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .interactorsModule(new InteractorsModule())
                .networkModule(new NetworkModule())
                .build();

    }

    /**
     * Provides the app component.
     *
     * @return {@link AppComponent}
     */
    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

    /**
     * @param context Any context.
     * @return {@link CoreApplication} instance.
     */
    public static CoreApplication getCoreApplication(final Context context) {
        return (CoreApplication) context.getApplicationContext();
    }

    /**
     * Provides application analytics reporter.
     * @return analytics reporter instance.
     */
    public static AnalyticsReporter getAnalyticsReporter() {
        return sAnalyticsReporter;
    }
}
