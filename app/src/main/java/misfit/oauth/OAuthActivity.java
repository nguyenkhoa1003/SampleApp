package misfit.oauth;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OAuthActivity extends AppCompatActivity {

    private Button mBtnNext;
    private TextView mTvResult;

    private final String TAG = OAuthActivity.class.getSimpleName();
    private final String INTENT_LAUNCH = "android.intent.category.LAUNCHER";

    private final String ACCESS_TOKEN = "access_token";
    private final String ERROR = "error";
    private final String PATH_TOKEN = "://authorize#access_token=";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oauth_activity);

        mTvResult = (TextView) findViewById(R.id.tv_result);
        mBtnNext = (Button) findViewById(R.id.btn_start_oauth);

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOAuthMisfit();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getDataString() != null) {
            Uri data = intent.getData();
            Log.i(TAG, "data : " + data + R.string.SCHEME_CALLBACK);

            String result = "";
            if (data.getScheme().startsWith("mfc")) {
                result = data.toString().replace(data.getScheme() + PATH_TOKEN, "");
                mTvResult.setText(ACCESS_TOKEN + " : " + result);
            } else {
                result = data.getQueryParameter(ERROR);
                mTvResult.setText(ERROR + " : " + result);
            }
        }
    }

    private void startOAuthMisfit() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String data = BuildConfig.PREFIX_URI + BuildConfig.APP_KEY + BuildConfig.SUFFIX_URI + BuildConfig.APP_SECRET;
            intent.setData(Uri.parse(data));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        } catch (ActivityNotFoundException ex) {
            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.MISFIT_APP_ID));
            startActivity(i);
        }
    }
}
