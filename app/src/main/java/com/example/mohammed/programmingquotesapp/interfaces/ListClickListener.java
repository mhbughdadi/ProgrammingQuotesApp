package com.example.mohammed.programmingquotesapp.interfaces;

import android.widget.ImageView;

import com.example.mohammed.programmingquotesapp.data.model.Quote;

/**
 * Created by Mohammed on 29/07/2017.
 */

public interface ListClickListener {
    public void onClick(Quote quote, int position, ImageView iv);
}
