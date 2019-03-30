package com.example.bookcase;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookInterface {

    boolean singlePane;
    BookDetailsFragment detailsFragment;
    ViewPagerFragment viewPagerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        singlePane = findViewById(R.id.container_2) == null;
        detailsFragment = new BookDetailsFragment();
        BookListFragment listFragment = new BookListFragment();
        viewPagerFragment = new ViewPagerFragment();

        if(!singlePane){
            addFragment(listFragment, R.id.container_1);
            addFragment(detailsFragment, R.id.container_2);
        } else {
            addFragment(viewPagerFragment, R.id.container_3);
        }

    }

    public void addFragment(Fragment fragment, int id){
        getSupportFragmentManager().
                beginTransaction().
                replace(id, fragment).
                addToBackStack(null).
                commit();
    }

    @Override
    public void bookSelected(Book bookObj) {
            detailsFragment.displayBook(bookObj);
    }

    @Override
    public void searchBook(JSONArray bookArray) {
        detailsFragment.searchBook(bookArray);
    }

}
