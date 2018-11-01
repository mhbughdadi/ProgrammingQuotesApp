package com.example.mohammed.programmingquotesapp.ui.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohammed.programmingquotesapp.R;
import com.example.mohammed.programmingquotesapp.data.model.Quote;
import com.example.mohammed.programmingquotesapp.ui.fragments.DetailsFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Mohammed on 29/07/2017.
 */

public class DetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    @InjectView(R.id.toolbar_title)
    TextView title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Slide slide=new Slide(Gravity.BOTTOM);
        slide.addTarget(R.id.details_card_view_container);
        slide.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.interpolator.linear_out_slow_in));
        slide.setDuration(3000);
        getWindow().setEnterTransition(slide);
        toolbar= (Toolbar) findViewById(R.id.detail_activity_tool_bar);
        ButterKnife.inject(this);
        title.setTypeface(Typeface.DEFAULT.createFromAsset(getAssets(),"fonts/Courgette-Regular.ttf"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
       Bundle bundle=intent.getExtras();
        DetailsFragment detailsFragment=new DetailsFragment();
        detailsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.details_card_view_container,detailsFragment,null).commit();


    }
}
