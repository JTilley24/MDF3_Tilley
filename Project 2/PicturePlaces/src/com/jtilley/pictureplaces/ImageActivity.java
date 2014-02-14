package com.jtilley.pictureplaces;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ImageActivity extends Activity {
ImageView imageLarge;
Button deleteButton;
Context context;
String location;
String path;
String filePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		
		context = this;
		
		imageLarge = (ImageView) findViewById(R.id.imageLarge);
		
		Intent intent = this.getIntent();
		Bitmap bitmap = intent.getParcelableExtra("bitmap");
		imageLarge.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 600, 600, false));
		
		filePath = intent.getStringExtra("file_path");
		path = "/mnt/sdcard/PicPlaces/";
		
		String[] fileName = filePath.replaceAll(path, "").split("_");
		location = fileName[0];
		Log.i("FILE", fileName[0]);
		
		deleteButton = (Button) findViewById(R.id.deleteButton);
		deleteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DialogFragment dialog = new DeleteDialog();
				dialog.show(getFragmentManager(), "dialog");
			}
		});
	}
	
	public void removeImage(){
		File imageFile = new File(filePath);
		imageFile.delete();
		
		File file = new File(Environment.getExternalStorageDirectory() + "/PicPlaces/");
		
		File imageList[] = file.listFiles();
		
		if(!(imageList.toString().contains(location))){
			LocStorage storage = LocStorage.getInstance();
			String locJSON = storage.readStringFile(context, "location_json");
			try {
				JSONArray locArray = new JSONArray(locJSON);
				JSONArray tempArray = new JSONArray();
				for(int i=0;i < locArray.length();i++){
					if(locArray.getJSONObject(i).get("name").toString().equals(location)){
						Log.i("FOUND", locArray.getJSONObject(i).get("name").toString());
						
					}else{
						tempArray.put(locArray.getJSONObject(i));
					}
				}
				storage.writeStringFile(context, "location_json", tempArray.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void returnMain(){
		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image, menu);
		return true;
	}
	
	public static class DeleteDialog extends DialogFragment{
		Button okButton;
		Button cancelButton;
		
		public DeleteDialog(){
			
		}
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
					View view = inflater.inflate(R.layout.delete_dialog, container, false);
					
					getDialog().setTitle("Are You Sure?");
					
					okButton = (Button) view.findViewById(R.id.okButton);
					cancelButton = (Button) view.findViewById(R.id.cancelButton);
					
					okButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							((ImageActivity)getActivity()).removeImage();
							dismiss();
							((ImageActivity)getActivity()).returnMain();
						}
					});
					
					cancelButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dismiss();
						}
					});
					
					
			return view;
			
		}
	}
}
