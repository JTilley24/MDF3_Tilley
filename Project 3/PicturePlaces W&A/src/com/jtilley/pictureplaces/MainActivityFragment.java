package com.jtilley.pictureplaces;


import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivityFragment extends Fragment{
Button camButton;
Button galleryButton;
ListView locList;
TextView listHeader;

	public interface OnListItemClicked{
		void openGallery(String position);
		void displaySaved(ArrayList<String> locStrings);
		void listFilter(String query, Boolean filtered);
	}

	private OnListItemClicked parentActivity;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		if(activity instanceof OnListItemClicked) {
			parentActivity = (OnListItemClicked) activity;
		}
		else{
			throw new ClassCastException(activity.toString() + "must implement onListItemClicked");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View view = inflater.inflate(R.layout.activity_main, container, false);
		
		locList = (ListView) view.findViewById(R.id.locatList);
		locList.setTextFilterEnabled(true);
		camButton = (Button) view.findViewById(R.id.camButton);
		galleryButton = (Button) view.findViewById(R.id.galleryButton);
		listHeader = (TextView) view.findViewById(R.id.listHeader);
		
		//Open Gallery to display images at Selected Location
		locList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> locListView, View view, int position,
							long row) {
						// TODO Auto-generated method stub
						parentActivity.openGallery(locListView.getItemAtPosition(position).toString());
					}
				});

		
		
		return view;
	}

	public void displaySaved(ArrayList<String> locStrings) {
		// TODO Auto-generated method stub
		locList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, locStrings));
		if(locStrings.size() > 0){
			listHeader.setText("Previous Locations");
		}else{
			listHeader.setText("No Saved Images");
		}
	}
	
	public void listFilter(String query, Boolean filtered){
		if(filtered == false){
			locList.clearTextFilter();
		}else{
			locList.setFilterText(query);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
