package com.jtilley.spiderwebbrowser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;

public class FavActivity extends Activity {
Button addButton;
String urlString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fav);
		
		Intent intent = this.getIntent();
		urlString = intent.getStringExtra("URL_NAME");
		if(urlString != null){
			Log.i("URL", urlString);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fav, menu);
		return true;
	}

}
