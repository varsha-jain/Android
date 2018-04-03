package tweetmark.android.com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> places = new ArrayList<>();
    //private MarkerOptions option;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();
        String userName = i.getStringExtra("userName");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("email", userName);
        editor.commit();
        email = userName;

    }

    public void showOnMap(View view){

            Intent i = new Intent(this, MapsActivity.class);
            i.putExtra("array", places);
            startActivity(i);

    }

    public void addPlaces(View view){
        EditText et = (EditText)findViewById(R.id.editText);
        String name = et.getText().toString();
        places.add(name);
        et.setText("");
        findLocation(name);

    }

    public void findLocation(String name){
        Place p = new Place();
        DBHelper db = new DBHelper(this);

        int userID = db.getUserID(email);
        if (Geocoder.isPresent()) {
            try {
                Geocoder geocoder = new Geocoder(this);
                List<Address> addresses;
                //for(String s : places){
                    addresses = geocoder.getFromLocationName(name, 1);
                    List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                    for(Address a : addresses){
                        if(a.hasLatitude() && a.hasLongitude()){
                            LatLng sydney = new LatLng(a.getLatitude(), a.getLongitude());
                            ll.add(sydney);
                            p.setLat(String.valueOf(a.getLatitude()));
                            p.setLon(String.valueOf(a.getLongitude()));
                            p.setPlaceName(name);
                            p.setUserID(userID);
                            db.addPlace(p);

                        }
                    }
               // }

            } catch (IOException e) {
                System.out.println("*****IN CATCH BLOCK****");
                e.printStackTrace();
            }
        } else {
            System.out.println("GEOCODER NOT PRESENT");
        }
    }
}
