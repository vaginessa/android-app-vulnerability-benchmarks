package edu.ksu.cs.benign;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WebView webView = (WebView) findViewById(R.id.webview1);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        /*
        The url throws an SSL error
         */
        String url = "https://" + getResources().getString(R.string.local_server_ipv4) + ":" +
                getResources().getString(R.string.local_server_port) + getResources().getString(R.string.url_extension);
        webView.loadUrl(url);
        webView.setWebViewClient(new MyWebViewClient());
    }
}