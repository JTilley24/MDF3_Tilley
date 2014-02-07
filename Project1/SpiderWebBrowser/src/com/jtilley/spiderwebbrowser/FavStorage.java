package com.jtilley.spiderwebbrowser;
/*
 * 	Author: 	Justin Tilley
 * 
 * 	Project:	SpiderWeb Browser
 * 
 * 	Package:	com.jtilley.spiderwebbrowser
 * 
 * 	File: 		FavStorage.java
 * 	
 * 	Purpose:	This Class is used to Write and Read Data from Internal Storage. It receives and sends an Array of Hashmaps
 * 				containing the URL and Title of the Web Page.
*/
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

public class FavStorage {
	
	private static FavStorage m_instance;
	
	private FavStorage(){
		
	}
	
	public static FavStorage getInstance(){
		if(m_instance == null){
			m_instance = new FavStorage();
		}
		return m_instance;
	}
	
	//Save Data to Internal Storage
	public Boolean writeStringFile (Context context, String filename, ArrayList<HashMap<String, String>> content){
		Boolean result = false;
			
		FileOutputStream output = null;
			
		try{
			output = context.openFileOutput(filename, Context.MODE_PRIVATE);
			ObjectOutputStream object = new ObjectOutputStream(output);
			object.writeObject(content);
			Log.i("WRITE STRING FILE", "success");
		}catch(Exception e){
			Log.e("WRITE STRING FILE", e.toString());
		}
			
		return result;
	} 
	
	//Access Data from Internal Storage
	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, String>> readStringFile(Context context, String filename){
		ArrayList<HashMap<String, String>> content = new ArrayList<HashMap<String, String>>();
		
		FileInputStream input = null;
		
		try{
			input = context.openFileInput(filename);
			ObjectInputStream object = new ObjectInputStream(input);
			
			content = (ArrayList<HashMap<String, String>>) object.readObject();
			
			 Log.i("READ STRING", "success");
		}catch(Exception e){
			
		}finally {
			try{
				input.close();
			}catch(Exception e){
				Log.e("CLOSE ERROR", e.toString());
			}
		}
		return content;
	}
	
}
