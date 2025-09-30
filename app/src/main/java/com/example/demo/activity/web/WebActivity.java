package com.example.demo.activity.web;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewClientCompat;
import androidx.webkit.WebViewCompat;

import com.example.demo.activity.BaseActivity;
import com.example.demo.databinding.ActivityWebviewBinding;
import com.example.demo.util.Timber;


public class WebActivity extends BaseActivity {
    private ActivityWebviewBinding binding;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        webView = binding.webview;

        PackageInfo webViewPackageInfo = WebViewCompat.getCurrentWebViewPackage(getApplicationContext());
        if (webViewPackageInfo != null) {
            Timber.d("WebView: " + webViewPackageInfo.packageName);
            Timber.d("WebView: " + webViewPackageInfo.versionName);
        }

        initWebView(webView);

        String url = getIntent().getStringExtra("url");
        if (url == null) {
            url = getIntent().getDataString();
        }
        if (url != null) {
            webView.loadUrl(url);
        } else {
            webView.loadUrl("file:///android_asset/test.html");
        }
        webView.loadUrl("https://www.baidu.com");
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else
            super.onBackPressed();
    }

    private void initWebView(WebView webview) {
        WebUtil.initWebView(webview);
        webview.setWebViewClient(new AppWebViewClient());
        webview.setWebChromeClient(new AppWebChromeClient());
    }

    class AppWebViewClient extends WebViewClientCompat {
        private final WebViewAssetLoader assetLoader;

        public AppWebViewClient() {
            this.assetLoader = WebUtil.createWebViewAssetLoader(getApplicationContext());
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return assetLoader.shouldInterceptRequest(request.getUrl());
        }

        @Override
        public boolean shouldOverrideUrlLoading(@NonNull WebView view, @NonNull WebResourceRequest request) {
            if (WebUtil.interceptUrl(request.getUrl().toString())) {
                return true;
            } else return super.shouldOverrideUrlLoading(view, request);
        }
    }

    class AppWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Timber.d("onProgressChanged: " + newProgress);
            runOnUiThread(() -> binding.pageProgress.setProgress(newProgress));
            if (newProgress == 100) {
                runOnUiThread(() -> binding.pageProgress.setVisibility(View.GONE));
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            runOnUiThread(() -> binding.toolbar.setTitle(title));
        }
    }
}
