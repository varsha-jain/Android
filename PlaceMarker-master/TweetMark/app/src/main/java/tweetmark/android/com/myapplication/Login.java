package tweetmark.android.com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
    }

    public void login(View view){
        DBHelper db = new DBHelper(this);
        EditText et = (EditText)findViewById(R.id.editText4);
        String userName = et.getText().toString();
        EditText etp = (EditText)findViewById(R.id.editText6);
        String password = etp.getText().toString();
        if(db.getUser(userName, password).trim().equals("")){
            Toast.makeText(this,"User name doesn't exist!!!", Toast.LENGTH_LONG).show();
        }else {
            User user = new User();
            user.setEmail(userName);
            user.setPassword(password);
            db.addUser(user);
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("email", userName);
            editor.commit();
            //Toast.makeText(this, "You have registered successfully!!!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        }
    }
}
