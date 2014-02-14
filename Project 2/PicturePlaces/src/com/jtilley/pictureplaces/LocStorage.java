package com.jtilley.pictureplaces;
/*
 * 	Author: 	Justin Tilley
 * 
 * 	Project:	PicturePlaces
 * 
 * 	Package:	com.jtilley.pictureplaces
 * 
 * 	File: 		LocStorage.java
 * 	
 * 	Purpose:	This class is used to save Locations to a file and get saved locations from file. It takes
 * 				the JSONArray as a string and saves it.
*/
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;
import android.util.Log;


public class LocStorage {
	
	private static LocStorage m_instance;
	
	private LocStorage(){
		
	}
	
	public static LocStorage getInstance(){
		if(m_instance == null){
			m_instance = new LocStorage();
		}
		return m_instance;
	}
	//Save Data to Internal Storage
	public Boolean writeStringFile (Context context, String filename, String content){
		Boolean result = false;
		
		FileOutputStream output = null;
		
		try{
			output = context.openFileOutput(filename, Context.MODE_PRIVATE);
			output.write(content.getBytes());
			Log.i("WRITE STRING FILE", "success");
		}catch(Exception e){
			Log.e("WRITE STRING FILE", e.toString());
		}
		
		return result;
	} 
	//Access Data from Internal Storage
	public String readStringFile(Context context, String filename){
		String content = "";
		
		FileInputStream input = null;
		
		try{
			input = context.openFileInput(filename);
			 BufferedInputStream buffInput = new BufferedInputStream(input);
			 byte[] contentBytes = new byte[1024];
			 int bytesRead = 0;
			 StringBuffer contentBuffer = new StringBuffer();
			 
			 while((bytesRead = buffInput.read(contentBytes)) != -1){
				 content = new String(contentBytes, 0, bytesRead);
				 contentBuffer.append(content);
			 }
			 content = contentBuffer.toString();
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
