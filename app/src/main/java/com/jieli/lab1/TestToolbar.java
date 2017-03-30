package com.jieli.lab1;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.jieli.lab1.R.string.alert_title;


public class TestToolbar extends AppCompatActivity {
    Context ctx;
    private String message="You selected item 1";
    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Testing the Toolbar in a FloatingActionButton", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        ctx=this;

    }
    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi){     //item clicked

        switch (mi.getItemId()) {
            case R.id.action_one:             //option1 clicked
                Log.d("Toolbar", "Option1 selected");
                Snackbar.make(findViewById(R.id.toolbarLayout), message, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                break;

            case R.id.action_two:            //option2 clicked
                Log.d("Toolbar", "Option2 selected");
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle(alert_title);
// Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog ,do nothing
                    }
                });
// Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.action_three:                     //option3 clicked
                Log.d("Toolbar", "Option3 selected");
                AlertDialog.Builder builder3 = new AlertDialog.Builder(ctx);
                builder3.setTitle("New Message");
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_custom_layout,null);
                builder3.setView(view);
                text = (EditText) view.findViewById(R.id.custom_message);

// Add the buttons
                builder3.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog3, int id) {
                        // User clicked OK button
 //                       View iview = findViewById(R.id.custom_imageview);

                    if (text!=null)
                    message= text.getText().toString();
                    }
                });
                builder3.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog3, int id) {
                        // User cancelled the dialog ,do nothing
                    }
                });
// Create the AlertDialog
                AlertDialog dialog3 = builder3.create();
                dialog3.show();
                break;

            case R.id.action_four:
                Log.d("Toolbar", "About selected");
                Toast toast = Toast.makeText(this,"Version 1.0, by Jieli Zhang",Toast.LENGTH_LONG);
                toast.show();
                break;
        }
        return true;
    }

}
