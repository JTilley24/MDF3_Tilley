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
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class WebActivity extends Activity {
WebView web;
EditText urlText;
Button goButton;
Button backButton;
ImageButton favButton;
Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		
		mContext = this;
		
		web= (WebView) findViewById(R.id.webView);
		urlText = (EditText) findViewById(R.id.urlInput);
		goButton = (Button) findViewById(R.id.goButton);
		backButton = (Button) findViewById(R.id.backButton);
		favButton = (ImageButton) findViewById(R.id.favButton);
		
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

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				if(view.getTitle() != null){
					Log.i("TITLE", view.getTitle());
				}
				super.onPageFinished(view, url);
			}
		});
		
		
		
		goButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				web.loadUrl(urlText.getText().toString());
			}
		});
		
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(web.canGoBack()){
					web.goBack();
				}
			}
		});
		
		favButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent favActivity = new Intent(mContext, FavActivity.class);
				favActivity.putExtra("URL_NAME", web.getUrl().toString());
				favActivity.putExtra("URL_TITLE", web.getTitle().toString());
				startActivityForResult(favActivity, 0);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultsCode, Intent data){
		if(resultsCode == RESULT_OK && requestCode == 0){
			Bundle result = data.getExtras();
			String url = result.getString("URL_OPEN");
			web.loadUrl(url);
			
		}
	}
}
