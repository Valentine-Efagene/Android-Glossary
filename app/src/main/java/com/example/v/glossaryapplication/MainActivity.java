package com.example.v.glossaryapplication;

import android.content.Intent;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import android.app.AlertDialog;
import android.content.DialogInterface;

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
    private SearchView searchView;
    private RecyclerView rvWord;
    private WordAdapter wordadapter;
    private List<GlossaryModel> glossaryModelList;
    private DatabaseHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = (SearchView) findViewById(R.id.searchView);
        rvWord = (RecyclerView) findViewById(R.id.rvWord);
        rvWord.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This word is not available. Do you want to send it as a request to the developers?");

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

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

        glossaryModelList = mDBHelper.getListWord("");
        wordadapter = new WordAdapter(glossaryModelList);
        wordadapter.setData(glossaryModelList);
        rvWord.setAdapter(wordadapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                if (glossaryModelList.contains(query)) {
                    wordadapter.getFilter().filter(query);
                } else {
                    alertDialogBuilder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    sendMail(query);
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    Toast.makeText(MainActivity.this, "No Match found", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                wordadapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("Database", "copy successful");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendMail(String word) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"efagenevalentine@gmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Glossary word suggestion");
        email.putExtra(Intent.EXTRA_TEXT, word);

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
}





