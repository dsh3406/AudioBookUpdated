package com.example.bookcase;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import edu.temple.audiobookplayer.AudiobookService;

public class BookDetailsFragment extends Fragment {

    private BookDetailsInterface mListener;
    Context c;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    TextView textView;
    ImageView imageView;
    String title, author, publishyr;
    public static final String BOOK_KEY = "myBook";
    Book pagerBooks; ImageButton playButton, stopButton, pauseButton;
    SeekBar seekBar; TextView progressText;

    public static BookDetailsFragment newInstance(Book bookList) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BOOK_KEY, bookList);
        fragment.setArguments(bundle);
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
        playButton = view.findViewById(R.id.playButton);
        stopButton = view.findViewById(R.id.stopButton);
        pauseButton = view.findViewById(R.id.pauseButton);
        seekBar = view.findViewById(R.id.seekBar);
        progressText = view.findViewById(R.id.progressText);
        if(getArguments() != null) {
            displayBook(pagerBooks);
        }

        return view;
    }

    public void displayBook(final Book bookObj){
        author = bookObj.getAuthor();
        title = bookObj.getTitle(); publishyr = bookObj.getPublished();
        textView.setText(" \"" + title + "\" "); textView.append(", " + author); textView.append(", " + publishyr);
        textView.setTextSize(15);
        textView.setTextColor(Color.BLACK);
        String imageURL = bookObj.getCoverURL();
        Picasso.get().load(imageURL).into(imageView);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setMax(bookObj.getDuration());
                ((BookDetailsInterface) c).playBook(bookObj.getId());
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BookDetailsInterface) c).pauseBook();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(0);
                ((BookDetailsInterface) c).stopBook();
        }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    progressText.setText("" + progress + "s");
                    ((BookDetailsInterface) c).seekBook(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    public void updateSeekbar(int currentTime){
        seekBar.setProgress(currentTime);
        progressText.setText("" + currentTime + "s");
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookListFragment.BookInterface) {
            mListener = (BookDetailsInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        this.c = context;
    }

    public interface BookDetailsInterface{
        void playBook(int id);
        void pauseBook();
        void stopBook();
        void seekBook(int position);
        void progressHandler();
    }

}
