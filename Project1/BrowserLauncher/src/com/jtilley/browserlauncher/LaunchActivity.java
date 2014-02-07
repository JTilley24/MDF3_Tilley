package com.jtilley.browserlauncher;
/*
 * 	Author: 	Justin Tilley
 * 
 * 	Project:	SpiderWeb Browser
 * 
 * 	Package:	com.jtilley.browserlauncher
 * 
 * 	File: 		LaunchActivity.java
 * 	
 * 	Purpose:	The Activity is used to launch an implicit intent for a browser. By selecting one of
 * 				the given choices, it will open the browser to that URL.
*/

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LaunchActivity extends Activity {
Button googleButton;
Button fsButton;
Button wikiButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		
		googleButton = (Button) findViewById(R.id.googleButton);
		fsButton = (Button) findViewById(R.id.fsButton);
		wikiButton = (Button) findViewById(R.id.wikiButton);
		
		//Opens Google in SpiderWebBrowser
		googleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				displayBrowser("https://www.google.com/");
			}
		});
		
		//Opens Full Sail University in SpiderWebBrowser
		fsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				displayBrowser("http://www.fullsail.edu");
			}
		});
		
		//Opens Wikipedia in SpiderWebBrowser
		wikiButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				displayBrowser("https://www.wikipedia.org");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launch, menu);
		return true;
	}
	
	//Opens SpiderWebBrowser to Selected Page
	public void displayBrowser(String url){
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(intent);
	}

}