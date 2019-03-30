package com.example.bookcase;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Book {
    private int id;
    private String title, author, coverURL, published;

    public Book(JSONObject jsonBook) throws JSONException {
        this.title = jsonBook.getString("title"); this.author = jsonBook.getString("author");
        this.coverURL = jsonBook.getString("cover_url");
        this.id = jsonBook.getInt("book_id"); this.published = jsonBook.getString("published");
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public int getId() {
        return id;
    }

    public String getPublished() {
        return published;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCoverURL() {
        return coverURL;
    }
}
