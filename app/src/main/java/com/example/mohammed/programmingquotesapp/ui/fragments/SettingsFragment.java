package com.example.mohammed.programmingquotesapp.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import com.example.mohammed.programmingquotesapp.R;
import com.example.mohammed.programmingquotesapp.sync.QuoteAppSyncAdapter;

/**
 * Created by Mohammed on 03/08/2017.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener,SharedPreferences.OnSharedPreferenceChangeListener {


    

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_fragment);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_enable_notifications_key)));

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        setPreferenceSummary(preference,newValue);
        return true;
    }
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);
        boolean state=PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getBoolean(preference.getKey(), Boolean.parseBoolean(getResources().getString(R.string.pref_enable_notifications_default)));
        // Set the preference summaries
        setPreferenceSummary(preference,
                state?getResources().getString(R.string.pref_enable_notifications_true):getResources().getString(R.string.pref_enable_notifications_false));
    }
    private void setPreferenceSummary(Preference preference, Object value) {
        String stringValue = value.toString();
        String key = preference.getKey();
        preference.setSummary(stringValue);

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        QuoteAppSyncAdapter.syncImmediately(getContext());
    }
}
