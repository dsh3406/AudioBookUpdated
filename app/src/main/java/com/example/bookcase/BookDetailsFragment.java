package com.example.bookcase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BookDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    TextView textView; Button button;
    ImageView imageView; EditText editText;
    String bookSelected;
    String title, author, publishyr;
    public static final String BOOK_KEY = "myBook";
    Book pagerBooks;

    public static BookDetailsFragment newInstance(Book bookList) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BOOK_KEY, bookList);
        fragment.setArguments(bundle);
        //Log.d("Passed on Book", fragment.getArguments().getParcelable(BOOK_KEY).toString());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            pagerBooks = getArguments().getParcelable(BOOK_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_details, container, false);
        textView = view.findViewById(R.id.bookTitle);
        imageView = view.findViewById(R.id.bookImage);
        button = view.findViewById(R.id.button);
        editText = view.findViewById(R.id.searchBar);
        if(getArguments() != null) {
            displayBook(pagerBooks);
        }

        return view;
    }

    public void displayBook(Book bookObj){
        author = bookObj.getAuthor();
        title = bookObj.getTitle(); publishyr = bookObj.getPublished();
        textView.setText(" \"" + title + "\" "); textView.append(", " + author); textView.append(", " + publishyr);
        textView.setTextSize(20);
        String imageURL = bookObj.getCoverURL();
        Picasso.get().load(imageURL).into(imageView);
    }

}
