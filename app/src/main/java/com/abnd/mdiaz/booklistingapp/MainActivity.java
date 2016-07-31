package com.abnd.mdiaz.booklistingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final String BOOKS_QUERY_INIT =
            "https://www.googleapis.com/books/v1/volumes?q=";

    private static final int BOOKS_MAX_RESULTS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BooksAsyncTask task = new BooksAsyncTask();
        task.execute();


    }
}
