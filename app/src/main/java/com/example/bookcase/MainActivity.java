package com.example.bookcase;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.temple.audiobookplayer.AudiobookService;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookInterface, BookDetailsFragment.BookDetailsInterface {

    boolean singlePane;
    BookDetailsFragment detailsFragment;
    ViewPagerFragment viewPagerFragment;
    BookListFragment listFragment;
    EditText searchText; Button button;
    JSONArray bookArray; String searchBook;
    ArrayList<Book> bookList;
    boolean connected;
    AudiobookService.MediaControlBinder mediaControlBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchText = findViewById(R.id.searchText);
        searchText.clearFocus();
        button = findViewById(R.id.searchButton);
        bookList = new ArrayList<>();

        singlePane = findViewById(R.id.container_2) == null;
        detailsFragment = new BookDetailsFragment();
        listFragment = new BookListFragment();
        viewPagerFragment = new ViewPagerFragment();

        bindService(new Intent(this, AudiobookService.class), serviceConnection, BIND_AUTO_CREATE);
        if(!singlePane){
            addFragment(listFragment, R.id.container_1);
            addFragment(detailsFragment, R.id.container_2);
        } else {
            addFragment(detailsFragment, R.id.container_3); //need this to avoid app crashing when pressed play button on view pager.
            addFragment(viewPagerFragment, R.id.container_3);
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBook = searchText.getText().toString();
                downloadBook(searchBook);
            }
        });
    }

    public void addFragment(Fragment fragment, int id){
        getSupportFragmentManager().
                beginTransaction().
                replace(id, fragment).
                addToBackStack(null).
                commit();
    }

    public void downloadBook(final String search) {
        new Thread() {
            public void run() {
                try {
                    String urlString;
                    if(search.equals(" ")){
                        urlString = "https://kamorris.com/lab/audlib/booksearch.php";
                    } else {
                        urlString = "https://kamorris.com/lab/audlib/booksearch.php?search=" + search;
                    }
                    URL url = new URL(urlString);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    StringBuilder builder = new StringBuilder();
                    String tmpString;
                    while ((tmpString = reader.readLine()) != null) {
                        builder.append(tmpString);
                    }
                    Message msg = Message.obtain();
                    msg.obj = builder.toString();
                    urlHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler urlHandler = new Handler(new Handler.Callback() {
        @Override
            public boolean handleMessage(Message msg) {
            try {
                bookArray = new JSONArray((String) msg.obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            bookList.clear();
            for(int i = 0; i < bookArray.length(); i++){
                try {
                    bookList.add(new Book(bookArray.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(singlePane) {
                viewPagerFragment.addPager(bookList);
            } else {
                listFragment.getBooks(bookList);
            }
            return false;
        }
    });


    @Override
    public void bookSelected(Book bookObj) {
        detailsFragment.displayBook(bookObj);
    }

    @Override
    public void playBook(int id) {
        mediaControlBinder.play(id);
    }

    @Override
    public void pauseBook() {
        mediaControlBinder.pause();
    }

    @Override
    public void stopBook() {
        mediaControlBinder.stop();
    }

    @Override
    public void seekBook(int position) {
        mediaControlBinder.seekTo(position);
    }

    @Override
    public void setProgress(Handler progressHandler) {
        mediaControlBinder.setProgressHandler(progressHandler);
    }


    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mediaControlBinder = ((AudiobookService.MediaControlBinder) service);
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
            mediaControlBinder = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(connected) {
            unbindService(serviceConnection);
            connected = false;
        }
    }
}
