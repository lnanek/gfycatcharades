package name.nanek.gfycathack.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import name.nanek.gfycathack.R;
import name.nanek.gfycathack.databinding.ActivitySplashScreenBinding;

/**
 * Shows splash screen HTML.
 *
 * Created by lnanek on 1/28/17.
 */

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySplashScreenBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_splash_screen);

        binding.webView.loadUrl(
                "file:///android_asset/gfycat4.html");

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("gfycat2.html")) {
                    finish();
                    
                    return true;
                }
                if (url.contains("gfycat3.html")) {
                    finish();

                    Intent intent = new Intent(ShareActivity.this, ShowGfycatActivity.class);
                    startActivity(intent);

                    return true;
                }
                return false;
            }
        });

    }
}
