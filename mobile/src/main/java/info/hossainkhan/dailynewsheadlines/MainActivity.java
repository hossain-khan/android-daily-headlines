package info.hossainkhan.dailynewsheadlines;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.hossainkhan.android.core.base.BaseActivity;
import info.hossainkhan.android.core.headlines.HeadlinesContract;
import info.hossainkhan.android.core.headlines.HeadlinesPresenter;
import info.hossainkhan.android.core.model.CardItem;
import info.hossainkhan.android.core.model.CategoryNameResolver;
import info.hossainkhan.android.core.model.NavigationRow;
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
        mHeadlinesPresenter = new HeadlinesPresenter(this, CategoryNameResolver.getSupportedCategories());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.d("onStart() called");
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        Timber.d("setLoadingIndicator() called with: active = [" + active + "]");
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
    public void showLoadingHeadlinesError() {
        Timber.d("showLoadingHeadlinesError() called");
        Toast.makeText(this, "Unable to load headlines", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoHeadlines() {
        Timber.d("showNoHeadlines() called");
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
