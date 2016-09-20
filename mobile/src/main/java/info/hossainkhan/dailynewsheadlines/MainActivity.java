package info.hossainkhan.dailynewsheadlines;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import info.hossainkhan.android.core.CoreConfig;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: " + CoreConfig.NAME);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Test firebase crash
        //FirebaseCrash.report(new Exception("My first Android non-fatal error"));
    }
}
