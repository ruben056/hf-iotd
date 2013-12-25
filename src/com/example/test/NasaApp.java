package com.example.test;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;

import com.example.test.fragments.IotdFragment;

public class NasaApp extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nasa_app);
	}
	
	 /** Delegate button actions to correct fragment */
    public void onRefresh(View v) {
    	FragmentManager fragMgr = getFragmentManager();
    	IotdFragment frag = (IotdFragment)fragMgr.findFragmentById(R.id.fragment_iotd);
    	frag.onRefresh(v);    	
	}
    
    public void onSetWallPaper(View v){    	
    	FragmentManager fragMgr = getFragmentManager();
    	IotdFragment frag = (IotdFragment)fragMgr.findFragmentById(R.id.fragment_iotd);
    	frag.onSetWallPaper(v);   
    }
}