package tweetmark.android.com.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    public void login(View view){
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }

    public void createAccount(View view){

        Intent i = new Intent(this, CreateAccount.class);
        startActivity(i);

    }
}
