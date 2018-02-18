package edu.ksu.cs.benign;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {

    @Override
    public void onPageFinished(WebView wv, String url) {
        wv.loadUrl("javascript:uploadFile()");
    }
}