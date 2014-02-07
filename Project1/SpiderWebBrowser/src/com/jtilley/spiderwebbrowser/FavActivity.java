package com.jtilley.spiderwebbrowser;
/*
 * 	Author: 	Justin Tilley
 * 
 * 	Project:	SpiderWeb Browser
 * 
 * 	Package:	com.jtilley.spiderwebbrowser
 * 
 * 	File: 		FavActivity.java
 * 	
 * 	Purpose:	This Activity displays a list of saved web sites and allows the user to add the current web site
 * 				to the list. Selecting a web site will open a Dialog Fragment asking the user to Open, Delete, or Cancel.
*/
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.Toast;

public class FavActivity extends Activity {
Button addButton;
ListView favList;
String urlName;
String urlTitle;
FavStorage storage;
Context context;
public ArrayList<HashMap<String, String>> favorites;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fav);
		
		this.setTitle("Saved Favorites");
		
		context = this;
		storage = FavStorage.getInstance();
		
		addButton = (Button) findViewById(R.id.addButton);
		favList = (ListView) findViewById(R.id.favList);
		
		//Get Data from WebActivity
		Intent intent = this.getIntent();
		urlName = intent.getStringExtra("URL_NAME");
		urlTitle = intent.getStringExtra("URL_TITLE");
		
		if(urlName != null){
			Log.i("URL", urlName);
		}
		if(urlTitle != null){
			Log.i("TITLE", urlTitle);
		}
		
		//Add Current Page from WebActivity to list of Favorites
		addButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					HashMap<String, String> urlStrings = new HashMap<String, String>();
					urlStrings.put("name", urlName);
					urlStrings.put("title", urlTitle);
					favorites.add(urlStrings);
					
					storage.writeStringFile(context, "favorites", favorites);
					
					Toast.makeText(context, urlTitle + " is saved!", Toast.LENGTH_LONG).show();
					displayFavorites();
			}
		});
		
		displayFavorites();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fav, menu);
		return true;
	}
	
	//Display Stored Favorites in ListView
	public void displayFavorites(){
		favorites = storage.readStringFile(context, "favorites");
		ArrayList<String> favStrings = new ArrayList<String>();
		if(favorites != null){
			
			for(int i = 0; i < favorites.size(); i++){
				favStrings.add(favorites.get(i).get("title"));
				Log.i("SAVED", favorites.get(i).get("title"));
			}
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, favStrings);
		
		favList.setAdapter(adapter);
		
		favList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listItem, View view, int position, long row) {
				// TODO Auto-generated method stub
				DialogFragment dialog = FavDialog.newInstance(position);
				dialog.show(getFragmentManager(), "saved");
			}
			
		});
	}
	
	//Remove Selection from Saved Favorites
	public void removeItem(int position){
		favorites.remove(position);
		
		storage.writeStringFile(context, "favorites", favorites);
		
		displayFavorites();
	}
	
	//Open Selection in WebActivity
	public void openItem(int position){
		Intent data = new Intent();
		String url = favorites.get(position).get("name");
		data.putExtra("URL_OPEN", url.toString());
		setResult(RESULT_OK, data);
		super.finish();
	}
	
	//DialogFragment for Open and Delete of Selection in ListView
	public static class FavDialog extends DialogFragment{
		Button openButton;
		Button deleteButton;
		Button cancelButton;
		int position;
		
		static FavDialog newInstance(int pos){
			FavDialog saved = new FavDialog();
			
			Bundle args = new Bundle();
			args.putInt("position", pos);
			saved.setArguments(args);
			
			return saved;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fav_dialog, container, false);
			getDialog().setTitle("What would you like to do?");
			
			openButton = (Button) view.findViewById(R.id.openButton);
			deleteButton = (Button) view.findViewById(R.id.deleteButton);
			cancelButton = (Button) view.findViewById(R.id.cancelButton);
			
			openButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((FavActivity)getActivity()).openItem(position);
					dismiss();
				}
			});
			
			deleteButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((FavActivity)getActivity()).removeItem(position);
					dismiss();
				}
			});
			
			cancelButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});
			
			return view;
		}		
	}
}
