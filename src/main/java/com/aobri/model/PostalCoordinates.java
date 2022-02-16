package com.aobri.model;

public class PostalCoordinates {

    int id;
    String postCode;
    double latitude;
    double longitude;

    public PostalCoordinates() {}

    public PostalCoordinates(int id, String postCode, double latitude, double longitude) {
        this.id = id;
        this.postCode = postCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
