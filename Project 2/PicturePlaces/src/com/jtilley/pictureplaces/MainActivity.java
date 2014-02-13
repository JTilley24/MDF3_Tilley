package com.jtilley.pictureplaces;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
Context mContext;
Button camButton;
Button galleryButton;
ListView locList;
ImageView lastPic;
JSONArray locArray;
LocStorage storage;
private LocationManager lManager;
private String provider;

private static final int CAMERA_REQUEST = 1888;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		
		storage = LocStorage.getInstance();
		String locJSON = storage.readStringFile(mContext, "location_json");
		if(locJSON != ""){
			try {
				locArray = new JSONArray(locJSON);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			locArray = new JSONArray();
		}
		
		locList = (ListView) findViewById(R.id.locatList);
		
		displayLocations(locArray);
		locList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> locListView, View view, int position,
					long row) {
				// TODO Auto-generated method stub
				Intent gallery = new Intent(mContext, GalleryActivity.class);
				gallery.putExtra("LOCATION", locListView.getItemAtPosition(position).toString());
				startActivity(gallery);	
			}
		});
		
		camButton = (Button) findViewById(R.id.camButton);
		galleryButton = (Button) findViewById(R.id.galleryButton);
		
		camButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, CAMERA_REQUEST);
			}
		});
		
		galleryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent gallery = new Intent(mContext, GalleryActivity.class);
				gallery.putExtra("LOCATION", "all");
				startActivity(gallery);	
			}
		});
		
		lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		provider = lManager.getBestProvider(criteria, false);
		
		lManager.requestLocationUpdates(provider, 0, 1, new MyLocationListener());
	}

	public void saveImage(String locName){
		Toast.makeText(mContext, "Image Saved", Toast.LENGTH_LONG).show();
		File path = Environment.getExternalStoragePublicDirectory("/PicPlaces/");
		if(!path.exists()){
			path.mkdir();
		}
		String day = String.valueOf(Calendar.getInstance().getTime().getTime());
		Log.i("TIME", day);
		
		String filename = locName + "_" + day + ".jpg";
		
		
		
		File file = new File(path, filename);
		try {
			OutputStream fos = new FileOutputStream(file);
			Bitmap bitmap = ((BitmapDrawable)lastPic.getDrawable()).getBitmap();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
			exifAttr(filename, file, locName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void exifAttr(String filename, File file, String locName){
		
		Location location = lManager.getLastKnownLocation(provider);
		if(location != null){
			String longitude =  String.valueOf(location.getLongitude());
			
			String latitude =  String.valueOf(location.getLatitude());
			
			ExifInterface exif;

			try {
				exif = new ExifInterface(file.getAbsolutePath());
				exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, String.valueOf(location.getLatitude()));
				exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, String.valueOf(location.getLongitude()));
				exif.saveAttributes();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			saveLocation(locName, longitude, latitude);
			
		}
	}
	
	public void saveLocation(String locName, String longitude, String latitude){
		JSONObject locObject = new JSONObject();
		try {
			locObject.put("name", locName);
			locObject.put("longitude", longitude);
			locObject.put("latitude", latitude);
			if(!locArray.toString().contains(locName)){
				locArray.put(locObject);
				Log.i("LOCATIONS", locArray.toString());
				storage.writeStringFile(mContext, "location_json", locArray.toString());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void displayLocations(JSONArray locArray){
		ArrayList<String> locStrings = new ArrayList<String>();
		for(int i=0; i< locArray.length(); i++){
			try {
				String tempName = locArray.getJSONObject(i).getString("name");
				Log.i("LOC_NAME", tempName);
				locStrings.add(tempName);
				
				locList.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, locStrings));
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		lManager.requestLocationUpdates(provider, 400, 1, new MyLocationListener());
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
		
	private final class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
			
			for(int i=0;i < locArray.length();i++){
				Location tempLocation = new Location("");
				try {
					int latitude = (int) locArray.getJSONObject(i).getInt("latitude");
					int longitude = (int) locArray.getJSONObject(i).getInt("longitude");
					tempLocation.setLatitude(latitude);
					tempLocation.setLongitude(longitude);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				float distance = tempLocation.distanceTo(location);
				Log.i("DISTANCE", String.valueOf(distance));
				
			}
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	}

	
	
	
}
