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

package info.hossainkhan.android.core.dagger.modules;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.swagger.client.ApiClient;
import io.swagger.client.api.StoriesApi;

@Module
public class NetworkModule {
    public static final String API_KEY_NYTIMES = "4d94e696bef94de0b5fa2b14bab6b7e2";
    public static final String API_KEY_KEYWORD = "apikey";

    @Singleton
    @Provides
    public ApiClient provideApiClient() {
        return new ApiClient(API_KEY_KEYWORD, API_KEY_NYTIMES);
    }

    @Provides
    public StoriesApi provideStoriesApi(ApiClient apiClient) {
        return apiClient.createService(StoriesApi.class);
    }
}
