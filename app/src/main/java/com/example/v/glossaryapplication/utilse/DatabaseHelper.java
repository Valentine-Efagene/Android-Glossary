package com.example.v.glossaryapplication.utilse;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "glossary.sqlite";
    public static final String DBLOCATION = "/data/data/ com.example.v.glossaryapplication/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase , int i, int i1) {

    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public List<GlossaryModel> getListWord(String wordSearch) {
        GlossaryModel glossaryModel = null;
        List<GlossaryModel> glossaryModelList = new ArrayList<>();
        openDatabase();
        String[] args = {"%" + wordSearch + "%"};

        Cursor cursor = mDatabase.rawQuery("Select * From tblWord  Where Word Like ?",args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            glossaryModel = new GlossaryModel(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            glossaryModelList.add(glossaryModel);
            cursor.moveToNext();

        }
        cursor.close();
        closeDatabase();
        return glossaryModelList;


    }
}

