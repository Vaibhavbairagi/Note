package com.vaibhav.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

class MyDatabase {
    private static final String DB_NAME = "NOTES";
    private static final int DB_VERSION=1;
    private static final String TABLE_NAME="NOTE_LIST";
    private static final String C_ID="ID";
    private static final String C_TITLE="TITLE";
    private static final String C_CONTENT="CONTENT";

    private String[] allColumns={C_ID,C_TITLE,C_CONTENT};

    private static final String CREATE_DB= "CREATE TABLE " + TABLE_NAME + "( " + C_ID + " INTEGER PRIMARY KEY, " + C_TITLE + " TEXT, " + C_CONTENT + " TEXT"+" )";

    private SQLiteDatabase myDatabase;
    private final Context myContext;
    private dbHelper myHelper;

    public MyDatabase (Context c){
        myContext=c;
    }

    public class dbHelper extends SQLiteOpenHelper{

        public dbHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_DB);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }

    public MyDatabase open() {
        myHelper= new dbHelper(myContext);
        myDatabase=myHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        myHelper.close();
    }

    public void createEntry (NoteContents n){
        ContentValues contentValues=new ContentValues();
        contentValues.put(C_ID,n.place);
        contentValues.put(C_TITLE,n.notetitle);
        contentValues.put(C_CONTENT,n.notecontent);
        myDatabase.insert(TABLE_NAME,null,contentValues);
        Log.e("dbCollect","entrycreated");
    }


    public ArrayList<NoteContents> getData() {
        ArrayList<NoteContents> list = new ArrayList<>();
        Cursor c = myDatabase.query(TABLE_NAME, allColumns, null, null, null, null, null);
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            NoteContents nrow = convertData(c);
            list.add(nrow);
        }
        c.close();
        Log.e("dbCollect","getdata done");
        return list;
    }

    private NoteContents convertData(Cursor cursor) {
        return new NoteContents(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
    }

    public void removeRow(NoteContents n) {
        int args = n.place;
        myDatabase.delete(TABLE_NAME, C_ID + " = " + args, null);
    }
}
