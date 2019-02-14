package com.example.ryan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class UpdateActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbRef;
    private EditText editTextName;
    private EditText editTextHeight;
    private EditText editTextWeight;
    private EditText editTextDob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_update);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent (UpdateActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
        dbRef = FirebaseDatabase.getInstance().getReference();
        editTextName = findViewById(R.id.updateName);
        editTextWeight = findViewById(R.id.updateWeight);
        editTextHeight = findViewById(R.id.updateHeight);
        editTextDob = findViewById(R.id.updateDob);

        //read data here
        Button updateSave = findViewById(R.id.updateSave);
        updateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = editTextName.getText().toString().trim();
                final String height = editTextHeight.getText().toString().trim();
                final String weight = editTextWeight.getText().toString().trim();
                final String dob = editTextDob.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    Toast.makeText(UpdateActivity.this, R.string.update_entName, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(height)){
                    Toast.makeText(UpdateActivity.this, R.string.update_entHeight, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(weight)){
                    Toast.makeText(UpdateActivity.this, R.string.update_entWeight, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(dob)){
                    Toast.makeText(UpdateActivity.this, R.string.update_entDob, Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseUser user = firebaseAuth.getCurrentUser();
                //write to DB
                dbRef.child(user.getUid()).child("name").setValue(name);
                dbRef.child(user.getUid()).child("height").setValue(height);
                dbRef.child(user.getUid()).child("weight").setValue(weight);
                dbRef.child(user.getUid()).child("dob").setValue(dob);

                Toast.makeText(UpdateActivity.this, R.string.update_Saved, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateActivity.this, SettingsActivity.class);
                finish();
                startActivity(intent);
            }
        });

        Button updateBack = findViewById(R.id.updateBackButton);
        updateBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateActivity.this, SettingsActivity.class);
                finish();
                startActivity(intent);
            }
        });
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
    public void onBackPressed(){
        Intent intent = new Intent(UpdateActivity.this, SettingsActivity.class);
        finish();
        startActivity(intent);
    }
}
