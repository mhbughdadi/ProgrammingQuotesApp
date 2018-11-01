package com.example.mohammed.programmingquotesapp.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohammed.programmingquotesapp.R;
import com.example.mohammed.programmingquotesapp.data.model.Quote;
import com.example.mohammed.programmingquotesapp.ui.activities.SettingsActivity;

/**
 * Created by Mohammed on 01/08/2017.
 */

public class DetailsFragment extends Fragment {
    public static final String QUOTE ="quote" ;
    TextView tvAuthor,tvQuote,tvPeralink;
    ImageView ivPhoto;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detaill_menu,menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View  view=inflater.inflate(R.layout.detail_fragment,container,false);
        tvAuthor= (TextView) view.findViewById(R.id.detail_fragment_author);
        tvQuote= (TextView) view.findViewById(R.id.detail_fragment_quote);
        tvPeralink= (TextView) view.findViewById(R.id.detail_fragment_peralink);
        ivPhoto= (ImageView) view.findViewById(R.id.detail_fragment_photo);
        if(savedInstanceState!=null){
            tvAuthor.setText(savedInstanceState.getString(getString(R.string.author_hint)));
            tvQuote.setText(savedInstanceState.getString(getString(R.string.quote_hint)));
            tvAuthor.setText(savedInstanceState.getString(getString(R.string.peralink_hint)));
            ivPhoto.setImageDrawable(getResources().getDrawable(savedInstanceState.getInt(getString(R.string.photo_hint))));

        }else{
            Bundle bundle=getArguments();
            Quote quote=bundle.getParcelable(QUOTE);
            tvAuthor.setText(quote.getAuthor());
            tvQuote.setText(quote.getQuote());
            tvPeralink.setText(quote.getPeraLink());
//            ivPhoto.setImageDrawable(getResources().getDrawable(bundle.getInt(getString(R.string.photo_hint))));
        }
        setHasOptionsMenu(true);
        return  view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int action=item.getItemId();
        switch (action){
            case R.id.action_open:{
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_VIEW);
                sharingIntent.setData(Uri.parse(tvPeralink.getText().toString()));
                startActivity(Intent.createChooser(sharingIntent, "Open with"));
                return true;
            }
            case R.id.action_share:{
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = tvQuote.getText().toString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Quote");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;
            }

            default:
                return false;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getResources().getString(R.string.author_hint),tvAuthor.getText().toString());
        outState.putString(getResources().getString(R.string.quote_hint),tvQuote.getText().toString());
        outState.putString(getResources().getString(R.string.peralink_hint),tvPeralink.getText().toString());

        outState.putInt(getResources().getString(R.string.photo_hint),R.drawable.ic_settings_icon);
    }
}
