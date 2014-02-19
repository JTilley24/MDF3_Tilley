package com.jtilley.pictureplaces;
/*
 * 	Author: 	Justin Tilley
 * 
 * 	Project:	PicturePlaces
 * 
 * 	Package:	com.jtilley.pictureplaces
 * 
 * 	File: 		ImageAdapter.java
 * 	
 * 	Purpose:	This Custom BaseAdapter is used for the GridView in GalleryActivity. It adds the bitmap to an
 *  			ImageView and sizes it.
*/
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter{
	private Context mContext;
	ArrayList<Bitmap> mImageArray;
	
	public ImageAdapter(Context context, ArrayList<Bitmap> imageArray){
		mContext = context;
		mImageArray = imageArray;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mImageArray.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView imageView = null;
		
		//Size image and set it to ImageView
		if(convertView == null){
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(200,200));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
		}else{
			imageView = (ImageView) convertView;
		}
		
		imageView.setImageBitmap(mImageArray.get(position));
		
		return imageView;
	}
}
