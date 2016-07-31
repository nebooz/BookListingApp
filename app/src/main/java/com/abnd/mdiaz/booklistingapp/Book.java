package com.abnd.mdiaz.booklistingapp;

/**
 * Created by neboo on 31-Jul-16.
 */
public class Book {
    private String mTitle;
    private String mAuthors;
    private String mDescription;
    private String mPublicationYear;
    private int mIsbn;

    public Book(String title, String authors, String description) {
        mTitle = title;
        mAuthors = authors;
        mDescription = description;

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

}
