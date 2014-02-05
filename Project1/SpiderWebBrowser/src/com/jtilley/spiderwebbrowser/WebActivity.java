package com.jtilley.spiderwebbrowser;
/*
 * 	Author: 	Justin Tilley
 * 
 * 	Project:	SpiderWeb Browser
 * 
 * 	Package:	com.jtilley.spiderwebbrowser
 * 
 * 	File: 		WebActivity.java
 * 	
 * 	Purpose:	The Activity is launched by an implicit intent using Action.View intent filter.
 * 				It contains a WebView and tool bar that allows the user to navigate the Internet
 * 				and display another activity for bookmarks.
*/

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class WebActivity extends Activity {
WebView web;
EditText urlText;
Button goButton;
Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		
		web= (WebView) findViewById(R.id.webView);
		urlText = (EditText) findViewById(R.id.urlInput);
		goButton = (Button) findViewById(R.id.goButton);
		backButton = (Button) findViewById(R.id.backButton);
		
		Intent intent = getIntent();
		Uri data = intent.getData();
		
		Log.i("DATA", data.toString());
		
		web.getSettings().setJavaScriptEnabled(true);
		web.loadUrl(data.toString());
		
		web.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				
				return false;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				urlText.setText(url.toString());
				Log.i("URL", url.toString());
				
				super.onPageStarted(view, url, favicon);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web, menu);
		return true;
	}

}
