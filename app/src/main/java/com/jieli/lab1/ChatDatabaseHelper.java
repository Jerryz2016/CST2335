package com.jieli.lab1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.R.attr.name;
import static android.provider.Contacts.SettingsColumns.KEY;

/**
 * Created by Jerry on 11-Feb-17.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    private static final int   VERSION_NUM = 1;
    public static final String  ACTIVITY_NAME="ChatDatabaseHelper";
    public static final String DATABASE_NAME ="Chats.db";
    public static final String TABLE_NAME = "TableMessage";
    public static final String KEY_ID ="_id";
    public static final String KEY_MESSAGE = "Message";

    //constructor
    public ChatDatabaseHelper(Context ctx){
        super(ctx,DATABASE_NAME,null,VERSION_NUM);
    }
    @Override
    public void  onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                           + KEY_MESSAGE +" TEXT)");

        Log.i(" "+ ACTIVITY_NAME, "Calling onCreate() ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate( db);

        Log.i(" " +ACTIVITY_NAME, "Calling OnUpgade(), oldVersion = " + oldVersion + ", newVersion = "+newVersion);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
