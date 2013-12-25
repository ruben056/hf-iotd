package com.example.test.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.util.IotdHandler;
import com.example.test.util.IotdItem;

public class IotdFragment extends Fragment {

	private Map<String, IotdItem> iotdMap;
	private String selectedItemKey;
	
	class refreshWallPaper extends AsyncTask<IotdItem, Integer, Boolean>{

		private IotdFragment act;
		
		public refreshWallPaper(IotdFragment act){
			this.act = act;
		}
		
		@Override
		protected Boolean doInBackground(IotdItem... arg0) {
			try {				
				WallpaperManager wpMgr = WallpaperManager.getInstance(this.act.getActivity());
				wpMgr.setBitmap(arg0[0].getImg());
				return true;
			} catch (IOException e) {			
				e.printStackTrace();
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			String msg;
			if(result){
				msg ="Wallpaper set";
			}else{
				msg ="Error setting wallpaper";
			}
			Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
		}		
	}
	
	class RefreshThread extends AsyncTask<Object, Integer, IotdHandler>{

		private IotdFragment act;
		ProgressDialog pd;
		public RefreshThread(IotdFragment act){
			this.act = act;			
		}
		
		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(getActivity(), "Loading", "Loading image of the day");
			super.onPreExecute();
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
			pd.dismiss();
		}
	}

    
    private void resetDisplay(Map<String, IotdItem> resultMap){
    	
    	this.iotdMap = resultMap;
    	Spinner dateSpinner = (Spinner)getActivity().findViewById(R.id.spinner2);
    	List<String> list = new ArrayList<String>();
    	Iterator<String> it = iotdMap.keySet().iterator();
    	while(it.hasNext()){
    		list.add(it.next());
    	}
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
    		android.R.layout.simple_spinner_item, list);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	dateSpinner.setAdapter(dataAdapter);
    	dateSpinner.setSelection(list.size()-1);
    	
    	dateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    		
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long id) {
				
				selectedItemKey = arg0.getItemAtPosition(pos).toString();
				setFields(iotdMap.get(selectedItemKey));	    					
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				selectedItemKey = null;
				setFields(null);	    					
			}
		});
    }
    
    public void setFields(IotdItem item){
    	TextView titleView = (TextView)getActivity().findViewById(R.id.imageTitle);
    	ImageView imgView = (ImageView)getActivity().findViewById(R.id.imageDisplay);
    	TextView descView = (TextView)getActivity().findViewById(R.id.imageDescription);
    	titleView.setText(item == null ? "" : item.getTitle());
    	imgView.setImageBitmap(item == null ? null : item.getImg());
    	descView.setText(item == null ? "" : item.getDescription());
    }
    
	
/*    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setContentView(R.layout.activity_main);
        new RefreshThread(this).execute(new Object[0]);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
         /*getActivity().setContentView(R.layout.activity_main);
         new RefreshThread(this).execute(new Object[0]);*/
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	return inflater.inflate(R.layout.iotd_fragment, container, false);
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	new RefreshThread(this).execute(new Object[0]);
    }
    
        
    /** Button actions */
    public void onRefresh(View v) {
    	new RefreshThread(this).execute(new Object[0]);
	}
    
    public void onSetWallPaper(View v){    	
    	new refreshWallPaper(this).execute(iotdMap.get(selectedItemKey));
    }
    
}