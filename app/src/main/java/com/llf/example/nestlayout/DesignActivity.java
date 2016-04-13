package com.llf.example.nestlayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by llf on 2016/4/13.
 *
 * @email llfer2006@gmail.com
 */
public class DesignActivity extends AppCompatActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_design);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setIcon(R.drawable.ic_launcher);

        CollapsingToolbarLayout layout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        layout.setTitle("Test NestedWebView");

        int id = getResources().getIdentifier("webview", "id", getPackageName());
        WebView webView = (WebView) findViewById(id);
        if (webView == null)
            return;
        webView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl("http://github.com");
    }
}
