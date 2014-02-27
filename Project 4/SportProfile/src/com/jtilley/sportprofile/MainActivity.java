package com.jtilley.sportprofile;


import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SharedPreferences prefs = this.getSharedPreferences("user_profile", 0);
		if(prefs.getAll().size() > 0){
			Log.i("PREFS", prefs.getAll().toString());
		}
		
		WebView webView = (WebView) findViewById(R.id.webView1);
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setAllowFileAccessFromFileURLs(true);
		settings.setAllowUniversalAccessFromFileURLs(true);
		webView.addJavascriptInterface(new WebAppInterface(this), "webview");
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
		public void getString(String text){
			Log.i("TEXT", text);
		}
		
		@JavascriptInterface
		public void getAccount(String account){
			//Log.i("ACCOUNT", account);
			try {
				JSONArray jsonArray = new JSONArray(account);
				JSONObject jsonObj = jsonArray.getJSONObject(0);
				//Log.i("ACCOUNT_NAME", jsonObj.getString("firstname"));
				SharedPreferences prefs = mContext.getSharedPreferences("user_profile", 0);
				SharedPreferences.Editor editPrefs = prefs.edit();
				editPrefs.putString(jsonObj.getString("email"), jsonObj.toString());
				editPrefs.commit();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public Boolean hasEmail(String email){
			SharedPreferences prefs = mContext.getSharedPreferences("user_profile", 0);
			Map<String, ?> accounts = prefs.getAll();
			for(int i=0; i<accounts.size();i++){
				if(accounts.containsKey(email)){
					return true;
				}
			}
			return false;
		}
		
		public void getBoolean(Boolean check){
			Log.i("BOOLEAN", check.toString());
		}
		
		public Boolean hasPassword(String email, String password){
			SharedPreferences prefs = mContext.getSharedPreferences("user_profile", 0);
			String account = prefs.getString(email, null);
			Log.i("EMAIL", "account");
			if(account != null){
				try {
					JSONObject jsonObject = new JSONObject(account);
					if(jsonObject.getString("password").equalsIgnoreCase(password)){
						Log.i("PASSWORD", jsonObject.getString("password"));
						return true;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			return false;
		}
		
		public void getLogin(String email, String password){
			SharedPreferences prefs = mContext.getSharedPreferences("user_profile", 0);
			Map<String, ?> accounts = prefs.getAll();
			for(int i=0; i<accounts.size();i++){
				if(accounts.containsKey(email)){
					Log.i("ACCOUNT", accounts.get(email).toString());
				}
			}
			//Log.i("LOGIN", email + " " + password);
		}
	}
	
	
}
