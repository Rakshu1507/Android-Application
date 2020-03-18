package com.example.bustrack;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainConductorActivity extends AppCompatActivity {

    DatabaseReference databaseconductor;

    LocationManager locationManager;
    LocationListener locationListener;
    String busId;
    Intent intent;

    @SuppressLint("SetTextI18n")
    public void updateLocationInfo(Location location){

        Log.i("LocationInfo", location.toString());

        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());

        databaseconductor.child(busId).child("latitude").setValue(location.getLatitude());
        databaseconductor.child(busId).child("longitude").setValue(location.getLongitude());

        Geocoder geocoder = new Geocoder( MainConductorActivity.this);

        try {


            List<Address> listaddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1 );

            Log.i("PlacedInfo", listaddress.get(0).toString());

            String address1 = " ";
            if(listaddress.get(0).getSubThoroughfare() != null){

                address1 += listaddress.get(0).getSubThoroughfare() + "";
            }

            if(listaddress.get(0).getThoroughfare() != null){

                address1 += listaddress.get(0).getThoroughfare();
            }

            String address2 = " ";
            if(listaddress.get(0).getLocality() != null){

                address2 = listaddress.get(0).getLocality();
            }

            String address3 = " ";
            if(listaddress.get(0).getPostalCode() != null){

                address3 = listaddress.get(0).getPostalCode();
            }

            TextView roadName1 = findViewById(R.id.roadName);
            roadName1.setText("Road Name: " +address1);
            TextView city1 = findViewById(R.id.city);
            city1.setText("City: " +address2);
            TextView pcode= findViewById(R.id.pcode);
            pcode.setText("Postal Code: " +address3);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){

            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){

                startListening();
            }
        }
    }

    public void startListening(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    public void map1(View mapview){

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("busId", busId);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_conductor);
        intent=getIntent();
        busId=intent.getStringExtra("busId");

        databaseconductor = FirebaseDatabase.getInstance().getReference("");

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
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

        if(Build.VERSION.SDK_INT <23){

            startListening();
        }
        else{

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

            else{

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location==null){
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if(location==null){
                    location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                }
                if(location != null) {
                    updateLocationInfo(location);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MainConductorActivity.this, MainConductor2Activity.class);
        startActivity(i);
        finish();
    }

}
