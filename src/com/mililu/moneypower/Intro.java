package com.mililu.moneypower;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Intro extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_intro);
	    /****** Create Thread that will sleep for 3 seconds *************/        
		Thread background = new Thread() {
			public void run() {
				try {
					// Thread will sleep for 5 seconds
					sleep(1000);
	                
					// After 5 seconds redirect to another intent
					Intent i=new Intent(getBaseContext(),LoginActivity.class);
					startActivity(i);
					
					//Remove activity
					finish();
				} 
				catch (Exception e) {
				}
			}
		};
		background.start();
	}
	@Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
