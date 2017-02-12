package com.jieli.lab1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "StartActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        int i = R.mipmap.ic_launcher;

        Button iButton = (Button) findViewById(R.id.button);
        iButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                startActivityForResult(intent,5);}

        });
         Log.i(ACTIVITY_NAME, "In onCreate()");

        Button chatButton = (Button) findViewById(R.id.startChat);
        chatButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                               Log.i(ACTIVITY_NAME,"User clicked Start Chat");
          Intent intent = new Intent(getApplicationContext(), ChatWindow.class);
                                              startActivity(intent);
                                          }
                                      }
        );
    }
    protected void  onActivityResult(int requestCode, int resultCode, Intent data){
        Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
        String messagePassed="no message";
        if (requestCode == 5 && resultCode == Activity.RESULT_OK ){
             messagePassed = "ListItemsActivity passed:"+data.getStringExtra("Response");
        }
        Toast toast = Toast.makeText(StartActivity.this,messagePassed,Toast.LENGTH_LONG);
        toast.show();



    }
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
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
