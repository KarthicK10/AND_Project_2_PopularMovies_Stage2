package com.example.karthick.popularmovies;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by KarthicK on 6/12/2016.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Add 'general' preferences defined in the xml */
        addPreferencesFromResource(R.xml.pref_general);

        /*For all preferences attach an OnPreferenceChangeListener */
        bindPreferenceSummaryToValue(findPreference("sortOrder"));

    }


    private void bindPreferenceSummaryToValue(Preference preference){
        //Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        //Trigger the listener immediately with the preference's current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }


    /**
     * Called when a Preference has been changed by the user. This is
     * called before the state of the Preference is about to be updated and
     * before the state is persisted.
     *
     * @param preference The changed Preference.
     * @param newValue   The new value of the Preference.
     * @return True to update the state of the Preference with the new value.
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();

        if(preference instanceof ListPreference){
            //For List preferences, look up the correct display value in
            //the preference's 'entries' list (since they have seperate values/labels).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if(prefIndex >= 0){
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }else{
            //For other preferences set the summary to the value's simple string representation
            preference.setSummary(stringValue);
        }
        return true;
    }
}

