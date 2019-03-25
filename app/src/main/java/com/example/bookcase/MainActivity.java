package com.example.bookcase;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookInterface {

    boolean singlePane;
    BookDetailsFragment detailsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        singlePane = findViewById(R.id.container_2) == null;
        detailsFragment = new BookDetailsFragment();
        BookListFragment listFragment = new BookListFragment();
        addFragment(listFragment, R.id.container_1);

        if(!singlePane){
            addFragment(detailsFragment, R.id.container_2);
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
    public void bookSelected(String book) {
        if(singlePane){
            Bundle bundle = new Bundle();
            bundle.putString("bookTitle", book);
            detailsFragment.setArguments(bundle);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.container_1, detailsFragment)
                    .addToBackStack(null)
                    .commit();
            detailsFragment.displayBook();
        } else{
            Bundle bundle = new Bundle();
            bundle.putString("bookTitle", book);
            detailsFragment.setArguments(bundle);
            detailsFragment.displayBook();
        }
    }
}
