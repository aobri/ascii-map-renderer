package com.aobri.model;

public class GeographicCoordinates {

    int id;
    double latitude;
    double longitude;
    String postCode;

    public GeographicCoordinates() {
    }

    public GeographicCoordinates(int id, String postCode, double latitude, double longitude) {
        this.id = id;
        this.postCode = postCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
