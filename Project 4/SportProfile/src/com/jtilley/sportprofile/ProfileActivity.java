package com.jtilley.sportprofile;
/*
 * 	Author: 	Justin Tilley
 * 
 * 	Project:	SportProfile
 * 
 * 	Package:	com.jtilley.sportprofile
 * 
 * 	File: 		ProfileActivity.java
 * 	
 * 	Purpose:	This Activity displays the User Information and Team Information from the Account the is logged in.
 * 				UserName is passed from the MainActivity and a JSONObject from SharedPreferences is parsed and displayed.
*/
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends Activity {
String userData;
TextView header;
Button espnButton;
String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		header = (TextView) findViewById(R.id.helloHeader);
		espnButton = (Button) findViewById(R.id.espnButton);
		espnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				displayEspn();
			}
		});
		
		//Get Data from MainActivity
		Intent intent = this.getIntent();
		Log.i("USERNAME", intent.getStringExtra("user"));
		String user = intent.getStringExtra("user");
		SharedPreferences prefs = getSharedPreferences("user_profile", 0);
		Map<String, ?> accounts = prefs.getAll();
		Log.i("ACCOUNT" , accounts.get(user).toString());
			try {
				JSONObject account = new JSONObject(accounts.get(user).toString());
				if(account != null){
					displayData(account);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	//Display User Data 
	public void displayData(JSONObject account){
		try {
			String firstname = account.get("firstname").toString();
			JSONObject team = new JSONObject(account.get("team").toString());
			String color = team.get("color").toString();
			header.setText("Hello, " + firstname);
			header.setBackgroundColor(Color.parseColor("#" + color));
			TextView teamText = (TextView) findViewById(R.id.teamName);
			String teamName = team.getString("location") + " " + team.getString("name");
			teamText.setText(teamName);
			getTeamData(team);
			getLink(team);
			displayPic(account);
 		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Display Team Data
	public void getTeamData(JSONObject team){
		try {
			JSONArray locArray = team.getJSONArray("venues");
			JSONObject locObject = locArray.getJSONObject(0);
			String location = locObject.getString("name") + " in " + locObject.getString("city") + " , " + locObject.getString("state");
			TextView venue = (TextView) findViewById(R.id.venue);
			venue.setText(location);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Get Link to Espn for Team
	public void getLink(JSONObject team){
		try {
			JSONObject link = team.getJSONObject("links");
			JSONObject mobile = link.getJSONObject("mobile");
			JSONObject teams  = mobile.getJSONObject("teams");
			String href = teams.getString("href");
			url = href;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Display Profile Picture
	public void displayPic(JSONObject account){
		try {
			String picture = account.getString("picture");
			if(picture.equalsIgnoreCase("images/profile.png")){
				try {
					InputStream bitmap = getAssets().open("www/images/profile.png");
					Bitmap pic = BitmapFactory.decodeStream(bitmap);
					ImageView profilePic = (ImageView) findViewById(R.id.profilePic);
					profilePic.setImageBitmap(pic);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.i("DEFAULT",picture);
			}else{
				String picPath = picture.split("sdcard")[1];
				String path = Environment.getExternalStorageDirectory().getAbsolutePath() + picPath;
				Bitmap pic = BitmapFactory.decodeFile(path);
				ImageView profilePic = (ImageView) findViewById(R.id.profilePic);
				profilePic.setImageBitmap(pic);
				Log.i("PICTURE", picture);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Open Intent to Espn WebSite 
	public void displayEspn(){
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(intent);
	}
	
}
