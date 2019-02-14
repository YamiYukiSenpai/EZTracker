package com.example.ryan.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    Button changeLangLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        Button createAccount =  findViewById(R.id.loginSignUp);
        Button login = findViewById(R.id.loginLogin);
        Button pwreset = findViewById(R.id.loginPwRest);

        changeLangLogin = findViewById(R.id.loginSettings);
        changeLangLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                finish();
                startActivity(intent);
            }
        });

        pwreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPassword.class);
                finish();
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextEmail = findViewById(R.id.loginEmail);
                String email = editTextEmail.getText().toString().trim();

                EditText editTextPassword = findViewById(R.id.loginPw);
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, R.string.loginToast_email, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, R.string.loginToast_pw, Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    Toast.makeText(LoginActivity.this, R.string.loginToast_success, Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, R.string.loginToast_incorrect, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void showChangeLanguageDialog() {
        final String[] langItems = {getString(R.string.choose_Eng), getString(R.string.choose_french)};
        AlertDialog.Builder langBuilder = new AlertDialog.Builder(LoginActivity.this);
        langBuilder.setTitle(R.string.login_LangSelect);
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
}
