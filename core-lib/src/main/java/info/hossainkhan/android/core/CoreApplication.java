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

import info.hossainkhan.android.core.dagger.components.AppComponent;
import info.hossainkhan.android.core.dagger.components.DaggerAppComponent;
import info.hossainkhan.android.core.dagger.modules.InteractorsModule;
import info.hossainkhan.android.core.dagger.modules.NetworkModule;
import timber.log.Timber;

/**
 * Extended {@link Application} that is shared among all the android application modules.
 */
public class CoreApplication extends Application {

    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initAppComponent();
        initLogger();
    }

    private void initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void initAppComponent() {
        sAppComponent = DaggerAppComponent.builder()
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
}
