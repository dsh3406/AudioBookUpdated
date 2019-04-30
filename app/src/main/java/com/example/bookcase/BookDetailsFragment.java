package com.example.bookcase;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.temple.audiobookplayer.AudiobookService;

public class BookDetailsFragment extends Fragment {

    private BookDetailsInterface mListener;
    Context c;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    TextView textView;
    ImageView imageView; int currentTime;
    String title, author, publishyr;
    public static final String BOOK_KEY = "myBook";
    Book pagerBooks; ImageButton playButton, stopButton, pauseButton; Button downloadButton, deleteButton;
    SeekBar seekBar; TextView progressText; File file;
    SharedPreferences preferences; SharedPreferences.Editor editor;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        downloadButton = view.findViewById(R.id.downloadButton);
        deleteButton = view.findViewById(R.id.deleteButton);
        seekBar = view.findViewById(R.id.seekBar);
        progressText = view.findViewById(R.id.progressText);
        preferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = preferences.edit();

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

        seekBar.setMax(bookObj.getDuration());

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTime = preferences.getInt("SAVED_PROGRESS" + bookObj.getId(), 0);
                if(currentTime <= 10){
                    currentTime = 0;
                } else {
                    currentTime = currentTime - 10;
                }
                Log.d("Current Time", String.valueOf(currentTime));
                if(file != null){
                    Toast.makeText(getActivity(), "Playing Audio Book File", Toast.LENGTH_SHORT).show();
                    //((BookDetailsInterface) c).playBookFile(file);
                    ((BookDetailsInterface) c).playBookFilePosition(file, currentTime);
                }else {
                    Toast.makeText(getActivity(), "Streaming Audio Book", Toast.LENGTH_SHORT).show();
                    //((BookDetailsInterface) c).playBook(bookObj.getId());
                    ((BookDetailsInterface) c).playBookPosition(bookObj.getId(), currentTime);
                }
                ((BookDetailsInterface) c).setProgress(progressHandler);
            }

        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt("SAVED_PROGRESS" + bookObj.getId(), seekBar.getProgress());
                editor.apply();
                ((BookDetailsInterface) c).pauseBook();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt("SAVED_PROGRESS" + bookObj.getId(), 0);
                editor.apply();
                seekBar.setProgress(0);
                progressText.setText("0s");
                ((BookDetailsInterface) c).stopBook();
        }
        });
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(file == null) {
                    Toast.makeText(getActivity(), "Downloading...", Toast.LENGTH_LONG).show();
                    int bookId = bookObj.getId();
                    downloadBook(bookId);
                } else {
                    Toast.makeText(getActivity(), "Already Downloaded", Toast.LENGTH_SHORT).show();
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(file != null) {
                    file.delete();
                    file = null;
                    Toast.makeText(getActivity(), "File Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "File does not exist", Toast.LENGTH_SHORT).show();
                }
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

    Handler progressHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            updateSeekbar(msg.what);
            return false;
        }
    });

    public void updateSeekbar(int currentTime){
        seekBar.setProgress(currentTime);
        Log.d("Progress", ":" + seekBar.getProgress());
        progressText.setText("" + currentTime + "s");
    }

    public void downloadBook(final int search) {
        new Thread() {
            public void run() {
                try {
                    String urlString = "https://kamorris.com/lab/audlib/download.php?id=" + search;
                    URL url = new URL(urlString);
                    InputStream inputStream = url.openStream();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[4096];
                    int num;
                    while((num = inputStream.read(buffer)) > 0){
                        byteArrayOutputStream.write(buffer, 0, num);
                    }
                    file = new File(getActivity().getFilesDir(), String.valueOf(search));
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(byteArrayOutputStream.toByteArray());

                    Log.d("File downloaded", file.toString());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
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
        void playBookFile(File file);
        void pauseBook();
        void stopBook();
        void seekBook(int position);
        void setProgress(Handler progressHandler);
        void playBookPosition(int id, int position);
        void playBookFilePosition(File file, int position);
    }

}
