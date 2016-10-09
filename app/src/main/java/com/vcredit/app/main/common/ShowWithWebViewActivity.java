package com.vcredit.app.main.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import butterknife.BindView;
import com.vcredit.app.R;
import com.vcredit.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import com.vcredit.view.TitleBar;

/**
 * 整个的页面WebView。\n
 * 通过{@value KEY_STRING_CONTENT}(只支持静态html页面)和{@code KEY_URL}设置要显示的web页面。
 * 注意：如果同时设置了{@value KEY_STRING_CONTENT}和{@code KEY_URL}优先使用{@code KEY_URL}，
 * Created by shibenli on 2016/3/31.
 */
public class ShowWithWebViewActivity extends BaseActivity {
    /**
     * 传递html填充的字符串
     */
    public final static String KEY_STRING_CONTENT = "string_content";

    /**
     * 传递url
     */
    public final static String KEY_URL = "string_url";


    /**
     * 传递url
     */
    public final static String KEY_TITLE = "string_title";

    /**
     * 是否显示同意按钮，默认不显示
     */
    public final static String KEY_BTN_SURE = "string_btn_sure";

    WebView webView;

    @BindView(R.id.layout_btn)
    View btnLayout;

    @BindView(R.id.titlebar)
    TitleBar titleBuilder;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_with_webview_activity);
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
        window.setGravity(Gravity.CENTER);
        window.setAttributes(params);
        webView = new WebView(getApplicationContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(layoutParams);
        LinearLayout mll = ButterKnife.findById(this, R.id.layout_webview);
        mll.addView(webView);

        ButterKnife.bind(this);
        initWebView();
        initTitleBar();
    }

    private void initWebView() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(KEY_URL)) {
                String url = intent.getStringExtra(KEY_URL);
                if (!TextUtils.isEmpty(url)) {
                    webView.loadUrl(url);
                }
            } else if (intent.hasExtra(KEY_STRING_CONTENT)) {
                String html = intent.getStringExtra(KEY_STRING_CONTENT);
                if (!TextUtils.isEmpty(html)) {
                    webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

                }
            }

            boolean hasSure = intent.getBooleanExtra(KEY_BTN_SURE, false);
            btnLayout.setVisibility(hasSure ? View.VISIBLE : View.GONE);

            title = intent.getStringExtra(KEY_TITLE);
            if(TextUtils.isEmpty(title)){
                title = getString(R.string.app_name);
            }

            webView.requestFocusFromTouch();
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClient());
        }
    }

    private void initTitleBar() {
        titleBuilder.withBackIcon().setMiddleTitleText(title).withHomeIcon().isBackgroundTransparent();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    public void onBackPressed(View view) {
        setResult(RESULT_CANCELED);
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.removeAllViews();
            webView.clearCache(true);
            webView.clearHistory();
            webView.destroy();
            webView = null;
        }
    }

    @OnClick(R.id.btn_prototype_sure)
    public void onBtnClick(View view){
        setResult(RESULT_OK);
        onBackPressed();
    }
}
