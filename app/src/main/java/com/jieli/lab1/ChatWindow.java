package com.jieli.lab1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.duration;
import static android.R.id.message;
import static com.jieli.lab1.ChatDatabaseHelper.DATABASE_NAME;
import static com.jieli.lab1.ChatDatabaseHelper.KEY_ID;
import static com.jieli.lab1.ChatDatabaseHelper.KEY_MESSAGE;
import static com.jieli.lab1.ChatDatabaseHelper.TABLE_NAME;

public class ChatWindow extends AppCompatActivity {
    ListView list;
    EditText text;
    Button send;
    final  ArrayList<String> mess = new ArrayList<>();
    protected ChatDatabaseHelper dbHelper;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        dbHelper = new ChatDatabaseHelper(this);
       final  SQLiteDatabase db = dbHelper.getWritableDatabase();

        list = (ListView) findViewById(R.id.listView);
        text = (EditText) findViewById(R.id.editText);
        send = (Button) findViewById(R.id.sendButton);
        final ChatAdapter messageAdapter = new ChatAdapter(this);
        list.setAdapter(messageAdapter);

        cursor = db.query(false, ChatDatabaseHelper.TABLE_NAME,
                new String[] { ChatDatabaseHelper.KEY_MESSAGE},
                "Message not null", null,null,null,null,null);
       if (cursor != null) {
           int rows = cursor.getCount();
           cursor.moveToFirst();
//           SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this, R.layout.activity_chat_window, cursor,
//                   new String[]{ChatDatabaseHelper.KEY_MESSAGE}, // from column
//                  new int[]{R.id.message_text}, 0);            // to layout ID
//           list.setAdapter(listAdapter);
       }

        while (!cursor.isAfterLast()){
            mess.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i("ChatWinow ACTIVITY","SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i("ChatWinow ACTIVITY", "cursor's column count = "+cursor.getColumnCount());
            Log.i("ChatWinow ACTIVITY", "cursor's row number = " +cursor.getPosition());
            cursor.moveToNext();

        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (text.getText()!= null && !text.getText().toString().equals("")) {
                    mess.add(text.getText().toString());
                    ContentValues newValue = new ContentValues();
                    newValue.put(ChatDatabaseHelper.KEY_MESSAGE, text.getText().toString());
                    db.insert(ChatDatabaseHelper.TABLE_NAME,null,newValue);
                    messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount(),getView()
                    text.setText("");
                }
                else{
                    CharSequence warning = "Blank message cannot be sent!";
                    Toast toast = Toast.makeText(ChatWindow.this,warning,Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

    private  class  ChatAdapter extends ArrayAdapter<String> {   //baseAdapter
        public ChatAdapter(Context ctx){
            super(ctx,0);
        }
        public int getCount(){              return mess.size();        }
        public String getItem(int position){
            return mess.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position %2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position));  //get the string at position

            return(result);
        }

    }
    public void onDestory(){
        if (cursor != null) cursor.close();
        if (dbHelper != null)  dbHelper.close();

        }
}
