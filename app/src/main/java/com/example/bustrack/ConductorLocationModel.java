package com.example.bustrack;

public class ConductorLocationModel {

    private String busNumber;
    private String startLocation;
    private String destinationLocation;
    private double latitude;
    private double longitude;

    public ConductorLocationModel() {
    }

    public ConductorLocationModel(String busNumber, String startLocation, String destinationLocation,double latitude,double longitude) {
        this.busNumber = busNumber;
        this.startLocation = startLocation;
        this.destinationLocation = destinationLocation;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
