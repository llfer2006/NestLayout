package com.llf.example.nestlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.llf.nestlayout.library.NestLayout;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl("http://hiapk.com/");

        RecyclerView listview = (RecyclerView) findViewById(R.id.listview);
        RecyclerView.Adapter adapter = new SimpleStringAdapter(this,tests);
        listview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        listview.setAdapter(adapter);
        //WebView无法输入的问题
        webView.setFocusableInTouchMode(true);

        NestLayout layout = (NestLayout) findViewById(R.id.nestlayout);
        layout.setSectionChangeListener(new NestLayout.OnSectionChangedListener() {
            @Override public void onSectionChanged(CharSequence old, CharSequence current) {
                Toast.makeText(MainActivity.this, "old:"+old+",current:"+current, Toast.LENGTH_SHORT).show();
            }
        });
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
