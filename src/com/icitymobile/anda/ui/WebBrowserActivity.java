package com.icitymobile.anda.ui;

import com.hualong.framework.kit.StringKit;
import com.icitymobile.anda.R;
import com.icitymobile.anda.util.Const;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;



/**
 * webview
 * @author Sherry
 *
 */
public class WebBrowserActivity extends BaseActivity {

	public static final String TAG = "WebBrowserActivity";
	
	private WebView mWebView;
	private ProgressBar mProgressBar;
	private String goUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webbrowser_activity);
		String title = getIntent().getStringExtra(Const.EXTRA_WEBVIEW_TITLE);
		if (StringKit.isNotEmpty(title)) {
			setTitle(title);
		}
		
		initViews();
		
		goUrl = getIntent().getStringExtra(Const.EXTRA_WEBVIEW_URL);
		if(StringKit.isNotEmpty(goUrl)) {
			mWebView.loadUrl(goUrl);
		}
	}

	private void initViews() {
		mProgressBar = (ProgressBar) findViewById(R.id.webbrowser_progress);
		mWebView = (WebView) findViewById(R.id.webbrowser_webview);
		mWebView.setWebViewClient(mWebViewClient);
		mWebView.setWebChromeClient(mWebChromeClient);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setSupportZoom(true);
		webSettings.setJavaScriptEnabled(true);

		webSettings.setUseWideViewPort(true); 
		webSettings.setLoadWithOverviewMode(true); 
	}

	@Override
	public void onBackPressed() {
		if(mWebView.canGoBack()) {
			mWebView.goBack();
			mWebView.reload();
		} else {
			super.onBackPressed();
		}
	}
	WebViewClient mWebViewClient = new WebViewClient() {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			mProgressBar.setVisibility(View.VISIBLE);
			goUrl = url;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			mProgressBar.setVisibility(View.GONE);
			goUrl = url;
		}
	};

	WebChromeClient mWebChromeClient = new WebChromeClient() {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			mProgressBar.setProgress(newProgress);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
		}
	};
}