package com.example.ryan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class pop extends Activity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private String userID;

    //private EditText goalNum;

    float curGoal;
    int total_steps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDatabase();

        setContentView(R.layout.popwindow);

        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.5),(int)(height*.2));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;
        getWindow().setAttributes(params);

        final Button goalBtn = findViewById(R.id.goalButton);

        goalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText goalNum = findViewById(R.id.goalNumber);
                String goalCheck = goalNum.getText().toString().trim();

                if (TextUtils.isEmpty(goalCheck)) {
                    Toast.makeText(pop.this, R.string.pop_EnterVal, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    int goal = Integer.parseInt(goalNum.getText().toString().trim());

                    if (goal > total_steps) {
                        myRef.child(userID).child("steps").child("goalSteps").setValue(goal);
                        Toast.makeText(pop.this, R.string.pop_Saved, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    if (goal < total_steps) {
                        Toast.makeText(pop.this, R.string.pop_Error, Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        DatabaseReference namer = FirebaseDatabase.getInstance().getReference(userID);

        namer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float realMonday = dataSnapshot.child("steps").child("monday").getValue(Integer.class);
                float realTuesday = dataSnapshot.child("steps").child("tuesday").getValue(Integer.class);
                float realWednesday = dataSnapshot.child("steps").child("wednesday").getValue(Integer.class);
                float realThursday = dataSnapshot.child("steps").child("thursday").getValue(Integer.class);
                float realFriday = dataSnapshot.child("steps").child("friday").getValue(Integer.class);
                float realSaturday = dataSnapshot.child("steps").child("saturday").getValue(Integer.class);
                float realSunday = dataSnapshot.child("steps").child("sunday").getValue(Integer.class);

                total_steps = (int) (realMonday + realTuesday + realWednesday + realThursday +
                        realFriday + realSaturday + realSunday);

                curGoal = dataSnapshot.child("steps").child("goalSteps").getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(pop.this, R.string.pop_connectErr, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDatabase() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(pop.this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
