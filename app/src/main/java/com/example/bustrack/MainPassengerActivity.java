package com.example.bustrack;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainPassengerActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    Location lastKnownLocation;
    private int locationRequestCode=1000;

    private RecyclerView recyclerview;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutmanager;
    private DatabaseReference busData;

    private ArrayList<ConductorLocationModel> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_passenger);

        busData= FirebaseDatabase.getInstance().getReference("");

        recyclerview = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerview.setHasFixedSize(true);

        // use a linear layout manager
        layoutmanager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutmanager);

        listItems = new ArrayList<>();


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (ActivityCompat.checkSelfPermission(MainPassengerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainPassengerActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(lastKnownLocation==null){
                        lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    }
                    if(lastKnownLocation==null){
                        lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                    }

                }

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
                lastKnownLocation=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        busData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listItems.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ConductorLocationModel bus=snapshot.getValue(ConductorLocationModel.class);
                    listItems.add(bus);
                }

                adapter = new MyAdapter(listItems,lastKnownLocation, MainPassengerActivity.this);
                recyclerview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
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
        Intent i = new Intent(MainPassengerActivity.this, MainPassangerActivity1.class);
        startActivity(i);
        finish();
    }

}
