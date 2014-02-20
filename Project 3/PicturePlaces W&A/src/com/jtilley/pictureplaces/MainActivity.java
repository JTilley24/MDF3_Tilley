package com.jtilley.pictureplaces;
/*
 * 	Author: 	Justin Tilley
 * 
 * 	Project:	PicturePlaces Widget and ActionBar
 * 
 * 	Package:	com.jtilley.pictureplaces
 * 
 * 	File: 		MainActivity.java
 * 	
 * 	Purpose:	This Activity displays a button to open the camera and a button to view a gallery of saved images.
 * 				A ListView is also used to display previous location pictures were taken.
*/


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ActionBar.Tab;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnQueryTextListener, MainActivityFragment.OnListItemClicked, GalleryFragment.OnImageSelected {
Context mContext;
Bitmap lastPic;
JSONArray locArray;
LocStorage storage;
private LocationManager lManager;
private String provider;
Location location;
SearchView searchField;
MenuItem settings;
MainActivityFragment fragment1;


private static final int CAMERA_REQUEST = 1888;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main_fragment);
		
		mContext = this;
		
	
		
		ActionBar aBar = getActionBar();
		aBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		Tab tab1 = aBar.newTab().setText("Home")
								.setTabListener(new TabListener<MainActivityFragment>(this, "home", MainActivityFragment.class));
		
		Tab tab2 = aBar.newTab().setText("Gallery")
								.setTabListener(new TabListener<GalleryFragment>(this, "gallery", GalleryFragment.class));
		
		aBar.addTab(tab1);
		aBar.addTab(tab2);
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		fragment1 = (MainActivityFragment) getFragmentManager().findFragmentByTag("home");
		displayLocations();
		
		if(!checkBattery()){
			Toast.makeText(mContext, "Battery Low!", Toast.LENGTH_LONG).show();
		}else{
			//Get Location and Check for Closest Previous Location
			lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			provider = lManager.getBestProvider(criteria, false);
			
			lManager.requestLocationUpdates(provider, 0, 1, new MyLocationListener());
			location = lManager.getLastKnownLocation(provider);
			if(gpsConnected(location)){
				closestLocation(location);
			}
		}
	}

	public void openGallery(String position){
		if(position != null){
			Intent gallery = new Intent(mContext, GalleryActivity.class);
			gallery.putExtra("LOCATION", position);
			startActivity(gallery);	
		}else{
			Intent gallery = new Intent(mContext, GalleryActivity.class);
			gallery.putExtra("LOCATION", "all");
			startActivity(gallery);	
		}
	}
	
	
	//Save Image to External Storage
	public void saveImage(String locName){
		File path = Environment.getExternalStoragePublicDirectory("/PicPlaces/");
		if(!path.exists()){
			path.mkdir();
		}
		String day = String.valueOf(Calendar.getInstance().getTime().getTime());
		Log.i("TIME", day);
		
		String filename = locName + "_" + day + ".jpg";
	
		Toast.makeText(mContext, "Image Saved: " + filename, Toast.LENGTH_LONG).show();
		
		File file = new File(path, filename);
		try {
			OutputStream fos = new FileOutputStream(file);
			Bitmap bitmap = lastPic;
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
			exifAttr(filename, file, locName);
			displayLocations();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Check for Location
	public Boolean gpsConnected(Location location){
		if(location != null){
			return true;
		}
		return false;
	}
	
	//GeoTag Saved Image
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
	
	//Save Location
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
	
	//Display Saved Locations in ListView
	public void displayLocations(){
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
		
		ArrayList<String> locStrings = new ArrayList<String>();
		for(int i=0; i< locArray.length(); i++){
			try {
				String tempName = locArray.getJSONObject(i).getString("name");
				Log.i("LOC_NAME", tempName);
				locStrings.add(tempName);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		fragment1.displaySaved(locStrings);
		
	}
	
	//Get Image after taken by Camera
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK){
			if(requestCode == CAMERA_REQUEST){
				lastPic = (Bitmap) data.getExtras().get("data");
				DialogFragment dialog = new PicDialog();
				dialog.show(getFragmentManager(), "dialog");
			}
		}
	}
	
	//Find Closest Previous Location to Current Location
	public void closestLocation(Location location){
		if(locArray != null){
			Double[] distanceList = new Double[locArray.length()];
			for(int i=0;i < locArray.length();i++){
				Location tempLocation = new Location("");
				try {
					int latitude = (int) locArray.getJSONObject(i).getInt("latitude");
					int longitude = (int) locArray.getJSONObject(i).getInt("longitude");
					tempLocation.setLatitude(latitude);
					tempLocation.setLongitude(longitude);
					double distance = tempLocation.distanceTo(location);
					locArray.getJSONObject(i).put("distance", distance);
					distanceList[i] = distance;
					Log.i("DISTANCE", locArray.getJSONObject(i).get("distance").toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			Arrays.sort(distanceList);
			StringBuilder closest = new StringBuilder();
			for(int i=0;i < locArray.length();i++){
				try {
					
					if(distanceList[0].toString().equals(locArray.getJSONObject(i).get("distance").toString())){
						closest.append(locArray.getJSONObject(i).get("name") + " ");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Log.i("CLOSEST", closest.toString());
			if(closest.toString() != ""){
				Toast.makeText(mContext, "Current Location is close to " + closest.toString(), Toast.LENGTH_LONG).show();
			}
		}
	}
	
	//Check for Battery Status
	public Boolean checkBattery(){
		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent battery = this.registerReceiver(null, filter);
		int bLevel = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int bScale = battery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		
		if((bLevel > 0) && (bScale > 0)){
			int bPercent = (bLevel * 100)/bScale;
			if(bPercent < 30){
				Toast.makeText(mContext, "Battery low!", Toast.LENGTH_LONG).show();
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_action, menu);
		searchField = (SearchView) menu.findItem(R.id.action_search).getActionView();
		setupSearchView(searchField);
		settings = (MenuItem) menu.findItem(R.id.action_settings);
		return true;
	}

	
	public void openCamera(){
		displayLocations();	
		Location location = lManager.getLastKnownLocation(provider);
		if(!(checkBattery())){
				Toast.makeText(mContext, "Battery Low and Camera is Disabled!", Toast.LENGTH_LONG).show();
			}else if(gpsConnected(location)){
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, CAMERA_REQUEST);
			}else{
				Toast.makeText(mContext, "Please Enable GPS!", Toast.LENGTH_LONG).show();
			}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == findViewById(R.id.action_camera).getId()){
			openCamera();
		}else if(item.getItemId() == settings.getItemId()){
			Log.i("SETTINGS", "Selected Settings");
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void setupSearchView(SearchView search){
		search.setIconifiedByDefault(false);
		search.setSubmitButtonEnabled(false);
		search.setOnQueryTextListener(this);
	}
	@Override
	public boolean onQueryTextChange(String query){
		if(TextUtils.isEmpty(query)){
			fragment1.listFilter(query, false);
		}else{
			fragment1.listFilter(query, true);
		}
		return true;
	}
	
	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		searchField.clearFocus();
		return false;
	}
	//Display Dialog for user's input of location
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
					
					getDialog().setTitle("Save Image");
					
					okButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							String locString = locText.getText().toString();
							//locString.replaceAll(" ", ".");
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
	
	//Location Changes
	private final class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			if(location != null){
				closestLocation(location);
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
	public static class TabListener<T extends Fragment>implements ActionBar.TabListener{
		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;
		
		public TabListener(Activity activity, String tag, Class<T> clas){
			mActivity = activity;
			mTag = tag;
			mClass = clas;
		}
		
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if(mFragment == null){
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				ft.add(android.R.id.content, mFragment, mTag);
			}else{
				ft.attach(mFragment);
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if(mFragment != null){
				ft.detach(mFragment);
			}
		}

	}
	@Override
	public void displaySaved(ArrayList<String> locStrings) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listFilter(String query, Boolean filtered) {
		// TODO Auto-generated method stub
		
	}


	

	
	
	

	
	
	
}
