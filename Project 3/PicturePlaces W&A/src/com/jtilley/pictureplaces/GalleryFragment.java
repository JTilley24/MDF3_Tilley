package com.jtilley.pictureplaces;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class GalleryFragment extends Fragment{
GridView galleryView;
TextView headerText;
ArrayList<Bitmap> images;
int index;

	public interface OnImageSelected{
		ArrayList<Bitmap> displayGalleryImg();
		void displayImages();
		void openImageActivity(int position);
	}
	
	private OnImageSelected parentActivity;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if(activity instanceof OnImageSelected) {
			parentActivity = (OnImageSelected) activity;
		}
		else{
			throw new ClassCastException(activity.toString() + "must implement onModelSelected");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View view = inflater.inflate(R.layout.activity_gallery, container, false);
		
		headerText = (TextView) view.findViewById(R.id.headerText);
		
		ArrayList<Bitmap> images = parentActivity.displayGalleryImg();
		
		galleryView = (GridView) view.findViewById(R.id.galleryView);
		galleryView.setAdapter(new ImageAdapter(getActivity(), images));
		galleryView.setOnItemClickListener(new OnItemClickListener() {
			
			//Open ImageActivity to display larger image
			@Override
			public void onItemClick(AdapterView<?> imageList, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				
				parentActivity.openImageActivity(position);
				/*Intent image = new Intent(mContext, ImageActivity.class);
				image.putExtra("bitmap", images.get(position));
				image.putExtra("file_path", filesArray.get(position));
				startActivity(image);*/
			}
		});
		
		return view;
	}

	public void displayImages(){
		images = parentActivity.displayGalleryImg();
		galleryView.setAdapter(new ImageAdapter(getActivity(), images));
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		if(hidden == true){
			
		}else{
			displayImages();
		}
		super.onHiddenChanged(hidden);
	}
	

}
