package com.jtilley.pictureplaces;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class WidgetConfig extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_widget_config);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.widget_config, menu);
		return true;
	}

}
