package com.example.ajinafro.models;

import java.io.Serializable;

public class Adresse implements Serializable {
    private String ville;
    private Double longitude;
    private Double latitude;

    public Adresse() {
    }

    public Adresse(Double latitude, Double longitude,String ville) {
        this.ville = ville;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    @Override
    public String toString() {
        return "Adresse{" +
                "ville='" + ville + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
