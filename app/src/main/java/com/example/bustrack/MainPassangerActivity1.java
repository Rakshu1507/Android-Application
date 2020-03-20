package com.example.bustrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("ALL")
public class MainPassangerActivity1 extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_passanger1);

    }


    public void map2(View mapview) {
        Intent intent = new Intent(this, MapsActivityPassanger.class);
        startActivity(intent);
        finish();
    }
    public void list(View mapview) {
        Intent intent = new Intent(this, MainPassengerActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MainPassangerActivity1.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
