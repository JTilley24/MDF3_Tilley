package com.jtilley.pictureplaces;
/*
 * 	Author: 	Justin Tilley
 * 
 * 	Project:	PicturePlaces Widget and ActionBar
 * 
 * 	Package:	com.jtilley.pictureplaces
 * 
 * 	File: 		PicWidgetProvider.java
 * 	
 * 	Purpose:	This Activity handles the Updates for the Widget. Data is collected from last time using the application
 * 				and the Widget displays the image for last picture or location.
*/
import java.io.File;
import java.util.ArrayList;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.RemoteViews;

public class PicWidgetProvider extends AppWidgetProvider{
String lastLoc;
String lastpic;
ArrayList<Bitmap> images;
Bitmap lastBitmap;
ArrayList<String> imagePaths;

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		//Check if Config is Done and Set UI to current image
		SharedPreferences prefs = context.getSharedPreferences("user_prefs", 0);
		if(prefs.getBoolean("config", false)){
			RemoteViews remote = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			images = new ArrayList<Bitmap>();
			
			File file = new File(Environment.getExternalStorageDirectory() + "/PicPlaces/");
			
			File imageList[] = file.listFiles();
			imagePaths = new ArrayList<String>();
			if(imageList != null){
				for(int i=0; i< imageList.length; i++){
					if(imageList[i].getAbsolutePath().toString().contains(lastLoc)){
						Bitmap imageB = BitmapFactory.decodeFile(imageList[i].getAbsolutePath());
						images.add(imageB);
						imagePaths.add(imageList[i].getAbsolutePath());
						Log.i("IMAGE", imageList[i].getAbsolutePath());
					}
				}
				
				String option = prefs.getString("option", null);
				
				if(option.equals("location")){
					lastLoc = prefs.getString("last_location", null).replace(" ", "");
					
					remote.setTextViewText(R.id.widgetHeader, "Last Location: " + lastLoc);
					remote.setImageViewUri(R.id.widgetImage, Uri.fromFile(new File(imagePaths.get(0))));
				}else if(option.equals("picture")){
					lastpic = prefs.getString("last_image", null);
					String[] picLoc = lastpic.replace("/mnt/sdcard/PicPlaces/", "").split("_");
					remote.setTextViewText(R.id.widgetHeader,"Last Picture: " + picLoc[0]);
					remote.setImageViewUri(R.id.widgetImage, Uri.fromFile(new File(lastpic)));
				}
			}else{
				remote.setTextViewText(R.id.widgetHeader, "No Picture to Display");
			}
			appWidgetManager.updateAppWidget(appWidgetIds, remote);
		}
	}

}
