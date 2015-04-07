package com.teeterpal.androidteeterpal.data;

/**
 * Created by weiwu on 3/19/15.
 */
public class LocationObject {
    private double latitude, longitude;
    private String address;

    public LocationObject(){}

    public LocationObject(double latitude, double longitude, String address){
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }
}
