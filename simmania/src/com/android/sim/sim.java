package com.android.sim;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.Toast;

public class sim extends Service {
  public  TelephonyManager tm,tnew;
  public String se;
  public String snew,sf,st;
  public String s;
  public File f; 
  public int b;
  public String str[];
  private final IBinder mbinder=new LocalBinder();
  public StringBuffer sb;
 public LocationManager lm;
 public Location l;
 public double lat,lot;
 public int cellid,lac;
 public GsmCellLocation loc;
 public int lati,longi;
 public String sg,sgr;
 public int isc;
 public boolean val;
	@Override
	public IBinder onBind(Intent arg0) {	
		return mbinder;
	}
	public class LocalBinder extends Binder{
	 sim getService() {
            return sim.this;
	}
	}
	 @Override
	    public void onCreate() {
	        super.onCreate();        
	      show();
	     
	  }
public void onDestroy()
{
	super.onDestroy();
}
public void onStart(Intent intent,int startId){
	super.onStart(intent, startId);	  
	
}
public void show(){	
		try{
			wait(1000);
		}catch(Exception e){
			e.printStackTrace();
		}
	tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE );
		se=tm.getSimSerialNumber();	
	//Toast.makeText(this, se, Toast.LENGTH_LONG).show();
	save();
	
	compare();
		}
public void save(){	
	try{		
		//filename="SIM.txt";
		// FileWriter fw = new FileWriter(f+"/SIM.txt");			
		FileOutputStream fos=openFileOutput("SIM",Context.MODE_APPEND);
		BufferedReader br=new BufferedReader(new InputStreamReader(openFileInput("SIM")));
		//FileInputStream fin=openFileInput(filename);
		//b=br.read();	
		s=br.readLine();
		sb=new StringBuffer();
		
		//while( ()!=null){
		//	sb.append(s+"\n");
		//}
		//Toast.makeText(this,"see",Toast.LENGTH_LONG).show();
		// Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
		 if((s==null))
		  {
			 fos.write(se.getBytes());
		 }
		  fos.close();
		 //br.close();
	}catch(Exception e){
		e.printStackTrace();
	}
}
/*public void read(){
	try{
 	//File mf = new File(f+"SIM.txt");
    // FileInputStream fIn = new FileInputStream(mf);
       BufferedReader myReader = new BufferedReader(new InputStreamReader(openFileInput("SIM")));
        //String aDataRow = "";
    // s = "";       
       // while ((aDataRow = myReader.readLine()) != null) {
       //   s += aDataRow + "\n";
       sb=new StringBuffer();
		while( (s=myReader.readLine())!=null){
			sb.append(s);
		}
      //  s=myReader.readLine();          
      // myReader.close();  
		// Toast.makeText(this,"read",Toast.LENGTH_SHORT).show();
		// Toast.makeText(this,sb,Toast.LENGTH_LONG).show();
		// br.close();	
	}
			
    catch (Exception e) 
    {      
    e.printStackTrace();
    }
}*/
   
public void compare(){
	
	try{
		BufferedReader br=new BufferedReader(new InputStreamReader(openFileInput("SIM")));
		//s=br.readLine();
		 sb=new StringBuffer();
			while( (s=br.readLine())!=null){
				sb.append(s);
			}
		// br.close();
		/*File myFile = new File(f+"SIM.txt");
        FileInputStream fIn = new FileInputStream(myFile);
        BufferedReader myReader = new BufferedReader(
                new InputStreamReader(fIn));
        String aDataRow = "";
     // s = "";
       
        while ((aDataRow = myReader.readLine()) != null) {
          s += aDataRow + "\n";
          
        }     */
     
     //  br.close();     
    } catch (Exception exc) 
    {      
    exc.printStackTrace();
    }       
    if(se.equals(sb.toString()))
	{    	
    //	Toast.makeText(getApplicationContext(),"perform",Toast.LENGTH_SHORT).show();
    	/*String message = String.format(
               "Current Location \n Longitude: %1$s \n Latitude: %2$s",
               lot, lat);
    	Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();*/
    	return;
        	//Toast.makeText(getApplicationContext(),er,Toast.LENGTH_SHORT).show(); 
    		}
	
   
	else
	{
		loc=(GsmCellLocation)tm.getCellLocation();
    	cellid=loc.getCid();
    	lac=loc.getLac();
    	String e=String.valueOf(cellid);
    	String er=String.valueOf(lac);
    	Toast.makeText(getApplicationContext(),e,Toast.LENGTH_SHORT).show();
    	Toast.makeText(getApplicationContext(),er,Toast.LENGTH_SHORT).show();
    	if(RqsLocation(cellid, lac))
    	{
        sg=String.valueOf((float)lati/1000000) ;           
    	sgr= String.valueOf((float)longi/1000000);  
    	//Toast.makeText(getApplicationContext(),sg,Toast.LENGTH_LONG).show();
    	//Toast.makeText(getApplicationContext(),sgr,Toast.LENGTH_LONG).show();
    	}
    	else
    	{       
    		//Toast.makeText(getApplicationContext(),"no location",Toast.LENGTH_SHORT).show();
    	}
		str=new String[100];
		/*Intent in=new Intent(this,sendsms.class);
		//Bundle b=new Bundle();
		//b.putString("key",se);
		//in.putExtras(b);
		in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(in);*/
	//	BufferedReader br=new BufferedReader(new FileReader("/sdcard/Test.txt"));
		
		int i=0;
		try{
			
			BufferedReader br=new BufferedReader(new FileReader("/sdcard/Test.txt"));
			st=br.readLine();
			//str[0]=st;
		//	for(int i=0;i<st;i++){
			while((st)!=null){
				str[i++]=st;
				st=br.readLine();
			}
		isc=tm.getSimState();
		if(isc==TelephonyManager.SIM_STATE_READY){
			netavail();
		if(val==true){	
			
		for(int j=0;j<str.length;j++){
	SmsManager sms=SmsManager.getDefault();
	sms.sendTextMessage(str[j],null,"PHONE FOUND"+"\n"+"SIM NO :"+se+"\n"+"LATITUDE:"+sg+"\n"+"LONGITUDE:"+sgr,null,null);
	}
		}else{
			wait(3000);
			for(int j=0;j<str.length;j++){
				SmsManager sms=SmsManager.getDefault();
				sms.sendTextMessage(str[j],null,"PHONE FOUND"+"\n"+"SIM NO :"+se+"\n"+"LATITUDE:"+sg+"\n"+"LONGITUDE:"+sgr,null,null);	
		}
		}
		}
		br.close();
		}catch(Exception exc){
		exc.printStackTrace();
	}	
}
}
private Boolean RqsLocation(int cid, int lac){      
	Boolean result = false;   
	String urlmmap = "http://www.google.com/glm/mmap";       
	try {       URL url = new URL(urlmmap);         
	URLConnection conn = url.openConnection();       
	HttpURLConnection httpConn = (HttpURLConnection) conn;               
	httpConn.setRequestMethod("POST");         
	httpConn.setDoOutput(true);     
	httpConn.setDoInput(true);  
	httpConn.connect();   
	OutputStream outputStream = httpConn.getOutputStream();       
	WriteData(outputStream, cid, lac);               
	InputStream inputStream = httpConn.getInputStream();       
	DataInputStream dataInputStream = new DataInputStream(inputStream);             
	dataInputStream.readShort();        
	dataInputStream.readByte();       
	int code = dataInputStream.readInt();        
	if (code == 0) {         
		lati= dataInputStream.readInt();         
		longi= dataInputStream.readInt();                      
		result = true;                   
		} } catch (Exception e) { 
		
			e.printStackTrace(); } 
		return result;   
		}
private void WriteData(OutputStream out, int cid, int lac)  throws IOException  {          
	DataOutputStream dataOutputStream = new DataOutputStream(out);     
	dataOutputStream.writeShort(21);      
	dataOutputStream.writeLong(0);     
	dataOutputStream.writeUTF("en");     
	dataOutputStream.writeUTF("Android");     
	dataOutputStream.writeUTF("1.0");      
	dataOutputStream.writeUTF("Web");      
	dataOutputStream.writeByte(27);      
	dataOutputStream.writeInt(0);      
	dataOutputStream.writeInt(0);      
	dataOutputStream.writeInt(3);      
	dataOutputStream.writeUTF("");      
	dataOutputStream.writeInt(cid);     
	dataOutputStream.writeInt(lac);        
	dataOutputStream.writeInt(0);     
	dataOutputStream.writeInt(0);     
	dataOutputStream.writeInt(0);     
	dataOutputStream.writeInt(0);     
	dataOutputStream.flush();      
	}
public boolean netavail()
{
 val=false;
	ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo  ni=cm.getActiveNetworkInfo();
	if((ni!=null&&ni.isAvailable())||(ni!=null&&ni.isConnected())){
		val=true;
	}
	return val;
}
}