package com.jieli.lab1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    protected ListView list;
    protected EditText text;
    protected Button send;
    final  ArrayList<String> mess = new ArrayList<>();
    protected ChatDatabaseHelper dbHelper;
    protected SQLiteDatabase db;
    protected ChatAdapter messageAdapter;
    private Cursor cursor;
    private Boolean isTablet;  // for to check if a phone or tablet
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

       dbHelper = new ChatDatabaseHelper(this);
       db = dbHelper.getWritableDatabase();

        list = (ListView) findViewById(R.id.listView);
        text = (EditText) findViewById(R.id.editText);
        send = (Button) findViewById(R.id.sendButton);
        messageAdapter = new ChatAdapter(this);
        list.setAdapter(messageAdapter);

        cursor = db.query(false, ChatDatabaseHelper.TABLE_NAME,
                new String[] { ChatDatabaseHelper.KEY_MESSAGE, ChatDatabaseHelper.KEY_ID},
                "Message not null", null,null,null,null,null);
       if (cursor != null) {
           int rows = cursor.getCount();
           cursor.moveToFirst();
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
                    // new message added into db, update the cursor for fragement
                    cursor = db.query(false, ChatDatabaseHelper.TABLE_NAME,
                            new String[] { ChatDatabaseHelper.KEY_MESSAGE, ChatDatabaseHelper.KEY_ID},
                            "Message not null", null,null,null,null,null);
                }
                else{
                    CharSequence warning = "Blank message cannot be sent!";
                    Toast toast = Toast.makeText(ChatWindow.this,warning,Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
         //lab7- click item of listview
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("ListView","onItemClick: " + position + " " + id);

                Bundle bundle = new Bundle();
                bundle.putLong("ID", id );//id is the database ID of selected item
                String clickedMessage = (String) (list.getItemAtPosition(position));// get the message of the clicked item
                bundle.putString("Message",clickedMessage); //

                //lab7-step 2, if is a tablet, insert fragment into FrameLayout, pass data
                if(isTablet) {
                    bundle.putBoolean("isTablet",true);
                    MessageFragment frag = new MessageFragment();
                    frag.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.framelayout1,frag).commit();
                }
                //lab7-step 3 if is a phone, transition to empty Activity that has FrameLayout
                else
                {
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtras(bundle); //pass the clicked item ID and message to next activity
                    startActivityForResult(intent,3); //go to MessageDetais activity and view message details
                }
            }
        });
        //Lab7 fragement
        //step 1, find out if you are on a phone or tablet.
        isTablet =(findViewById(R.id.framelayout1)!=null );
    }
    //lab7-delete clicked item from the listview
    public void deleteMessage(int id) {
      db.delete(ChatDatabaseHelper.TABLE_NAME, "_id=?",new String[]{Integer.toString(id)});
        // int del= db.delete(ChatDatabaseHelper.TABLE_NAME, "Message =?",new String[]{mess.get(id)});
        cursor = db.query(false, ChatDatabaseHelper.TABLE_NAME,
                new String[] { ChatDatabaseHelper.KEY_MESSAGE, ChatDatabaseHelper.KEY_ID},
                "Message not null", null,null,null,null,null);
        mess.clear();
        if (cursor != null) {
            int rows = cursor.getCount();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                mess.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                Log.i("ChatWinow ACTIVITY","SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                Log.i("ChatWinow ACTIVITY", "cursor's column count = "+cursor.getColumnCount());
                Log.i("ChatWinow ACTIVITY", "cursor's row number = " +cursor.getPosition());
                cursor.moveToNext();
            }
        }
        messageAdapter.notifyDataSetChanged();

  //      MessageFragment mf = (MessageFragment)getFragmentManager().findFragmentById(R.id.framelayout1);
 //       getFragmentManager().beginTransaction().remove(mf).commit();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            int  delID = (int)extras.get("Delete");
            deleteMessage(delID);
            /*
            String message = (String)extras.get("Message");
         //   db.delete(ChatDatabaseHelper.TABLE_NAME, "Message =?",new String[]{mess.get(id)});
           db.execSQL("delete from "+ChatDatabaseHelper.TABLE_NAME+" where Message =" + message);
         //   db.execSQL("delete from "+ChatDatabaseHelper.TABLE_NAME+" where Message ="+"abcd" );
         //   db.execSQL("delete from "+ChatDatabaseHelper.TABLE_NAME+" where Message ="+"bcde" );
            cursor = db.query(false, ChatDatabaseHelper.TABLE_NAME,
                    new String[] { ChatDatabaseHelper.KEY_MESSAGE, ChatDatabaseHelper.KEY_ID},
                    "Message not null", null,null,null,null,null);
            if (cursor != null) {
                int rows = cursor.getCount();
                cursor.moveToFirst();
            }
      //      int del=db.delete(ChatDatabaseHelper.TABLE_NAME, "_id=?",new String[]{ Integer.toString(delID)});
            Log.i("ChatW.onAResult.DelID: ", String.valueOf(delID));
           // mess.remove(delID);
            messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount(),getView()
            */
        }
    }

    private  class  ChatAdapter extends ArrayAdapter<String> {   //baseAdapter
        public ChatAdapter(Context ctx){
            super(ctx,0);
        }
        public int getCount(){              return mess.size();        }
        public String getItem(int position){
            return mess.get(position);
        }
        //
        public long getItemId(int position)
        {
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex( ChatDatabaseHelper.KEY_ID))       ;
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
