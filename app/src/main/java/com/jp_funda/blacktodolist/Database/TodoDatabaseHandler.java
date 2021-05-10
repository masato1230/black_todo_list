package com.jp_funda.blacktodolist.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.jp_funda.blacktodolist.Constants.TodoConstants;

public class TodoDatabaseHandler extends SQLiteOpenHelper {
    private Context context;

    public TodoDatabaseHandler(@Nullable Context context) {
        super(context, TodoConstants.DB_NAME, null, TodoConstants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
