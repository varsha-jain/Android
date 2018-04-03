package com.example.acronyms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity 
{
	//String ff="";
	public String sf="",ff="";
	SQLiteDatabase db;
	String a[];
	int pos;
	ListView lv;
	Button btn;
	Button btn1;
	ArrayAdapter <String> ad;
	public String sht="shortform";
	public String fft="fullform";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent i=getIntent();
        i.getStringExtra("new1");
		db=openOrCreateDatabase("Mydata",SQLiteDatabase.CREATE_IF_NECESSARY,null);
        db.execSQL("Create table if not exists Database(shortform varchar,fullform varchar)");
        a=getAllData(); //method gets all the data present in the database
        Log.i("Count", a.length+"");
         
        if(a.length<1)
        {
        	Initailaize();
        }
        listCall(); // shows data in list view
        btn=(Button)findViewById(R.id.button1);
       btn1=(Button)findViewById(R.id.button2);
        registerForContextMenu(lv); //Necessary for contextMenu i.e long click on list** 
      
        }
	
	public void listCall() 
	{
		lv=(ListView)findViewById(R.id.listView1);                
    	ad=new ArrayAdapter<String>(this,R.layout.text,R.id.textView2,a); //Setting Adapter for ListView
    	lv.setAdapter(ad);
    	
    	//Setting listener on List
    	lv.setOnItemClickListener(new OnItemClickListener() {

			
			public void onItemClick(AdapterView<?> parent, View arg1, int position,
					long id) 
			{
				// TODO Auto-generated method stub
				String sf=a[position];
				String ff="";
				String query1="Select fullform from database where shortform ='"+sf+"'";
				Cursor c=db.rawQuery(query1, null);
				if(c.moveToNext())
				{
					ff=c.getString(0);
				}			
				Toast.makeText(getApplicationContext(),"Fullform of"+"  "+sf+"  "+"is"+"  "+ff, Toast.LENGTH_LONG).show();
				
			}
		});
    	lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long id) 
			{
				// TODO Auto-generated method stub
				pos=position;
				return false;
			}
		});
    	
		
	}
//Deafult values
	public void Initailaize() 
	{
		// TODO Auto-generated method stub
		String str="";
		   str="Insert into database values('COBOL','Common Business Oriented Language')";
		   db.execSQL(str);
		   str="Insert into database values('ADSL','Assymetric Digital Subscriber Line')";
		   db.execSQL(str);
		   str="Insert into database values('EOF','End Of File')";
		   db.execSQL(str);
		   str="Insert into database values('LIFO','Last in,First Out')";
		   db.execSQL(str);
		   str="Insert into database values('DNS','Domain Name Service')";
		   db.execSQL(str);
		   str="Insert into database values('TY','Thank You')";
		   db.execSQL(str);
	}

	private String[] getAllData() 
	{
		String query1="Select shortform from database Order By shortform";
		Cursor c=db.rawQuery(query1, null);
		int i=0;
		String a[]=new String[c.getCount()];
		while(c.moveToNext())
		{
			a[i]=c.getString(0);
			i=i+1;
		}
		return a;
	}
 //Setting up contextMenu
	@Override
	public void onCreateContextMenu(ContextMenu menu,View view,ContextMenuInfo menuinfo)
	{
		super.onCreateContextMenu(menu, view, menuinfo);
		menu.setHeaderTitle("Menu");
		
		menu.add(0, view.getId(),0,"Edit");
		menu.add(0, view.getId(),0,"Delete");
		
	}
	
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		if(item.getTitle()=="Edit")
			Edit(item.getItemId());
		
		else if(item.getTitle()=="Delete")
			Delete(item.getItemId());
		
	
		return false;
		
		
	}

	public void New(View v) 
	{
		// Forming DialogBox for Addition
		 AlertDialog.Builder x =new AlertDialog.Builder(this);
		   x.setTitle("Add");
		   LinearLayout ll=new LinearLayout(this);
		   ll.setOrientation(LinearLayout.VERTICAL);
		   TextView tv=new TextView(this);
		   tv.setText("Enter Short-Form");
		   //tv.setId(0);
	       ll.addView(tv);
	       final EditText et=new EditText(this);
	       //et.setId(0);
	       ll.addView(et);
	       
	    	   final TextView tv1=new TextView(this);
	       tv1.setText("Enter Full-Form");
	       //tv1.setId(1);
	      ll.addView(tv1);
	       final EditText et1=new EditText(this);
	       //et1.setId(1);
	       ll.addView(et1);
	      x.setView(ll); //Setting Linear Layout to the Dialog Box
	     
			
		       
	      x.setPositiveButton("OK",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				

				sf=et.getText().toString().trim();
				ff=et1.getText().toString().trim();
				if(sf.equals("") || ff.equals(""))
			       {
			    	   Toast.makeText(getBaseContext(), "Please enter valid short form and fullforrm",Toast.LENGTH_SHORT).show();
			       //return;
			       }else
			       {
				Log.i("Short", sf);
				Log.i("Full Form", ff);
				String query="Insert into Database values('"+sf+"','"+ff+"')";
				db.execSQL(query);
				Toast.makeText(getBaseContext(), "Congratulations Your List Is Now More Rich",Toast.LENGTH_LONG).show();
				finish();
				startActivity(getIntent());
			       }
			}
		});
		       
	      x.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
				
			}
		});
	       x.show();

		
		
	}

	public void Edit(int id)
	{
		Toast.makeText(getBaseContext(),a[pos], Toast.LENGTH_SHORT).show();
		final String sf=a[pos];
		//*-********************************
		
		AlertDialog.Builder x =new AlertDialog.Builder(this);
		   x.setTitle("Update");
		   LinearLayout ll=new LinearLayout(this);
		   ll.setOrientation(LinearLayout.VERTICAL);
		   TextView tv=new TextView(this);
		   tv.setText("Short-Form Which is to be Edited");
		   tv.setId(0);
	       ll.addView(tv);
	       TextView tv1=new TextView(this);
		   tv1.setText(sf);
	       
	       final TextView tv2=new TextView(this);
	       tv2.setText("Enter new Full-Form");
	       tv2.setId(1);
	      ll.addView(tv1);
	       final EditText et1=new EditText(this);
	       et1.setId(1);
	       ll.addView(et1);
	      x.setView(ll);
	      
	      x.setPositiveButton("OK",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				String ff="";
				ff=et1.getText().toString().trim();
				
				String query1="Select * from database where shortform='"+sf+"'";
				Cursor c=db.rawQuery(query1, null);
				int i=0;
				if(c.moveToNext())
				{
					i=i+1;
				}
				
				if(i>=1)
				{
				String query="Update database set fullform='"+ff+"' where shortform='"+sf+"'";
				db.execSQL(query);
				Toast.makeText(getBaseContext(), "Record Updated",Toast.LENGTH_LONG).show();
				}
				else
				{
				Toast.makeText(getBaseContext(), "Record Not Found", Toast.LENGTH_SHORT).show();	
				}
				
			}
		});
	      
	      x.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
				
			}
		});
	       x.show();
	 
		
		
		
		
	}
	
	public void Delete(int id)

	{
	
		String query="";
		
		 AlertDialog.Builder x =new AlertDialog.Builder(this);
		   x.setTitle("Delete");
		   LinearLayout ll=new LinearLayout(this);
		   ll.setOrientation(LinearLayout.VERTICAL);
		   TextView tv=new TextView(this);
		   tv.setText("Delete "+a[pos]+"?");
		   ll.addView(tv);
	       x.setView(ll);
	      
	       x.setPositiveButton("Delete",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
			Log.i("My position is", pos+"");
			String txt="";
			txt=a[pos];
			Log.i("My name is :", txt);
			String query1="Select * from database where shortform='"+txt+"'";
			Cursor c=db.rawQuery(query1, null);
			int i=0;
			i=c.getCount();
			Log.i("Value of i", i+"");
			if(i>=1)
			{
			String query="delete from database where shortform='"+txt+"'";
			db.execSQL(query);
			Toast.makeText(getBaseContext(),txt+" deleted",Toast.LENGTH_SHORT).show();
			finish();
			startActivity(getIntent());
			}
			else
			{
				Toast.makeText(getBaseContext(),"Record Not Found",Toast.LENGTH_SHORT).show();
			}
			}
		});
	       
	       x.setNegativeButton("Cancel",new DialogInterface.OnClickListener() 
	       {
	   		
	   		@Override
	   		public void onClick(DialogInterface dialog, int which) {
	   			return;
	   			
	   		}
	   	});
	       
		x.show();
	}
	
	public void Search(View v)
	{
	EditText et=(EditText)findViewById(R.id.editText1);
	String str=et.getText().toString().trim();
	String a[];
	
	
	String query1="Select * from database where shortform like '%"+str+"%'";
	Cursor c1=db.rawQuery(query1, null);
	a=new String[c1.getCount()];
	int i=0;
	while(c1.moveToNext())
	{	
		a[i]=c1.getString(0);
		i++;
	}
	if(c1.getCount()>=1)
	{
Intent in=new Intent(this,Search.class);
in.putExtra("Search", a);
startActivity(in);
	}
	else
	{
		Toast.makeText(getBaseContext(),"Record Not Found",Toast.LENGTH_SHORT).show();
		
		
	}
	
	}
	
	
	//Just see this too..not Working ME Here
	 public boolean onCreateOptionsMenu(Menu menu) 
	 {
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.activity_main, menu);
	      return true;
	    }
	
	 public boolean onOptionsItemSelected(MenuItem item) {
	      switch (item.getItemId()) 
	      {
	      case R.id.help:
	         //Provide Intent to Help Activity Here!!!!!  
	    	  Intent in=new Intent(this,Help.class);
	    	  in.putExtra("new","hey");
	    	  startActivity(in);
	            return true;
	     
	      case R.id.about:
	    	  Intent inr=new Intent(this,about.class);
	    	  inr.putExtra("new1","hey");
	    	  startActivity(inr);
		            return true;
	    
	      default:
	            return super.onOptionsItemSelected(item);
	      }
	}
}
	

