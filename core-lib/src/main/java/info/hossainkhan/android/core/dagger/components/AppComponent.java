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

package info.hossainkhan.android.core.dagger.components;

import android.app.Activity;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import info.hossainkhan.android.core.CoreApplication;
import info.hossainkhan.android.core.dagger.modules.AppModule;
import info.hossainkhan.android.core.dagger.modules.InteractorsModule;
import info.hossainkhan.android.core.dagger.modules.NetworkModule;
import info.hossainkhan.android.core.data.ExampleInteractor;
import io.swagger.client.ApiClient;

@Singleton
@Component(
        modules = {
                AppModule.class,
                InteractorsModule.class,
                NetworkModule.class
        }
)
public interface AppComponent {
    void inject(CoreApplication app);

    void inject(Activity activity);

    ExampleInteractor providesExampleInteractorImpl();

    ApiClient getApiClient();

    Context getContext();
}