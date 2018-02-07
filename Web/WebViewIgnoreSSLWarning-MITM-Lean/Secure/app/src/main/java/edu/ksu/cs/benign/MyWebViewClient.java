package edu.ksu.cs.benign;

import android.net.http.SslError;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {

    private static final String TAG = "WebViewSSLError";

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                   SslError error) {
        Log.d(TAG, "SSL error detected");
        handler.cancel();

        view.loadData("SSL error detected", "text/css", "UTF-8");
    }
}