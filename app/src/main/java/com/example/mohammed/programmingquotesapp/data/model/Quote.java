package com.example.mohammed.programmingquotesapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mohammed on 25/07/2017.
 */

public class Quote implements Parcelable{
    String author;
    String quote;
    String peraLink;
    String id;

    public Quote(String author, String quote, String peraLink, String  id) {
        this.author = author;
        this.quote = quote;
        this.peraLink = peraLink;
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public String getQuote() {
        return quote;
    }

    public String getPeraLink() {
        return peraLink;
    }

    public String getId() {
        return id;
    }

    protected Quote(Parcel in) {
        author = in.readString();
        quote = in.readString();
        peraLink = in.readString();
        id = in.readString();
    }

    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(quote);
        dest.writeString(peraLink);
        dest.writeString(id);
    }
}
