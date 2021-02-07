package com.example.ajinafro.models;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Date;

public class Carpool implements Serializable {
    private String start_city;
    private String end_city;

    private String publisher;

    private Timestamp start_date;
    private Integer start_hours;
    private Integer start_minutes;

    private Integer available_places;
    private Integer price;
    
    private String more_details;

    @Override
    public String toString() {
        return "Carpool{" +
                "start_city='" + start_city + '\'' +
                ", end_city='" + end_city + '\'' +
                ", publisher='" + publisher + '\'' +
                ", start_date=" + start_date +
                ", start_hours=" + start_hours +
                ", start_minutes=" + start_minutes +
                ", available_places=" + available_places +
                ", price=" + price +
                ", more_details='" + more_details + '\'' +
                '}';
    }

    public Carpool(String start_city, String end_city, String publisher, Timestamp start_date, Integer start_hours, Integer start_minutes, Integer available_places, Integer price, String more_details) {
        this.start_city = start_city;
        this.end_city = end_city;
        this.publisher = publisher;
        this.start_date = start_date;
        this.start_hours = start_hours;
        this.start_minutes = start_minutes;
        this.available_places = available_places;
        this.price = price;
        this.more_details = more_details;
    }

    public Carpool() {
    }

    public String getStart_city() {
        return start_city;
    }

    public void setStart_city(String start_city) {
        this.start_city = start_city;
    }

    public String getEnd_city() {
        return end_city;
    }

    public void setEnd_city(String end_city) {
        this.end_city = end_city;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Timestamp getStart_date() {
        return start_date;
    }

    public void setStart_date(Timestamp start_date) {
        this.start_date = start_date;
    }

    public Integer getStart_hours() {
        return start_hours;
    }

    public void setStart_hours(Integer start_hours) {
        this.start_hours = start_hours;
    }

    public Integer getStart_minutes() {
        return start_minutes;
    }

    public void setStart_minutes(Integer start_minutes) {
        this.start_minutes = start_minutes;
    }

    public Integer getAvailable_places() {
        return available_places;
    }

    public void setAvailable_places(Integer available_places) {
        this.available_places = available_places;
    }

    public String getMore_details() {
        return more_details;
    }

    public void setMore_details(String more_details) {
        this.more_details = more_details;
    }
}
