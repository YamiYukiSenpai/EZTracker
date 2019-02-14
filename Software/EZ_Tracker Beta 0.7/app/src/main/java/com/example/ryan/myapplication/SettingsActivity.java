package com.example.ryan.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    Switch push_notif;
    Button settings_back;
    Button updateInfo;
    Button viewInfo;
    Button aboutUs;
    Button changeLang;

    SharedPreferences shared_pref;

    public static final String pref_name = "prefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_settings);
        shared_pref = getApplicationContext().getSharedPreferences(pref_name, 0);

        aboutUs = findViewById(R.id.aboutUs_button);
        push_notif = findViewById(R.id.push_Switch);
        settings_back = findViewById(R.id.settings_back);
        updateInfo = findViewById(R.id.updateInfo_button);
        viewInfo = findViewById(R.id.viewInfo_button);
        changeLang = findViewById(R.id.language_button);
/*
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));*/

        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show alertdiaglog to display list of languages
                showChangeLanguageDialog();
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, Main2Activity.class);
                save_settings();
                finish();
                startActivity(intent);
            }
        });

        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, UpdateActivity.class);
                save_settings();
                finish();
                startActivity(intent);
            }
        });

        viewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ViewDataActivity.class);
                save_settings();
                finish();
                startActivity(intent);
            }
        });

        settings_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (SettingsActivity.this, HomeActivity.class);
                save_settings();
                finish();
                startActivity(intent);
            }
        });

        load_settings();
    }

    private void showChangeLanguageDialog() {
        final String[] langItems = {getString(R.string.choose_Eng), getString(R.string.choose_french)};
        AlertDialog.Builder langBuilder = new AlertDialog.Builder(SettingsActivity.this);
        langBuilder.setTitle(R.string.settings_SelectLang);
        langBuilder.setSingleChoiceItems(langItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i==0){
                    setLocale("en");
                    recreate();
                } else if (i == 1){
                    setLocale("fr");
                    recreate();
                } else {
                    return;
                }
                dialog.dismiss();
            }

        });

        AlertDialog langDialog = langBuilder.create();
        langDialog.show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //save data
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
        editor.commit();
    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }

    @Override
    public void onBackPressed() {
        Intent back_home = new Intent(SettingsActivity.this, HomeActivity.class);
        save_settings();
        startActivity(back_home);
        super.onBackPressed();
    }

    private void save_settings() {
        SharedPreferences.Editor settings_pref = shared_pref.edit();
        settings_pref.putBoolean("push_notif", push_notif.isChecked());
        settings_pref.commit();
    }

    private void load_settings() {
        push_notif.setChecked(shared_pref.getBoolean("push_notif", false));
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
        }
    }
}
