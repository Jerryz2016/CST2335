package com.jieli.lab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.app.PendingIntent.getActivity;

public class LoginActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME ="LoginActivity";
    public static final String LOGIN = "com.jieli.lab3.LoginEmail";
    public static final String EMAIL = "Email";
//    public static final String PASSWORD = "Password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Log.i(ACTIVITY_NAME, "In onCreate()");

    }
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");

//========= To get saved/ retrieve data from LOGINDETAILS==============
        SharedPreferences sp = this.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        String emailS = sp.getString(EMAIL,"email@domain.com");
        Log.d(EMAIL +"is: ", emailS);
        EditText emailView = (EditText) findViewById(R.id.editText);
        emailView.setText(emailS);

        Button loginButton = (Button) findViewById(R.id.button2);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mEmailView = (EditText) findViewById(R.id.editText);
//                EditText mPasswordView = (EditText) findViewById(R.id.editText2);

                String email = mEmailView.getText().toString();
//                String password = mPasswordView.getText().toString();
                //======== To save data to preference file LOGINDETAILS===================
                SharedPreferences sp = getSharedPreferences(LOGIN ,Context.MODE_PRIVATE);
                sp.edit().putString(EMAIL,email).commit();

        Intent intent = new Intent(LoginActivity.this, StartActivity.class);
        startActivity(intent);
            }

        });
    }
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
