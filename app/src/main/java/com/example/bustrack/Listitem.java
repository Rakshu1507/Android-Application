package com.example.bustrack;

public class Listitem {

    private String busno;
    private String spoint;
    private String epoint;
    private String distance;

    public Listitem(String busno, String spoint, String epoint, String distance) {
        this.busno = busno;
        this.spoint = spoint;
        this.epoint = epoint;
        this.distance = distance;
    }

    public String getBusno() {
        return busno;
    }

    public String getSpoint() {
        return spoint;
    }

    public String getEpoint() {
        return epoint;
    }

    public String getDistance() {
        return distance;
    }
}
