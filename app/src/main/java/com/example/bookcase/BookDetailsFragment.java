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
    public static final String BOOK_KEY = "bookTitle";

    public static BookDetailsFragment newInstance(String book) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BOOK_KEY, book);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            bookSelected = getArguments().getString(BOOK_KEY);
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchBook = editText.getText().toString();

            }
        });

        //displayBook(bookSelected);

        return view;
    }

    public void displayBook(Book bookObj){
        author = bookObj.getAuthor();
        title = bookObj.getTitle(); publishyr = bookObj.getPublished();
        textView.setText(" \"" + title + "\" "); textView.append(", " + author); textView.append(", " + publishyr);
        String imageURL = bookObj.getCoverURL();
        Picasso.get().load(imageURL).into(imageView);
    }

}
