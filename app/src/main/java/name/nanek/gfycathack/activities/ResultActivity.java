package name.nanek.gfycathack.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import name.nanek.gfycathack.R;
import name.nanek.gfycathack.databinding.ActivitySplashScreenBinding;

/**
 * Shows splash screen HTML.
 *
 * Created by lnanek on 1/28/17.
 */

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = ResultActivity.class.getSimpleName();

    public static final String EXTRA_GFYNAME = ResultActivity.class.getName()
            + ".EXTRA_GFYNAME";

    final String DEBUG_TEST_VALUE =
            "OnlyBoilingFinch";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String gfyname = getIntent().getStringExtra(EXTRA_GFYNAME);
        Log.d(TAG, "onCreate showing: " + gfyname);

        if ( null == gfyname ) {
            gfyname = DEBUG_TEST_VALUE;
        }

        ActivitySplashScreenBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_splash_screen);

        String template = loadAssetTextAsString(this, "gfycat3.html");
        String data = template.replaceAll("%NAME%", gfyname);

        binding.webView.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "utf-8", null);

//        binding.webView.loadUrl(
//                "file:///android_asset/gfycat3.html");

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("gfycat4.html")) {
                    finish();

                    Intent intent = new Intent(ResultActivity.this, ShareActivity.class);
                    startActivity(intent);

                    return true;
                }
                return false;
            }
        });

    }

    private String loadAssetTextAsString(Context context, String name) {
        BufferedReader in = null;
        try {
            StringBuilder buf = new StringBuilder();
            InputStream is = context.getAssets().open(name);
            in = new BufferedReader(new InputStreamReader(is));

            String str;
            boolean isFirst = true;
            while ( (str = in.readLine()) != null ) {
                if (isFirst)
                    isFirst = false;
                else
                    buf.append('\n');
                buf.append(str);
            }
            return buf.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error opening asset " + name);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing asset " + name);
                }
            }
        }

        return null;
    }
}
