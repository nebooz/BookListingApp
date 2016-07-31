package com.abnd.mdiaz.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by neboo on 31-Jul-16.
 */
public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.basic_book_list_item, parent,false);
        }

        Book book = getItem(position);

        TextView titleText = (TextView) listItemView.findViewById(R.id.title_text);
        titleText.setText(book.getTitle());

        TextView authorsText = (TextView) listItemView.findViewById(R.id.authors_text);
        authorsText.setText(book.getAuthors());

        TextView descriptionText = (TextView) listItemView.findViewById(R.id.description_text);
        descriptionText.setText(book.getDescription());

        return listItemView;

    }
}
