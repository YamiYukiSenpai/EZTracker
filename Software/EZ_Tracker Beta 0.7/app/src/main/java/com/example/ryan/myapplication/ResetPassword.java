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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class ResetPassword extends AppCompatActivity {

    private EditText reset_email;
    private Button verify_email;
    private FirebaseAuth firebase_auth;
    private Button backButn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        backButn = findViewById(R.id.reset_back);
        reset_email = findViewById(R.id.reset_email);
        verify_email = findViewById(R.id.reset_verify);

        firebase_auth = FirebaseAuth.getInstance();

        verify_email.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String email = reset_email.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ResetPassword.this, R.string.toastReset_email,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                firebase_auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPassword.this, R.string.toastReset_sent,
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ResetPassword.this,
                                            LoginActivity.class);
                                    finish();
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(ResetPassword.this,
                                            R.string.toastReset_dne, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });
            }
        });

        backButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPassword.this, LoginActivity.class);
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
        Intent intent = new Intent(ResetPassword.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }
}
