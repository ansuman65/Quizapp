package com.example.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AuthHelper {

    private final SQLiteDatabase db;

    public AuthHelper(Context context) {
        DBHelper helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public boolean registerUser(String username, String password) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        long result = db.insert("users", null, values);
        return result != -1;
    }

    public boolean loginUser(String username, String password) {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE username = ? AND password = ?",
                new String[]{username, password});
        boolean loggedIn = cursor.getCount() > 0;
        cursor.close();
        return loggedIn;
    }
}
