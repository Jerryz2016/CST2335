package com.jieli.lab1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.id.message;

public class ChatWindow extends AppCompatActivity {
    ListView list;
    EditText text;
    Button send;
    final  ArrayList<String> mess = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        list = (ListView) findViewById(R.id.listView);
        text = (EditText) findViewById(R.id.editText);
        send = (Button) findViewById(R.id.sendButton);
        final ChatAdapter messageAdapter = new ChatAdapter(this);
        list.setAdapter(messageAdapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (text.getText()!= null)
                mess.add(text.getText().toString());
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount(),getView()
                text.setText("");
            }
        });
    }

    private  class  ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx){
            super(ctx,0);
        }
        public int getCount(){
            return mess.size();
        }
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
}
