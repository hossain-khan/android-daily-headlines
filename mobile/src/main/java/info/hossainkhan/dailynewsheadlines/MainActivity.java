package info.hossainkhan.dailynewsheadlines;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.hossainkhan.android.core.base.BaseActivity;
import info.hossainkhan.android.core.headlines.HeadlinesContract;
import info.hossainkhan.android.core.headlines.HeadlinesPresenter;
import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.android.core.model.NavigationRow;
import info.hossainkhan.android.core.model.NewsProvider;
import info.hossainkhan.android.core.newsprovider.AndroidPoliceFeedNewsProvider;
import info.hossainkhan.android.core.newsprovider.Nine2FiveFeedNewsProvider;
import info.hossainkhan.android.core.newsprovider.NyTimesNewsProvider;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements HeadlinesContract.View {
    private static final String TAG = "MainActivity";

    HeadlinesPresenter mHeadlinesPresenter;

    @BindView(R.id.test_textview)
    TextView demoTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Move this to super class
        ButterKnife.bind(this);

        // TODO use DI to inject
        List<NewsProvider> providers = new ArrayList<>(2);
        providers.add(new NyTimesNewsProvider());
        Context context = getApplicationContext();
        providers.add(new AndroidPoliceFeedNewsProvider(context));
        providers.add(new Nine2FiveFeedNewsProvider(context));
        mHeadlinesPresenter = new HeadlinesPresenter(context, this, providers);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.d("onStart() called");
    }

    @Override
    protected void onStop() {
        mHeadlinesPresenter.detachView();
        super.onStop();
    }

    @Override
    public void toggleLoadingIndicator(final boolean active) {
        Timber.d("toggleLoadingIndicator() called with: active = [" + active + "]");
    }

    @Override
    public void showHeadlines(final List<NavigationRow> headlines) {
        Timber.d("showHeadlines() called with: Headlines = [" + headlines.size() + "]");
        demoTextview.setText("Total headline categories: " + headlines.size());
    }

    @Override
    public void showHeadlineDetailsUi(final CardItem cardItem) {
        Timber.d("showHeadlineDetailsUi() called with: cardItem = [" + cardItem + "]");
    }

    @Override
    public void showDataLoadingError() {
        Timber.d("showDataLoadingError() called");
        Toast.makeText(this, "Unable to load headlines", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDataNotAvailable() {
        Timber.d("showDataNotAvailable() called");
    }

    @Override
    public void showAppAboutScreen() {
        Timber.d("showAppAboutScreen() called");
    }

    @Override
    public void showAppContributionScreen() {
        Timber.d("showAppContributionScreen() called");
    }

    @Override
    public void showAppSettingsScreen() {
        Timber.d("showAppSettingsScreen() called");
    }

    @Override
    public void showHeadlineBackdropBackground(final URI imageURI) {
        Timber.d("showHeadlineBackdropBackground() called with: imageURI = [" + imageURI + "]");
    }
}
