package com.llf.example.nestlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        int color = getResources().getColor(R.color.window_ground_ashen);
//        getWindow().setBackgroundDrawable(new ColorDrawable(color));

        WebView webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl("http://github.com");

        RecyclerView listview = (RecyclerView) findViewById(R.id.listview);
        RecyclerView.Adapter adapter = new SimpleStringAdapter(this,tests);
        listview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        listview.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        listview.setAdapter(adapter);
    }

    String[] tests = new String[]{
            "aaaaaaaaaaaaa",
            "bbbbbbbbbbbbb",
            "ccccccccccccc",
            "ddddddddddddd",
            "eeeeeeeeeeeee",
            "fffffffffffff",
            "ggggggggggggg",
            "hhhhhhhhhhhhh",
            "jjjjjjjjjjjjj",
            "kkkkkkkkkkkkk",
            "lllllllllllll",
            "mmmmmmmmmmmmm",
            "nnnnnnnnnnnnnn",
            "qqqqqqqqqqqqq",
            "aaaaaaaaaaaaa",
            "bbbbbbbbbbbbb",
            "ccccccccccccc",
            "ddddddddddddd",
            "eeeeeeeeeeeee",
            "fffffffffffff",
            "ggggggggggggg",
            "hhhhhhhhhhhhh",
            "jjjjjjjjjjjjj",
            "kkkkkkkkkkkkk",
            "lllllllllllll",
            "mmmmmmmmmmmmm",
            "nnnnnnnnnnnnnn",
            "qqqqqqqqqqqqq"
    };

}
