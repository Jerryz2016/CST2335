package com.jieli.lab1;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;

/**
 *  Lab7 Created by Jerry on 19-Mar-17.
 */

public class MessageFragment extends Fragment {
    Context parent;
    int id;
    String message;
    boolean isTablet;


    //no matter how you got here, the data is in the getArguments
    @Override
    public void onCreate(Bundle b)
    {
        super.onCreate(b);
        Bundle bundle = getArguments();
        id = (int)bundle.getLong("ID");
        message = bundle.getString("Message") ;
        isTablet = bundle.getBoolean("isTablet");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parent = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View gui = inflater.inflate(R.layout.fragment_layout, null);

        TextView textViewMessage = (TextView)gui.findViewById(R.id.tv_message);
        textViewMessage.setText("You clicked message: "+ message);
        TextView textViewID = (TextView)gui.findViewById(R.id.tv_IDnumber);
        textViewID.setText("You clicked on ID: " + id);

        Button delButton = (Button) gui.findViewById(R.id.delete_message);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTablet) {
                    Log.i("MessageFragDel",Integer.toString(id));
                    ((ChatWindow) getActivity()).deleteMessage(id);
                } else {
                    Intent data = new Intent();
                    data.putExtra("Delete", id);
                    data.putExtra("Message",message);
                    getActivity().setResult(RESULT_OK, data);
                    getActivity().finish();
                }
            }
        });

        return gui;
    }

    public MessageFragment(){
        super();
    }

}
