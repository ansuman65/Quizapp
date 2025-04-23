package com.example.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "QuizApp.db";
    public static final int DB_VERSION = 9;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users table
        db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE, " +
                "password TEXT, " +
                "name TEXT, " +
                "age INTEGER, " +
                "gender TEXT)");


        // Questions table
        db.execSQL("CREATE TABLE IF NOT EXISTS questions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "quiz_name TEXT, " +
                "question TEXT, " +
                "option1 TEXT, " +
                "option2 TEXT, " +
                "option3 TEXT, " +
                "option4 TEXT, " +
                "answer TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS scores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "quiz_name TEXT, " +
                "score INTEGER)");


        insertSampleQuestions(db); // Insert all quiz questions
    }
    public void saveScore(String username, String quizName, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("quiz_name", quizName);
        values.put("score", score);
        db.insert("scores", null, values);
    }
    // get distinct quiz names
    public Set<String> getAllQuizNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT quiz_name FROM scores", null);
        Set<String> quizNames = new HashSet<>();
        while (cursor.moveToNext()) {
            quizNames.add(cursor.getString(0));
        }
        cursor.close();
        return quizNames;
    }

    public String getGenderForUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT gender FROM users WHERE username = ?", new String[]{username});
        String gender = null;
        if (cursor.moveToFirst()) {
            gender = cursor.getString(0);
        }
        cursor.close();
        return gender;
    }

    // get top 5 scores for a quiz
    public List<String> getTopScoresForQuiz(String quizName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT username, score FROM scores WHERE quiz_name = ? ORDER BY score DESC LIMIT 5",
                new String[]{quizName}
        );

        List<String> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            result.add(cursor.getString(0) + " - " + cursor.getInt(1));
        }

        cursor.close();
        return result;
    }

    public int getQuizAttemptCount(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM scores WHERE username = ?", new String[]{username});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS questions");
        db.execSQL("DROP TABLE IF EXISTS scores"); // ← this was missing
        onCreate(db); // recreate all tables with updated schema
    }


    // Sign up (register)
    public boolean registerUser(String username, String password, String name, int age, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("password", password);
            values.put("name", name);
            values.put("age", age);
            values.put("gender", gender);

            long result = db.insert("users", null, values);
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }




    // Login
    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?",
                new String[]{username.trim().toLowerCase(), password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public int getBestScoreForUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(score) FROM scores WHERE username = ?", new String[]{username});
        int best = 0;
        if (cursor.moveToFirst()) {
            best = cursor.getInt(0);
        }
        cursor.close();
        return best;
    }

    public String[] getUserDetails(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name, age, gender FROM users WHERE username = ?", new String[]{username});
        String[] result = new String[3];
        if (cursor.moveToFirst()) {
            result[0] = cursor.getString(0); // name
            result[1] = String.valueOf(cursor.getInt(1)); // age
            result[2] = cursor.getString(2); // gender
        }
        cursor.close();
        return result;
    }


    private void insertSampleQuestions(SQLiteDatabase db) {
        db.execSQL("INSERT INTO questions (quiz_name, question, option1, option2, option3, option4, answer) VALUES " +
                "('Java Quiz', 'Which company developed Java?', 'Google', 'Microsoft', 'Sun Microsystems', 'Apple', 'Sun Microsystems')," +
                "('Java Quiz', 'Java is...', 'Compiled', 'Interpreted', 'Both', 'None', 'Both')," +
                "('Java Quiz', 'Which of these is not a Java keyword?', 'class', 'int', 'get', 'if', 'get')," +
                "('Java Quiz', 'Which method is entry point in Java?', 'init()', 'start()', 'main()', 'run()', 'main()')," +
                "('Java Quiz', 'Which keyword is used for inheritance?', 'this', 'super', 'extends', 'implements', 'extends')," +
                "('Java Quiz', 'Which collection class allows you to grow or shrink its size?', 'Array', 'ArrayList', 'Map', 'Set', 'ArrayList')," +
                "('Java Quiz', 'Which of these is not OOP concept?', 'Encapsulation', 'Polymorphism', 'Compiler', 'Inheritance', 'Compiler')," +
                "('Java Quiz', 'Which keyword creates object?', 'create', 'new', 'make', 'init', 'new')," +
                "('Java Quiz', 'Java Virtual Machine is...', 'hardware', 'interpreter', 'compiler', 'IDE', 'interpreter')," +
                "('Java Quiz', 'Which operator is used for comparison?', '=', '==', '!=', '&&', '==')");

        db.execSQL("INSERT INTO questions (quiz_name, question, option1, option2, option3, option4, answer) VALUES " +
                "('Probability Quiz', 'Probability ranges from?', '0 to 1', '0 to ∞', '-1 to 1', 'None', '0 to 1')," +
                "('Probability Quiz', 'P(E) + P(E'') = ?', '1', '0', '2', 'Depends', '1')," +
                "('Probability Quiz', 'A coin is tossed once. Probability of Head?', '0.5', '0.25', '1', '0', '0.5')," +
                "('Probability Quiz', 'Die has how many faces?', '4', '6', '8', '10', '6')," +
                "('Probability Quiz', 'What is impossible event probability?', '0', '1', '0.5', 'Not defined', '0')," +
                "('Probability Quiz', 'Two dice rolled, total outcomes?', '12', '18', '36', '6', '36')," +
                "('Probability Quiz', 'Complement of certain event?', 'Impossible', 'Unlikely', 'Same', 'Probable', 'Impossible')," +
                "('Probability Quiz', 'Which one is NOT probability axiom?', '0 ≤ P(E) ≤ 1', 'P(S)=1', 'P(E+F)=P(E)+P(F)', 'P(E)=2', 'P(E)=2')," +
                "('Probability Quiz', 'Card picked: what is chance of Ace?', '1/52', '4/52', '13/52', 'None', '4/52')," +
                "('Probability Quiz', 'Toss 2 coins, what is P(2 heads)?', '1/4', '1/2', '3/4', '1', '1/4')");

        db.execSQL("INSERT INTO questions (quiz_name, question, option1, option2, option3, option4, answer) VALUES " +
                "('Current Affairs in India Quiz', 'PM of India (2024)?', 'Modi', 'Rahul', 'Yogi', 'Kejriwal', 'Modi')," +
                "('Current Affairs in India Quiz', 'Which state won Khelo India 2023?', 'Maharashtra', 'Odisha', 'Haryana', 'Punjab', 'Maharashtra')," +
                "('Current Affairs in India Quiz', 'India''s moon mission 2023?', 'Chandrayaan-3', 'Gaganyaan', 'Suryaan', 'Mission Moon', 'Chandrayaan-3')," +
                "('Current Affairs in India Quiz', 'Current RBI Governor?', 'Urjit Patel', 'Shaktikanta Das', 'Rajan', 'None', 'Shaktikanta Das')," +
                "('Current Affairs in India Quiz', 'ISRO launched which satellite recently?', 'INSAT-3DS', 'GSAT-10', 'Aryabhata', 'Bhaskar', 'INSAT-3DS')," +
                "('Current Affairs in India Quiz', 'India''s 2024 Republic Day guest?', 'Macron', 'Biden', 'Putin', 'Sunak', 'Macron')," +
                "('Current Affairs in India Quiz', 'Which airport is busiest?', 'Delhi', 'Mumbai', 'Bangalore', 'Chennai', 'Delhi')," +
                "('Current Affairs in India Quiz', 'India''s new Chief Justice?', 'Ramana', 'Lalit', 'Chandrachud', 'Gogoi', 'Chandrachud')," +
                "('Current Affairs in India Quiz', 'G20 summit hosted in?', 'Delhi', 'Mumbai', 'Hyderabad', 'Kolkata', 'Delhi')," +
                "('Current Affairs in India Quiz', 'Ayodhya Ram Mandir inauguration year?', '2022', '2023', '2024', '2025', '2024')");

        db.execSQL("INSERT INTO questions (quiz_name, question, option1, option2, option3, option4, answer) VALUES " +
                "('Psychometric Quiz', 'You find it easy to start conversations.', 'Strongly Agree', 'Agree', 'Disagree', 'Strongly Disagree', 'Agree')," +
                "('Psychometric Quiz', 'You plan things carefully.', 'Always', 'Sometimes', 'Rarely', 'Never', 'Always')," +
                "('Psychometric Quiz', 'You enjoy social settings.', 'Yes', 'No', 'Maybe', 'Sometimes', 'Yes')," +
                "('Psychometric Quiz', 'You prefer routine over spontaneity.', 'Yes', 'No', 'Neutral', 'Often', 'Yes')," +
                "('Psychometric Quiz', 'You get stressed easily.', 'Always', 'Rarely', 'Sometimes', 'Never', 'Sometimes')," +
                "('Psychometric Quiz', 'You take time before decisions.', 'Yes', 'No', 'Immediately', 'Never', 'Yes')," +
                "('Psychometric Quiz', 'You are comfortable in large crowds.', 'Yes', 'No', 'Sometimes', 'Depends', 'Sometimes')," +
                "('Psychometric Quiz', 'You analyze people''s intentions often.', 'Yes', 'No', 'Sometimes', 'Rarely', 'Yes')," +
                "('Psychometric Quiz', 'You like solving logic puzzles.', 'Love it', 'Hate it', 'Sometimes', 'Never', 'Love it')," +
                "('Psychometric Quiz', 'You prefer teamwork.', 'Yes', 'No', 'Sometimes', 'Depends', 'Yes')");
    }
}
