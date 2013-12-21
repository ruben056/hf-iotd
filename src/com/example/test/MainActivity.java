package com.example.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

	Map<String, IotdItem> iotdMap;
	
	class RefreshThread extends AsyncTask<Object, Integer, IotdHandler>{

		private MainActivity act;
		public RefreshThread(MainActivity act){
			this.act = act;
		}
		
		@Override
		protected IotdHandler doInBackground(Object... params) {
			IotdHandler handler = new IotdHandler();
	        handler.processFeed();	        
	        return handler;
		}		
		
		@Override
		protected void onPostExecute(IotdHandler result) {
			act.resetDisplay(result.getMap());
		}
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new RefreshThread(this).execute(new Object[0]);
    }

    private void resetDisplay(Map<String, IotdItem> resultMap){
    	
    	this.iotdMap = resultMap;
    	Spinner dateSpinner = (Spinner)findViewById(R.id.spinner2);
    	List<String> list = new ArrayList<String>();
    	Iterator<String> it = iotdMap.keySet().iterator();
    	while(it.hasNext()){
    		list.add(it.next());
    	}
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
    		android.R.layout.simple_spinner_item, list);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	dateSpinner.setAdapter(dataAdapter);
    	dateSpinner.setSelection(list.size()-1);
    	
    	dateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    		
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long id) {
				
				String s = arg0.getItemAtPosition(pos).toString();
				setFields(iotdMap.get(s));	    					
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub	
			}
		});
    }
    
    public void setFields(IotdItem item){
    	TextView titleView = (TextView)findViewById(R.id.imageTitle);
    	ImageView imgView = (ImageView)findViewById(R.id.imageDisplay);
    	TextView descView = (TextView)findViewById(R.id.imageDescription);
    	titleView.setText(item.getTitle());
    	imgView.setImageBitmap(item.getImg());
    	descView.setText(item.getDescription());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }   
}
