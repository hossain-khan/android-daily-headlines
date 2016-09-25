package info.hossainkhan.dailynewsheadlines;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.hossainkhan.android.core.base.BaseActivity;
import info.hossainkhan.android.core.headlines.HeadlinesContract;
import io.swagger.client.model.Article;

public class MainActivity  extends BaseActivity implements HeadlinesContract.View {
    private static final String TAG = "MainActivity";

    HealinesPresenter mHeadlinesPresenter;

    @BindView(R.id.test_textview)
    TextView demoTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Move this to super class
        ButterKnife.bind(this);

        // TODO use DI to inject
        mHeadlinesPresenter = new HealinesPresenter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart() called");

    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        Log.d(TAG, "setLoadingIndicator() called with: active = [" + active + "]");
    }

    @Override
    public void showHeadlines(final List<Article> headlines) {
        Log.d(TAG, "showHeadlines() called with: Headlines = [" + headlines.size() + "]");
        demoTextview.setText(headlines.toString());
    }

    @Override
    public void showHeadlineDetailsUi(final Article article) {
        Log.d(TAG, "showHeadlineDetailsUi() called with: article = [" + article + "]");
    }

    @Override
    public void showLoadingHeadlinesError() {
        Log.d(TAG, "showLoadingHeadlinesError() called");
    }

    @Override
    public void showNoHeadlines() {
        Log.d(TAG, "showNoHeadlines() called");
    }
}
