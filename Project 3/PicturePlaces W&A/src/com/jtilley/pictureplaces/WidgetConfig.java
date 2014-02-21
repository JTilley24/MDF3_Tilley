package com.jtilley.pictureplaces;
/*
 * 	Author: 	Justin Tilley
 * 
 * 	Project:	PicturePlaces Widget and ActionBar
 * 
 * 	Package:	com.jtilley.pictureplaces
 * 
 * 	File: 		WidgetConfig.java
 * 	
 * 	Purpose:	This activity is the configuration options for the Widget. The User can select what image is
 * 				displayed in the Widget.
*/
import java.io.File;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class WidgetConfig extends Activity implements OnClickListener {
TextView title;
Button doneButton;
RadioButton imageButton;
RadioButton locButton;
String lastLoc;
String lastpic;
ArrayList<String> imagePaths;
SharedPreferences prefs;
SharedPreferences.Editor editPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_widget_config);
		
		//Get UI Elements
		title = (TextView) this.findViewById(R.id.textView1);
		doneButton = (Button) this.findViewById(R.id.doneButton);
		doneButton.setOnClickListener(this);
		imageButton = (RadioButton) this.findViewById(R.id.lastPicButton);
		locButton = (RadioButton) this.findViewById(R.id.lastLocButton);
		
		//Get Location and Last Picture Taken
		prefs = getSharedPreferences("user_prefs", 0);
		lastLoc = prefs.getString("last_location", null).replace(" ", "");
		Log.i("LOCATION", lastLoc);
		lastpic = prefs.getString("last_image", null);
		
		//Set Config to Not Done
		editPrefs = prefs.edit();
		editPrefs.putBoolean("config", false);
		editPrefs.commit();
		
		File file = new File(Environment.getExternalStorageDirectory() + "/PicPlaces/");
		
		File imageList[] = file.listFiles();
		imagePaths = new ArrayList<String>();
		
		for(int i=0; i< imageList.length; i++){
			if(imageList[i].getAbsolutePath().toString().contains(lastLoc)){
				imagePaths.add(imageList[i].getAbsolutePath());
				Log.i("IMAGE", imageList[i].getAbsolutePath());
			}
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.widget_config, menu);
		return true;
	}
	//Save the User's Selection
	public void saveOption(String option){
		editPrefs.putString("option", option);
		editPrefs.putBoolean("config", true);
		editPrefs.commit();
	}
	//Finish with Config
	public void sendWidget(int widgetId){
		Intent resultsValue = new Intent();
		resultsValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		setResult(RESULT_OK, resultsValue);
		finish();
	}
	
	//Setup for Widget
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Bundle extras = getIntent().getExtras();
		
		if(extras != null){
			int widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
			if(widgetId != AppWidgetManager.INVALID_APPWIDGET_ID){
				RemoteViews remote = new RemoteViews(this.getPackageName(), R.layout.widget_layout);
				
				//Check for Selection
				if(locButton.isChecked()){
					remote.setTextViewText(R.id.widgetHeader, "Last Location: " + lastLoc);
					remote.setImageViewUri(R.id.widgetImage, Uri.fromFile(new File(imagePaths.get(0))));
					AppWidgetManager.getInstance(this).updateAppWidget(widgetId, remote);
					saveOption("location");
					Intent mainIntent = new Intent(this, MainActivity.class);
					PendingIntent pend = PendingIntent.getActivity(this, 0, mainIntent, 0);
					remote.setOnClickPendingIntent(R.id.widgetImage, pend);
					AppWidgetManager.getInstance(this).updateAppWidget(widgetId, remote);
					sendWidget(widgetId);
				}else if(imageButton.isChecked()){
					String[] picLoc = lastpic.replace("/mnt/sdcard/PicPlaces/", "").split("_");
					remote.setTextViewText(R.id.widgetHeader,"Last Picture: " + picLoc[0]);
					File path = new File("/mnt/sdcard/PicPlaces/" + lastpic);
					remote.setImageViewUri(R.id.widgetImage, Uri.fromFile(path));
					AppWidgetManager.getInstance(this).updateAppWidget(widgetId, remote);
					saveOption("picture");
					Intent mainIntent = new Intent(this, MainActivity.class);
					PendingIntent pend = PendingIntent.getActivity(this, 0, mainIntent, 0);
					remote.setOnClickPendingIntent(R.id.widgetImage, pend);
					AppWidgetManager.getInstance(this).updateAppWidget(widgetId, remote);
					sendWidget(widgetId);
				}else{
					Toast.makeText(this, "Please Select Option", Toast.LENGTH_LONG).show();
				}
				
			}
		}
	}
	
	

}
