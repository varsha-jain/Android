package com.GPSproj.android;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class mainGPSproj extends MapActivity {
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
    protected EditText et;
    protected LocationManager locationManager;       
    public String sw;
    protected Button b;
    protected String message;
    protected  LocationListener lr;
    protected int Satellites;
    protected int count;
    protected File myFile;
    public TextView tv;
    public TextView t;
    public TextView tt;
    public TextView ttt;
    public String lotude,latude,spd,sate,alt,text;
    public MapView mv;
    public MapController mpc;
    private MyLocationOverlay mll;
    public MyOverlays itemizedoverlay;
    public int lat,lot;
    public Location location;
	@Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.main);   
       locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
       if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
       {
    	  Toast.makeText(getApplicationContext(),"switch on GPS",Toast.LENGTH_SHORT).show();
       }      
        startGPS();           
        mv=(MapView)findViewById(R.id.m_view); 
        mv.setBuiltInZoomControls(true);
		mv.setSatellite(true);
		//mv.setStreetView(true);
		mpc=mv.getController();
		mpc.setZoom(18);
		mll=new MyLocationOverlay(this,mv);
		mv.getOverlays().add(mll);	
		
        tv=(TextView)findViewById(R.id.m_text);
        t=(TextView)findViewById(R.id.my_text);
        tt=(TextView)findViewById(R.id.myy_text);
        ttt=(TextView)findViewById(R.id.mi_text); 
        b=(Button)findViewById(R.id.my_button);
    b.setOnClickListener(new OnClickListener()
    {
	public void onClick(View v)
	{		
				
		try{
			sw=et.getText().toString();
	    	if(sw.equals(""))
				return;
	    	}catch(Exception e){
	    		Toast.makeText(getApplicationContext(),"enter the file name first",Toast.LENGTH_SHORT).show();
	    	}
	    	showCurrentLocation();   	
	    	showloc();
	    	 File directory=new File("/sdcard/");
		directory.mkdirs();
		myFile=new File(directory,sw+".txt");
		
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(myFile,true));
			   bw.write(spd+",");
			   bw.write(text+",");
			   bw.write(lotude+",");
    	       bw.write(latude+",");
    	       bw.write(alt+",");
    	       bw.write(sate);    	      	 
			   bw.newLine();
    	       bw.close();
    	  // Toast.makeText(getBaseContext(),
                 //  "saved",
                 //  Toast.LENGTH_SHORT).show();
            //bw.newLine();

		}catch (Exception e) 
        {
          	e.printStackTrace();
        }		
	}
	
	});
    
	}
	protected void startGPS()
	{
		locationManager.requestLocationUpdates(
	            LocationManager.GPS_PROVIDER,
	            MINIMUM_TIME_BETWEEN_UPDATES,
	            MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
	        new MyLocationListener()	       
	            );
		
	}
	
	protected void showCurrentLocation() {
		 
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);     
		
		 	if (location!= null) {
    	  long time=(location.getTime());
    	  Date now=new Date(time);
    	  SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	    text = sdf.format(now);   	 
    	  countsate();
    	  
    	  alt=String.valueOf(location.getAltitude());
    	  lotude=String.valueOf(location.getLongitude());
    	  tv. setText("LONGITUDE"+"\t\t"+":"+"\t"+lotude);
    	  latude=String.valueOf(location.getLatitude());
    	  t.setText("LATITUDE"+"\t\t"+":"+"\t"+latude);
    	  spd=String.valueOf(location.getSpeed());
    	  tt.setText("SPEED"+"\t\t\t\t"+":"+"\t"+spd);
    	  sate=String.valueOf(Satellites);
    	  ttt.setText("SATELLITES"+"\t\t"+":"+"\t"+sate);
    	/*  message = String.format(
	                    "%1$s,%2$s ,%3$s,%4$s,%5$s,%6$s",
	                   location.getSpeed(),text, location.getLatitude(),location.getLongitude(),location.getAltitude(),Satellites//,n
	            );
	            Toast.makeText(mainGPSproj.this, message,
	                    Toast.LENGTH_LONG).show();*/
	        }
		
	}
	
public void countsate(){
	GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener()
	{
		public void onGpsStatusChanged(int event) {    	        
	        if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS || event == GpsStatus.GPS_EVENT_FIRST_FIX) {
	            GpsStatus status = locationManager.getGpsStatus(null);
	            Iterable<GpsSatellite> sats = status.getSatellites();
	            // Check number of satellites in list to determine fix state
	            Satellites=0;
	            for (GpsSatellite sat:sats) {
	              if(sat.usedInFix())
	                Satellites++;
	            }    	          
	        }
	    }
	};
	locationManager.addGpsStatusListener(gpsStatusListener);
}
public void showloc()
{
	mll.runOnFirstFix(new Runnable(){
		public void run(){
		mv.getController().animateTo(mll.getMyLocation());
		}
	});
	Drawable dr = this.getResources().getDrawable(R.drawable.icon);
	itemizedoverlay = new MyOverlays(this, dr);
	createMarker();	
	
}
public boolean onCreateOptionsMenu(Menu menu)
{
	MenuInflater inflater=getMenuInflater();
	inflater.inflate(R.menu.mainmenu, menu);
    return true;
}
public boolean onOptionsItemSelected(MenuItem item)
{
	switch(item.getItemId())
	{
	case R.id.s_item:
		          return true;
	case R.id.e_item:this.finish();
	         return true;
	case R.id.f_item: 
	{
		AlertDialog.Builder ab=new AlertDialog.Builder(this);
		//EditText ip=new EditText(this);
		et=new EditText(this);
		ab.setView(et);
		ab.setTitle("FILENAME");
		ab.setIcon(R.drawable.ne);		
		ab.setNeutralButton("OK",new DialogInterface.OnClickListener(){
			 public void onClick(DialogInterface arg0, int arg1) {        
		}
	
		});
		ab.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {			
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});		
	ab.show();
	}
	return true;
	case R.id.se_item:		
 	   startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));	
		return true;	
	default:return super.onOptionsItemSelected(item);       
	}	
}
protected boolean isRouteDisplayed() {	
	return false;
}	
private class MyLocationListener implements LocationListener {	 
    public void onLocationChanged(Location location) {    	
    	long time=location.getTime();
    	  Date now=new Date(time);
    	  SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	  text = sdf.format(now);
    	  countsate();    	
    	 
    	  alt=String.valueOf(location.getAltitude());
    	  lotude=String.valueOf(location.getLongitude());    	
    	  tv. setText("LONGITUDE"+"\t\t"+":"+"\t"+lotude);
    	  latude=String.valueOf(location.getLatitude());
    	  t.setText("LATITUDE"+"\t\t"+":"+"\t"+latude);
    	  spd=String.valueOf(location.getSpeed());    	  	
	   	  tt.setText("SPEED"+"\t\t\t\t"+":"+"\t"+spd);
	   	  sate=String.valueOf(Satellites);
	   	  ttt.setText("SATELLITES"+"\t\t"+":"+"\t"+sate);
            /* message = String.format(
                    "%1$s,%2$s,%3$s,%4$s,%5$s,%6$S",
               location.getSpeed(),text , location.getLatitude(),location.getLongitude(),location.getAltitude(),Satellites//,nm//
            );
            Toast.makeText(mainGPSproj.this, message, Toast.LENGTH_SHORT).show();*/
            try
    		{
    			BufferedWriter bw = new BufferedWriter(new FileWriter(myFile,true));
    		   bw.write(spd+",");
    		   bw.write(text+",");
    		   bw.write(lotude+",");
        	   bw.write(latude+",");
        	   bw.write(alt+",");
        	   bw.write(sate);
        	   bw.newLine();
        	   bw.close();
        	  
    		}catch (Exception e) 
            {
              	e.printStackTrace();
            }     	
    		 
      	  lat=(int)(location.getLatitude()*1E6);
    		lot=(int)(location.getLongitude()*1E6);
    		GeoPoint point=new GeoPoint(lot,lat);    		
    		createMarker();
    		mpc.animateTo(point);
    }
    
        public void onStatusChanged(String s, int i, Bundle b) {
          
        }
       

    public void onProviderDisabled(String s) {
          
        }
 
        public void onProviderEnabled(String s) {
       
        }      
     }
private void createMarker(){	
	GeoPoint p=mv.getMapCenter();
	OverlayItem oli=new OverlayItem(p,"","");
	itemizedoverlay.addOverlay(oli);
	if(itemizedoverlay.size()>0){
		mv.getOverlays().add(itemizedoverlay);		
	}
}
@Override
protected void onResume() {
	super.onResume();
	mll.enableMyLocation();
	mll.enableCompass();
}
@Override
protected void onPause() {
	super.onPause();
	mll.disableMyLocation();
	mll.disableCompass();
 }
}
	
	
	
