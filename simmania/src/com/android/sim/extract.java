package com.android.sim;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class extract extends Activity {
	public Button b;
	public EditText et;
	public String phoneno;
	public File fnew;
	public Intent intent;
public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	//handleintent(getIntent());
	b=(Button)findViewById(R.id.my_button);
	et=(EditText)findViewById(R.id.my_edit);
	b.setOnClickListener(new OnClickListener(){
		public void onClick(View v){
			phoneno=et.getText().toString();			
				try {				   		   
				    fnew=new File("/sdcard/Test.txt");
					//FileWriter fw = new FileWriter("/sdcard/Test.txt");	   
				    BufferedWriter bw = new BufferedWriter(new FileWriter(fnew,true));
				    bw.write(phoneno);
				    bw.newLine();
				    bw.close();
			}catch(Exception e){
				e.printStackTrace();
			}		 
		
		}
	});
	
}
public boolean onCreateOptionsMenu(Menu menu)
{
	MenuInflater inflater=getMenuInflater();
	inflater.inflate(R.menu.menu, menu);
    return true;
}
public boolean onOptionsItemSelected(MenuItem item)
{
	switch(item.getItemId())
	{
	case R.id.s_item:et.setText("");
		          return true;
	case R.id.e_item:this.finish();
	         return true;
	
	default:return super.onOptionsItemSelected(item);       
	}	
}
}
