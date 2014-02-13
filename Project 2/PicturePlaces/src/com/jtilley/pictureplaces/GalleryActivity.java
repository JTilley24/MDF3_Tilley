package com.jtilley.pictureplaces;

import java.io.File;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.widget.GridView;

public class GalleryActivity extends Activity {
GridView galleryView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		
		ArrayList<Bitmap> images = new ArrayList<Bitmap>();
		
		File file = new File(Environment.getExternalStorageDirectory() + "/PicPlaces/");
		
		File imageList[] = file.listFiles();
		
		for(int i=0; i< imageList.length; i++){
			Log.i("IMAGE", imageList[i].getAbsolutePath());
			Bitmap imageB = BitmapFactory.decodeFile(imageList[i].getAbsolutePath());
			images.add(imageB);
		}
		
		galleryView = (GridView) findViewById(R.id.galleryView);
		galleryView.setAdapter(new ImageAdapter(this, images));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gallery, menu);
		return true;
	}

	
	
}
