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

package info.hossainkhan.android.core.usersource;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import info.hossainkhan.android.core.base.BasePresenter;
import timber.log.Timber;


/**
 * Presenter for the user news source.
 */
public class UserSourcePresenter
        extends BasePresenter<UserSourceContract.View>
        implements UserSourceContract.Presenter {

    private final Set<String> mRemovedSourcesQueue = new HashSet<>();
    private final UserSourceProvider mUserSourceProvider;

    public UserSourcePresenter(final UserSourceContract.View view, final UserSourceProvider userSourceProvider) {
        attachView(view);
        mUserSourceProvider = userSourceProvider;
    }

    @Override
    public void onSourceSelected(final String url, final boolean isRemove) {
        if (isRemove) {
            Timber.d("Adding URL for removal: %s", url);
            mRemovedSourcesQueue.add(url);
        } else {
            Timber.d("Removing URL from removal queue: %s", url);
            mRemovedSourcesQueue.remove(url);
        }

        getView().toggleRemoveAction(!mRemovedSourcesQueue.isEmpty());
    }

    @Override
    public void onRemoveConfirm() {
        Timber.d("Going to remove all URLs from queue: %s", mRemovedSourcesQueue);
    }

    @Override
    public void onCancelRemoval() {
        getView().closeScreen();
    }

    @Override
    public Map<String, String> getUserNewsSources() {
        return mUserSourceProvider.getSources();
    }
}
