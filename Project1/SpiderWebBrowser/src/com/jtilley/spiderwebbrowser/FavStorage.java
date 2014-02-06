package com.jtilley.spiderwebbrowser;

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
			//output.write(content.getBytes());
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
			/*input = context.openFileInput(filename);
			 BufferedInputStream buffInput = new BufferedInputStream(input);
			 byte[] contentBytes = new byte[1024];
			 int bytesRead = 0;
			 StringBuffer contentBuffer = new StringBuffer();
			 
			 while((bytesRead = buffInput.read(contentBytes)) != -1){
				 content = new String(contentBytes, 0, bytesRead);
				 contentBuffer.append(content);
			 }*/
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
