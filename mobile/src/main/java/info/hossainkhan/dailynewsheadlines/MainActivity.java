package info.hossainkhan.dailynewsheadlines;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.hossainkhan.android.core.base.BaseActivity;
import info.hossainkhan.android.core.headlines.HeadlinesContract;
import info.hossainkhan.android.core.headlines.HeadlinesPresenter;
import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.android.core.model.NavigationRow;
import info.hossainkhan.android.core.model.ScreenType;
import info.hossainkhan.android.core.newsprovider.NewsProviderManager;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements HeadlinesContract.View {

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
        Context context = getApplicationContext();
        NewsProviderManager newsProviderManager = new NewsProviderManager(context);
        mHeadlinesPresenter = new HeadlinesPresenter(context, this, newsProviderManager);
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
    public void showAddNewsSourceScreen() {
        Timber.d("showAddNewsSourceScreen() called");
    }

    @Override
    public void showUiScreen(final ScreenType type) {
        Timber.d("showUiScreen() called with: type = [" + type + "]");
    }

    @Override
    public void showAppSettingsScreen() {
        Timber.d("showAppSettingsScreen() called");
    }

    @Override
    public void showHeadlineBackdropBackground(final String imageUrl) {
        Timber.d("showHeadlineBackdropBackground() called with: imageUrl = [" + imageUrl + "]");
    }

    @Override
    public void showDefaultBackground() {
        Timber.d("showDefaultBackground() called");
    }
}
