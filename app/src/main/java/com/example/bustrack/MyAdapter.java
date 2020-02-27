package com.example.bustrack;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private ArrayList<ConductorLocationModel> listitems;
    private Context context;
    Location location;

    public MyAdapter(ArrayList<ConductorLocationModel> listitems,Location location, Context context) {
        this.listitems = listitems;
        this.context = context;
        this.location=location;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ConductorLocationModel listitem = listitems.get(position);

        double lat1= location.getLatitude();
        double lng1= location.getLongitude();
        double lat2=listitem.getLatitude();
        double lng2=listitem.getLongitude();

        double dist=calculateDistanceInKilometer(lat1,lng1,lat2,lng2);

        holder.busNo.setText(listitem.getBusNumber());
        holder.sPoint.setText(listitem.getStartLocation());
        holder.ePoint.setText(listitem.getDestinationLocation());
        holder.distance.setText(String.valueOf(dist));

    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView busNo;
        public TextView ePoint;
        public TextView sPoint;
        public TextView distance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            busNo = itemView.findViewById(R.id.busNo);
            sPoint = itemView.findViewById(R.id.sPoint);
            ePoint = itemView.findViewById(R.id.ePoint);
            distance = itemView.findViewById(R.id.distance);
        }
    }


    public double calculateDistanceInKilometer(double userLat, double userLng,
                                               double venueLat, double venueLng) {

        double AVERAGE_RADIUS_OF_EARTH_M = 6371000;

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return  (Math.round(AVERAGE_RADIUS_OF_EARTH_M * c));
    }
}

