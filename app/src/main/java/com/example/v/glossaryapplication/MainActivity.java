package com.example.v.glossaryapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.Toast;

import com.example.v.glossaryapplication.adapters.WordAdapter;
import com.example.v.glossaryapplication.utilse.DatabaseHelper;
import com.example.v.glossaryapplication.utilse.GlossaryModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvWord;
    private WordAdapter wordadapter;
    private List<GlossaryModel> glossaryModelList;
    private DatabaseHelper mDBHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvWord = (RecyclerView) findViewById(R.id.rvWord);
        rvWord.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mDBHelper = new DatabaseHelper(this);

        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        if (database.exists() == false) {
            mDBHelper.getReadableDatabase();
            if (copyDatabase(this)) {
                Toast.makeText(getApplicationContext(), "copy successful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "copy unsuccessful", Toast.LENGTH_LONG).show();
                return;
                }
        }

            glossaryModelList= mDBHelper.getListWord("");

            wordadapter=new WordAdapter();
            wordadapter.setData(glossaryModelList);
            rvWord.setAdapter(wordadapter);

        }
        private boolean copyDatabase(Context context){
            try{
                InputStream inputStream= context.getAssets().open(DatabaseHelper.DBNAME);
                String outFileName=DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
                OutputStream outputStream=new FileOutputStream(outFileName);
                byte [] buff = new  byte[1024];
                int length = 0;
                while ((length=inputStream.read(buff)) > 0){
                    outputStream.write(buff,0,length);
                }
                outputStream.flush();
                outputStream.close();
                Log.w("Database","copy successful");
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
    }



    }





