package com.example.brian.newsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

public class SettingsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements
            Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference section = findPreference(getString(R.string.settings_section_key));
            bindPreferenceSummaryToValue(section);

            Preference date = findPreference(getString(R.string.settings_before_date_key));
            bindPreferenceSummaryToValue(date);
        }

        // Update the displayed preference summary after it has been changed
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            // If no date Date Preference selected, the search will not limit the date.
            if(TextUtils.isEmpty(value.toString())) {
                preference.setSummary(R.string.current_date);
                return true;
            }

            // Capitalize the first letter of the Section Preference summary selection.
            String stringValue = value.toString().substring(0, 1).toUpperCase()
                     + value.toString().substring(1);
            preference.setSummary(stringValue);
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences((preference.getContext()));
            String preferenceString =
                    preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }
}
