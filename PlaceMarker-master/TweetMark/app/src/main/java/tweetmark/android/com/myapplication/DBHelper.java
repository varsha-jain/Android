package tweetmark.android.com.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by varsha on 7/28/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "markPlaces";
    // table name
    private static final String TABLE_PLACES = "Place";
    private static final String TABLE_USERS = "User";

    //User Table Column names
    private static final String KEY_USERID = "user_id";
    private static final String KEY_EMAIL_ID = "emailID";
    private static final String KEY_PASSWORD = "password";

    //Place Table Column names
    private static final String KEY_PLACEID = "place_id";
    private static final String KEY_PLACE_USERID = "user_id";
    private static final String KEY_PLACENAME = "placeName";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LON = "lon";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "(" + KEY_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_EMAIL_ID + " TEXT," + KEY_PASSWORD + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);

        String CREATE_PLACES_TABLE = "CREATE TABLE " + TABLE_PLACES + "("
                + KEY_PLACEID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PLACENAME + " TEXT,"
                + KEY_LAT + " TEXT," + KEY_LON + " TEXT,"  + KEY_PLACE_USERID + " INTEGER,"
                + " FOREIGN KEY(user_id) REFERENCES " + TABLE_USERS + "(" + KEY_USERID + ")" + ")";
        sqLiteDatabase.execSQL(CREATE_PLACES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_USERS);
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_PLACES);
        // Creating tables again
        onCreate(sqLiteDatabase);
    }

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL_ID, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public void addPlace(Place place){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PLACENAME, place.getPlaceName());
        values.put(KEY_LAT, place.getLat());
        values.put(KEY_LON, place.getLon());
        values.put(KEY_PLACE_USERID, place.getUserID());
        db.insert(TABLE_PLACES, null, values);
        db.close();
    }
    //to be called during login
    public String getUser(String emailID, String password){
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String userFirstName = "";
        try{
            String query = "SELECT * FROM "+ TABLE_USERS + " WHERE "+ KEY_EMAIL_ID  + " = " + "'" +
                    emailID + "'" + " and " + KEY_PASSWORD + " = " + "'" + password + "'" ;
            cursor = db.rawQuery(query, null);
            if(cursor.getCount()>0) {
                cursor.moveToFirst();
                System.out.println("****INSIDE DBHELPER CLASS GETUSER METHOD*****");
                userFirstName = cursor.getString(1);
            }
            return userFirstName;
        }finally{
            cursor.close();
        }

    }

    public List<Place> getAllPlaces(int userID) {
        List<Place> itemList = new ArrayList<Place>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PLACES +" where "  + KEY_PLACE_USERID + " = " + userID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Place place = new Place();
                place.setPlaceID(Integer.parseInt(cursor.getString(0)));
                place.setPlaceName(cursor.getString(1));
                place.setLat(cursor.getString(2));
                place.setLon(cursor.getString(3));
                place.setUserID(Integer.parseInt(cursor.getString(4)));

            // Adding contact to list
                itemList.add(place);
            } while (cursor.moveToNext());
        }

            // return contact list
        return itemList;
    }

    public int getUserID(String emailID){
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        int userID = 0;
        try{
            String query = "SELECT * FROM "+ TABLE_USERS + " WHERE "+ KEY_EMAIL_ID  + " = " + "'" +
                    emailID + "'";
            cursor = db.rawQuery(query, null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                userID = Integer.parseInt(cursor.getString(0));
            }
            return userID;
        }finally{
            cursor.close();
        }

    }
}
