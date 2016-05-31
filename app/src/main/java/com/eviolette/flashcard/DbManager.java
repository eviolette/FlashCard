package com.eviolette.flashcard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by eviolette on 5/19/15.
 */
public class DbManager extends SQLiteOpenHelper {
    public DbManager(Context context) {
        super(context, "flashcard", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table card (answer text, question text, deck_name text)");
        db.execSQL("create table deck (name text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
