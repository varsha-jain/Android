package tweetmark.android.com.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button button;
    final Context context = this;
    private String result = "";
    private MarkerOptions option;
    private ArrayList<String> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i = getIntent();
        places = (ArrayList<String>)i.getSerializableExtra("array");
        //alertDialogBox();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String email = sharedpreferences.getString("email", "");
        option = new MarkerOptions();

        //get the current user
        //get the list of places asscoaited with the user
        DBHelper db = new DBHelper(this);
        int userID = db.getUserID(email);
        List<Place> places = new ArrayList<>();
        places = db.getAllPlaces(userID);
        for(Place p : places){
            //String placeName = p.getPlaceName();
            LatLng placeName = new LatLng(Double.parseDouble(p.getLat()), Double.parseDouble(p.getLon()));
            option.position(placeName);
            option.title("Marker");
            mMap.addMarker(option);
        }


        // Add a marker in Sydney and move the camera
       /* if (Geocoder.isPresent()) {
            try {
                Geocoder geocoder = new Geocoder(this);
                List<Address> addresses;
                for(String s : places){
                    addresses = geocoder.getFromLocationName(s, 1);
                    List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                    for(Address a : addresses){
                        if(a.hasLatitude() && a.hasLongitude()){
                            LatLng sydney = new LatLng(a.getLatitude(), a.getLongitude());
                            //ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                            ll.add(sydney);
                            //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            p.setLat(String.valueOf(a.getLatitude()));
                            p.setLon(String.valueOf(a.getLongitude()));
                            p.setPlaceName(s);


                            option.position(sydney);
                            option.title("Marker");
                            mMap.addMarker(option);
                        }
                    }
                }

                if (addresses.size() > 0) {
                    double latitude = addresses.get(0).getLatitude();
                    System.out.println(latitude);

                    double longitude = addresses.get(0).getLongitude();
                    System.out.println(longitude);

                    LatLng sydney = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                }
                //



            } catch (IOException e) {
                System.out.println("*****IN CATCH BLOCK****");
                e.printStackTrace();
            }
        } else {
            System.out.println("GEOCODER NOT PRESENT");
        }*/

    }

    public void alertDialogBox(){

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.layout, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text
                                        //result.setText(userInput.getText());
                                        result = userInput.getText().toString();
                                        System.out.println(result);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        System.out.println("***INSIDE CANCEL BUTTON BLOCK***");
                                    }

                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                System.out.println("***ABOVE SHOW DIALOG BOX STATEMENT***");
                alertDialog.show();

            }
        });
    }
}
