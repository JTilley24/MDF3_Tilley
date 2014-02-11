package com.jtilley.pictureplaces;


import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
Context mContext;
Button camButton;
ImageView lastPic;

private static final int CAMERA_REQUEST = 1888;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		
		camButton = (Button) findViewById(R.id.camButton);
		
		camButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, CAMERA_REQUEST);
			}
		});
	}

	public void saveImage(String locName){
		Toast.makeText(mContext, "Image Saved", Toast.LENGTH_LONG).show();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK){
			if(requestCode == CAMERA_REQUEST){
				lastPic = (ImageView) findViewById(R.id.lastPic);
				Bitmap photo = (Bitmap) data.getExtras().get("data");
				lastPic.setImageBitmap(photo);
				DialogFragment dialog = new PicDialog();
				dialog.show(getFragmentManager(), "dialog");
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static class PicDialog extends DialogFragment{
		EditText locText;
		Button okButton;
		Button cancelButton;
		
		public PicDialog(){
			
		}
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
					View view = inflater.inflate(R.layout.pic_dialog, container, false);
					
					locText = (EditText) view.findViewById(R.id.locText);
					okButton = (Button) view.findViewById(R.id.okButton);
					cancelButton = (Button) view.findViewById(R.id.cancelButton);
					
					okButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							String locString = locText.getText().toString();
							if(locString.length() > 0){
								((MainActivity)getActivity()).saveImage(locString);
								dismiss();
							}else{
								Toast.makeText(getActivity(), "Please enter the name of location", Toast.LENGTH_LONG).show();
							}
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
