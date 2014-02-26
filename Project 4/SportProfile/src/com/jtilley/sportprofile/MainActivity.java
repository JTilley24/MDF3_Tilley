package com.jtilley.sportprofile;


import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		WebView webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new WebAppInterface(this), "native");
		webView.loadUrl("file:///android_asset/www/index.html");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public class WebAppInterface{
		Context mContext;
		
		WebAppInterface(Context context){
			mContext = context;
		}
		
		@JavascriptInterface
		public String getString(String text){
			String newText = text + "NEW";
			return newText;
		}
	}
	
	
}
