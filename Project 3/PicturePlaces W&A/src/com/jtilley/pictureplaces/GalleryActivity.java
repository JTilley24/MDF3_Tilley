package com.jtilley.pictureplaces;
/*
 * 	Author: 	Justin Tilley
 * 
 * 	Project:	PicturePlaces Widget and ActionBar
 * 
 * 	Package:	com.jtilley.pictureplaces
 * 
 * 	File: 		GalleryActvity.java
 * 	
 * 	Purpose:	This Activity display saved images in a GridView depending on the user's selection on MainActivity.
 * 				The GridView will display all images or sort from saved location. 
*/
import java.io.File;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class GalleryActivity extends Activity {
GridView galleryView;
TextView headerText;
String locationString;
Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		
		mContext = this;
	
		//Gather Data sent from MainActivity
		Intent intent = this.getIntent();
		locationString = intent.getStringExtra("LOCATION");
		Log.i("LOCATION", locationString);
		
		final ArrayList<String>filesArray = new ArrayList<String>();
		final ArrayList<Bitmap> images = new ArrayList<Bitmap>();
		
		//Get List of Files in Directory
		File file = new File(Environment.getExternalStorageDirectory() + "/PicPlaces/");
		
		File imageList[] = file.listFiles();
		
		headerText = (TextView) findViewById(R.id.headerText);
		
		//Displays if no images are saved
		if(images.size() == 0){
			headerText.setText("No Images to Display");
		}
		
		//Check for selection from MainActivity and Add images
		if(locationString.equalsIgnoreCase("all")){
			headerText.setText("All Images");
			for(int i=0; i< imageList.length; i++){
				Log.i("IMAGE", imageList[i].getAbsolutePath());
				Bitmap imageB = BitmapFactory.decodeFile(imageList[i].getAbsolutePath());
				images.add(imageB);
				filesArray.add(imageList[i].getAbsolutePath());	
			}
		}else{
			headerText.setText(locationString);
			for(int i=0; i< imageList.length; i++){
				Log.i("IMAGE", imageList[i].getAbsolutePath());
				if(imageList[i].getAbsolutePath().contains(locationString)){
					Bitmap imageB = BitmapFactory.decodeFile(imageList[i].getAbsolutePath());
					images.add(imageB);
					filesArray.add(imageList[i].getAbsolutePath());
				}
			}
		}
		
		galleryView = (GridView) findViewById(R.id.galleryView);
		galleryView.setAdapter(new ImageAdapter(this, images));
		galleryView.setOnItemClickListener(new OnItemClickListener() {
			
			//Open ImageActivity to display larger image
			@Override
			public void onItemClick(AdapterView<?> imageList, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent image = new Intent(mContext, ImageActivity.class);
				image.putExtra("bitmap", images.get(position));
				image.putExtra("file_path", filesArray.get(position));
				startActivity(image);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gallery, menu);
		return true;
	}

	
	
}
