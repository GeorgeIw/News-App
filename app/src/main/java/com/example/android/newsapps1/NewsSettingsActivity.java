package com.example.android.newsapps1;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NewsSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_settings);
    }

    public static class ArticlePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            //find the preference with stored String:number_of_articles and store it at numOfArticles variable
            Preference numOfArticles = findPreference(getString(R.string.number_of_articles_key));
            savePreference(numOfArticles);
            //find the preference with stored String:settings_order_by_key and store it at orderBy variable
            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            savePreference(orderBy);
            //find the preference with stored String:settings_order_by_use_date_key and store it to orderByUseDate variable
            Preference orderByUseDate = findPreference(getString(R.string.settings_order_by_use_date_key));
            savePreference(orderByUseDate);

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            //set the label and values of a category in settings Activity
            if(preference instanceof ListPreference){
                ListPreference listPreference = (ListPreference) preference;
                int preferenceIndex = listPreference.findIndexOfValue(stringValue);
                if(preferenceIndex >= 0){
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[preferenceIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
        //save the preferences selected by the user for the next time the app opens
        public void savePreference(Preference preference){
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(),"0");
            onPreferenceChange(preference,preferenceString);
        }
    }
}
