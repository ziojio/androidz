package com.example.demo.web;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.example.demo.databinding.ActivityWebviewBinding;
import com.example.demo.ui.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewClientCompat;
import androidx.webkit.WebViewCompat;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import timber.log.Timber;

public class WebActivity extends BaseActivity {
    private ActivityWebviewBinding binding;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebviewBinding.inflate(getLayoutInflater());
        webView = binding.webview;
        setContentView(binding.getRoot());

        PackageInfo webViewPackageInfo = WebViewCompat.getCurrentWebViewPackage(getApplicationContext());
        if (webViewPackageInfo != null) {
            Timber.d("WebView: %s", webViewPackageInfo.packageName);
            Timber.d("WebView: %s", webViewPackageInfo.versionName);
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
        // webView.loadUrl("http://www.baidu.com");

        Observable<Long> observable = Observable.intervalRange(1, 100, 0, 100, TimeUnit.MILLISECONDS);
        Disposable disposable = observable.subscribe(aLong ->
                runOnUiThread(() -> {
                    binding.pageProgress.setProgress(Math.toIntExact(aLong));
                }));
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
        WebViewUtil.initWebView(webview);
        webview.setWebViewClient(new AppWebViewClient(WebViewUtil.createWebViewAssetLoader(this)));
        webview.setWebChromeClient(new AppWebChromeClient());
    }

    static class AppWebViewClient extends WebViewClientCompat {
        private final WebViewAssetLoader assetLoader;

        public AppWebViewClient(@NonNull WebViewAssetLoader assetLoader) {
            this.assetLoader = assetLoader;
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return assetLoader.shouldInterceptRequest(request.getUrl());
        }

        @Override
        public boolean shouldOverrideUrlLoading(@NonNull WebView view, @NonNull WebResourceRequest request) {
            if (WebViewUtil.interceptUrl(request.getUrl().toString())) {
                return true;
            } else return super.shouldOverrideUrlLoading(view, request);
        }
    }

    class AppWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            runOnUiThread(() -> binding.pageProgress.setProgress(newProgress));
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            runOnUiThread(() -> binding.titlebar.title.setText(title));
        }
    }
}
