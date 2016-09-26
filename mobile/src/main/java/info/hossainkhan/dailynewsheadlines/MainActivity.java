package info.hossainkhan.dailynewsheadlines;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.hossainkhan.android.core.base.BaseActivity;
import info.hossainkhan.android.core.headlines.HeadlinesContract;
import info.hossainkhan.android.core.headlines.HeadlinesPresenter;
import io.swagger.client.model.Article;
import timber.log.Timber;

public class MainActivity  extends BaseActivity implements HeadlinesContract.View {
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
        mHeadlinesPresenter = new HeadlinesPresenter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Timber.d(TAG, "onStart() called");

    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        Timber.d(TAG, "setLoadingIndicator() called with: active = [" + active + "]");
    }

    @Override
    public void showHeadlines(final List<Article> headlines) {
        Timber.d(TAG, "showHeadlines() called with: Headlines = [" + headlines.size() + "]");
        demoTextview.setText(headlines.toString());
    }

    @Override
    public void showHeadlineDetailsUi(final Article article) {
        Timber.d(TAG, "showHeadlineDetailsUi() called with: article = [" + article + "]");
    }

    @Override
    public void showLoadingHeadlinesError() {
        Timber.d(TAG, "showLoadingHeadlinesError() called");
    }

    @Override
    public void showNoHeadlines() {
        Timber.d(TAG, "showNoHeadlines() called");
    }
}
