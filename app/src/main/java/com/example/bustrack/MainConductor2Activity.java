package com.example.bustrack;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainConductor2Activity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    DatabaseReference databaseconductor;
    Location lastKnownLocation;
    Button button;
    int locationRequestCode=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_conductor2);

        databaseconductor = FirebaseDatabase.getInstance().getReference("");

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);

        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation==null){
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if(lastKnownLocation==null){
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }

        }


        button=findViewById(R.id.dataButton);
        button.setOnClickListener(

            new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TextView busName1 = findViewById(R.id.busId);
                String busId= busName1.getText().toString().toUpperCase().trim();
                TextView start = findViewById(R.id.sPoint);
                String startLoc= start.getText().toString().trim();
                TextView end = findViewById(R.id.ePoint);
                String endLoc= end.getText().toString().trim();

                if(busId.length()>0 && startLoc.length()>0 && endLoc.length()>0) {
                    if(lastKnownLocation!=null) {
                        ConductorLocationModel conductor = new ConductorLocationModel(busId, startLoc, endLoc, lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        databaseconductor.child(busId).setValue(conductor);
                        Toast.makeText(MainConductor2Activity.this, "Journey Started!", Toast.LENGTH_LONG).show();
                    }
                    Intent i = new Intent(MainConductor2Activity.this, MainConductorActivity.class);
                    i.putExtra("busId", busId);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(MainConductor2Activity.this, "Data Invalid", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(locationRequestCode){

            case 1000:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                        lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(lastKnownLocation==null){
                            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        }
                        if(lastKnownLocation==null){
                            lastKnownLocation=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                        }

                    }

                }
                else{

                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }

            }

            break;
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MainConductor2Activity.this, MainActivity.class);
        startActivity(i);
        finish();
    }


}
