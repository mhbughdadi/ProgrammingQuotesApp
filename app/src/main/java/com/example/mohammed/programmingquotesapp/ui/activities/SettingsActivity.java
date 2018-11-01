package com.example.mohammed.programmingquotesapp.ui.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.mohammed.programmingquotesapp.R;
import com.example.mohammed.programmingquotesapp.ui.fragments.SettingsFragment;

import butterknife.ButterKnife;

/**
 * Created by Mohammed on 02/08/2017.
 */

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar= (Toolbar) findViewById(R.id.tool_bar_layout);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });
        toolbar.setTitle(R.string.settings_activity_label);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().add(R.id.settings_activity_container,new SettingsFragment(),null).commit();
    }
}
