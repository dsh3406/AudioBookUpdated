package com.example.bookcase;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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


public class ViewPagerFragment extends Fragment {

    public ViewPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    BookDetailsFragment newFragment;
    Book bookObj; ArrayList<Book> books;
    ArrayList<BookDetailsFragment> bookFragments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_pager, container, false);
        pagerAdapter = new PagerAdapter(getChildFragmentManager());
        books = new ArrayList<>();
        viewPager = v.findViewById(R.id.viewPager);
        bookFragments = new ArrayList<>();
        viewPager.setAdapter(pagerAdapter);

        return v;
    }

    public void addPager(final ArrayList<Book> bookArray){
        for(int i = 0; i < bookArray.size(); i++) {
            bookObj = bookArray.get(i);
            newFragment = BookDetailsFragment.newInstance(bookObj);
            bookFragments.add(newFragment);
        }
        pagerAdapter.addBooks(bookFragments);
    }

    class PagerAdapter extends FragmentStatePagerAdapter{

        ArrayList<BookDetailsFragment> pagerFragments;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            pagerFragments = new ArrayList<>();
        }

        public void addBooks(ArrayList<BookDetailsFragment> books) {
            pagerFragments.clear();
            pagerFragments.addAll(books);
            notifyDataSetChanged();
        }

        public void add(BookDetailsFragment fragment){
            pagerFragments.add(fragment);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        @Override
        public Fragment getItem(int i) {
            return pagerFragments.get(i);
        }

        @Override
        public int getCount() {
            return pagerFragments.size();
        }
    }

}
