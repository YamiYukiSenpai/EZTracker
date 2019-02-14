package com.example.ryan.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private String userID;

    private BarChart barChart;
    private PieChart goal_chart;

    private TextView name;
    private TextView goal;
    private TextView current;
    private TextView currentPercent;
    private Button goalButton;

    int goal_steps;
    int total_steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale(); //load language
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        //getting date for bar graph manipulation
        Calendar calendar = Calendar.getInstance();
        final int currentDay = calendar.get(Calendar.DAY_OF_WEEK);

        //initialize database and views
        getDatabase();
        findViews();


        DatabaseReference namer = FirebaseDatabase.getInstance().getReference(userID);
        DatabaseReference getSteps = namer.child("steps");

        namer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String realName = dataSnapshot.child("name").getValue(String.class);
                name.setText(realName+ "'s " + getString(R.string.home_dailySteps));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, R.string.toastHome_connectErr, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent (HomeActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

        getSteps.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float realMonday = dataSnapshot.child("monday").getValue(Integer.class);
                float realTuesday = dataSnapshot.child("tuesday").getValue(Integer.class);
                float realWednesday = dataSnapshot.child("wednesday").getValue(Integer.class);
                float realThursday = dataSnapshot.child("thursday").getValue(Integer.class);
                float realFriday = dataSnapshot.child("friday").getValue(Integer.class);
                float realSaturday = dataSnapshot.child("saturday").getValue(Integer.class);
                float realSunday = dataSnapshot.child("sunday").getValue(Integer.class);

                barChart.setDrawBarShadow(false);
                barChart.setDrawGridBackground(false);
                barChart.setPinchZoom(false);
                barChart.setDoubleTapToZoomEnabled(false);
                barChart.setDragEnabled(true);
                //barChart.setScaleEnabled(true);
                barChart.getLegend().setEnabled(false);
                barChart.getDescription().setEnabled(false);
                barChart.getXAxis().setDrawGridLines(false);
                barChart.getAxisLeft().setDrawGridLines(false);
                barChart.getAxisRight().setDrawGridLines(false);
                barChart.getAxisRight().setDrawLabels(false);
                barChart.getAxisLeft().setTextColor(Color.WHITE);
                barChart.notifyDataSetChanged();
                barChart.invalidate();
                barChart.setBackgroundColor(Color.TRANSPARENT);
                barChart.animateY(1000,Easing.Linear);
                barChart.getAxisLeft().setAxisMinimum(0f);
                barChart.setScaleEnabled(false);

                ArrayList<BarEntry> barEntries = new ArrayList<>();

                //set bargraph data depending on the day
                switch (currentDay){
                    case 1:
                        barEntries.add(new BarEntry(6,realSunday));
                        barEntries.add(new BarEntry(5,realSaturday));
                        barEntries.add(new BarEntry(4,realFriday));
                        barEntries.add(new BarEntry(3,realThursday));
                        barEntries.add(new BarEntry(2,realWednesday));
                        barEntries.add(new BarEntry(1,realTuesday));
                        barEntries.add(new BarEntry(0,realMonday));
                        break;
                    case 2:
                        barEntries.add(new BarEntry(6,realMonday));
                        barEntries.add(new BarEntry(5,realSunday));
                        barEntries.add(new BarEntry(4,realSaturday));
                        barEntries.add(new BarEntry(3,realFriday));
                        barEntries.add(new BarEntry(2,realThursday));
                        barEntries.add(new BarEntry(1,realWednesday));
                        barEntries.add(new BarEntry(0,realTuesday));
                        break;
                    case 3:
                        barEntries.add(new BarEntry(6,realTuesday));
                        barEntries.add(new BarEntry(5,realMonday));
                        barEntries.add(new BarEntry(4,realSunday));
                        barEntries.add(new BarEntry(3,realSaturday));
                        barEntries.add(new BarEntry(2,realFriday));
                        barEntries.add(new BarEntry(1,realThursday));
                        barEntries.add(new BarEntry(0,realWednesday));
                        break;
                    case 4:
                        barEntries.add(new BarEntry(6,realWednesday));
                        barEntries.add(new BarEntry(5,realTuesday));
                        barEntries.add(new BarEntry(4,realMonday));
                        barEntries.add(new BarEntry(3,realSunday));
                        barEntries.add(new BarEntry(2,realSaturday));
                        barEntries.add(new BarEntry(1,realFriday));
                        barEntries.add(new BarEntry(0,realThursday));
                        break;
                    case 5:
                        barEntries.add(new BarEntry(6,realThursday));
                        barEntries.add(new BarEntry(5,realWednesday));
                        barEntries.add(new BarEntry(4,realTuesday));
                        barEntries.add(new BarEntry(3,realMonday));
                        barEntries.add(new BarEntry(2,realSunday));
                        barEntries.add(new BarEntry(1,realSaturday));
                        barEntries.add(new BarEntry(0,realFriday));
                        break;
                    case 6:
                        barEntries.add(new BarEntry(6,realFriday));
                        barEntries.add(new BarEntry(5,realThursday));
                        barEntries.add(new BarEntry(4,realWednesday));
                        barEntries.add(new BarEntry(3,realTuesday));
                        barEntries.add(new BarEntry(2,realMonday));
                        barEntries.add(new BarEntry(1,realSaturday));
                        barEntries.add(new BarEntry(0,realSunday));
                        break;
                    case 7:
                        barEntries.add(new BarEntry(6,realSaturday));
                        barEntries.add(new BarEntry(5,realFriday));
                        barEntries.add(new BarEntry(4,realThursday));
                        barEntries.add(new BarEntry(3,realWednesday));
                        barEntries.add(new BarEntry(2,realTuesday));
                        barEntries.add(new BarEntry(1,realMonday));
                        barEntries.add(new BarEntry(0,realSunday));
                        break;
                    default:
                        return;
                }

                BarDataSet dataSet = new BarDataSet(barEntries, "Steps Taken");
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                BarData barData = new BarData(dataSet);
                barData.setValueTextColor(Color.WHITE);
                barData.setValueTextSize(14f);

                barChart.setData(barData);

                String[] days = new String[] {getString(R.string.bar_Mon),getString(R.string.bar_Tue),getString(R.string.bar_Wed),getString(R.string.bar_Thurs),getString(R.string.bar_Fri),getString(R.string.bar_Sat),getString(R.string.bar_Sun)};

                //set bargraph day placement depending on the day
                switch (currentDay){
                    case 1:
                        days = new String[] {getString(R.string.bar_Mon),getString(R.string.bar_Tue),getString(R.string.bar_Wed),getString(R.string.bar_Thurs),getString(R.string.bar_Fri),getString(R.string.bar_Sat),getString(R.string.bar_Sun)};
                        break;
                    case 2:
                        days = new String[] {getString(R.string.bar_Tue),getString(R.string.bar_Wed),getString(R.string.bar_Thurs),getString(R.string.bar_Fri),getString(R.string.bar_Sat),getString(R.string.bar_Sun),getString(R.string.bar_Mon)};
                        break;
                    case 3:
                        days = new String[] {getString(R.string.bar_Wed),getString(R.string.bar_Thurs),getString(R.string.bar_Fri),getString(R.string.bar_Sat),getString(R.string.bar_Sun),getString(R.string.bar_Mon),getString(R.string.bar_Tue)};
                        break;
                    case 4:
                        days = new String[] {getString(R.string.bar_Thurs),getString(R.string.bar_Fri),getString(R.string.bar_Sat),getString(R.string.bar_Sun),getString(R.string.bar_Mon),getString(R.string.bar_Tue),getString(R.string.bar_Wed)};
                        break;
                    case 5:
                        days = new String[] {getString(R.string.bar_Fri),getString(R.string.bar_Sat),getString(R.string.bar_Sun),getString(R.string.bar_Mon),getString(R.string.bar_Tue),getString(R.string.bar_Wed),getString(R.string.bar_Thurs)};
                        break;
                    case 6:
                        days = new String[] {getString(R.string.bar_Sun),getString(R.string.bar_Sat),getString(R.string.bar_Mon),getString(R.string.bar_Tue),getString(R.string.bar_Wed),getString(R.string.bar_Thurs),getString(R.string.bar_Fri)};
                        break;
                    case 7:
                        days = new String[] {getString(R.string.bar_Sun),getString(R.string.bar_Mon),getString(R.string.bar_Tue),getString(R.string.bar_Wed),getString(R.string.bar_Thurs),getString(R.string.bar_Fri),getString(R.string.bar_Sat)};
                        break;
                    default:

                        return;
                }

                //String[] days = new String[] {getString(R.string.bar_Mon),getString(R.string.bar_Tue),getString(R.string.bar_Wed),getString(R.string.bar_Thurs),getString(R.string.bar_Fri),getString(R.string.bar_Sat),getString(R.string.bar_Sun)};

                XAxis xAxis = barChart.getXAxis();
                xAxis.setTextSize(12f);
                xAxis.setTextColor(Color.WHITE);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setValueFormatter(new MyXAxisValueFormatter(days));

                total_steps = (int) (realMonday + realTuesday + realWednesday + realThursday +
                        realFriday + realSaturday + realSunday);

                ArrayList<PieEntry> goal_entries = new ArrayList<>();
                ArrayList<String> pie_label = new ArrayList<>();

                goal_chart.setDrawHoleEnabled(true);
                goal_chart.setBackgroundColor(Color.TRANSPARENT);
                goal_chart.setTransparentCircleRadius(20f);
                goal_chart.setHoleRadius(30f);
                goal_chart.setTouchEnabled(false);
                goal_chart.notifyDataSetChanged();
                goal_chart.invalidate();
                goal_chart.setHoleColor(Color.TRANSPARENT);
                goal_chart.getDescription().setEnabled(false);
                goal_chart.animateY(1000,Easing.EaseInOutCubic);
                goal_chart.getLegend().setEnabled(true);
                goal_chart.getLegend().setTextColor(Color.WHITE);
                goal_chart.getLegend().setTextSize(12f);
                goal_chart.setDrawEntryLabels(false);

                Legend l = goal_chart.getLegend();
                l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

                int goalDisplayer = dataSnapshot.child("goalSteps").getValue(Integer.class);
                float goal_steps = dataSnapshot.child("goalSteps").getValue(Integer.class);
                goal.setText(getString(R.string.home_goal) + "\n" + goalDisplayer);

                goal_entries.add(new PieEntry((float)total_steps, getString(R.string.home_totalSteps)));
                goal_entries.add(new PieEntry((goal_steps - total_steps), getString(R.string.home_remainSteps)));

                int percentSteps = (int)(((double) total_steps / (double) goal_steps) * 100.0);
                currentPercent.setText(percentSteps + "%");

                pie_label.add(getString(R.string.home_pie_total));
                pie_label.add(getString(R.string.pi_goal));

                PieDataSet data_set = new PieDataSet(goal_entries, "");
                PieData goal_data = new PieData(data_set);

                data_set.setColors(ColorTemplate.JOYFUL_COLORS);
                goal_data.setValueTextColor(Color.WHITE);
                goal_data.setValueTextSize(13f);

                goal_chart.setData(goal_data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                Toast.makeText(HomeActivity.this, R.string.toastHome_connErr, Toast.LENGTH_SHORT).show();
                finish();
                startActivity(intent);
            }
        });

        //Popup Menu
        final Button settingsBtn = findViewById(R.id.homeSettings);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(HomeActivity.this, settingsBtn);

                popup.getMenuInflater().inflate(R.menu.home_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.menuSettings:
                                Intent intent_settings = new Intent(HomeActivity.this, SettingsActivity.class);
                                finish();
                                startActivity(intent_settings);
                                return true;
                            case R.id.menuQuit:
                                finishAndRemoveTask();
                                return true;
                            case R.id.menuSignout:
                                mAuth.signOut();
                                Toast.makeText(HomeActivity.this, R.string.toastHome_signedout, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                finish();
                                startActivity(intent);
                                return true;
                        }
                        return HomeActivity.super.onOptionsItemSelected(item);
                    }

                });
                popup.show();
            }

        });

        goalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, pop.class);
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


    private void findViews(){
        name = findViewById(R.id.welcomeHome);
        goal = findViewById(R.id.goal_steps);
        currentPercent = findViewById(R.id.percentSteps);
        barChart = findViewById(R.id.barChart);
        goal_chart = findViewById(R.id.goal_pieChart);
        goalButton = findViewById(R.id.goalButton);
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter{
        private String[] mValues;
        public MyXAxisValueFormatter(String[] values){
            this.mValues = values;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int)value];
        }
    }

    private void getDatabase() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
    }
}