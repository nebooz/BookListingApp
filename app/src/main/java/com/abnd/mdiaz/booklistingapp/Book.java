package com.abnd.mdiaz.booklistingapp;

import android.graphics.Bitmap;

public class Book {
    private String mTitle;
    private String mAuthors;
    private String mDescription;
    private Bitmap mImage;

    public Book(String title, String authors, String description, Bitmap image) {
        mTitle = title;
        mAuthors = authors;
        mDescription = description;
        mImage = image;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthors() {
        return mAuthors;
    }

    public String getDescription() {
        return mDescription;
    }

    public Bitmap getImage() {
        return mImage;
    }

}
