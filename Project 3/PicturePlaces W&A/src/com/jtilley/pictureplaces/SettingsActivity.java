package com.jtilley.pictureplaces;
/*
 * 	Author: 	Justin Tilley
 * 
 * 	Project:	PicturePlaces Widget and ActionBar
 * 
 * 	Package:	com.jtilley.pictureplaces
 * 
 * 	File: 		SettingsActivity.java
 * 	
 * 	Purpose:	This activity allows the User to select what camera to use and open GPS settings. An Intent
 * 				is launched to display the device's location settings.
*/
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;

public class SettingsActivity extends Activity {
SharedPreferences prefs;
SharedPreferences.Editor editPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		prefs = getSharedPreferences("user_prefs", 0);
		editPrefs = prefs.edit();
		
		Button gpsButton = (Button) findViewById(R.id.gpsButton);
		gpsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent gps = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(gps);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	//Check For RadioButtons
	public void onRadioButtonClicked(View view){
		boolean checked = ((RadioButton) view).isChecked();
		
		if(view.getId() == R.id.radioButton1){
			if(checked){
				Log.i("BUTTON", "FRONT");
				editPrefs.putString("CAMERA", "front");
				editPrefs.commit();
			}
		}else if(view.getId() == R.id.radioButton2){
			if(checked){
				Log.i("BUTTON", "BACK");
				editPrefs.putString("CAMERA", "back");
				editPrefs.commit();
			}
		}
		
	}
}
