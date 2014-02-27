package com.jtilley.sportprofile;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends Activity {
Bitmap profilePic;
String filename;
private static final int CAMERA_REQUEST = 1888;

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

	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK){
			if(requestCode == CAMERA_REQUEST){
				profilePic = (Bitmap) data.getExtras().get("data");
				saveImage();
			}
		}
	}
	
	public void saveImage(){
		File path = Environment.getExternalStoragePublicDirectory("/SportProfile/");
		if(!path.exists()){
			path.mkdir();
		}
		
		filename = "profile" + String.valueOf(Calendar.getInstance().getTime().getTime()) + ".jpg";
		
		
		File file = new File(path, filename);
		try {
			OutputStream fos = new FileOutputStream(file);
			Bitmap bitmap = profilePic;
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		public void getCamera(){
			profilePic = null;
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, CAMERA_REQUEST);
		}
		@JavascriptInterface
		public Boolean hasImage(){
			if(profilePic != null){
				return true;
			}
			return false;
		}
		@JavascriptInterface
		public String getImage(){
			String path = "file:///sdcard/SportProfile/" + filename;
			
			return path;
		}
		
		@JavascriptInterface
		public void openProfile(String user){
			Intent intent = new Intent(mContext, ProfileActivity.class);
			intent.putExtra("user", user);
			startActivity(intent);
		}
		
		@JavascriptInterface
		public void getAccount(String account){
			//Log.i("ACCOUNT", account);
			try {
				JSONArray jsonArray = new JSONArray(account);
				JSONObject jsonObj = jsonArray.getJSONObject(0);
				SharedPreferences prefs = mContext.getSharedPreferences("user_profile", 0);
				SharedPreferences.Editor editPrefs = prefs.edit();
				String user = prefs.getString("user", null);
				if(user != null){
					String newUser = user + ", " +  jsonObj.toString();
					editPrefs.putString(jsonObj.getString("user"), newUser);
					editPrefs.commit();
				}else{
					editPrefs.putString(jsonObj.getString("user"), jsonObj.toString());
					editPrefs.commit();	
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@JavascriptInterface
		public Boolean hasUser(String user){
			SharedPreferences prefs = mContext.getSharedPreferences("user_profile", 0);
			Map<String, ?> accounts = prefs.getAll();
			for(int i=0; i<accounts.size();i++){
				if(accounts.containsKey(user)){
					return true;
				}
			}
			return false;
		}
		
		@JavascriptInterface
		public void getBoolean(Boolean check){
			Log.i("BOOLEAN", check.toString());
		}
		
		@JavascriptInterface
		public Boolean hasPassword(String user, String password){
			SharedPreferences prefs = mContext.getSharedPreferences("user_profile", 0);
			String account = prefs.getString(user, null);
			Log.i("USER", "account");
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
	
	}
}
