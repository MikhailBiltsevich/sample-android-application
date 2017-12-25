package com.mikhailbiltsevich.app7;

public class BeltelecomBranch {

    private String name, address;
    private double latitude, longitude, distance;

    public double getDistance() {
        return distance;
    }

    public BeltelecomBranch(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;

        this.longitude = longitude;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s\n%f, %f\n%f м.", name, address, latitude, longitude, distance);
    }
}